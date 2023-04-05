package ar.edu.unq.eperdemic.modelo

import java.io.Serializable

@Entity
@Table(name="Patogeno")
class Patogeno(var tipo: String) : Serializable{

    @Id
    @Column(name="Id")
    var id : Long? = null

    @Column(name="CantidadDeEspecies")
    var cantidadDeEspecies: Int = 0

    override fun toString(): String {
        return tipo
    }

    fun crearEspecie(nombreEspecie: String, paisDeOrigen: String) : Especie {
        cantidadDeEspecies++
        return Especie(this, nombreEspecie, paisDeOrigen)
    }
}