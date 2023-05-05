package ar.edu.unq.eperdemic.modelo

import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
abstract class Mutacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long? = null


    open fun compararPorPotencia(mutacion: Mutacion) : Boolean{
        return false
    }

    open fun compararPorTipo(mutacion: Mutacion): Boolean{
        return false
    }

    abstract fun equals(mutacion: Mutacion) : Boolean


}


class SupresionBiomecanica(val potencia: Int) : Mutacion() {

    override fun equals(mutacion: Mutacion) : Boolean{
        return mutacion.compararPorPotencia(this)
    }

    override fun compararPorPotencia(mutacion: Mutacion) : Boolean{
        mutacion as SupresionBiomecanica
        return mutacion.potencia  == this.potencia
    }

}


class BioalteracionGenetica(val tipoVectorContagiable: TipoDeVector) : Mutacion() {

    override fun equals(mutacion: Mutacion) : Boolean{
        return mutacion.compararPorTipo(this)
    }

    override fun compararPorTipo(mutacion: Mutacion) : Boolean{
        mutacion as BioalteracionGenetica
        return mutacion.tipoVectorContagiable  == this.tipoVectorContagiable
    }

}


