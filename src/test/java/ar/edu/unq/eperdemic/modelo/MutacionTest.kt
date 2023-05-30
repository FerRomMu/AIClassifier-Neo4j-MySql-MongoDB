package ar.edu.unq.eperdemic.modelo

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class MutacionTest {

    lateinit var supresion1: Mutacion
    lateinit var supresion2: Mutacion
    lateinit var supresion3: Mutacion

    lateinit var bioAlteracionAnimal: Mutacion
    lateinit var bioAlteracionAnimal2: Mutacion
    lateinit var bioAlteracionPersona: Mutacion

    lateinit var especie1: Especie
    lateinit var especie2: Especie
    lateinit var especie3: Especie

    lateinit var patogeno1: Patogeno
    lateinit var patogeno2: Patogeno

    lateinit var vector: Vector


    @BeforeEach
    fun setUp() {
        supresion1 = SupresionBiomecanica(15)
        supresion2 = SupresionBiomecanica(50)
        supresion3 = SupresionBiomecanica(15)

        bioAlteracionAnimal = BioalteracionGenetica(TipoDeVector.Animal)
        bioAlteracionAnimal2 = BioalteracionGenetica(TipoDeVector.Animal)
        bioAlteracionPersona = BioalteracionGenetica(TipoDeVector.Persona)

        patogeno1 = Patogeno("Gripe")
        patogeno2 = Patogeno("Gripe2")

        vector = Vector(TipoDeVector.Persona)

        especie1 = Especie(patogeno1, "virusT", "1")
        especie2 = Especie(patogeno1, "virusG", "2")
        especie3 = Especie(patogeno2, "virusC", "3")
    }


    @Test
    fun `comparar por potencia 2 supreciones distintas de diferente poder`(){
        assertFalse(supresion1.compararPorPotencia(supresion2))
    }

    @Test
    fun `comparar por potencia 2 supreciones distintas de igual poder`(){
        assertTrue(supresion1.compararPorPotencia(supresion3))
    }

    @Test
    fun `comparar por potencia la misma supresion`(){
        assertTrue(supresion1.compararPorPotencia(supresion1))
    }

    @Test
    fun `comparar por potencia una supresiones con una Bioalteracion`(){
        assertFalse(bioAlteracionAnimal.compararPorPotencia(supresion1))
    }

    @Test
    fun `comparar por potencia 2 Bioalteraciones `(){
        assertFalse(bioAlteracionAnimal.compararPorPotencia(bioAlteracionPersona))
    }

    @Test
    fun `comparar por tipo 2 bioalteraciones distintas de diferente tipo es falso` () {
        assertFalse(bioAlteracionAnimal.compararPorTipo(bioAlteracionPersona))
    }

    @Test
    fun `comparar por tipo 2 bioalteraciones distintas de igual tipo es verdadero` () {
        assertTrue(bioAlteracionAnimal.compararPorTipo(bioAlteracionAnimal2))
    }

    @Test
    fun `comparar por tipo la misma bioalteracion es verdadero` () {
        assertTrue(bioAlteracionAnimal.compararPorTipo(bioAlteracionAnimal))
    }

    @Test
    fun `comparar por tipo una bioalteracion y una supresion es falso` () {
        assertFalse(supresion1.compararPorTipo(bioAlteracionAnimal))
    }

    @Test
    fun `comparar por tipo 2 supresiones es falso`(){
        assertFalse(supresion1.compararPorTipo(supresion2))
    }

    @Test
    fun `Me fijo si son equivalentes 2 supresiones distintas de diferente poder mediante el mensaje equals`(){
        assertFalse(supresion1.equals(supresion2))
        assertFalse(supresion2.equals(supresion1))
    }

    @Test
    fun `Me fijo si son equivalentes 2 supresiones distintas de igual poder mediante el mensaje equals`(){
        supresion1.definirEspecie(especie1); supresion3.definirEspecie(especie1)
        assertTrue(supresion1.equals(supresion3))
        assertTrue(supresion3.equals(supresion1))
    }

    @Test
    fun `Me fijo si son equivalentes una supresiones con una Bioalteracion`(){
        assertFalse(bioAlteracionAnimal.equals(supresion1))
        assertFalse(supresion1.equals(bioAlteracionAnimal))
    }

    @Test
    fun `Me fijo si son equivalentes 2 Bioalteraciones de diferete tipo `(){
        assertFalse(bioAlteracionAnimal.equals(bioAlteracionPersona))
        assertFalse(bioAlteracionPersona.equals(bioAlteracionAnimal))
    }

    @Test
    fun `Me fijo si son equivalentes 2 Bioalteraciones del mismo tipo `(){
        bioAlteracionAnimal.especie = especie1
        bioAlteracionAnimal2.especie = especie1

        assertTrue(bioAlteracionAnimal.equals(bioAlteracionAnimal2))
        assertTrue(bioAlteracionAnimal2.equals(bioAlteracionAnimal))
    }

    @Test
    fun `La mutacion esta en las mutaciones a ver`(){
     var mutaciones : MutableSet<Mutacion> = HashSet()
        supresion1.definirEspecie(especie1)
        supresion2.definirEspecie(especie1)
        supresion3.definirEspecie(especie1)

        mutaciones.add(supresion1)
        mutaciones.add(supresion2)
        mutaciones.add(supresion3)
        mutaciones.add(bioAlteracionPersona)

        assertTrue(supresion2.estaEn(mutaciones))
    }

    @Test
    fun `La mutacion no esta en las mutaciones a ver`(){
        var mutaciones : MutableSet<Mutacion> = HashSet()
        mutaciones.add(supresion1)
        mutaciones.add(supresion2)
        mutaciones.add(supresion3)
        mutaciones.add(bioAlteracionPersona)

        assertFalse(bioAlteracionAnimal.estaEn(mutaciones))
    }

    @Test
    fun `La mutacion no esta en las mutaciones a ver (esta vez vacio)`(){
        var mutaciones : MutableSet<Mutacion> = HashSet()

        assertFalse(bioAlteracionAnimal.estaEn(mutaciones))
    }

    @Test
    fun `si supresion pregunta si tiene mutacion para contagiar cualquier tipo devuelve falso `(){
        assertFalse(supresion1.permitoContagiarATipo(TipoDeVector.Insecto))
        assertFalse(supresion1.permitoContagiarATipo(TipoDeVector.Persona))
        assertFalse(supresion1.permitoContagiarATipo(TipoDeVector.Animal))
    }

    @Test
    fun `si bioAlteracionAnimal pregunta si tiene mutacion para contagiar animal da verdadero `(){
        assertTrue(bioAlteracionAnimal.permitoContagiarATipo(TipoDeVector.Animal))
    }

    @Test
    fun `si bioAlteracionAnimal pregunta si tiene mutacion para contagiar tipo no animal da falso`(){
        assertFalse(bioAlteracionAnimal.permitoContagiarATipo(TipoDeVector.Insecto))
        assertFalse(bioAlteracionAnimal.permitoContagiarATipo(TipoDeVector.Persona))
    }

    @Test
    fun `Es supresion y impide el contagio porque su potencia es mayor`(){
        var patogeno : Patogeno = Patogeno("tipoPat")
        patogeno.setCapacidadDeDefensa(30)

        var especie : Especie = Especie(patogeno,"nombreEspecie","Francia")

        assertTrue(supresion2.impideContagioDe(especie))
    }

    @Test
    fun `Es supresion y no impide el contagio porque su potencia es igual`(){
        var patogeno : Patogeno = Patogeno("tipoPat")
        patogeno.setCapacidadDeDefensa(50)

        var especie : Especie = Especie(patogeno,"nombreEspecie","Francia")

        assertFalse(supresion2.impideContagioDe(especie))
    }


    @Test
    fun `Es supresion y no impide el contagio porque su potencia es menor`(){
        var patogeno : Patogeno = Patogeno("tipoPat")
        patogeno.setCapacidadDeDefensa(30)

        var especie : Especie = Especie(patogeno,"nombreEspecie","Francia")

        assertFalse(supresion1.impideContagioDe(especie))
    }

    @Test
    fun `Es bioalteracion por ende no impide`(){
        var patogeno : Patogeno = Patogeno("tipoPat")
        patogeno.setCapacidadDeDefensa(30)

        var especie : Especie = Especie(patogeno,"nombreEspecie","Francia")

        assertFalse(bioAlteracionAnimal.impideContagioDe(especie))
    }

    @Test
    fun `si surto efecto de supresion elimino las enfermedades con defensa menor del vector`(){
        patogeno1.setCapacidadDeDefensa(20)
        patogeno2.setCapacidadDeDefensa(50)
        vector.agregarEspecie(especie1); vector.agregarEspecie(especie2); vector.agregarEspecie(especie3)

        assertEquals(3, vector.especiesContagiadas.size)

        supresion2.surtirEfectoEn(vector)

        assertEquals(1, vector.especiesContagiadas.size)
        assertTrue(vector.especiesContagiadas.contains(especie3))
    }

    @Test
    fun `si surto efecto de biomecanizacion a las enfermedades con defensa no hace nada`(){
        patogeno1.setCapacidadDeDefensa(20)
        vector.agregarEspecie(especie1); vector.agregarEspecie(especie2)

        assertEquals(vector.especiesContagiadas.size, 2)

        bioAlteracionAnimal.surtirEfectoEn(vector)

        assertEquals(vector.especiesContagiadas.size, 2)
    }

    @AfterEach
    fun tearDown() {

    }

}