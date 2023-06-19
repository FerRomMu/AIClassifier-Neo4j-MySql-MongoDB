package ar.edu.unq.eperdemic.modelo

import org.springframework.data.mongodb.core.geo.GeoJsonPoint

class Coordenada(val latitud: Double, val longitud: Double) {

    fun toGeoJsonPoint(): GeoJsonPoint {
        return GeoJsonPoint(longitud, latitud)
    }

}