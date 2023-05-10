package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.exceptions.IdNotFoundException
import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.persistencia.repository.spring.EspecieRepository
import ar.edu.unq.eperdemic.services.EspecieService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Transactional
@Service
class EspecieServiceImpl() : EspecieService {

    @Autowired
    lateinit var especieRepository : EspecieRepository

    override fun recuperarEspecie(id: Long): Especie {
        return especieRepository.findById(id)
            .getOrNull() ?: throw IdNotFoundException("No se encontró una especie con el id dado.")
    }

    override fun recuperarTodos(): List<Especie> {
        return especieRepository.findAll().toList()
    }

    override fun cantidadDeInfectados(especieId: Long): Int {
        return especieRepository.cantidadDeInfectados(especieId)
    }

}