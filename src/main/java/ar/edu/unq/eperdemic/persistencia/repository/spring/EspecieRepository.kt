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

    @Query("select es \n" +
            "from Vector v \n" +
            "join Especie es \n" +
            "where v.tipo = :tipo\n" +
            "group by es.id \n" +
            "order by count(es.id) \n" +
            "desc")
    fun especieLider(@Param("tipo") tipo: TipoDeVector = TipoDeVector.Persona): Especie

}