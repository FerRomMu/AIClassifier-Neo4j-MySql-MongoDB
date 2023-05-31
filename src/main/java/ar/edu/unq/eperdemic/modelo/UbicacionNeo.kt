package ar.edu.unq.eperdemic.modelo

import org.springframework.data.neo4j.core.schema.Node

import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Relationship

@Node
class UbicacionNeo(var nombre: String) {

    @Id
    @GeneratedValue
    var id: Long? = null

    fun agregarCamino(camino: Camino) {
        this.caminos.add(camino)
    }

    @Relationship(type = "caminos")
    var caminos : MutableList<Camino> = mutableListOf()

    fun esLaUbicacion(name : String):Boolean {
        return this.nombre == name
    }

}