package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.Especie

interface EstadisticaDAO {
    fun especieLider(): Especie
    fun lideres(): List<Especie>
}