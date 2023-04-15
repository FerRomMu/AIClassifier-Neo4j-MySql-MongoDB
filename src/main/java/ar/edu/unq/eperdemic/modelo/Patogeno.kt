package ar.edu.unq.eperdemic.modelo

import java.io.Serializable
import javax.persistence.*

@Entity
class Patogeno(var tipo: String) : Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long? = null

    fun cantidadDeEspecies() : Int {
        return this.especies.size
    }

    override fun toString(): String {
        return tipo
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], mappedBy = "patogeno")
    val especies: MutableList<Especie> = mutableListOf()

    fun crearEspecie(nombreEspecie: String, paisDeOrigen: String) : Especie {
        val especie = Especie(nombreEspecie, paisDeOrigen, this)
        this.especies.add(especie)

        return especie
    }
}