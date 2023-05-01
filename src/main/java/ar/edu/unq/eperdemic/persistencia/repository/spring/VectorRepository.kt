package ar.edu.unq.eperdemic.persistencia.repository.spring

import ar.edu.unq.eperdemic.modelo.Vector
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface VectorRepository : CrudRepository<Vector, Long>