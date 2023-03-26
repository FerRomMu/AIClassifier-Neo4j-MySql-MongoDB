package ar.edu.unq.eperdemic

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.DataDAO
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.jdbc.JDBCDataDAO
import ar.edu.unq.eperdemic.persistencia.dao.jdbc.JDBCPatogenoDAO
import ar.edu.unq.eperdemic.services.PatogenoService
import ar.edu.unq.eperdemic.services.impl.DataServiceImpl
import ar.edu.unq.eperdemic.services.impl.PatogenoServiceImpl
import org.junit.jupiter.api.*
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

        patogeno = Patogeno("Gripe");
        patogeno.cantidadDeEspecies = 1
        patogeno.id = 1;

    }

    @Test
    fun testCrear(){
        val patogenoCreado = patogenoService.crearPatogeno(patogeno);

        Assertions.assertEquals(patogenoCreado.id!! , patogeno.id!! )
    }

    @AfterEach
    fun restartDB() {
        dataService.deleteAll()
    }
}