package ar.edu.unq.eperdemic.modelo

import org.aspectj.weaver.GeneratedReferenceTypeDelegate
import javax.persistence.*
import kotlin.streams.toList

@Entity
@Table(name = "vector")
class Vector(var tipo: TipoDeVector) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long? = null

    @ManyToOne
    lateinit var ubicacion: Ubicacion

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, mappedBy = "vectores")
    var especiesContagiadas: MutableSet<Especie> = HashSet()

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var mutacionesSufridas : MutableSet<Mutacion> = HashSet()

    fun agregarEspecie(especie: Especie) {
        especiesContagiadas.add(especie)
        especie.agregarVector(this)
    }

    fun intentarInfectar(vectorAContagiar: Vector){
        val especiesAContagiar = this.especiesContagiadas
        for (especie in especiesAContagiar){
            this.intentarContagiarA(vectorAContagiar,especie)
        }
    }

    private fun intentarContagiarA(vectorAContagiar: Vector,especieAContagiar: Especie){
        if (this.condicionParaInfectar(vectorAContagiar,especieAContagiar)){
            vectorAContagiar.agregarEspecie(especieAContagiar)
            this.intentarMutar(especieAContagiar)
        }
    }

    private fun condicionParaInfectar(vectorAContagiar: Vector,especieAContagiar: Especie): Boolean{
        return vectorAContagiar.puedoSerContagiadoPor(this,especieAContagiar) &&
                this.haySuerte(especieAContagiar,vectorAContagiar.tipo) &&
                !vectorAContagiar.sobrepasaPorMutaciones(especieAContagiar)
    }

    private fun sobrepasaPorMutaciones(especieAContagiar: Especie): Boolean {
        return this.mutacionesSufridas.stream().anyMatch { m -> m.impideContagioDe(especieAContagiar) }
    }

    private fun haySuerte (especieAContagiar: Especie,tipoVictima : TipoDeVector): Boolean{
        val dado = Randomizador.getInstance()
        val numeroContagio = dado.valor(1,10) + especieAContagiar.capacidadDeContagioA(tipoVictima)
        val numeroRuleta = dado.valor(1,100)
        return numeroContagio >= numeroRuleta
    }

    private fun puedoSerContagiadoPor(vectorQueMeIntentaContagiar :Vector, especieAContagiar: Especie): Boolean{
        return this.tipo.puedeContagiarme(vectorQueMeIntentaContagiar.tipo)  ||
                vectorQueMeIntentaContagiar.tengoMutacionParaContagiarATipoCon(this.tipo,especieAContagiar)
    }

    private fun tengoMutacionParaContagiarATipoCon(tipoAVer: TipoDeVector, especieAContagiar: Especie) : Boolean{
        return this.mutacionesSufridas.stream().anyMatch { m -> m.permitoContagiarATipo(tipoAVer) &&
                                                                m.especie == especieAContagiar }
    }

    private fun intentarMutar(especieAMutar : Especie){
        val mutacionesPosibles = this.mutacionesNuevas(especieAMutar)
        if (this.haySuerteMutacion(especieAMutar) && mutacionesPosibles.isNotEmpty()){
            val mutacionAMutar=  mutacionesPosibles.random()
            mutacionAMutar.especie = especieAMutar
            mutacionAMutar.surtirEfectoEn(this)
            this.mutacionesSufridas.add(mutacionAMutar)
        }
    }

    private fun mutacionesNuevas(especieAMutar: Especie): List<Mutacion>  {
        val mutacionesDelVector = this.mutacionesSufridas
        val mutacionesDeEspecie = especieAMutar.mutacionesPosibles

        return mutacionesDeEspecie.stream().filter {me -> me.estaEn(mutacionesDelVector).not()}.toList()
    }

    private fun haySuerteMutacion(especieAMutar: Especie): Boolean{
        val dado = Randomizador.getInstance()
        val numeroContagio = especieAMutar.capacidadDeBiomecanizacion()
        val numeroRuleta = dado.valor(1,100)
        return numeroContagio >= numeroRuleta
    }

    fun eliminarEspeciesPorSupresion(potencia: Int) {
        val especiesAEliminar = this.especiesContagiadas.stream().filter { es -> this.eliminaPorSupresion(es, potencia) }.toList()
        this.removerEspecies(especiesAEliminar)
    }

    private fun removerEspecies(especies: List<Especie>) {
        especies.forEach{
            it.vectores.remove(this)
            this.especiesContagiadas.remove(it)
        }
    }

    private fun eliminaPorSupresion(especie: Especie, potencia: Int): Boolean {
        return especie.defensa() < potencia
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

    fun esAnimal(): Boolean{
        return  this == Animal
    }

}