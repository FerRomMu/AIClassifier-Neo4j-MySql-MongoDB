package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Randomizador
import ar.edu.unq.eperdemic.exceptions.DataDuplicationException
import ar.edu.unq.eperdemic.exceptions.DataNotFoundException
import ar.edu.unq.eperdemic.exceptions.IdNotFoundException
import ar.edu.unq.eperdemic.modelo.Camino
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector
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

    @Autowired lateinit var ubicacionNeoRepository: UbicacionNeoRepository
    @Autowired lateinit var ubicacionRepository : UbicacionRepository

    @Autowired lateinit var vectorRepository : VectorRepository

    override fun mover(vectorId: Long, ubicacionid: Long) {
            val listaDeVectores = ubicacionRepository.vectoresEn(ubicacionid).toList()
            val vectorAMover = vectorRepository.findById(vectorId).get()

             if(listaDeVectores.isNotEmpty()){
                 vectorAMover.ubicacion = listaDeVectores[0].ubicacion

                 for (vector in listaDeVectores){
                     vectorAMover.intentarInfectar(vector)
                     vectorRepository.save(vector)
                 }
                 vectorRepository.save(vectorAMover)
             }else{
                 val ubicacionAMover = ubicacionRepository.findById(ubicacionid).get()
                 vectorAMover.ubicacion = ubicacionAMover
                 vectorRepository.save(vectorAMover)
             }
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

    override fun conectar(nombreDeUbicacion1: String, nombreDeUbicacion2: String, tipoCamino: Camino.TipoDeCamino) {
       try {
           val ubicacion1 = ubicacionNeoRepository.findByNombre(nombreDeUbicacion1)
           val ubicacion2 = ubicacionNeoRepository.findByNombre(nombreDeUbicacion2)

           val camino = Camino(ubicacion2, tipoCamino)
           ubicacion1.caminos.add(camino)

           ubicacionNeoRepository.save(ubicacion1)
           ubicacionNeoRepository.save(ubicacion2)
       } catch (e: Exception) {
           throw DataNotFoundException("No existe una ubicacion con el nombre dado")
       }
    }

}