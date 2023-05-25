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


    @Query("select e.id, e.nombre, e.pais_de_origen, e.id_patogeno from especie_vector_contagiado evc\n" +
            "join vector v on v.tipo = :tipo\n" +
            "join especie e on e.id = evc.especie_id\n" +
            "group by e.id, e.nombre, e.pais_de_origen, e.id_patogeno\n" +
            "order by count(e.id) desc\n" +
            "limit 1 ", nativeQuery = true )
    fun especieLider(@Param("tipo") tipo: TipoDeVector = TipoDeVector.Persona): Especie

    @Query( "select es \n" +
            "from Vector v \n" +
            "join v.especiesContagiadas es \n" +
            "where v.tipo = :tipoa and v.tipo = :tipob\n" +
            "group by es.id \n" +
            "order by count(es.id) \n" +
            "desc")
    fun lideres(@Param("tipoa") tipoA: TipoDeVector = TipoDeVector.Persona,
                @Param("tipob") tipoB: TipoDeVector = TipoDeVector.Animal,
                pageable: Pageable = PageRequest.of(0, 10)
    ): List<Especie>

}