package ar.edu.unq.eperdemic.utils.impl

import ar.edu.unq.eperdemic.exceptions.InvalidDataTypeException
import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.persistencia.dao.*
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.*
import ar.edu.unq.eperdemic.persistencia.repository.spring.PatogenoRepository
import ar.edu.unq.eperdemic.services.runner.TransactionRunner
import ar.edu.unq.eperdemic.utils.DataService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class DataServiceSpring: DataService {

    @Autowired lateinit var patogenoRepository: PatogenoRepository

    override fun eliminarTodo() {
        patogenoRepository.deleteAll()
    }

    override fun persistir(entidades: List<Any>): List<Any> {
        entidades.forEach { entidad ->
            when (entidad) {
                is Patogeno -> patogenoRepository.save(entidad)
                else -> throw InvalidDataTypeException("El dato no es persistible.")
            }
        }
        return entidades
    }

    override fun persistir(entidad: Any): Any {
        return this.persistir(listOf(entidad)).first()
    }

    override fun crearSetDeDatosIniciales() {

        for (i in 0..20) {
            var patogeno = Patogeno("Tipo $i")
            patogeno.tipo = "Tipo $i"
            patogenoRepository.save(patogeno)
        }

    }
    override fun crearPandemiaPositiva(): Especie {
        return TODO()
    }
}