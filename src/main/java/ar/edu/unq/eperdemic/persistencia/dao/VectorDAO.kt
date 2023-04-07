package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.Vector

interface VectorDAO {
    
    fun crear(vector: Vector): Vector

    fun recuperar(vector: Vector): Vector

    fun recuperarTodos(): List<Vector>

}