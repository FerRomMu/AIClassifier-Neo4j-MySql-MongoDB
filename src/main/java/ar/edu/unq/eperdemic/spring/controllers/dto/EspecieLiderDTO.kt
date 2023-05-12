package ar.edu.unq.eperdemic.spring.controllers.dto


class EspecieLiderDTO(val especie_nombre: String?, val especie_patogeno: Long?, val cantidadInfectados: Int?, val esPandemia: Boolean?) {

    companion object {
        fun desdeModelo(especieNombre: String?, patogenoId: Long?, cantidadDeInfectados: Int?, esPandemia: Boolean?) : EspecieLiderDTO {
            return EspecieLiderDTO(especieNombre , patogenoId ,cantidadDeInfectados , esPandemia)
        }
    }

}
