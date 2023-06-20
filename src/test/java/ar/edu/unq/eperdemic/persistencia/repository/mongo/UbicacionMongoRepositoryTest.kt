package ar.edu.unq.eperdemic.persistencia.repository.mongo

import ar.edu.unq.eperdemic.modelo.Coordenada
import ar.edu.unq.eperdemic.modelo.UbicacionMongo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UbicacionMongoRepositoryTest {

    @Autowired
    lateinit var ubicacionMongoRepository: UbicacionMongoRepository

    @Test
    fun `Si pido una ubicacion por su nombre me la devuelve`(){
        val coordenada = Coordenada(1.0,1.0)
        val ubicacion = UbicacionMongo(coordenada, "Ubicacion1")
        val ubicacion2 = UbicacionMongo(coordenada, "Ubicacion2")

        ubicacionMongoRepository.save(ubicacion)
        ubicacionMongoRepository.save(ubicacion2)

        val ubicacionRecuperada = ubicacionMongoRepository.findByNombre(ubicacion.nombre!!)

        assertEquals(ubicacion.nombre, ubicacionRecuperada.nombre)
        assertEquals(ubicacion.id, ubicacionRecuperada.id)
        assertEquals(ubicacion.coordenada.latitud, ubicacionRecuperada.coordenada.latitud)
        assertEquals(ubicacion.coordenada.longitud, ubicacionRecuperada.coordenada.longitud)
        assertEquals(ubicacion.hayAlgunInfectado, ubicacionRecuperada.hayAlgunInfectado)
    }

    @Test
    fun `Si una ubicacion esta a menos de una distancia dada de un punto dado devuelve verdadero`(){
        val coordenada = Coordenada(1.0,1.0)
        val coordenada2 = Coordenada(1.1,1.1)
        val ubicacion = UbicacionMongo(coordenada, "Ubicacion1")

        ubicacionMongoRepository.save(ubicacion)

        assertTrue(ubicacionMongoRepository.isLocationNearby(ubicacion.nombre!!, coordenada2.longitud, coordenada2.latitud, 100000.0))
    }

    @Test
    fun `Si una ubicacion esta a mas de una distancia dada de un punto dado devuelve verdadero`(){
        val coordenada = Coordenada(1.0,1.0)
        val coordenada2 = Coordenada(2.1,2.1)
        val ubicacion = UbicacionMongo(coordenada, "Ubicacion1")

        ubicacionMongoRepository.save(ubicacion)

        assertFalse(ubicacionMongoRepository.isLocationNearby(ubicacion.nombre!!, coordenada2.longitud, coordenada2.latitud, 100000.0))
    }

    @Test
    fun `Si hay infectados me devuelve una lista con las ubicaciones infectadas`() {
        val coordenada = Coordenada(1.0,1.0)
        val coordenada2 = Coordenada(4.0,1.0)
        val coordenada3 = Coordenada(3.0,1.0)
        val coordenada4 = Coordenada(2.0,1.0)
        val ubicacion = UbicacionMongo(coordenada, "Ubicacion1")
        val ubicacion2 = UbicacionMongo(coordenada2, "Ubicacion2")
        val ubicacion3 = UbicacionMongo(coordenada3, "Ubicacion3")
        val ubicacionSinInfectados = UbicacionMongo(coordenada4, "Ubicacion4")
        ubicacion.hayAlgunInfectado = true
        ubicacion2.hayAlgunInfectado = true
        ubicacion3.hayAlgunInfectado = true
        ubicacionMongoRepository.save(ubicacion)
        ubicacionMongoRepository.save(ubicacion2)
        ubicacionMongoRepository.save(ubicacion3)
        ubicacionMongoRepository.save(ubicacionSinInfectados)

        val coordenadasDeInfectados = ubicacionMongoRepository.findCoordenadasConInfectados()
        assertEquals(3, coordenadasDeInfectados.size)
        assertTrue(coordenadasDeInfectados.any { co -> coordenada.latitud == co.latitud && coordenada.longitud == co.longitud })
        assertTrue(coordenadasDeInfectados.any { co -> coordenada2.latitud == co.latitud && coordenada2.longitud == co.longitud })
        assertTrue(coordenadasDeInfectados.any { co -> coordenada3.latitud == co.latitud && coordenada3.longitud == co.longitud })
    }

    @Test
    fun `Si no hay infectados me devuelve una lista vacía`() {
        val coordenada = Coordenada(1.0,1.0)

        val ubicacion = UbicacionMongo(coordenada, "Ubicacion1")

        ubicacionMongoRepository.save(ubicacion)

        assertTrue(ubicacionMongoRepository.findCoordenadasConInfectados().isEmpty())
    }

    @AfterEach
    fun cleanup() {
        ubicacionMongoRepository.deleteAll()
    }
}