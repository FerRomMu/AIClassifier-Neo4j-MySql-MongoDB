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

}