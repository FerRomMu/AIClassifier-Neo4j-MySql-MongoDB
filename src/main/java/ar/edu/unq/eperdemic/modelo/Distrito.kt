package ar.edu.unq.eperdemic.modelo

import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import javax.persistence.Id

@Document("Distrito")
class Distrito {

    @Id
    var id: String? = null
    @Indexed(unique = true)
    var nombre: String? = null
    var coordenadas: MutableList<Coordenada> = mutableListOf()

    protected constructor() {}

    constructor(nombreD: String, coordenadasList: List<Coordenada>) {
        coordenadas = coordenadasList.toMutableList()
        nombre = nombreD
    }

}