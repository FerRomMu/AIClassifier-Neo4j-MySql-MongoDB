package ar.edu.unq.eperdemic

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.jdbc.JDBCDataDAO
import ar.edu.unq.eperdemic.persistencia.dao.jdbc.JDBCPatogenoDAO
import ar.edu.unq.eperdemic.services.PatogenoService
import ar.edu.unq.eperdemic.services.impl.DataServiceImpl
import ar.edu.unq.eperdemic.services.impl.PatogenoServiceImpl
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS


@TestInstance(PER_CLASS)
class PatogenoServiceTest {
    private val patogenoDAO: PatogenoDAO = JDBCPatogenoDAO()
    lateinit var patogeno: Patogeno
    private val patogenoService: PatogenoService = PatogenoServiceImpl(patogenoDAO);
    private val dataDAO = JDBCDataDAO();
    private val dataService = DataServiceImpl(patogenoDAO, dataDAO)

    @BeforeEach
    fun crearModelo() {

        dataService.crearSetDatosIniciales()

    }

    @Test
    fun testCrear(){
        patogeno = Patogeno("Gripe");
        patogeno.cantidadDeEspecies = 1
        patogeno.id = 10000000;
        val patogenoCreado = patogenoService.crearPatogeno(patogeno);

        assertEquals(patogenoCreado.id!! , patogeno.id!! )
    }

    @Test
    fun testRecuperar() {
        val patogenoObtenido = patogenoService.recuperarPatogeno(1)

        assertEquals(1, patogenoObtenido.id!!)
        assertEquals(0, patogenoObtenido.cantidadDeEspecies)
        assertEquals("Gripe", patogenoObtenido.tipo)
    }
    
    @Test
    fun testRecuperarATodos(){
        val patogenos = patogenoService.recuperarATodosLosPatogenos()
        assertEquals(21, patogenos.size)
    }

    @Test
    fun testAgregarEspecie() {
        var patogenoToTest = patogenoService.recuperarPatogeno(1)
        assertEquals(1, patogenoToTest.cantidadDeEspecies)

        patogenoService.agregarEspecie(1, "sars", "China")

        patogenoToTest = patogenoService.recuperarPatogeno(1)
        assertEquals(2, patogenoToTest.cantidadDeEspecies)
    }

    @AfterEach
    fun restartDB() {
        dataService.deleteAll()
    }
}