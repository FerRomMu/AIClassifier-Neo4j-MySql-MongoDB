package ar.edu.unq.eperdemic.spring.controllers.dto

import ar.edu.unq.eperdemic.modelo.Ubicacion

class UbicacionDTO(val nombreDeLaUbicacion: String,
                   val patogenoId: Long?) {

//    TODO: implementar aModelo
//    fun aModelo() : Vector {
//        return null
//    }

    companion object {
        fun desdeModelo(ubicacion: Ubicacion) : UbicacionDTO {
            return UbicacionDTO(ubicacion.nombre,ubicacion.id)
        }
    }

}