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

        fun puedeTransitar(tipo: TipoDeVector): Boolean {
            return when (this) {
                TipoDeCamino.CaminoTerreste -> tipo.esPersona() || tipo.esAnimal() || tipo.esInsecto()
                TipoDeCamino.CaminoMaritimo -> tipo.esPersona() || tipo.esAnimal()
                TipoDeCamino.CaminoAereo -> tipo.esInsecto() || tipo.esAnimal()
            }
        }
    }
}