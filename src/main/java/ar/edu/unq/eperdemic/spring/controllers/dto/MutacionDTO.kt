package ar.edu.unq.eperdemic.spring.controllers.dto

import ar.edu.unq.eperdemic.modelo.BioalteracionGenetica
import ar.edu.unq.eperdemic.modelo.Mutacion
import ar.edu.unq.eperdemic.modelo.SupresionBiomecanica

class MutacionDTO(

    val tipoDeMutacion: TipoDeMutacion,
    val especieId: Long,
    val tipoDeVector : TipoDeVector?,
    val poderDeMutacion: Int?) {

    enum class TipoDeVector {
        Persona, Insecto, Animal
    }
    enum class TipoDeMutacion {
        Supresion_Biomecanica, Bioalteracion_Genetica,
    }

    fun aModelo() : Mutacion {
        return if(this.tipoDeMutacion == TipoDeMutacion.Supresion_Biomecanica) {
            SupresionBiomecanica(this.poderDeMutacion!!)
        } else {
            BioalteracionGenetica(
                when(this.tipoDeVector){
                    TipoDeVector.Persona -> ar.edu.unq.eperdemic.modelo.TipoDeVector.Persona
                    TipoDeVector.Animal -> ar.edu.unq.eperdemic.modelo.TipoDeVector.Animal
                    TipoDeVector.Insecto -> ar.edu.unq.eperdemic.modelo.TipoDeVector.Insecto
                    else -> throw Exception("Tipo invalido")
                })
        }
    }


    companion object {
        fun desdeModelo(mutacion:Mutacion, especieId: Long) = MutacionDTO(
            this.classToEnum(mutacion),
            especieId,
            this.tipo(mutacion),
            this.poderDeMutacion(mutacion)
        )

        fun classToEnum(mutacion: Mutacion): TipoDeMutacion = if (mutacion is SupresionBiomecanica)
                                                                { TipoDeMutacion.Supresion_Biomecanica }
                                                                else { TipoDeMutacion.Bioalteracion_Genetica }

        fun tipo(mutacion: Mutacion): TipoDeVector? {
            if(mutacion is BioalteracionGenetica) {
                return when(mutacion.tipoVectorContagiable) {
                    ar.edu.unq.eperdemic.modelo.TipoDeVector.Persona -> TipoDeVector.Persona
                    ar.edu.unq.eperdemic.modelo.TipoDeVector.Animal -> TipoDeVector.Animal
                    ar.edu.unq.eperdemic.modelo.TipoDeVector.Insecto -> TipoDeVector.Insecto
                }
            }else {
                return null
            }
        }

        fun poderDeMutacion(mutacion: Mutacion): Int? {
            if(mutacion is SupresionBiomecanica) {
                return mutacion.potencia
            } else {
                return null
            }
        }
    }

}