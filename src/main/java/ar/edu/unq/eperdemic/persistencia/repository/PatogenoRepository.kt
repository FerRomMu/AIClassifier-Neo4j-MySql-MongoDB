package ar.edu.unq.eperdemic.persistencia.repository

import ar.edu.unq.eperdemic.modelo.Patogeno
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PatogenoRepository : CrudRepository<Patogeno,Long>