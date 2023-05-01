package ar.edu.unq.eperdemic.persistencia.repository.spring

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.utils.DataService
import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PatogenoRepositoryTest {

    @Autowired lateinit var patogenoRepository: PatogenoRepository
    lateinit var patogeno: Patogeno
    @Autowired lateinit var data: DataService

    @BeforeEach
    fun setUp() {
        data.eliminarTodo()
        patogeno = Patogeno("Gripe")
    }

    @Test
    fun `si creo un Patogeno al guardarlo se le asigna un id`() {

        assertNull(patogeno.id)

        patogenoRepository.save(patogeno)

        assertNotNull(patogeno.id)

    }

    @Test
    fun `si guardo un Patogeno con id se actualiza`() {

        data.persistir(patogeno)
        assertEquals(0, patogeno.cantidadDeEspecies())

        patogeno.crearEspecie("especieA", "Japon")

        patogenoRepository.save(patogeno)
        val patogenoActualizado = patogenoRepository.findById(patogeno.id!!).get()

        assertEquals(1, patogenoActualizado.cantidadDeEspecies())
    }

    @Test
    fun `si guardo un Patogeno lo puedo recuperar con su id`() {
        data.persistir(patogeno)
        val patogenoRecuperado =  patogenoRepository.findById(patogeno.id!!).get()

        assertEquals(patogeno.id, patogenoRecuperado.id)
        assertEquals(patogeno.tipo, patogenoRecuperado.tipo)
        assertEquals(patogeno.cantidadDeEspecies(), patogenoRecuperado.cantidadDeEspecies())
    }

    @Test
    fun `si trato de recuperar un Patogeno inexistente devuelve null`() {
        assertTrue(patogenoRepository.findById(10000001).isEmpty)
    }

    @Test
    fun `si recupero todos los vectores recibo todos`(){

        data.crearSetDeDatosIniciales()
        val recuperadosi = patogenoRepository.findAll()
        val recuperados = recuperadosi.toList()
        assertEquals(21, recuperados.size)

    }

    @Test
    fun `si trato de recuperar las especies de un patogeno las devuelve`() {
/*
        patogeno = Patogeno("Gripe")

        patogeno.crearEspecie("virusT", "mansion spencer")
        patogeno.crearEspecie("virusG", "raccoon city")
        patogeno.crearEspecie("virus progenitor", "montanas arklay")
        data.persistir(patogeno)

        val especies: List<String> = patogenoRepository.especiesDePatogeno(patogeno.id!!).map{ e -> e.nombre}

        assertEquals(3, especies.size)
        assertTrue(especies.contains("virusT"))
        assertTrue(especies.contains("virusG"))
        assertTrue(especies.contains("virus progenitor"))*/

    }

    @Test
    fun `esPandemia`() {
        val especiePandemica = data.crearPandemiaPositiva()
        assertTrue(patogenoRepository.esPandemia(especiePandemica.id!!))
    }

    @AfterEach
    fun tearDown() {
        data.eliminarTodo()
    }

}