package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.Patogeno

interface PatogenoDAO {
    fun guardar(entity: Patogeno)
    //fun actualizar(patogeno: Patogeno )
    fun recuperar(id: Long?): Patogeno
    //fun recuperarATodos() : List<Patogeno>
}