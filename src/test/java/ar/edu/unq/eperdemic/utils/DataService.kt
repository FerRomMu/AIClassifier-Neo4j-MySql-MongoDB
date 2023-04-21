package ar.edu.unq.eperdemic.utils

import ar.edu.unq.eperdemic.modelo.Especie

interface DataService {
    /**
     * Crea un set de datos iniciales.
     */
    fun crearSetDeDatosIniciales()

    /***
     * Crea un set de datos donde hay una pandemia
     * y devuelve la especie que la causa
     */
    fun crearPandemiaPositiva(): Especie

    /**
     * Elimina toda la información persistida en la base de datos.
     */
    fun eliminarTodo()

    /**
     * Persiste todas las entidades y las devuelve.
     */
    fun persistir(entidades: List<Any>): List<Any>
    fun persistir(entidad: Any): Any
}