package ar.edu.unq.eperdemic.modelo

import org.springframework.data.mongodb.core.geo.GeoJsonPoint

class Coordenada(val latitud: Double, val longitud: Double) {

    fun toGeoJsonPoint(): GeoJsonPoint {
        return GeoJsonPoint(longitud, latitud)
    }

    fun perteneceA(coordenadas: MutableList<Coordenada>): Boolean {
        return coordenadas.any { c -> c.equals(this) }
    }

    fun equals(coordenada: Coordenada): Boolean {
        return coordenada.latitud == this.latitud && coordenada.longitud == this.longitud
    }

}