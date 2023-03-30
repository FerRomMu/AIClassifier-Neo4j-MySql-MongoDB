package ar.edu.unq.eperdemic.modelo

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PatogenoTest {

    lateinit var patogeno: Patogeno

    @BeforeEach
    fun setUp() {
        patogeno = Patogeno("Gripe");
    }

    @Test
    fun `toString de un patogeno devuelve el tipo`() {
        assertEquals("Gripe", patogeno.toString())
    }

    @Test
    fun `crearEspecie aumenta en 1 la cantidad de especies`() {

        assertEquals(0, patogeno.cantidadDeEspecies)
        patogeno.crearEspecie("unaEspecie", "Japon")
        assertEquals(1, patogeno.cantidadDeEspecies)
    }
}