package ar.edu.unq.eperdemic.utils

interface DataService {
    /**
     * Crea un set de datos iniciales.
     */
    fun crearSetDeDatosIniciales()

    /**
     * Elimina toda la información persistida en la base de datos.
     */
    fun eliminarTodo()
}