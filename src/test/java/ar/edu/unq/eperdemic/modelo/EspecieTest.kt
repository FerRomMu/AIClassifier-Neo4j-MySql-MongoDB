package ar.edu.unq.eperdemic.modelo

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EspecieTest {
    lateinit var especie: Especie
    lateinit var patogeno: Patogeno
    lateinit var vector: Vector
    lateinit var suprecion1  : Mutacion
    lateinit var suprecion2  : Mutacion
    lateinit var bioalterPer : Mutacion

    @BeforeEach
    fun setup(){
        patogeno = Patogeno("Gripe")
        patogeno.setCapacidadDeContagioHumano(50)
        especie = Especie(patogeno, "virusT", "raccoon city")
        vector = Vector(TipoDeVector.Persona)

        suprecion1 = SupresionBiomecanica(48)
        suprecion2 = SupresionBiomecanica(20)
        bioalterPer = BioalteracionGenetica(TipoDeVector.Persona)
    }

    @Test
    fun `se agrego un vector al set se agrega`(){
        assertEquals(0, especie.vectores.size)
        especie.agregarVector(vector)

        assertEquals(1, especie.vectores.size)
        val vectorAgregado = especie.vectores.first()
        assertEquals(vector, vectorAgregado)
    }

    @Test
    fun `si pido la capacidad de contagio a una persona la devuelve`(){
        assertEquals(50, especie.capacidadDeContagioA(TipoDeVector.Persona))
    }

    @Test
    fun `si pido la capacidad de defensa la devuelve`(){
        patogeno.setCapacidadDeDefensa(25)
        assertEquals(25, especie.defensa())
    }

    @Test
    fun `si pido la capacidad de biomecanizacion la devuelve`(){
        patogeno.setCapacidadDeBiomecanizacion(10)
        assertEquals(10, especie.capacidadDeBiomecanizacion())
    }

    @Test
    fun `agrega mutaciones correctamente`(){
        assertEquals(0,especie.mutacionesPosibles.size)

        especie.agregarMutacion(suprecion1)

        assertEquals(1,especie.mutacionesPosibles.size)

        assertTrue(suprecion1.equals(especie.mutacionesPosibles.first()))
    }

    @Test
    fun `agrega mutacion que no esta ya que esta es la primera `(){
        assertEquals(0,especie.mutacionesPosibles.size)

        especie.intentarAgregarMutacion(suprecion1)

        assertEquals(1,especie.mutacionesPosibles.size)


        assertTrue(suprecion1.equals(especie.mutacionesPosibles.first()))
    }

    @Test
    fun `agrega mutacion que no esta`(){
        assertEquals(0,especie.mutacionesPosibles.size)

        especie.intentarAgregarMutacion(suprecion1)
        assertEquals(1,especie.mutacionesPosibles.size)

        especie.intentarAgregarMutacion(suprecion2)
        assertEquals(2,especie.mutacionesPosibles.size)

        var mutacionesDeEspecie = especie.mutacionesPosibles.toList()
        assertFalse(mutacionesDeEspecie[0].equals(mutacionesDeEspecie[1]))
        assertTrue(mutacionesDeEspecie.stream().allMatch{m -> m.equals(suprecion1) || m.equals(suprecion2)})
    }


    @Test
    fun `no agrega mutacion que ya esta`(){
        assertEquals(0,especie.mutacionesPosibles.size)

        especie.intentarAgregarMutacion(suprecion1)
        assertEquals(1,especie.mutacionesPosibles.size)
        assertTrue(suprecion1.equals(especie.mutacionesPosibles.first()))

        especie.intentarAgregarMutacion(suprecion1)
        assertEquals(1,especie.mutacionesPosibles.size)
        assertTrue(suprecion1.equals(especie.mutacionesPosibles.first()))
    }

    @Test
    fun `no agrega mutacion que ya esta una equivalente `(){
        val mutacionAComparar : Mutacion = SupresionBiomecanica(25)
        mutacionAComparar as SupresionBiomecanica

        val mutacionEquivalente : Mutacion = SupresionBiomecanica(mutacionAComparar.potencia)
        assertEquals(0,especie.mutacionesPosibles.size)

        especie.intentarAgregarMutacion(mutacionAComparar)
        assertEquals(1,especie.mutacionesPosibles.size)
        assertTrue(mutacionAComparar.equals(especie.mutacionesPosibles.first()))

        especie.intentarAgregarMutacion(mutacionEquivalente)
        assertEquals(1,especie.mutacionesPosibles.size)
        assertTrue(mutacionEquivalente.equals(especie.mutacionesPosibles.first()))
    }




}