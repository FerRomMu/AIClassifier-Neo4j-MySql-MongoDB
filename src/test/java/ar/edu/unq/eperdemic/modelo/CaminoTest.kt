package ar.edu.unq.eperdemic.modelo

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class CaminoTest {
    lateinit var caminoTerrestre: Camino
    lateinit var caminoTerrestre2: Camino
    lateinit var caminoAereo: Camino
    lateinit var caminoMaritimo: Camino

    lateinit var vectorPersona: Vector
    lateinit var vectorAnimal: Vector
    lateinit var vectorInsecto: Vector

    lateinit var ubicacion: Ubicacion
    lateinit var ubicacion2: Ubicacion

    @BeforeEach
    fun setUp() {
        vectorPersona = Vector(TipoDeVector.Persona)
        vectorAnimal = Vector(TipoDeVector.Animal)
        vectorInsecto = Vector(TipoDeVector.Insecto)

        ubicacion = Ubicacion("Cordoba")
        ubicacion2 = Ubicacion("Santa fe")

        caminoTerrestre = Camino(ubicacion2,TipoDeCamino.CaminoTerreste)
        caminoTerrestre2 = Camino(ubicacion2,TipoDeCamino.CaminoTerreste)
        caminoAereo = Camino(ubicacion2,TipoDeCamino.CaminoAereo)
        caminoMaritimo = Camino(ubicacion2,TipoDeCamino.CaminoMaritimo)



    }

    @Test
    fun `El vector Humano puede pasar por el camino Terrestre`() {
        assertTrue(caminoTerrestre.puedePasar(vectorPersona))
    }

    @Test
    fun `El vector Animal puede pasar por el camino Terrestre`() {
        assertTrue(caminoTerrestre.puedePasar(vectorAnimal))
    }

    @Test
    fun `El vector Insecto puede pasar por el camino Terrestre`() {
        assertTrue(caminoTerrestre.puedePasar(vectorInsecto))
    }

    @Test
    fun `El vector Humano puede pasar por el camino Maritimo`() {
        assertTrue(caminoMaritimo.puedePasar(vectorPersona))
    }

    @Test
    fun `El vector Animal puede pasar por el camino Maritimo`() {
        assertTrue(caminoMaritimo.puedePasar(vectorAnimal))
    }

    @Test
    fun `El vector Insecto no puede pasar por el camino Maritimo`() {
        assertFalse(caminoMaritimo.puedePasar(vectorInsecto))
    }

    @Test
    fun `El vector Humano no puede pasar por el camino Aereo`() {
        assertFalse(caminoAereo.puedePasar(vectorPersona))
    }

    @Test
    fun `El vector Animal puede pasar por el camino Aereo`() {
        assertTrue(caminoAereo.puedePasar(vectorAnimal))
    }

    @Test
    fun `El vector Insecto puede pasar por el camino Aereo`() {
        assertTrue(caminoAereo.puedePasar(vectorInsecto))
    }


    @Test
    fun `caminoTerrestre llega a ubicacion2`() {
        assertTrue(caminoTerrestre.llegaA(ubicacion2))
    }

    @Test
    fun `caminoTerrestre no llega a ubicacion3`() {
        val ubicacion3 = Ubicacion("Jujuy")
        assertFalse(caminoTerrestre.llegaA(ubicacion3))
    }

    @Test
    fun ` caminoTerrestre equals caminoTerrestre2`() {
        assertTrue(caminoTerrestre.equals(caminoTerrestre2))
    }

    @Test
    fun ` caminoTerrestre no es equals caminoTerrestre3`() {
        val caminoTerrestre3 = Camino(ubicacion,TipoDeCamino.CaminoTerreste)
        assertTrue(caminoTerrestre.equals(caminoTerrestre))
    }

    @AfterEach
    fun tearDown() {
    }


}