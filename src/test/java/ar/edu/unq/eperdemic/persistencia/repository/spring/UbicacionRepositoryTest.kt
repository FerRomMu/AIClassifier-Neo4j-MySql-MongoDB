package ar.edu.unq.eperdemic.persistencia.repository.spring

import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.utils.DataService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UbicacionRepositoryTest {
  
    lateinit var ubicacion: Ubicacion
    @Autowired
    lateinit var ubicacionRepository: UbicacionRepository
    @Autowired
    lateinit var data: DataService

    @BeforeEach
    fun setUp() {
        ubicacion = Ubicacion("unaUbicacion")
    }

    @Test
    fun `si creo una ubicacion al guardarla se le asigna un id`() {

        assertNull(ubicacion.id)

        ubicacionRepository.save(ubicacion)

        assertNotNull(ubicacion.id)

    }

    @Test
    fun `Puedo recuperar una ubicacion persistida con su id`() {
        data.persistir(ubicacion)
        val ubicacionRecuperada =  ubicacionRepository.findById(ubicacion.id!!).get()

        assertEquals(ubicacion.id, ubicacionRecuperada.id)
        assertEquals(ubicacion.nombre, ubicacionRecuperada.nombre)
    }

    @Test
    fun `si trato de recuperar una ubicacion inexistente devuelve null`() {
        assertTrue(ubicacionRepository.findById(10000001).isEmpty)
    }

    @Test
    fun `si recupero todas las ubicaciones recibo todas`(){

        val ubicacionPersistidos = data.crearSetDeDatosIniciales().filterIsInstance<Ubicacion>()
        val recuperados = ubicacionRepository.findAll().toList()

        assertEquals(21, recuperados.size)
        assertTrue(
            recuperados.all { ubicacion ->
                ubicacionPersistidos.any {
                    it.id == ubicacion.id &&
                            it.nombre == ubicacion.nombre
                }
            }
        )
    }

    @Test
    fun `si borro un ubicacion este deja de estar persistido y el resto sigue estando`(){

        val ubicacions = data.crearSetDeDatosIniciales().filterIsInstance<Ubicacion>()
        val ubicacionABorrar = ubicacions.first()

        ubicacionRepository.deleteById(ubicacionABorrar.id!!)

        assertTrue(
            ubicacions.all { ubicacion ->
                ubicacion.id != ubicacionABorrar.id && ubicacionRepository.findById(ubicacion.id!!).isPresent
                        ||
                        ubicacion.id == ubicacionABorrar.id && ubicacionRepository.findById(ubicacion.id!!).isEmpty
            }
        )
    }

    @AfterEach
    fun tearDown() {
        data.eliminarTodo()
    }
}
