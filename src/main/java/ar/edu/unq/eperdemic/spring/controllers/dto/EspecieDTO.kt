package ar.edu.unq.eperdemic.spring.controllers.dto

import ar.edu.unq.eperdemic.modelo.Especie

class EspecieDTO(
    val nombre: String,
    val paisDeOrigen: String,
    val patogenoId: Long?
) {

// TODO: Implementar aModelo
//        fun aModelo() : Especie {
//        return null
//   }


companion object {
    fun desdeModelo(especie: Especie) : EspecieDTO {
        return EspecieDTO(especie.nombre, especie.paisDeOrigen, especie.patogeno.id)
    }
}
}
