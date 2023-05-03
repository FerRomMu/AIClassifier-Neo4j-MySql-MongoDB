package ar.edu.unq.eperdemic.modelo

import org.junit.jupiter.api.Assertions.*
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

        assertEquals(0, patogeno.cantidadDeEspecies())
        patogeno.crearEspecie("unaEspecie", "Japon")
        assertEquals(1, patogeno.cantidadDeEspecies())

        var especieAVer = patogeno.especies[0]

        assertEquals("unaEspecie",especieAVer.nombre)
        assertEquals("Japon",especieAVer.paisDeOrigen)
    }

    @Test
    fun `crearEspecie me devuelve una especie con los datos correctos`() {

        val especieCreada = patogeno.crearEspecie("unaEspecie", "Japon")
        assertEquals("unaEspecie", especieCreada.nombre)
        assertEquals("Japon", especieCreada.paisDeOrigen)
        assertEquals(patogeno, especieCreada.patogeno)

    }

    @Test
    fun `cantidad de especies devuelve la cantidad de especies del patogeno`(){
        assertEquals(0, patogeno.cantidadDeEspecies())

        patogeno.crearEspecie("virusT", "raccoon city")
        patogeno.crearEspecie("virusG", "bernal")
        patogeno.crearEspecie("virusC", "quilmes")

        assertEquals(3, patogeno.cantidadDeEspecies())
    }

    @Test
    fun `si a estaEnElRango le paso un numero valido devuelve verdadero`(){
        assertTrue(patogeno.estanEnElRango(0,10,5))
    }

    @Test
    fun `si a estaEnElRango le paso un numero invalido devuelve false`(){
        assertFalse(patogeno.estanEnElRango(0,10,20))
    }

    @Test
    fun `si pido la capacidad de contagio a un humano devuelve el valor correcto`(){
        patogeno.setCapacidadDeContagioHumano(10)
        assertEquals(10, patogeno.capacidadDeContagioA(TipoDeVector.Persona))
    }

    @Test
    fun `si pido la capacidad de contagio a un animal devuelve el valor correcto`(){
        patogeno.setCapacidadDeContagioAnimal(20)
        assertEquals(20, patogeno.capacidadDeContagioA(TipoDeVector.Animal))
    }

    @Test
    fun `si pido la capacidad de contagio a un insecto devuelve el valor correcto`(){
        patogeno.setCapacidadDeContagioInsecto(30)
        assertEquals(30, patogeno.capacidadDeContagioA(TipoDeVector.Insecto))
    }

}