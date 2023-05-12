package ar.edu.unq.eperdemic.persistencia.repository.spring

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.TipoDeVector
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


    @Query("select e.id, e.nombre, e.pais_de_origen, e.id_patogeno from especie_vector_contagiado evc\n" +
            "join vector v on v.tipo = :tipo\n" +
            "join especie e on e.id = evc.especie_id\n" +
            "group by e.id, e.nombre, e.pais_de_origen, e.id_patogeno\n" +
            "order by count(e.id) desc\n" +
            "limit 1 ", nativeQuery = true )
    fun especieLider(@Param("tipo") tipo: TipoDeVector = TipoDeVector.Persona): Especie

    @Query("SELECT e.id, e.nombre, e.pais_de_origen, e.id_patogeno\n" +
            "FROM especie_vector_contagiado evc\n" +
            "JOIN vector v ON v.id = evc.vector_id \n" +
            "JOIN especie e ON e.id = evc.especie_id\n" +
            "WHERE e.id IN (\n" +
            "  SELECT especie_id \n" +
            "  FROM especie_vector_contagiado evc2 \n" +
            "  JOIN vector v2 ON v2.id = evc2.vector_id\n" +
            "  WHERE v2.tipo = 2\n" +
            "  AND especie_id IN (\n" +
            "    SELECT especie_id \n" +
            "    FROM especie_vector_contagiado evc3 \n" +
            "    JOIN vector v3 ON v3.id = evc3.vector_id\n" +
            "    WHERE v3.tipo = 0\n" +
            "  )\n" +
            ")\n" +
            "group by e.id, e.nombre, e.pais_de_origen, e.id_patogeno\n" +
            "order by count(e.id) desc\n" +
            "limit 10 ", nativeQuery = true )
    fun lideres(@Param("tipoa") tipoA: TipoDeVector = TipoDeVector.Persona,
                @Param("tipob") tipoB: TipoDeVector = TipoDeVector.Animal): List<Especie>

}