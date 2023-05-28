package ar.edu.unq.eperdemic.persistencia.repository.spring

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface EspecieRepository: CrudRepository<Especie, Long> {

    @Query("SELECT COUNT(*)\n" +
            "FROM Especie es\n" +
            "JOIN es.vectores e\n" +
            "WHERE es.id = :idEspecie\n")
    fun cantidadDeInfectados(@Param("idEspecie") especieId: Long): Int

    @Query("SELECT es " +
            "FROM Vector v " +
            "JOIN v.especiesContagiadas es " +
            "WHERE EXISTS (" +
            "    SELECT v2 FROM Vector v2 " +
            "    JOIN v2.especiesContagiadas es2 " +
            "    WHERE v2.tipo = :tipob " +
            "    AND es2 = es" +
            ") " +
            "AND EXISTS (" +
            "    SELECT v3 FROM Vector v3 " +
            "    JOIN v3.especiesContagiadas es3 " +
            "    WHERE v3.tipo = :tipoa " +
            "    AND es3 = es" +
            ") " +
            "GROUP BY es.id " +
            "ORDER BY COUNT(es.id) DESC")
    fun lideres(@Param("tipoa") tipoA: TipoDeVector = TipoDeVector.Persona,
                @Param("tipob") tipoB: TipoDeVector = TipoDeVector.Animal,
                pageable: Pageable = PageRequest.of(0, 10)
    ): List<Especie>

}