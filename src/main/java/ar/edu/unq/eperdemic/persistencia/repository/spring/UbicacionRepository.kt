package ar.edu.unq.eperdemic.persistencia.repository.spring

import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UbicacionRepository : CrudRepository<Ubicacion, Long> {
    
    @Query("FROM Vector v " +
            "WHERE v.ubicacion.id = :idUbicacion")
    fun vectoresEn(@Param("idUbicacion") ubicacionId: Long?): Collection<Vector>
}