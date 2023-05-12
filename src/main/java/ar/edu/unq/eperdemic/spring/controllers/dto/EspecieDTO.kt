package ar.edu.unq.eperdemic.spring.controllers.dto

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno

class EspecieDTO(
    val nombre: String,
    val paisDeOrigen: String,
    val patogenoId: Long?
) {

    fun aModelo(patogeno: Patogeno) : Especie = Especie(patogeno, this.nombre, this.paisDeOrigen)


companion object {
    fun desdeModelo(especie: Especie) : EspecieDTO {
        return EspecieDTO(especie.nombre, especie.paisDeOrigen, especie.patogeno.id)
    }
}
}
