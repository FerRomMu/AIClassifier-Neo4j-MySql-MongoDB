package ar.edu.unq.eperdemic.modelo

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "especie")
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
    @JoinTable(name = "especie_vector_contagiado",
        joinColumns = [JoinColumn(name = "especie_id")],
        inverseJoinColumns = [JoinColumn(name = "vector_id")])
    val vectores: MutableSet<Vector> = HashSet()

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var mutacionesPosibles : MutableSet<Mutacion> = HashSet()


    fun capacidadDeContagioA(tipoVictima : TipoDeVector): Int {
        return this.patogeno.capacidadDeContagioA(tipoVictima)
    }
    fun agregarVector(vector:Vector){
        vectores.add(vector)
    }

    fun agregarMutacion(mutacion:Mutacion){
        this.mutacionesPosibles.add(mutacion)
        mutacion.definirEspecie(this)
    }

    fun intentarAgregarMutacion(mutacionAAgregar:Mutacion){
        val existe  = this.mutacionesPosibles.stream().anyMatch{m -> m.equals(mutacionAAgregar)}
        if (existe.not()){
            this.agregarMutacion(mutacionAAgregar)
        }
    }

    fun capacidadDeBiomecanizacion() : Int {
        return this.patogeno.getCapacidadDeBiomecanizacion()
    }

    fun esMismaEspecie(especie: Especie?): Boolean{
        return this.nombre == especie!!.nombre
    }

    fun defensa(): Int{
        return this.patogeno.capacidadDeDefensa()
    }

}
