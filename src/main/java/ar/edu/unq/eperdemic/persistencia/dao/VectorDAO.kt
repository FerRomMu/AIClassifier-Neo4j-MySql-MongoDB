package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.Vector

interface VectorDAO {

    fun guardar(entity: Vector)

    fun recuperar(id: Long?): Vector

    fun recuperarTodos(): List<Vector>

}