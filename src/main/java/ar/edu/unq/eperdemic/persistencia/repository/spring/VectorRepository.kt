package ar.edu.unq.eperdemic.persistencia.repository.spring

import ar.edu.unq.eperdemic.modelo.Vector
import org.springframework.data.repository.CrudRepository

interface VectorRepository : CrudRepository<Vector, Long>