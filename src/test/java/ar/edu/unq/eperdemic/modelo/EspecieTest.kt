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

    @BeforeEach
    fun setup(){
        patogeno = Patogeno("Gripe")
        patogeno.setCapacidadDeContagioHumano(50)
        especie = Especie(patogeno, "virusT", "raccoon city")
        vector = Vector(TipoDeVector.Persona)
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
}