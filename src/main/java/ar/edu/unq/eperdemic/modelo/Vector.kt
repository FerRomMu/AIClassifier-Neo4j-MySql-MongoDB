package ar.edu.unq.eperdemic.modelo

import org.aspectj.weaver.GeneratedReferenceTypeDelegate
import javax.persistence.*

@Entity
@Table(name = "vector")
class Vector(var tipo: TipoDeVector) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long? = null

    @ManyToOne
    lateinit var ubicacion: Ubicacion

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var especiesContagiadas: MutableSet<Especie> = HashSet()

    fun agregarEspecie(especie: Especie) {
        especiesContagiadas.add(especie)
    }

    fun intentarInfectar(vectorAContagiar: Vector){
        var especiesAContagiar = this.especiesContagiadas
        for (especie in especiesAContagiar){
            this.intentarContagiarA(vectorAContagiar,especie)
        }
    }

    private fun intentarContagiarA(vectorAContagiar: Vector,especieAContagiar: Especie){
        if (vectorAContagiar.puedoSerContagiadoPor(this) && this.haySuerte(especieAContagiar,vectorAContagiar.tipo) ){
            vectorAContagiar.agregarEspecie(especieAContagiar)
        }
    }

    private fun haySuerte (especieAContagiar: Especie,tipoVictima : TipoDeVector): Boolean{
        val dado = Randomizador.getInstance()
        var numeroContagio = dado.valor(1,10) + especieAContagiar.capacidadDeContagioA(tipoVictima)
        var numeroRuleta = dado.valor(1,100)
        return numeroContagio >= numeroRuleta
    }

    private fun puedoSerContagiadoPor(vectorQueMeIntentaContagiar :Vector): Boolean{
        return this.tipo.puedeContagiarme(vectorQueMeIntentaContagiar.tipo)
    }

    fun eliminarEspeciesPorSupresion(potencia: Int) {
        TODO()
    }


}

enum class TipoDeVector {
    Persona, Insecto, Animal;

    fun puedeContagiarme(tipo: TipoDeVector) : Boolean {
        return when(this){
            Persona -> true
            Insecto -> tipo.esInsecto().not()
            Animal -> tipo.esInsecto()
        }
    }

    fun esInsecto(): Boolean {
        return this == Insecto
    }

    fun esPersona(): Boolean {
        return this == Persona
    }

}