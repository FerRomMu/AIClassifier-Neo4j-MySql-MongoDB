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

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var mutacionesSufridas : MutableSet<Mutacion> = HashSet()

    fun agregarEspecie(especie: Especie) {
        especiesContagiadas.add(especie)
        especie.agregarVector(this)
    }

    fun intentarInfectar(vectorAContagiar: Vector){
        var especiesAContagiar = this.especiesContagiadas
        for (especie in especiesAContagiar){
            this.intentarContagiarA(vectorAContagiar,especie)
        }
    }

    private fun intentarContagiarA(vectorAContagiar: Vector,especieAContagiar: Especie){
        if (condicionParaInfectar(vectorAContagiar,especieAContagiar)){
            vectorAContagiar.agregarEspecie(especieAContagiar)
            this.intentarMutar(especieAContagiar)
        }
    }

    private fun condicionParaInfectar(vectorAContagiar: Vector,especieAContagiar: Especie): Boolean{
        return vectorAContagiar.puedoSerContagiadoPor(this) &&
                this.haySuerte(especieAContagiar,vectorAContagiar.tipo)
                !vectorAContagiar.sobrepasaMutaciones(especieAContagiar)
    }

    private fun sobrepasaMutaciones(especieAContagiar: Especie): Boolean {
        return this.mutacionesSufridas.stream().anyMatch { m -> m.impideContagio(especieAContagiar) }
    }

    private fun haySuerte (especieAContagiar: Especie,tipoVictima : TipoDeVector): Boolean{
        val dado = Randomizador.getInstance()
        var numeroContagio = dado.valor(1,10) + especieAContagiar.capacidadDeContagioA(tipoVictima)
        var numeroRuleta = dado.valor(1,100)
        return numeroContagio >= numeroRuleta
    }

    private fun puedoSerContagiadoPor(vectorQueMeIntentaContagiar :Vector): Boolean{
        return this.tipo.puedeContagiarme(vectorQueMeIntentaContagiar.tipo)  ||
                vectorQueMeIntentaContagiar.tengoMutacionParaContagiarATipo(this.tipo)
    }

    private fun tengoMutacionParaContagiarATipo (tipoAVer: TipoDeVector) : Boolean{
        return this.mutacionesSufridas.stream().anyMatch { m -> m.permitoContagiarATipo(tipoAVer) }
    }

    private fun intentarMutar(especieAMutar : Especie){
        var mutacionesPosibles = this.mutacionesNuevas(especieAMutar)
        if (this.haySuerteMutacion(especieAMutar) && mutacionesPosibles.isNotEmpty()){
            val mutacionAMutar=  mutacionesPosibles.random()
            mutacionAMutar.surtirEfectoEn(this)
            this.mutacionesSufridas.add(mutacionAMutar)
        }
    }

    private fun mutacionesNuevas(especieAMutar: Especie): MutableSet<Mutacion>  {
        var mutacionesDelVector = this.mutacionesSufridas
        var mutacionesDeEspecie = especieAMutar.mutacionesPosibles

        return mutacionesDeEspecie.stream().filter {me -> me.estaEn(mutacionesDelVector).not()}
    }

    private fun haySuerteMutacion(especieAMutar: Especie): Boolean{
        val dado = Randomizador.getInstance()
        var numeroContagio = especieAMutar.capacidadDeBiomecanizacion
        var numeroRuleta = dado.valor(1,100)
        return numeroContagio >= numeroRuleta
    }

    fun eliminarEspeciesPorSupresion(potencia: Int) {
        this.especiesContagiadas.stream().forEach { es -> this.eliminarPorSupresion(es, potencia) }
    }

    private fun eliminarPorSupresion(especie: Especie, potencia: Int) {
        if (especie.defensa >= potencia) {
            this.eliminarEspecie(especie)
        }
    }

    private fun eliminarEspecie(especie: Especie) {
        this.especiesContagiadas.remove(especie)
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