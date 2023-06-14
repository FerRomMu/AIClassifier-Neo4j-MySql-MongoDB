package ar.edu.unq.eperdemic.persistencia.repository.spring

import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UbicacionRepository : CrudRepository<Ubicacion, Long> {

    @Query("FROM Vector v " +
            "WHERE v.ubicacion.id = :idUbicacion")
    fun vectoresEn(@Param("idUbicacion") ubicacionId: Long?): List<Vector>

    @Query("SELECT COUNT(*)\n" +
            "FROM Vector v\n" +
            "WHERE v.ubicacion.nombre = :nombreUbicacion")
    fun cantidadVectoresPresentes(@Param("nombreUbicacion") nombreDeLaUbicacion: String) : Long

    @Query("select count(distinct v.id) " +
            "from Vector v " +
            "join v.especiesContagiadas es " +
            "where v.ubicacion.nombre = :nombre")
    fun cantidadVectoresInfectados(@Param("nombre") nombreDeLaUbicacion: String) : Long

    @Query("select es.nombre " +
            "from Vector v " +
            "join v.especiesContagiadas es " +
            "where v.ubicacion.nombre = :nombreUbicacion " +
            "group by es.nombre " +
            "order by count(v) desc")
    fun nombreEspecieQueMasInfectaVectores(@Param("nombreUbicacion") nombreDeLaUbicacion: String, pageable: Pageable = PageRequest.of(0, 1)) : List<String>
    fun findByNombre(nombre: String): Ubicacion

    /*
    @Query("select \n" +
            "0 < (select count(distinct v.id) " +
                "from Vector v " +
                "join v.especiesContagiadas es " +
                "where v.ubicacion.nombre = :nombre)")
    // arreglar, no funciona
    fun tieneAlgunInfectado(@Param("nombre") nombre: String): Boolean*/

}