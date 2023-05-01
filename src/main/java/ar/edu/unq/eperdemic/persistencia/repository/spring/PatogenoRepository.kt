package ar.edu.unq.eperdemic.persistencia.repository.spring

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
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

    /*
    @Query(
        "SELECT (COUNT(DISTINCT V.UBICACION.ID) * 2) >= (SELECT COUNT(*) FROM UBICACION) AS CANTIDAD_UBICACIONES_MAYOR_A_LA_MITAD " +
                "FROM VECTOR V" +
                "JOIN V.ESPECIESCONTAGIADAS ES" +
                "WHERE ES.ID = 1?"
    )
    fun esPandemia(especieId: Long): Boolean*/

}