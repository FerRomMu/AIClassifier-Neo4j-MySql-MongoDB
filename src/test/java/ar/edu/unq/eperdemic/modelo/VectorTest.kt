package ar.edu.unq.eperdemic.modelo

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VectorTest {
    lateinit var vectorHumano1: Vector
    lateinit var vectorHumano2: Vector
    lateinit var vectorAnimal1: Vector
    lateinit var vectorAnimal2: Vector
    lateinit var vectorInsecto1: Vector
    lateinit var vectorInsecto2: Vector
    lateinit var especie: Especie
    lateinit var patogeno: Patogeno
    lateinit var ubicacion: Ubicacion
    lateinit var dado: Randomizador

    @BeforeEach
    fun setup() {
        vectorHumano1 = Vector(TipoDeVector.Persona)
        vectorHumano2 = Vector(TipoDeVector.Persona)

        vectorAnimal1 = Vector(TipoDeVector.Animal)
        vectorAnimal2 = Vector(TipoDeVector.Animal)

        vectorInsecto1 = Vector(TipoDeVector.Insecto)
        vectorInsecto2 = Vector(TipoDeVector.Insecto)

        ubicacion = Ubicacion("bernal")
        patogeno = Patogeno("Gripe")
        patogeno.setCapacidadDeContagioHumano(100)
        patogeno.setCapacidadDeContagioAnimal(100)
        patogeno.setCapacidadDeContagioInsecto(100)
        especie = Especie(patogeno, "virusT", "raccoon city")

        dado = Randomizador.getInstance()
        dado.estado = EstadoRandomizadorDeterminístico()

    }

    @Test
    fun `si agrego una especie se agrega al set de especies contagiadas`(){
        assertEquals(0, vectorHumano1.especiesContagiadas.size)

        vectorHumano1.agregarEspecie(especie)
        val nombreDeEspecies = vectorHumano1.especiesContagiadas.map { e -> e.nombre }

        assertTrue(nombreDeEspecies.contains("virusT"))
    }

    @Test
    fun `si un vector humano intenta infectar a un vector humano lo contagia`() {
        assertEquals(0, vectorHumano2.especiesContagiadas.size)

        vectorHumano1.agregarEspecie(especie)
        vectorHumano1.intentarInfectar(vectorHumano2)
        val nombreDeEspecies = vectorHumano2.especiesContagiadas.map { e -> e.nombre }

        assertTrue(nombreDeEspecies.contains("virusT"))
    }

    @Test
    fun `si un vector humano intenta infectar a un vector insecto lo contagia`() {
        assertEquals(0, vectorInsecto1.especiesContagiadas.size)

        vectorHumano1.agregarEspecie(especie)
        vectorHumano1.intentarInfectar(vectorInsecto1)
        val nombreDeEspecies = vectorInsecto1.especiesContagiadas.map { e -> e.nombre }

        assertTrue(nombreDeEspecies.contains("virusT"))
    }

    @Test
    fun `si un vector humano intenta infectar a un vector animal, no pasa nada`() {
        assertEquals(0, vectorAnimal1.especiesContagiadas.size)

        vectorHumano1.agregarEspecie(especie)
        vectorHumano1.intentarInfectar(vectorAnimal1)

        assertEquals(0, vectorAnimal1.especiesContagiadas.size)
    }

    @Test
    fun `si un vector insecto intenta infectar a un vector animal lo contagia`() {
        assertEquals(0, vectorAnimal1.especiesContagiadas.size)

        vectorInsecto1.agregarEspecie(especie)
        vectorInsecto1.intentarInfectar(vectorAnimal1)
        val nombreDeEspecies = vectorAnimal1.especiesContagiadas.map { e -> e.nombre }

        assertTrue(nombreDeEspecies.contains("virusT"))
    }

    @Test
    fun `si un vector insecto intenta infectar a un vector humano lo contagia`() {
        assertEquals(0, vectorHumano1.especiesContagiadas.size)

        vectorInsecto1.agregarEspecie(especie)
        vectorInsecto1.intentarInfectar(vectorHumano1)
        val nombreDeEspecies = vectorHumano1.especiesContagiadas.map { e -> e.nombre }

        assertTrue(nombreDeEspecies.contains("virusT"))
    }

    @Test
    fun `si un vector insecto intenta infectar a un vector insecto, no pasa nada`() {
        assertEquals(0, vectorInsecto2.especiesContagiadas.size)

        vectorInsecto1.agregarEspecie(especie)
        vectorInsecto1.intentarInfectar(vectorInsecto2)

        assertEquals(0, vectorInsecto2.especiesContagiadas.size)
    }

    @Test
    fun `si un vector animal intenta infectar a un vector humano lo contagia`() {
        assertEquals(0, vectorHumano1.especiesContagiadas.size)

        vectorAnimal1.agregarEspecie(especie)
        vectorAnimal1.intentarInfectar(vectorHumano1)
        val nombreDeEspecies = vectorHumano1.especiesContagiadas.map { e -> e.nombre }

        assertTrue(nombreDeEspecies.contains("virusT"))
    }

    @Test
    fun `si un vector animal intenta infectar a un vector insecto lo contagia`() {
        assertEquals(0, vectorInsecto1.especiesContagiadas.size)

        vectorAnimal1.agregarEspecie(especie)
        vectorAnimal1.intentarInfectar(vectorInsecto1)
        val nombreDeEspecies = vectorInsecto1.especiesContagiadas.map { e -> e.nombre }

        assertTrue(nombreDeEspecies.contains("virusT"))
    }

    @Test
    fun `si un vector animal intenta infectar a un vector animal, no pasa nada`() {
        assertEquals(0, vectorAnimal2.especiesContagiadas.size)

        vectorAnimal1.agregarEspecie(especie)
        vectorAnimal1.intentarInfectar(vectorAnimal2)

        assertEquals(0, vectorAnimal2.especiesContagiadas.size)
    }
}