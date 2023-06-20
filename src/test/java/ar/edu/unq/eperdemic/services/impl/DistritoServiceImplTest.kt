package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Coordenada
import ar.edu.unq.eperdemic.modelo.Distrito
import ar.edu.unq.eperdemic.modelo.UbicacionMongo
import ar.edu.unq.eperdemic.persistencia.repository.mongo.DistritoMongoRepository
import ar.edu.unq.eperdemic.persistencia.repository.mongo.UbicacionMongoRepository
import ar.edu.unq.eperdemic.services.DistritoService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DistritoServiceImplTest {

    @Autowired lateinit var distritoService: DistritoService
    @Autowired lateinit var distritoMongoRepository : DistritoMongoRepository
    @Autowired lateinit var ubicacionMongoRepository : UbicacionMongoRepository


    @Test
    fun `Si guardo un distrito, este se guarda correctamente`() {
        val distrito = Distrito("Nombre Distrito", listOf(Coordenada(1.0, 2.0), Coordenada(3.0, 4.0)))

        distritoService.crear(distrito)
        val distritoGuardado = distritoMongoRepository.findById(distrito.id!!).get()

        Assertions.assertEquals(distrito.id, distritoGuardado.id)
        Assertions.assertEquals(distrito.nombre, distritoGuardado.nombre)
        Assertions.assertEquals(distrito.coordenadas.size, distritoGuardado.coordenadas.size)
        Assertions.assertTrue(distrito.coordenadas.all { c ->
            distritoGuardado.coordenadas.any { co -> c.latitud == co.latitud && c.longitud == co.longitud }
        })
    }

    @Test
    fun `test distrito mas enfermo`() {
        val distrito1 = Distrito("Nombre Distrito 1",
                listOf(Coordenada(1.0, 2.0), Coordenada(3.0, 6.0), Coordenada(7.0, 10.0)))
        val distrito2 = Distrito("Nombre Distrito 2",
                listOf(Coordenada(11.0, 16.0), Coordenada(17.0, 20.0), Coordenada(21.0, 23.0)))
        val distrito3 = Distrito("Nombre Distrito 3",
                listOf(Coordenada(30.0, 32.0),Coordenada(38.0, 34.0), Coordenada(35.0, 40.0)))
        val distritoMasEnfermo = Distrito("Nombre Distrito Mas Enfermo",
                listOf(Coordenada(40.0, 42.0), Coordenada(45.0, 50.0), Coordenada(51.0, 53.0)))

        val ubicacion1Distr1 = UbicacionMongo(Coordenada(1.0, 2.0), "Ubicacion 1 Distr 1", distrito1)
        ubicacion1Distr1.hayAlgunInfectado = true
        val ubicacion2Distr1 = UbicacionMongo(Coordenada(3.0, 5.0), "Ubicacion 2 Distr 1", distrito1)
        ubicacion2Distr1.hayAlgunInfectado = true

        val ubicacion1Distr2 = UbicacionMongo(Coordenada(11.0, 16.0), "Ubicacion 1 Distr 2", distrito2)
        ubicacion1Distr2.hayAlgunInfectado = true
        val ubicacion2Distr2 = UbicacionMongo(Coordenada(17.0, 20.0), "Ubicacion 2 Distr 2", distrito2)
        ubicacion2Distr2.hayAlgunInfectado = true
        val ubicacion3Distr2 = UbicacionMongo(Coordenada(21.0, 23.0), "Ubicacion 3 Distr 2", distrito2)
        ubicacion3Distr2.hayAlgunInfectado = false

        val ubicacion1Distr3 = UbicacionMongo(Coordenada(31.0, 33.0), "Ubicacion 1 Distr 3", distrito3)
        ubicacion1Distr3.hayAlgunInfectado = true
        val ubicacion2Distr3 = UbicacionMongo(Coordenada(35.0, 37.0), "Ubicacion 2 Distr 3", distrito3)
        ubicacion2Distr3.hayAlgunInfectado = false
        val ubicacion3Distr3 = UbicacionMongo(Coordenada(36.0, 35.0), "Ubicacion 3 Distr 3", distrito3)
        ubicacion3Distr3.hayAlgunInfectado = false
        val ubicacion4Distr3 = UbicacionMongo(Coordenada(35.0, 39.0), "Ubicacion 4 Distr 3", distrito3)
        ubicacion4Distr3.hayAlgunInfectado = false

        val ubicacion1DistrMasEnfermo = UbicacionMongo(Coordenada( 40.0, 42.0), "Ubicacion 1 DistrMasEnfermo", distritoMasEnfermo)
        ubicacion1DistrMasEnfermo.hayAlgunInfectado = true
        val ubicacion2DistrMasEnfermo = UbicacionMongo(Coordenada(45.0, 48.0), "Ubicacion 2 DistrMasEnfermo", distritoMasEnfermo)
        ubicacion2DistrMasEnfermo.hayAlgunInfectado = true
        val ubicacion3DistrMasEnfermo = UbicacionMongo(Coordenada(51.0, 53.0), "Ubicacion 3 DistrMasEnfermo", distritoMasEnfermo)
        ubicacion3DistrMasEnfermo.hayAlgunInfectado = true

        ubicacionMongoRepository.saveAll(listOf(ubicacion1Distr1, ubicacion2Distr1, ubicacion1Distr2,
                ubicacion2Distr2, ubicacion3Distr2, ubicacion1Distr3, ubicacion2Distr3, ubicacion3Distr3,
                ubicacion4Distr3, ubicacion1DistrMasEnfermo, ubicacion2DistrMasEnfermo, ubicacion3DistrMasEnfermo))

        distritoMongoRepository.saveAll(listOf(distrito1, distrito2,distrito3, distritoMasEnfermo))

        val distrMasEnfermo = distritoService.distritoMasEnfermo()

        Assertions.assertEquals(distrMasEnfermo.nombre, distritoMasEnfermo.nombre)
    }

    @AfterEach
    fun cleanup() {
        distritoMongoRepository.deleteAll()
        ubicacionMongoRepository.deleteAll()
    }
}