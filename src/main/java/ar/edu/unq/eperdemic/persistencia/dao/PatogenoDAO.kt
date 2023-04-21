package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno

interface PatogenoDAO {
    fun guardar(entity: Patogeno)
    fun recuperar(id: Long?): Patogeno
    fun recuperarTodos() : List<Patogeno>
    fun especiesDePatogeno(id: Long?): List<Especie>
    fun esPandemia(especieId: Long): Boolean
}