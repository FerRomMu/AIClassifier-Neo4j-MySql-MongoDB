package ar.edu.unq.eperdemic.modelo

import org.springframework.data.neo4j.core.schema.RelationshipId
import org.springframework.data.neo4j.core.schema.RelationshipProperties
import org.springframework.data.neo4j.core.schema.TargetNode

@RelationshipProperties
class Camino(@TargetNode val ubicacioDestino: Ubicacion, val tipo: TipoDeCamino) {

    @RelationshipId
    var id: Long? = null

    fun puedePasar(vector: Vector): Boolean {
        return this.tipo.puedeTransitar(vector.tipo)
    }

    fun llegaA(ubicacionAMover: Ubicacion): Boolean {
        return this.ubicacioDestino.equals(ubicacionAMover)
    }

    fun equals(caminoAComparar: Camino): Boolean {
        return this.ubicacioDestino == caminoAComparar.ubicacioDestino &&
                this.tipo == caminoAComparar.tipo
    }

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