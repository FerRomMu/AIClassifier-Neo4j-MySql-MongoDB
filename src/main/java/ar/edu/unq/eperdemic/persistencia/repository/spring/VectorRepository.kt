package ar.edu.unq.eperdemic.persistencia.repository.spring

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Vector
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

    @Query("SELECT * FROM vector v " +
            "JOIN ubicacion u ON u.id = :idUbi " +
            "WHERE v.ubicacion_id = :idUbi " +
            "order by rand() " +
            "limit 1", nativeQuery = true)
    fun vectorAleatorioEn(@Param("idUbi") ubicacionId: Long): Vector
}