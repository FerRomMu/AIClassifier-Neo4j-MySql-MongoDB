package ar.edu.unq.eperdemic

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.jdbc.JDBCPatogenoDAO
import ar.edu.unq.eperdemic.services.PatogenoService
import ar.edu.unq.eperdemic.services.impl.PatogenoServiceImpl
import org.junit.jupiter.api.*
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class PatogenoServiceTest {
    private val patogenoDAO: PatogenoDAO = JDBCPatogenoDAO()
    lateinit var patogeno: Patogeno
    private val patogenoService: PatogenoService = PatogenoServiceImpl(patogenoDAO);

    @BeforeEach
    fun crearModelo() {

        patogeno = Patogeno("Gripe");
        patogeno.cantidadDeEspecies = 1
        patogeno.id = 1;

    }

    @Test
    fun testCrear(){
        val patogenoCreado = patogenoService.crearPatogeno(patogeno);

        Assertions.assertEquals(patogenoCreado.id!! , patogeno.id!! )
    }

}