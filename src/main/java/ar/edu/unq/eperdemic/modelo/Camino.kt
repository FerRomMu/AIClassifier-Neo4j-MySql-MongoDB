package ar.edu.unq.eperdemic.modelo

import org.springframework.data.neo4j.core.schema.RelationshipId
import org.springframework.data.neo4j.core.schema.RelationshipProperties
import org.springframework.data.neo4j.core.schema.TargetNode

@RelationshipProperties
class Camino(@TargetNode var ubicacioDestino: UbicacionNeo, val tipo: TipoDeCamino) {

    @RelationshipId
    var id: Long? = null

    enum class TipoDeCamino {
        CaminoTerreste, CaminoMaritimo, CaminoAereo;
    }

}