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

    @BeforeEach
    fun setUp() {
        supresion1 = SupresionBiomecanica(15)
        supresion2 = SupresionBiomecanica(50)
        supresion3 = SupresionBiomecanica(15)

        bioAlteracionAnimal = BioalteracionGenetica(TipoDeVector.Animal)
        bioAlteracionAnimal2 = BioalteracionGenetica(TipoDeVector.Animal)
        bioAlteracionPersona = BioalteracionGenetica(TipoDeVector.Persona)
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
        assertTrue(bioAlteracionAnimal.equals(bioAlteracionAnimal2))
        assertTrue(bioAlteracionAnimal2.equals(bioAlteracionAnimal))
    }

    @Test
    fun `La mutacion esta en las mutaciones a ver`(){
     var mutaciones : MutableSet<Mutacion> = HashSet()
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
        assertFalse(supresion1.tengoMutacionParaContagiarATipo(TipoDeVector.Insecto))
        assertFalse(supresion1.tengoMutacionParaContagiarATipo(TipoDeVector.Persona))
        assertFalse(supresion1.tengoMutacionParaContagiarATipo(TipoDeVector.Animal))
    }

    @Test
    fun `si bioAlteracionAnimal pregunta si tiene mutacion para contagiar animal da verdadero `(){
        assertTrue(bioAlteracionAnimal.tengoMutacionParaContagiarATipo(TipoDeVector.Animal))
    }

    @Test
    fun `si bioAlteracionAnimal pregunta si tiene mutacion para contagiar tipo no animal da falso`(){
        assertFalse(bioAlteracionAnimal.tengoMutacionParaContagiarATipo(TipoDeVector.Insecto))
        assertFalse(bioAlteracionAnimal.tengoMutacionParaContagiarATipo(TipoDeVector.Persona))
    }

    @AfterEach
    fun tearDown() {

    }

}