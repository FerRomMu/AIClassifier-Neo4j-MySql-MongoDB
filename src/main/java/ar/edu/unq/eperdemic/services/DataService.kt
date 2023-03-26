package ar.edu.unq.eperdemic.services

interface DataService {

    /**
     * Elimina toda la información persistida en la base de datos.
     */
    fun deleteAll()

    /**
     * Crea un set de datos iniciales.
     */
    fun crearSetDatosIniciales()
}