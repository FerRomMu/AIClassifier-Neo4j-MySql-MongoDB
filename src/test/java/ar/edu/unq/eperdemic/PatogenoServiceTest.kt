package ar.edu.unq.eperdemic

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.jdbc.JDBCPatogenoDAO
import ar.edu.unq.eperdemic.services.impl.PatogenoServiceImpl
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PatogenoServiceTest {

    @Test
    fun testCrear(){
        val dao = JDBCPatogenoDAO();
        val service = PatogenoServiceImpl(dao);

        val patogeno = Patogeno("Gripe");
        patogeno.id = 1;

        service.crearPatogeno(patogeno);
        assertTrue(true);
    }

}