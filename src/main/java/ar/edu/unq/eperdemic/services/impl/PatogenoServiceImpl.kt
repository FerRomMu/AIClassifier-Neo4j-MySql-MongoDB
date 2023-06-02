package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.exceptions.DataNotFoundException
import ar.edu.unq.eperdemic.exceptions.IdNotFoundException
import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.EspecieDAO
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.UbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.persistencia.repository.spring.EspecieRepository
import ar.edu.unq.eperdemic.persistencia.repository.spring.PatogenoRepository
import ar.edu.unq.eperdemic.persistencia.repository.spring.VectorRepository
import ar.edu.unq.eperdemic.services.EspecieService
import ar.edu.unq.eperdemic.services.PatogenoService
import ar.edu.unq.eperdemic.services.VectorService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Transactional
@Service
class PatogenoServiceImpl() : PatogenoService {

    @Autowired lateinit var patogenoRepository: PatogenoRepository
    @Autowired lateinit var vectorRepository: VectorRepository
    @Autowired lateinit var especieRepository: EspecieRepository

    override fun crearPatogeno(patogeno: Patogeno): Patogeno {
        return patogenoRepository.save(patogeno)
    }

    override fun recuperarPatogeno(id: Long): Patogeno {
        return patogenoRepository.findById(id)
            .getOrNull() ?: throw IdNotFoundException("No se encontró un patogeno con el id dado.")
    }

    override fun recuperarATodosLosPatogenos(): List<Patogeno> {
        return patogenoRepository.findAll().toList()
    }

    override fun agregarEspecie(id: Long, nombre: String, ubicacionId: Long): Especie {
        try {
            val vectorAInfectar = vectorRepository.vectorAleatorioEn(ubicacionId).first()
            val patogeno = this.recuperarPatogeno(id)
            val especieNueva = patogeno.crearEspecie(nombre, vectorAInfectar.ubicacion.nombre)

            vectorAInfectar.agregarEspecie(especieNueva)

            vectorRepository.save(vectorAInfectar)
            patogenoRepository.save(patogeno)
            especieRepository.save(especieNueva)

            return especieNueva
        } catch (e : NoSuchElementException){
            throw DataNotFoundException("No existe un vector aleatorio en la ubicacion dada.")
        }

    }

    override fun esPandemia(especieId: Long): Boolean {
        return patogenoRepository.esPandemia(especieId)
    }

    override fun especiesDePatogeno(id: Long): List<Especie> {
        return patogenoRepository.especiesDePatogeno(id)
    }

}