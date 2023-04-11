package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.exceptions.IdNotFoundException
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernatePatogenoDAO
import ar.edu.unq.eperdemic.services.impl.PatogenoServiceImpl
import ar.edu.unq.eperdemic.utils.DataService
import ar.edu.unq.eperdemic.utils.jdbc.DataServiceJDBC
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

    @AfterEach
    fun deleteAll() {

    }

}