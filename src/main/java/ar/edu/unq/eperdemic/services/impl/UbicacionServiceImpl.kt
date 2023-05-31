package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.exceptions.DataDuplicationException
import ar.edu.unq.eperdemic.exceptions.DataNotFoundException
import ar.edu.unq.eperdemic.exceptions.IdNotFoundException
import ar.edu.unq.eperdemic.modelo.Camino
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.UbicacionNeo
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.exceptions.UbicacionMuyLejana
import ar.edu.unq.eperdemic.exceptions.UbicacionNoAlcanzable
import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.persistencia.repository.neo.UbicacionNeoRepository
import ar.edu.unq.eperdemic.persistencia.repository.spring.UbicacionRepository
import ar.edu.unq.eperdemic.persistencia.repository.spring.VectorRepository
import ar.edu.unq.eperdemic.services.UbicacionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Transactional
@Service
class UbicacionServiceImpl(): UbicacionService {

    @Autowired lateinit var ubicacionNeoRepository: UbicacionNeoRepository
    @Autowired lateinit var ubicacionRepository : UbicacionRepository
    @Autowired lateinit var vectorRepository : VectorRepository

     fun moverVector(vectorAMover: Vector, ubicacionAMover: Ubicacion) {
            val listaDeVectores = ubicacionRepository.vectoresEn(ubicacionAMover.id).toList()

             if(listaDeVectores.isNotEmpty()){
                 vectorAMover.ubicacion = listaDeVectores[0].ubicacion

                 for (vector in listaDeVectores){
                     vectorAMover.intentarInfectar(vector)
                     vectorRepository.save(vector)
                 }
                 vectorRepository.save(vectorAMover)
             }else{
                 vectorAMover.ubicacion = ubicacionAMover
                 vectorRepository.save(vectorAMover)
             }
    }

    override fun mover(vectorId: Long, ubicacionid: Long){
        val vectorAMover = vectorRepository.findById(vectorId).get()
        val ubicacionAMover =  ubicacionRepository.findById(ubicacionid).get()
        val ubicacionesQueSePuedenLLegar = ubicacionNeoRepository.conectados(vectorAMover.ubicacion.nombre)

        if(! ubicacionesQueSePuedenLLegar.any { uNeo -> uNeo.esLaUbicacion(ubicacionAMover.nombre) } ){
            throw UbicacionMuyLejana("no es posible llegar desde la actual ubicación del vector a la nueva por medio de un camino.")
        }

        val caminosubicacionNEOOrigen =  ubicacionNeoRepository.findByNombre(vectorAMover.ubicacion.nombre).caminos.
                                            filter{c -> c.ubicacioDestino.nombre == ubicacionAMover.nombre}.toMutableList()
        if(this.puedoUsarAlgunCamino(vectorAMover,caminosubicacionNEOOrigen)){
            this.moverVector(vectorAMover,ubicacionAMover)
        }else{
            throw UbicacionNoAlcanzable("se intenta mover a un vector a través de un tipo de camino que no puede atravesar")
        }
    }

    fun puedoUsarAlgunCamino(vector: Vector, caminosUbicacionNeo: MutableList<Camino>): Boolean {
        return caminosUbicacionNeo.any { c -> c.puedePasar(vector) }
    }

    override fun expandir(ubicacionId: Long) {
            val vectores = ubicacionRepository.vectoresEn(ubicacionId).toMutableList()
            if (vectores.isNotEmpty()) {
                val dado = Randomizador.getInstance()
                val numeroAleatorio = dado.valor(0, vectores.size-1)
                val vectorContagioso = vectores.removeAt(numeroAleatorio)
                for(vector in vectores){
                    vectorContagioso.intentarInfectar(vector)
                    vectorRepository.save(vector)
                }
                vectorRepository.save(vectorContagioso)
            }
    }

    @Transactional(rollbackFor = [Exception::class], noRollbackFor = [DataIntegrityViolationException::class])
    override fun crearUbicacion(nombreUbicacion: String): Ubicacion {
        val ubicacion = Ubicacion(nombreUbicacion)
        val ubicacionNeo = UbicacionNeo(nombreUbicacion)
        try {
            ubicacionRepository.save(ubicacion)
            ubicacionNeoRepository.save(ubicacionNeo)
            return ubicacion
        } catch (e: DataIntegrityViolationException) {
            throw DataDuplicationException("Ya existe una ubicación con ese nombre.")
        }
    }

    override fun recuperar(id: Long): Ubicacion {
        return ubicacionRepository.findById(id)
            .getOrNull() ?: throw IdNotFoundException("No se encontró una especie con el id dado.")
    }

    override fun recuperarTodos(): List<Ubicacion> {
            return ubicacionRepository.findAll().toList()
    }

    override fun vectoresEn(id: Long): List<Vector> {
        return ubicacionRepository.vectoresEn(id).toList()
    }

    override fun conectar(nombreDeUbicacion1: String, nombreDeUbicacion2: String, tipoCamino: Camino.TipoDeCamino) {
       try {
           val ubicacion1 = ubicacionNeoRepository.findByNombre(nombreDeUbicacion1)
           val ubicacion2 = ubicacionNeoRepository.findByNombre(nombreDeUbicacion2)

           val camino = Camino(ubicacion2, tipoCamino)
           ubicacion1.agregarCamino(camino)

           ubicacionNeoRepository.save(ubicacion1)
           ubicacionNeoRepository.save(ubicacion2)
       } catch (e: Exception) {
           throw DataNotFoundException("No existe una ubicacion con el nombre dado")
       }
    }

    override fun conectados(nombreDeUbicacion:String): List<UbicacionNeo>{
        return ubicacionNeoRepository.conectados(nombreDeUbicacion)
    }

}