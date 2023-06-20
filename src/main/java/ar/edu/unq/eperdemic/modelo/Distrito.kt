package ar.edu.unq.eperdemic.modelo

import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon
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
    var polygon: GeoJsonPolygon? = null

    protected constructor() {}

    constructor(nombreD: String, coordenadasList: List<Coordenada>) {
        coordenadas = coordenadasList.toMutableList()
        coordenadas.add(coordenadas.first())
        polygon = GeoJsonPolygon(coordenadas.map { c -> Point(c.latitud, c.longitud)})
        nombre = nombreD
    }

}