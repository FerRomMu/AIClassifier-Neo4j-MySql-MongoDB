package ar.edu.unq.eperdemic.services

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.ReporteDeContagios

interface EstadisticaService {

    fun especieLider(): Especie
    fun reporteDeContagios(nombreDeLaUbicacion: String): ReporteDeContagios
    fun lideres(): List<Especie>

}