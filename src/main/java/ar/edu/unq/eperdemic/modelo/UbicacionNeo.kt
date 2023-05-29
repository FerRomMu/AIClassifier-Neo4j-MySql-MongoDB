package ar.edu.unq.eperdemic.modelo

import org.springframework.data.neo4j.core.schema.Node

import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Relationship
import javax.persistence.Column

@Node
class UbicacionNeo(var nombre: String) {

    @Id
    @GeneratedValue
    var id: Long? = null

    @Relationship(type = "caminos")
    var caminos : MutableList<Camino> = mutableListOf()

    fun agregarCamino(camino: Camino) {
        this.caminos.add(camino)
    }
}