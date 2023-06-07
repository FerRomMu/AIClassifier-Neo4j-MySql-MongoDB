package ar.edu.unq.eperdemic.persistencia.repository.neo

import ar.edu.unq.eperdemic.modelo.Camino
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

    @Query("""
        Match(destino:UbicacionNeo {nombre: ${'$'}ubicacionDestino})
        Match(partida:UbicacionNeo {nombre: ${'$'}ubicacionPartida})
        Match (u: UbicacionNeo)
        Match p=shortestPath((partida)-[*]->(destino))
        WHERE all(r in relationships(p) WHERE r.tipo IN ["CaminoTerreste",${'$'}tipoDeCamino1, ${'$'}tipoDeCamino2])
        return p
    """)
    fun caminoMasCorto(ubicacionPartida:String,
                       ubicacionDestino:String,
                       tipoDeCamino1: Camino.TipoDeCamino,
                       tipoDeCamino2: Camino.TipoDeCamino): List<UbicacionNeo>

    @Query("""
        Match(udestino:UbicacionNeo {nombre: ${'$'}ubicacionDestino})
        Match(upartida:UbicacionNeo {nombre: ${'$'}ubicacionPartida})
        OPTIONAL MATCH(upartida)-[caminos]->(udestino)
        WITH caminos, ${'$'}caminosValidos AS tipos
        RETURN
          CASE
            WHEN caminos IS NOT NULL AND caminos.tipo IN tipos THEN 0
            WHEN caminos IS NULL THEN 1
            ELSE 2
            END AS r
    """)
    fun validarMovimiento(ubicacionPartida:String, ubicacionDestino: String, caminosValidos:List<Camino.TipoDeCamino>): Int
}