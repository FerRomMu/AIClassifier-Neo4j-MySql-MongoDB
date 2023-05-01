package ar.edu.unq.eperdemic.persistencia.repository.spring

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PatogenoRepository : CrudRepository<Patogeno,Long> {
    @Query()
    fun especiesDePatogeno(id: Long?): List<Especie>
    @Query()
    fun esPandemia(especieId: Long): Boolean
}