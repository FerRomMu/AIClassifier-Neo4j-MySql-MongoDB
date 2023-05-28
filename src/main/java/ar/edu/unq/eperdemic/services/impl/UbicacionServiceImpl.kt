package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.exceptions.DataDuplicationException
import ar.edu.unq.eperdemic.exceptions.IdNotFoundException
import ar.edu.unq.eperdemic.exceptions.UbicacionMuyLejana
import ar.edu.unq.eperdemic.exceptions.UbicacionNoAlcanzable
import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.persistencia.repository.neo.UbicacionNeoRepository
import ar.edu.unq.eperdemic.persistencia.repository.spring.UbicacionRepository
import ar.edu.unq.eperdemic.persistencia.repository.spring.VectorRepository
import ar.edu.unq.eperdemic.services.UbicacionService
import org.hibernate.exception.ConstraintViolationException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Transactional
@Service
class UbicacionServiceImpl(): UbicacionService {

    @Autowired lateinit var ubicacionRepository : UbicacionRepository

    @Autowired lateinit var vectorRepository : VectorRepository

    @Autowired lateinit var ubicacionNeoRepository : UbicacionNeoRepository

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
        val ubicacionOrigen = vectorAMover.ubicacion
        var ubicacionAMover =  ubicacionRepository.findById(ubicacionid).get()
        var ubicacionesQueSePuedenLLegar = ubicacionNeoRepository.conectados(ubicacionAMover.nombre)

        if(! ubicacionesQueSePuedenLLegar.any { u -> u.esLaUbicacion(ubicacionAMover.id!!) } ){
            throw UbicacionMuyLejana("no es posible llegar desde la actual ubicación del vector a la nueva por medio de un camino.")
        }
        val tiposDeCaminosDisponibles = ubicacionNeoRepository.caminosEntre(ubicacionOrigen.nombre,ubicacionAMover.nombre)
        if(this.puedoUsarAlgunCamino(vectorAMover,tiposDeCaminosDisponibles)){
            this.moverVector(vectorAMover,ubicacionAMover)
        }else{
            throw UbicacionNoAlcanzable("se intenta mover a un vector a través de un tipo de camino que no puede atravesar")
        }
    }

    fun puedoUsarAlgunCamino(vector: Vector, tiposDeCaminosDisponibles: List<TipoDeCamino>): Boolean {
        return tiposDeCaminosDisponibles.any { tc -> tc.puedeTransitar(vector.tipo) }
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
        try {
            ubicacionRepository.save(ubicacion)
            return ubicacion
        } catch (e: DataIntegrityViolationException) {  // ConstraintViolationException
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

}