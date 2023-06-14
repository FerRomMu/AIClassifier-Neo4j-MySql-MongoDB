package ar.edu.unq.eperdemic.services

import ar.edu.unq.eperdemic.modelo.*

interface UbicacionService {

    fun mover(vectorId: Long, ubicacionid: Long)
    fun expandir(ubicacionId: Long)
    /* Operaciones CRUD*/
    fun crearUbicacion(nombreUbicacion: String, coordenada: Coordenadagi): Ubicacion
    fun recuperar(id: Long): Ubicacion
    fun recuperarTodos(): List<Ubicacion>
    fun vectoresEn(id: Long): List<Vector>
    fun conectar(nombreDeUbicacion1:String, nombreDeUbicacion2:String, tipoCamino:Camino.TipoDeCamino)
    fun conectados(nombreDeUbicacion:String): List<Ubicacion>
    fun moverMasCorto(vectorId: Long, nombreDeUbicacion: String)

}