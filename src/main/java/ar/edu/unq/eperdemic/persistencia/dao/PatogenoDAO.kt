package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.Vector

interface PatogenoDAO {
    fun guardar(entity: Patogeno)
    fun recuperar(id: Long?): Patogeno
    fun recuperarTodos() : List<Patogeno>
}