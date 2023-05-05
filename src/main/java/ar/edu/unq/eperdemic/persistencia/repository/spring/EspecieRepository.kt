package ar.edu.unq.eperdemic.persistencia.repository.spring

import ar.edu.unq.eperdemic.modelo.Especie
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
}