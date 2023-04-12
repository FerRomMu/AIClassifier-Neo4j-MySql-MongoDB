package ar.edu.unq.eperdemic.modelo

import org.aspectj.weaver.GeneratedReferenceTypeDelegate
import javax.persistence.*

@Entity
class Vector(var tipo: TipoDeVector) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long? = null

    @ManyToOne
    lateinit var ubicacion: Ubicacion

    @OneToMany(mappedBy = "id", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var especiesContagiadas: MutableSet<Especie> = HashSet()

    fun agregarEspecie(especie: Especie) {
        especiesContagiadas.add(especie)
    }
}


enum class TipoDeVector {
    Persona, Insecto, Animal
}