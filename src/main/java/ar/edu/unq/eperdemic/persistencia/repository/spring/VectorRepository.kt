package ar.edu.unq.eperdemic.persistencia.repository.spring

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Vector
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface VectorRepository : CrudRepository<Vector, Long> {

    @Query("SELECT es FROM Vector v " +
            "JOIN v.especiesContagiadas es " +
            "WHERE v.id = :idVector")
    fun enfermedades(@Param("idVector") id: Long?): List<Especie>

    @Query("""
            from Vector v 
            where v.ubicacion.id = :idUbi
            order by rand()
            """)
    fun vectorAleatorioEn(@Param("idUbi") ubicacionId: Long, pageable: Pageable = PageRequest.of(0, 1)): List<Vector>
}