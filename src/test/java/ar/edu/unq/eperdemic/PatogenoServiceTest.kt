package ar.edu.unq.eperdemic

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.jdbc.JDBCPatogenoDAO
import ar.edu.unq.eperdemic.services.PatogenoService
import ar.edu.unq.eperdemic.services.impl.PatogenoServiceImpl
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

    @BeforeEach
    fun crearModelo() {

        patogeno = Patogeno("Gripe");
        patogeno.cantidadDeEspecies = 0
        patogeno.id = 1;

    }

    @Test
    fun testCrear(){
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
        val otroPatogeno = Patogeno("Covid")
        otroPatogeno.id = 2

        //patogenoService.crearPatogeno(otroPatogeno)

        val patogenos = patogenoService.recuperarATodosLosPatogenos()
        Assertions.assertEquals(2, patogenos.size)
    }
}