package ar.edu.unq.eperdemic.spring.controllers.dto

import ar.edu.unq.eperdemic.modelo.Coordenada
import ar.edu.unq.eperdemic.modelo.Distrito

class DistritoDTO(val nombre: String, val coordenadas: MutableList<Coordenada>) {

    fun aModelo(): Distrito = Distrito(this.nombre, this.coordenadas)

    companion object {
        fun desdeModelo(distrito: Distrito): DistritoDTO = DistritoDTO(distrito.nombre!!, distrito.coordenadas)
    }

}