package ar.edu.unq.eperdemic.persistencia.repository.spring

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.utils.DataService
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EspecieRepositoryTest {

    @Autowired lateinit var especieRepository: EspecieRepository
    lateinit var especie: Especie
    @Autowired lateinit var data: DataService

    @BeforeEach
    fun setUp() {
        val patogeno = Patogeno("Gripa")
        especie = Especie(patogeno, "covid", "China")

        data.persistir(patogeno)
    }

    @Test
    fun `si creo un Especie al guardarlo se le asigna un id`() {

        assertNull(especie.id)

        especieRepository.save(especie)

        assertNotNull(especie.id)

    }

    @Test
    fun `si guardo una Especie con id se actualiza`() {

        data.persistir(especie)
        assertEquals("China", especie.paisDeOrigen)
        especie.paisDeOrigen = "USA"

        especieRepository.save(especie)
        val especieActualizado = especieRepository.findById(especie.id!!).get()

        assertEquals("USA", especieActualizado.paisDeOrigen)
    }

    @Test
    fun `si trato de recuperar una Especie existente con su id lo obtengo`() {
        data.persistir(especie)
        val especieRecuperado =  especieRepository.findById(especie.id!!).get()

        assertEquals(especie.id, especieRecuperado.id)
        assertEquals(especie.nombre, especieRecuperado.nombre)
        assertEquals(especie.paisDeOrigen, especieRecuperado.paisDeOrigen)
    }

    @Test
    fun `si trato de recuperar un Especie inexistente devuelve null`() {
        assertTrue(especieRepository.findById(10000001).isEmpty)
    }

    @Test
    fun `si recupero todos los especies recibo todos`(){

        val especiesPersistidos = data.crearSetDeDatosIniciales().filterIsInstance<Especie>()
        val recuperados = especieRepository.findAll().toList()
        assertEquals(especiesPersistidos.size, recuperados.size)
        assertTrue(
            recuperados.all {especie ->
                especiesPersistidos.any {
                    it.id == especie.id &&
                            it.nombre == especie.nombre &&
                            it.paisDeOrigen == especie.paisDeOrigen
                }
            }
        )
    }

    @Test
    fun `si borro un especie este deja de estar persistido`(){

        data.persistir(especie)

        especieRepository.deleteById(especie.id!!)

        assertTrue(especieRepository.findById(especie.id!!).isEmpty)
    }

    @Test
    fun `si borro un especie  con id invalido no devuelve nada`() {

        assertThrows(NullPointerException::class.java) { especieRepository.deleteById(especie.id!!) }
    }

    @Test
    fun `si pido la cantidad de infectados de una especie me la da`() {
        val especieContagiada = data.crearPandemiaPositiva()

        assertEquals(21, especieRepository.cantidadDeInfectados(especieContagiada.id!!))
    }

    @AfterEach
    fun tearDown() {
        data.eliminarTodo()
    }

}