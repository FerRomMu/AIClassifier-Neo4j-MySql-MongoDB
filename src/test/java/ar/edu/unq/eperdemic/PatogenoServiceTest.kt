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
    fun testCrearPatogeno(){
        patogeno = Patogeno("Gripe");
        patogeno.cantidadDeEspecies = 1
        patogeno.id = 10000000;
        val patogenoCreado = patogenoService.crearPatogeno(patogeno);

        assertEquals(patogenoCreado.id!! , patogeno.id!! )
    }

    @Test
    fun testRecuperarPatogeno() {
        val patogenoObtenido = patogenoService.recuperarPatogeno(1)

        assertEquals(1, patogenoObtenido.id!!)
        assertEquals(1, patogenoObtenido.cantidadDeEspecies)
        assertEquals("Tipo 1", patogenoObtenido.tipo)
    }
    
    @Test
    fun testRecuperarATodosLosPatogenos(){
        val patogenos = patogenoService.recuperarATodosLosPatogenos()

        assertEquals(21, patogenos.size)
    }

    @Test
    fun testActualizarPatogeno() {
        val patogenoAActualizar = patogenoService.recuperarPatogeno(2)
        patogenoAActualizar.tipo = "Tipo 2 actualizado"
        patogenoAActualizar.cantidadDeEspecies = 3000

        patogenoService.actualizarPatogeno(patogenoAActualizar)

        val patogenoActualizado = patogenoService.recuperarPatogeno(patogenoAActualizar.id!!)

        assertEquals("Tipo 2 actualizado", patogenoActualizado.tipo)
        assertEquals(3000, patogenoActualizado.cantidadDeEspecies)
    }

    @AfterEach
    fun restartDB() {
        dataService.deleteAll()
    }
}