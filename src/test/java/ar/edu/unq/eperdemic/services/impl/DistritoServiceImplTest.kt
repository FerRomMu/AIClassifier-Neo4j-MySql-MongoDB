package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Coordenada
import ar.edu.unq.eperdemic.modelo.Distrito
import ar.edu.unq.eperdemic.persistencia.repository.mongo.DistritoMongoRepository
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


    @AfterEach
    fun cleanup() {
        distritoMongoRepository.deleteAll()
    }
}