package ar.edu.unq.eperdemic.persistencia.repository.spring

import ar.edu.unq.eperdemic.modelo.Mutacion
import ar.edu.unq.eperdemic.modelo.SupresionBiomecanica
import ar.edu.unq.eperdemic.utils.DataService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MutacionRepositoryTest {

    @Autowired lateinit var mutacionRepository: MutacionRepository
    lateinit var supresion: Mutacion
    @Autowired lateinit var data: DataService

    @BeforeEach
    fun setUp() {
        supresion = SupresionBiomecanica(50)
    }

    @Test
    fun `si creo una mutación al guardarla se le asigna un id`() {
        assertNull(supresion.id)

        mutacionRepository.save(supresion)

        assertNotNull(supresion.id)
    }

    @Test
    fun `si trato de recuperar una mutación existente con su id la obtengo`() {
        data.persistir(supresion)
        val supresionRecuperado =  mutacionRepository.findById(supresion.id!!).get()

        assertEquals(supresion.id, supresionRecuperado.id)
        assertTrue(supresion.equals(supresionRecuperado))
    }

    @Test
    fun `si trato de recuperar una mutación inexistente devuelve null`() {
        assertTrue(mutacionRepository.findById(10000001).isEmpty)
    }

    @Test
    fun `si borro una mutación esta deja de estar persistida`(){
        data.persistir(supresion)

        mutacionRepository.deleteById(supresion.id!!)

        assertTrue(mutacionRepository.findById(supresion.id!!).isEmpty)
    }

    @Test
    fun `si borro una mutación con id invalido no devuelve nada`() {
        assertThrows(NullPointerException::class.java) { mutacionRepository.deleteById(supresion.id!!) }
    }

    @AfterEach
    fun tearDown() {
        data.eliminarTodo()
    }

}