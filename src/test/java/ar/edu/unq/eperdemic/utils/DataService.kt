package ar.edu.unq.eperdemic.utils

import ar.edu.unq.eperdemic.modelo.Especie

interface DataService {
    /**
     * Crea un set de datos iniciales.
     */
    fun crearSetDeDatosIniciales()
    fun crearPandemiaPositiva(): Especie

    /**
     * Elimina toda la información persistida en la base de datos.
     */
    fun eliminarTodo()
}