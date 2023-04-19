package ar.edu.unq.eperdemic.services

import ar.edu.unq.eperdemic.modelo.Especie

interface EstadisticaService {

    fun especieLider(): Especie

}