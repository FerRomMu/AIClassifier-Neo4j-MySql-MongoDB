package ar.edu.unq.eperdemic.persistencia.repository.mongo

import ar.edu.unq.eperdemic.modelo.Coordenada
import ar.edu.unq.eperdemic.modelo.Distrito
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DistritoMongoRepositoryTest {

    @Autowired
    lateinit var distritoMongoRepository: DistritoMongoRepository

    @Test
    fun `Si guardo un distrito, este se guarda correctamente`() {
        val distrito = Distrito("Nombre Distrito", listOf(Coordenada(1.0, 2.0), Coordenada(3.0, 4.0)))

        distritoMongoRepository.save(distrito)
        val distritoGuardado = distritoMongoRepository.findById(distrito.id!!).get()

        assertEquals(distrito.id, distritoGuardado.id)
        assertEquals(distrito.nombre, distritoGuardado.nombre)
        assertEquals(distrito.coordenadas.size, distritoGuardado.coordenadas.size)
        assertTrue(distrito.coordenadas.all {
            c -> distritoGuardado.coordenadas.any { co -> c.latitud == co.latitud && c.longitud == co.longitud }
        })
    }

    @AfterEach
    fun cleanup() {
        distritoMongoRepository.deleteAll()
    }
}