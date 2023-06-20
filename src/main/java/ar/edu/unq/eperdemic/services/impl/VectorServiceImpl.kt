package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.exceptions.IdNotFoundException
import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.DataDAO
import ar.edu.unq.eperdemic.persistencia.dao.EspecieDAO
import ar.edu.unq.eperdemic.persistencia.dao.UbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.persistencia.repository.mongo.UbicacionMongoRepository
import ar.edu.unq.eperdemic.persistencia.repository.spring.EspecieRepository
import ar.edu.unq.eperdemic.persistencia.repository.spring.UbicacionRepository
import ar.edu.unq.eperdemic.persistencia.repository.spring.VectorRepository
import ar.edu.unq.eperdemic.services.VectorService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Transactional
@Service
class VectorServiceImpl(): VectorService {

    @Autowired lateinit var vectorRepository: VectorRepository
    @Autowired lateinit var ubicacionRepository: UbicacionRepository
    @Autowired lateinit var ubicacionMongoRepository: UbicacionMongoRepository
    @Autowired lateinit var especieRepository: EspecieRepository

    override fun infectar(vector: Vector, especie: Especie) {
        vector.agregarEspecie(especie)
        vectorRepository.save(vector)
        especieRepository.save(especie)

        val ubicacionMongo = ubicacionMongoRepository.findByNombre(vector.ubicacion.nombre)
        ubicacionMongo.hayAlgunInfectado = true
        ubicacionMongoRepository.save(ubicacionMongo)

    }

    override fun enfermedades(vectorId: Long): List<Especie> {
        return vectorRepository.enfermedades(vectorId)
    }

    override fun crearVector(tipo: TipoDeVector, ubicacionId: Long): Vector {
        val ubicacionDelVector = ubicacionRepository.findById(ubicacionId)
            .getOrNull()?: throw IdNotFoundException("No se encontró una ubicación con el id dado.")

        val vector = Vector( tipo)
        vector.ubicacion = ubicacionDelVector
        vectorRepository.save(vector)

        return vector
    }

    override fun recuperarVector(vectorId: Long): Vector {
        return vectorRepository.findById(vectorId)
            .getOrNull() ?: throw IdNotFoundException("No se encontró un vector con el id dado.")
    }

    override fun borrarVector(vectorId: Long) {
        vectorRepository.deleteById(vectorId)
    }

    override fun recuperarTodos(): List<Vector> {
        return vectorRepository.findAll().toList()
    }

}