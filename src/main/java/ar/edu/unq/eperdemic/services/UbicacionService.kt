package ar.edu.unq.eperdemic.services

import ar.edu.unq.eperdemic.modelo.Camino
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.UbicacionNeo
import ar.edu.unq.eperdemic.modelo.Vector

interface UbicacionService {

    fun mover(vectorId: Long, ubicacionid: Long)
    fun expandir(ubicacionId: Long)
    /* Operaciones CRUD*/
    fun crearUbicacion(nombreUbicacion: String): Ubicacion
    fun recuperar(id: Long): Ubicacion
    fun recuperarTodos(): List<Ubicacion>
    fun vectoresEn(id: Long): List<Vector>
    fun conectar(nombreDeUbicacion1:String, nombreDeUbicacion2:String, tipoCamino:Camino.TipoDeCamino)
    fun conectados(nombreDeUbicacion:String): List<UbicacionNeo>
    fun moverMasCorto(vectorId: Long, nombreDeUbicacion: String)

}