package ar.edu.unq.eperdemic.persistencia.repository.spring

import ar.edu.unq.eperdemic.modelo.Mutacion
import org.springframework.data.repository.CrudRepository

interface MutacionRepository: CrudRepository<Mutacion, Long> {}