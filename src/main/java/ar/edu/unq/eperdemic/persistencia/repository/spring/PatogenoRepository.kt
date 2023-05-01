package ar.edu.unq.eperdemic.persistencia.repository.spring

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PatogenoRepository : CrudRepository<Patogeno,Long> {

    /*
    @Query(
        "SELECT ES FROM PATOGENO P" +
                "JOIN P.ESPECIES ES" +
                "WHERE P.ID = 1? "
    )
    fun especiesDePatogeno(id: Long?): List<Especie>*/


    @Query("select \n" +
            "(count(distinct v.ubicacion.id) * 2) >= (select count(*) from Ubicacion) as cantidad_ubicaciones_mayor_a_la_mitad \n" +
            "from Vector v \n" +
            "join v.especiesContagiadas es \n" +
            "where es.id = :idEsp \n")
    fun esPandemia(@Param("idEsp") especieId: Long): Boolean

}