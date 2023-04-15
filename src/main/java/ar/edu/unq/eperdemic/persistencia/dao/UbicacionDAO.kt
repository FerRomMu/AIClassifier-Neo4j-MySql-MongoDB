package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.Ubicacion


interface UbicacionDAO {


    fun guardar(entity: Ubicacion)

    fun recuperar(id: Long?): Ubicacion

    fun recuperarTodos(): List<Ubicacion>

    fun vectoresDeY(ubicacionId: Long?,vectorId: Long?): Collection<Vector>
}