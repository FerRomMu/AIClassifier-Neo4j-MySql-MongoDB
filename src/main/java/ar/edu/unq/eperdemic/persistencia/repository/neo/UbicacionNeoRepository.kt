package ar.edu.unq.eperdemic.persistencia.repository.neo

import ar.edu.unq.eperdemic.modelo.UbicacionNeo
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository

@Repository
interface UbicacionNeoRepository : Neo4jRepository<UbicacionNeo, Long> {

    fun findByNombre(nombreDeUbicacion:String) : UbicacionNeo
   // fun conectados(nombreDeUbicacion:String): List<UbicacionNeo>

   // fun moverMasCorto(vectorId:Long, nombreDeUbicacion:String)
}