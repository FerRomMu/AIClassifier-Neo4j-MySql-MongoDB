package ar.edu.unq.eperdemic.persistencia.repository.neo

import ar.edu.unq.eperdemic.modelo.UbicacionNeo
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.stereotype.Repository

@Repository
interface UbicacionNeoRepository : Neo4jRepository<UbicacionNeo, Long> {

    fun findByNombre(nombre: String): UbicacionNeo

    @Query("""
        Match(u:UbicacionNeo)
        Match(upartida:UbicacionNeo {nombre: ${'$'}nombreDeUbicacion })
        Match(upartida)-[caminos]->(u)
        return u
    """)
    fun conectados(nombreDeUbicacion:String): List<UbicacionNeo>

    //fun moverMasCorto(vectorId:Long, nombreDeUbicacion:String)

}