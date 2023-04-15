package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.exceptions.IdNotFoundException
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernatePatogenoDAO
import ar.edu.unq.eperdemic.utils.DataService
import ar.edu.unq.eperdemic.utils.impl.DataServiceImpl
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PatogenoServiceTest {

    lateinit var patogenoDAO : PatogenoDAO;
    lateinit var patogeno :Patogeno;
    lateinit var patogenoService: PatogenoServiceImpl;
    lateinit var dataService : DataService

    @BeforeEach
    fun crearModelo() {
        patogenoDAO = HibernatePatogenoDAO()
        patogenoService = PatogenoServiceImpl(patogenoDAO)
        dataService = DataServiceImpl()
    }

    @Test
    fun `si creo un patogeno este recibe un id`(){
        patogeno = Patogeno("Gripe")
        assertNull(patogeno.id)

        patogenoService.crearPatogeno(patogeno)
        assertNotNull(patogeno.id)
    }

    @Test
    fun `si creo un patogeno lo puedo recuperar con sus datos`() {
        patogeno = Patogeno("Gripe")

        patogenoService.crearPatogeno(patogeno)
        val patogenoRecuperado = patogenoService.recuperarPatogeno(patogeno.id!!)

        assertEquals(patogenoRecuperado.id!!, patogeno.id)
        assertEquals(patogenoRecuperado.tipo, patogeno.tipo)
        assertEquals(patogenoRecuperado.cantidadDeEspecies, patogeno.cantidadDeEspecies)
    }

    @Test
    fun `si trato de recuperar un patogeno inexistente falla`() {
        assertThrows(IdNotFoundException::class.java) { patogenoService.recuperarPatogeno(10000001) }
    }

    @Test
    fun `si trato de recuperar todos llegan todos`() {
        dataService.crearSetDeDatosIniciales()
        val patogenos = patogenoService.recuperarATodosLosPatogenos()

        assertEquals(21, patogenos.size)
    }

    @Test
    fun `si trato de recuperar todos y no hay nadie simplemente recibo 0`() {

        val patogenos = patogenoService.recuperarATodosLosPatogenos()

        assertEquals(0, patogenos.size)
    }

    @AfterEach
    fun deleteAll() {
        dataService.eliminarTodo()
    }

}