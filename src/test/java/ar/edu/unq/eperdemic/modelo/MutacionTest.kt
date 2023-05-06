package ar.edu.unq.eperdemic.modelo

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class MutacionTest {

    lateinit var suprecion1: Mutacion
    lateinit var suprecion2: Mutacion
    lateinit var suprecion3: Mutacion

    lateinit var bioAlteracionAnimal: Mutacion
    lateinit var bioAlteracionAnimal2: Mutacion
    lateinit var bioAlteracionPersona: Mutacion

    @BeforeEach
    fun setUp() {
        suprecion1 = SupresionBiomecanica(15)
        suprecion2 = SupresionBiomecanica(50)
        suprecion3 = SupresionBiomecanica(15)

        bioAlteracionAnimal = BioalteracionGenetica(TipoDeVector.Animal)
        bioAlteracionAnimal2 = BioalteracionGenetica(TipoDeVector.Animal)
        bioAlteracionPersona = BioalteracionGenetica(TipoDeVector.Persona)

    }


    @Test
    fun `comparar por potencia 2 supreciones distintas de diferente poder`(){
        assertFalse(suprecion1.compararPorPotencia(suprecion2))
    }

    @Test
    fun `comparar por potencia 2 supreciones distintas de igual poder`(){
        assertTrue(suprecion1.compararPorPotencia(suprecion3))
    }

    @Test
    fun `comparar por potencia la misma suprecion`(){
        assertTrue(suprecion1.compararPorPotencia(suprecion1))
    }

    @Test
    fun `comparar por potencia una supreciones con una Bioalteracion`(){
        assertFalse(bioAlteracionAnimal.compararPorPotencia(suprecion1))
    }

    @Test
    fun `comparar por potencia 2 Bioalteraciones `(){
        assertFalse(bioAlteracionAnimal.compararPorPotencia(bioAlteracionPersona))
    }



    @Test
    fun compararPorTipo() {

    }

    @Test
    fun testEquals() {

    }

    @AfterEach
    fun tearDown() {

    }

}