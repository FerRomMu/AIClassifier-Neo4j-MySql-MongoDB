package ar.edu.unq.eperdemic.modelo

import java.io.Serializable
import javax.persistence.*

@Entity
class Especie(patogenoParam: Patogeno,
              var nombre: String,
              var paisDeOrigen: String):Serializable  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long? = null

    @ManyToOne
    @JoinColumn(name="id_patogeno")
    val patogeno: Patogeno = patogenoParam

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    val vectores: MutableSet<Vector> = HashSet()


    fun capacidadDeContagioA(tipoVictima : TipoDeVector): Int {
        return this.patogeno.capacidadDeContagioA(tipoVictima)
    }
    fun agregarVector(vector:Vector){
        vectores.add(vector)
    }

}
