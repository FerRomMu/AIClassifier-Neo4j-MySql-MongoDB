package ar.edu.unq.eperdemic.spring.controllers.dto

import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector


class VectorDTO(val tipoDeVector : TipoDeVector,
                val ubicacion: Ubicacion) {

    fun aModelo() : Vector {
        val vector = Vector(this.tipoDeVector)
        vector.ubicacion = this.ubicacion
        return vector
    }

    companion object {
        fun desdeModelo(vector: Vector): VectorDTO = VectorDTO(vector.tipo, vector.ubicacion)
    }

}