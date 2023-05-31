package ar.edu.unq.eperdemic.modelo

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class UbicacionNeoTest {

    lateinit var ubicacionNeo1: UbicacionNeo
    lateinit var ubicacionNeo2: UbicacionNeo
    lateinit var ubicacion1 : Ubicacion
    lateinit var ubicacion2 : Ubicacion

    lateinit var caminoTerrestre: Camino
    lateinit var caminoAereo: Camino
    lateinit var caminoMaritimo: Camino

    @BeforeEach
    fun setUp() {
        ubicacionNeo1 = UbicacionNeo("Cordoba")
        ubicacionNeo2 = UbicacionNeo("Santa fe")

        caminoTerrestre = Camino(ubicacionNeo2, Camino.TipoDeCamino.CaminoTerreste)
        caminoAereo = Camino(ubicacionNeo2, Camino.TipoDeCamino.CaminoAereo)
        caminoMaritimo = Camino(ubicacionNeo2, Camino.TipoDeCamino.CaminoMaritimo)

        ubicacionNeo1.caminos.add(caminoTerrestre)
        ubicacionNeo1.caminos.add(caminoAereo)
        ubicacionNeo1.caminos.add(caminoMaritimo)
    }

    @Test
    fun `Es la misma Ubicacion`() {
        assertTrue(ubicacionNeo1.esLaUbicacion("Cordoba"))
    }

    @Test
    fun `No es la misma Ubicacion`() {
        assertFalse(ubicacionNeo2.esLaUbicacion("Cordoba"))
    }

    @AfterEach
    fun tearDown() {
    }

}