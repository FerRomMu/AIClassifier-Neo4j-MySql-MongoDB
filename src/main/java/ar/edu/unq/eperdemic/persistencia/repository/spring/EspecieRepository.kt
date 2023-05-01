package ar.edu.unq.eperdemic.persistencia.repository.spring

import ar.edu.unq.eperdemic.modelo.Especie
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EspecieRepository: CrudRepository<Especie, Long>