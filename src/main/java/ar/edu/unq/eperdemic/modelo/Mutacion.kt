package ar.edu.unq.eperdemic.modelo

import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "mutacion")
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

    fun estaEn(mutaciones: MutableSet<Mutacion>): Boolean {
        return mutaciones.stream().anyMatch { m -> m.equals(this) }
    }

    open fun impideContagioDe(especie: Especie) : Boolean {
        return false
    }

    abstract fun equals(mutacion: Mutacion) : Boolean
    abstract fun permitoContagiarATipo(tipo: TipoDeVector) : Boolean
    open fun surtirEfectoEn(vector: Vector) {}
}

@Entity
@Table(name = "supresionBiomecanica")
class SupresionBiomecanica(val potencia: Int) : Mutacion() {

    override fun equals(mutacion: Mutacion) : Boolean{
        return mutacion.compararPorPotencia(this)
    }

    override fun compararPorPotencia(mutacion: Mutacion) : Boolean{
        mutacion as SupresionBiomecanica
        return mutacion.potencia  == this.potencia
    }

    override fun permitoContagiarATipo(tipo: TipoDeVector): Boolean {
        return false
    }

    override fun surtirEfectoEn(vector: Vector) {
        vector.eliminarEspeciesPorSupresion(this.potencia)
    }

    override fun impideContagioDe(especie:Especie): Boolean {
        return this.potencia > especie.defensa()
    }

}

@Entity
@Table(name = "bioalteracionGenetica")
class BioalteracionGenetica(val tipoVectorContagiable: TipoDeVector) : Mutacion() {

    override fun equals(mutacion: Mutacion) : Boolean{
        return mutacion.compararPorTipo(this)
    }

    override fun compararPorTipo(mutacion: Mutacion) : Boolean{
        mutacion as BioalteracionGenetica
        return mutacion.tipoVectorContagiable  == this.tipoVectorContagiable
    }

    override fun permitoContagiarATipo(tipo: TipoDeVector): Boolean {
        return this.tipoVectorContagiable == tipo
    }

}


