package ar.edu.unq.eperdemic.modelo

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class UbicacionTest {
    lateinit var ubicacion1: Ubicacion
    lateinit var ubicacion2: Ubicacion

    lateinit var caminoTerrestre: Camino
    lateinit var caminoAereo: Camino
    lateinit var caminoMaritimo: Camino


    @BeforeEach
    fun setUp() {
        ubicacion1 = Ubicacion("Cordoba")
        ubicacion2 = Ubicacion("Santa fe")

        caminoTerrestre = Camino(ubicacion2,TipoDeCamino.CaminoTerreste)
        caminoAereo = Camino(ubicacion2,TipoDeCamino.CaminoAereo)
        caminoMaritimo = Camino(ubicacion2,TipoDeCamino.CaminoMaritimo)

        ubicacion1.caminos.add(caminoTerrestre)
        ubicacion1.caminos.add(caminoAereo)
        ubicacion1.caminos.add(caminoMaritimo)
    }

    // ¿ Borrar desde aca a abajo, borrar test?

    fun `no hay caminos a ubicacion4`() {
        val ubicacion4 = Ubicacion("Chaco")

        var caminosDisponibles = ubicacion1.caminosA(ubicacion4)

        assertEquals(0,caminosDisponibles.size)
    }

    @Test
    fun `caminos a ubicacion1`() {
        val caminoAereo2 = Camino(ubicacion1,TipoDeCamino.CaminoAereo)

        ubicacion2.caminos.add(caminoAereo2)
        var caminosDisponibles = ubicacion2.caminosA(ubicacion1)

        assertEquals(1,caminosDisponibles.size)

        val caminoAVer = caminosDisponibles.toList()[0]
        assertEquals(caminoAereo2.ubicacioDestino,caminoAVer.ubicacioDestino)
        assertEquals(caminoAereo2.tipo,caminoAVer.tipo)
    }

    @Test
    fun `intentar agregar un camino que ya esta, no lo agrega`(){
        val ubicacion3 = Ubicacion("Santa fe")
        val caminoTerrestre2 = Camino(ubicacion3,TipoDeCamino.CaminoTerreste)

        assertEquals(3,ubicacion1.caminos.size)

        ubicacion1.agregarCamino(caminoTerrestre2)

        assertEquals(3,ubicacion1.caminos.size)
    }

    @Test
    fun `intentar agregar un camino que no esta,lo agrega`(){
        assertEquals(0,ubicacion2.caminos.size)

        val caminoTerrestreNuevo  = Camino(ubicacion1,TipoDeCamino.CaminoTerreste)

        ubicacion2.agregarCamino(caminoTerrestreNuevo)

        assertEquals(1,ubicacion2.caminos.size)

        val caminoAVer = ubicacion2.caminos.toList()[0]

        assertEquals(caminoTerrestreNuevo.ubicacioDestino,caminoAVer.ubicacioDestino)
        assertEquals(caminoTerrestreNuevo.tipo,caminoAVer.tipo)
    }

    @AfterEach
    fun tearDown() {
    }


}