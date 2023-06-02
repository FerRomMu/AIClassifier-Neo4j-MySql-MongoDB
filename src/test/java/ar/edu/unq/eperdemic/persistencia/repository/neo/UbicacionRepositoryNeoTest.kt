package ar.edu.unq.eperdemic.persistencia.repository.neo

import ar.edu.unq.eperdemic.modelo.Camino
import ar.edu.unq.eperdemic.modelo.UbicacionNeo
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UbicacionRepositoryNeoTest {

    @Autowired lateinit var ubicacionNeoRepository: UbicacionNeoRepository

    @BeforeEach
    fun setUp() {

    }

    @Test
    fun `Si pido el camino mas corto y es completamente terreste trae ese`() {
        val bera = UbicacionNeo("bera")
        val quilmes = UbicacionNeo("quilmes")
        val varela = UbicacionNeo("varela")
        val bernal = UbicacionNeo("bernal")
        val solano = UbicacionNeo("solano")

        val caminoABeraTerrestre = Camino(bera, Camino.TipoDeCamino.CaminoTerreste)
        val caminoAVarelaTerrestre = Camino(varela, Camino.TipoDeCamino.CaminoTerreste)
        val caminoABernalTerrestre = Camino(bernal, Camino.TipoDeCamino.CaminoTerreste)
        val caminoASolanoTerrestre = Camino(solano, Camino.TipoDeCamino.CaminoTerreste)
        val caminoAQuilmesTerrestre = Camino(quilmes, Camino.TipoDeCamino.CaminoTerreste)

        quilmes.agregarCamino(caminoAVarelaTerrestre)
        varela.agregarCamino(caminoABernalTerrestre)
        bernal.agregarCamino(caminoAVarelaTerrestre)
        bera.agregarCamino(caminoAQuilmesTerrestre)
        bernal.agregarCamino(caminoASolanoTerrestre)
        solano.agregarCamino(caminoAVarelaTerrestre)
        solano.agregarCamino(caminoABeraTerrestre)

        ubicacionNeoRepository.save(bera)

        val caminoMasCorto = ubicacionNeoRepository.caminoMasCorto(solano.nombre, bernal.nombre, Camino.TipoDeCamino.CaminoTerreste, Camino.TipoDeCamino.CaminoMaritimo)

        Assertions.assertEquals(caminoMasCorto[0].nombre, solano.nombre)
        Assertions.assertEquals(caminoMasCorto[1].nombre, varela.nombre)
        Assertions.assertEquals(caminoMasCorto[2].nombre, bernal.nombre)
    }

    @Test
    fun `si se quiere llegar por camino terrestre o maritimo y el mas corto es aereo, trae el mas corto que sea terreste y marítimo`() {
        val bera = UbicacionNeo("bera")
        val quilmes = UbicacionNeo("quilmes")
        val varela = UbicacionNeo("varela")
        val bernal = UbicacionNeo("bernal")
        val solano = UbicacionNeo("solano")

        val caminoABeraMaritimo = Camino(bera, Camino.TipoDeCamino.CaminoMaritimo)
        val caminoAVarelaMaritimo = Camino(varela, Camino.TipoDeCamino.CaminoMaritimo)
        val caminoAVarelaAereo = Camino(varela, Camino.TipoDeCamino.CaminoAereo)
        val caminoABernalMaritimo = Camino(bernal, Camino.TipoDeCamino.CaminoMaritimo)
        val caminoASolanoTerrestre = Camino(solano, Camino.TipoDeCamino.CaminoTerreste)
        val caminoAQuilmesMaritimo = Camino(quilmes, Camino.TipoDeCamino.CaminoMaritimo)

        quilmes.agregarCamino(caminoAVarelaMaritimo)
        varela.agregarCamino(caminoABernalMaritimo)
        bernal.agregarCamino(caminoAVarelaAereo)
        bera.agregarCamino(caminoAQuilmesMaritimo)
        bernal.agregarCamino(caminoASolanoTerrestre)
        solano.agregarCamino(caminoAVarelaAereo)
        solano.agregarCamino(caminoABeraMaritimo)

        ubicacionNeoRepository.save(bera)

        val caminoMasCorto = ubicacionNeoRepository.caminoMasCorto(solano.nombre, bernal.nombre, Camino.TipoDeCamino.CaminoTerreste, Camino.TipoDeCamino.CaminoMaritimo)

        Assertions.assertEquals(caminoMasCorto[0].nombre, solano.nombre)
        Assertions.assertEquals(caminoMasCorto[1].nombre, bera.nombre)
        Assertions.assertEquals(caminoMasCorto[2].nombre, quilmes.nombre)
        Assertions.assertEquals(caminoMasCorto[3].nombre, varela.nombre)
        Assertions.assertEquals(caminoMasCorto[4].nombre, bernal.nombre)
    }

    @Test
    fun `si se pide el camino mas corto a una ubicacion por caminos maritimos o terrestres pero no existe un camino así da vacio`() {
        val bera = UbicacionNeo("bera")
        val quilmes = UbicacionNeo("quilmes")
        val varela = UbicacionNeo("varela")
        val bernal = UbicacionNeo("bernal")
        val solano = UbicacionNeo("solano")

        val caminoABeraAereo = Camino(bera, Camino.TipoDeCamino.CaminoAereo)
        val caminoAVarelaMaritimo = Camino(varela, Camino.TipoDeCamino.CaminoMaritimo)
        val caminoAVarelaAereo = Camino(varela, Camino.TipoDeCamino.CaminoAereo)
        val caminoABernal = Camino(bernal, Camino.TipoDeCamino.CaminoMaritimo)
        val caminoASolano = Camino(solano, Camino.TipoDeCamino.CaminoTerreste)
        val caminoAQuilmesAereo = Camino(quilmes, Camino.TipoDeCamino.CaminoAereo)

        quilmes.agregarCamino(caminoAVarelaMaritimo)
        varela.agregarCamino(caminoABernal)
        bernal.agregarCamino(caminoAVarelaAereo)
        bera.agregarCamino(caminoAQuilmesAereo)
        bernal.agregarCamino(caminoASolano)
        solano.agregarCamino(caminoAVarelaAereo)
        solano.agregarCamino(caminoABeraAereo)

        ubicacionNeoRepository.save(bera)

        val caminoMasCorto = ubicacionNeoRepository.caminoMasCorto(solano.nombre, bernal.nombre, Camino.TipoDeCamino.CaminoTerreste, Camino.TipoDeCamino.CaminoMaritimo)

        Assertions.assertTrue(caminoMasCorto.isEmpty())
    }

    @Test
    fun `si se quiere llegar por camino terrestre o aereo y el mas corto es maritimo, trae el mas corto que sea terreste y aereo`(){
        val bera = UbicacionNeo("bera")
        val quilmes = UbicacionNeo("quilmes")
        val varela = UbicacionNeo("varela")
        val bernal = UbicacionNeo("bernal")
        val solano = UbicacionNeo("solano")

        val caminoABeraAereo = Camino(bera, Camino.TipoDeCamino.CaminoAereo)
        val caminoAVarelaAereo = Camino(varela, Camino.TipoDeCamino.CaminoAereo)
        val caminoAVarelaMaritimo = Camino(varela, Camino.TipoDeCamino.CaminoMaritimo)
        val caminoAVarelaTerrestre = Camino(varela, Camino.TipoDeCamino.CaminoTerreste)
        val caminoABernalAereo = Camino(bernal, Camino.TipoDeCamino.CaminoAereo)
        val caminoASolanoTerrestre = Camino(solano, Camino.TipoDeCamino.CaminoTerreste)
        val caminoAQuilmesAereo = Camino(quilmes, Camino.TipoDeCamino.CaminoAereo)

        quilmes.agregarCamino(caminoAVarelaAereo)
        varela.agregarCamino(caminoABernalAereo)
        bernal.agregarCamino(caminoAVarelaTerrestre)
        bera.agregarCamino(caminoAQuilmesAereo)
        bernal.agregarCamino(caminoASolanoTerrestre)
        solano.agregarCamino(caminoAVarelaMaritimo)
        solano.agregarCamino(caminoABeraAereo)

        ubicacionNeoRepository.save(bera)

        val caminoMasCorto = ubicacionNeoRepository.caminoMasCorto(solano.nombre, bernal.nombre, Camino.TipoDeCamino.CaminoTerreste, Camino.TipoDeCamino.CaminoAereo)

        Assertions.assertEquals(caminoMasCorto[0].nombre, solano.nombre)
        Assertions.assertEquals(caminoMasCorto[1].nombre, bera.nombre)
        Assertions.assertEquals(caminoMasCorto[2].nombre, quilmes.nombre)
        Assertions.assertEquals(caminoMasCorto[3].nombre, varela.nombre)
        Assertions.assertEquals(caminoMasCorto[4].nombre, bernal.nombre)
    }

    @Test
    fun `si se pide el camino mas corto a una ubicacion por caminos aereos o terrestres pero no existe un camino así da vacio`(){
        val bera = UbicacionNeo("bera")
        val quilmes = UbicacionNeo("quilmes")
        val varela = UbicacionNeo("varela")
        val bernal = UbicacionNeo("bernal")
        val solano = UbicacionNeo("solano")

        val caminoABeraMaritimo = Camino(bera, Camino.TipoDeCamino.CaminoMaritimo)
        val caminoAVarelaAereo = Camino(varela, Camino.TipoDeCamino.CaminoAereo)
        val caminoAVarelaMaritimo = Camino(varela, Camino.TipoDeCamino.CaminoMaritimo)
        val caminoAVarelaTerrestre = Camino(varela, Camino.TipoDeCamino.CaminoTerreste)
        val caminoABernalAereo = Camino(bernal, Camino.TipoDeCamino.CaminoAereo)
        val caminoASolanoTerrestre = Camino(solano, Camino.TipoDeCamino.CaminoTerreste)
        val caminoAQuilmesAereo = Camino(quilmes, Camino.TipoDeCamino.CaminoAereo)

        quilmes.agregarCamino(caminoAVarelaAereo)
        varela.agregarCamino(caminoABernalAereo)
        bernal.agregarCamino(caminoAVarelaTerrestre)
        bera.agregarCamino(caminoAQuilmesAereo)
        bernal.agregarCamino(caminoASolanoTerrestre)
        solano.agregarCamino(caminoAVarelaMaritimo)
        solano.agregarCamino(caminoABeraMaritimo)

        ubicacionNeoRepository.save(bera)

        val caminoMasCorto = ubicacionNeoRepository.caminoMasCorto(solano.nombre, bernal.nombre, Camino.TipoDeCamino.CaminoTerreste, Camino.TipoDeCamino.CaminoAereo)

        Assertions.assertTrue(caminoMasCorto.isEmpty())
    }

    @Test
    fun `si se quiere llegar a la ubicacion por camino mas corto por vía marítimo o aéreo elegirá sin importar el tipo de camino`() {
        val bera = UbicacionNeo("bera")
        val quilmes = UbicacionNeo("quilmes")
        val varela = UbicacionNeo("varela")
        val bernal = UbicacionNeo("bernal")
        val solano = UbicacionNeo("solano")

        val caminoABeraMaritimo = Camino(bera, Camino.TipoDeCamino.CaminoMaritimo)
        val caminoAVarelaAereo = Camino(varela, Camino.TipoDeCamino.CaminoAereo)
        val caminoAVarelaTerrestre = Camino(varela, Camino.TipoDeCamino.CaminoTerreste)
        val caminoABernalAereo = Camino(bernal, Camino.TipoDeCamino.CaminoAereo)
        val caminoASolanoTerrestre = Camino(solano, Camino.TipoDeCamino.CaminoTerreste)
        val caminoAQuilmesTerrestre = Camino(quilmes, Camino.TipoDeCamino.CaminoTerreste)

        quilmes.agregarCamino(caminoAVarelaAereo)
        varela.agregarCamino(caminoABernalAereo)
        bernal.agregarCamino(caminoAVarelaTerrestre)
        bera.agregarCamino(caminoAQuilmesTerrestre)
        bernal.agregarCamino(caminoASolanoTerrestre)
        solano.agregarCamino(caminoAVarelaTerrestre)
        solano.agregarCamino(caminoABeraMaritimo)

        ubicacionNeoRepository.save(bera)

        val caminoMasCorto = ubicacionNeoRepository.caminoMasCorto(solano.nombre, bernal.nombre, Camino.TipoDeCamino.CaminoAereo, Camino.TipoDeCamino.CaminoMaritimo)

        Assertions.assertEquals(caminoMasCorto[0].nombre, solano.nombre)
        Assertions.assertEquals(caminoMasCorto[1].nombre, varela.nombre)
        Assertions.assertEquals(caminoMasCorto[2].nombre, bernal.nombre)
    }

    @Test
    fun `si se quiere llegar a una ubicacion y hay 2 caminos igual de cortos elige el primero que encuentra`() {
        val bera = UbicacionNeo("bera")
        val quilmes = UbicacionNeo("quilmes")
        val varela = UbicacionNeo("varela")
        val bernal = UbicacionNeo("bernal")
        val solano = UbicacionNeo("solano")

        val caminoABeraMaritimo = Camino(bera, Camino.TipoDeCamino.CaminoMaritimo)
        val caminoAVarelaAereo = Camino(varela, Camino.TipoDeCamino.CaminoAereo)
        val caminoAVarelaTerrestre = Camino(varela, Camino.TipoDeCamino.CaminoTerreste)
        val caminoABernalAereo = Camino(bernal, Camino.TipoDeCamino.CaminoAereo)
        val caminoASolanoTerrestre = Camino(solano, Camino.TipoDeCamino.CaminoTerreste)
        val caminoAQuilmesTerrestre = Camino(quilmes, Camino.TipoDeCamino.CaminoTerreste)

        bera.agregarCamino(caminoASolanoTerrestre)
        varela.agregarCamino(caminoAQuilmesTerrestre)
        solano.agregarCamino(caminoAQuilmesTerrestre)
        quilmes.agregarCamino(caminoAVarelaAereo)
        varela.agregarCamino(caminoABernalAereo)
        bernal.agregarCamino(caminoAVarelaTerrestre)
        bernal.agregarCamino(caminoASolanoTerrestre)
        quilmes.agregarCamino(caminoABeraMaritimo)

        ubicacionNeoRepository.save(bera)

        val caminoMasCorto = ubicacionNeoRepository.caminoMasCorto(bernal.nombre, quilmes.nombre, Camino.TipoDeCamino.CaminoAereo, Camino.TipoDeCamino.CaminoMaritimo)

        Assertions.assertEquals(caminoMasCorto.size, 3)
        Assertions.assertEquals(caminoMasCorto[0].nombre, bernal.nombre)
        Assertions.assertEquals(caminoMasCorto[1].nombre, solano.nombre)
        Assertions.assertEquals(caminoMasCorto[2].nombre, quilmes.nombre)
    }

    @AfterEach
    fun tearDown() {
        ubicacionNeoRepository.deleteAll()
    }

}
