package ar.edu.unq.eperdemic.persistencia.repository.spring

import ar.edu.unq.eperdemic.modelo.Especie
import org.springframework.data.repository.CrudRepository

interface EspecieRepository: CrudRepository<Especie, Long>