package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector

interface VectorDAO {

    fun guardar(entity: Vector)

    fun recuperar(id: Long?): Vector

    fun recuperarTodos(): List<Vector>

    fun enfermedades(id: Long?): List<Especie>

    fun borrar(id: Long?)

    fun vectorAleatorioEn(ubicacionId: Long): Vector

}