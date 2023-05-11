package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.exceptions.IdNotFoundException
import ar.edu.unq.eperdemic.modelo.Mutacion
import ar.edu.unq.eperdemic.persistencia.repository.spring.EspecieRepository
import ar.edu.unq.eperdemic.persistencia.repository.spring.MutacionRepository
import ar.edu.unq.eperdemic.services.MutacionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MutacionServiceImpl: MutacionService {

    @Autowired lateinit var especieRepository: EspecieRepository
    @Autowired lateinit var mutacionRepository: MutacionRepository

    override fun agregarMutacion(especieId: Long, mutacion: Mutacion): Mutacion {
        try {
            mutacionRepository.save(mutacion)
            val especie = especieRepository.findById(especieId).get()
            especie.agregarMutacion(mutacion)
            especieRepository.save(especie)
            return mutacion
        } catch (e: java.util.NoSuchElementException){
            throw IdNotFoundException("No existe especie con id dado.")
        }
    }

}