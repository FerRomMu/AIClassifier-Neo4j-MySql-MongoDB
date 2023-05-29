package ar.edu.unq.eperdemic.modelo

import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship

@Node
class UbicacionNeo(name:String) {

    @Id
    @GeneratedValue
    var id: Long? = null

    var idUbicacion: Long? = null

    @Relationship(type = "caminos")
    var caminos : MutableList<Camino> = mutableListOf()

    var nombre: String = name

    fun esLaUbicacion(name : String):Boolean {
        return this.nombre == name
    }


}