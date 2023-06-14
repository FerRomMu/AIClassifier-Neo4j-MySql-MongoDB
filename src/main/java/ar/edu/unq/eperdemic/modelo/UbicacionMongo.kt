package ar.edu.unq.eperdemic.modelo

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed


@Document("Ubicacion")
class UbicacionMongo{

    @Id
    var id: String? = null

    @Indexed(unique = true)
    var nombre: String? = null

    lateinit var coordenada: Coordenada
    var hayAlgunInfectado : Boolean = false

    protected constructor() {}

    constructor(coordenada: Coordenada, nombre: String) {
        this.coordenada = coordenada
        this.nombre = nombre
    }
}