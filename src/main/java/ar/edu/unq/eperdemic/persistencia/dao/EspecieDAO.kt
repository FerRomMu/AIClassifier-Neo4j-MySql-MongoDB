package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.Especie

interface EspecieDAO {

    fun guardar(entity: Especie)
    fun recuperar(id: Long?) : Especie
    fun recuperarTodos(): List<Especie>
    fun cantidadDeInfectados(especieId: Long): Int
}