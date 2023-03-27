package ar.edu.unq.eperdemic

import ar.edu.unq.eperdemic.modelo.Especie
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
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.jupiter.MockitoExtension


//@TestInstance(PER_CLASS)
//@RunWith(MockitoJUnitRunner::class)

@ExtendWith(MockitoExtension::class)
class PatogenoServiceTest {

    @Mock
    lateinit var patogenoDAO : PatogenoDAO;

    lateinit var patogeno :Patogeno;

    @InjectMocks
    lateinit var patogenoService: PatogenoServiceImpl;

    @BeforeEach
    fun crearModelo() {
    }


    @Test
    fun testCrearPatogeno(){
        patogeno = Patogeno("Gripe")
        patogenoService.crearPatogeno(patogeno)
        Mockito.verify(patogenoDAO).crear(patogeno)
    }

    @Test
    fun testRecuperarPatogeno() {
        var id : Long = 1;
        patogenoService.recuperarPatogeno(id)
        Mockito.verify(patogenoDAO).recuperar(id)
    }
    
    @Test
    fun testRecuperarATodosLosPatogenos(){
        patogenoService.recuperarATodosLosPatogenos()
        Mockito.verify(patogenoDAO).recuperarATodos()
    }

    @Test
    fun testAgregarEspecie() {
        patogeno = Patogeno("Gripe")
        var id : Long = 1;
        `when`(patogenoDAO.recuperar(id)).thenReturn(patogeno)

        patogenoService.agregarEspecie(id,"Gripe","Chile")
        Mockito.verify(patogenoDAO).recuperar(id)
        Mockito.verify(patogenoDAO).actualizar(patogeno)
    }

    @Test
    fun testActualizarPatogeno() {
        patogeno = Patogeno("Gripe")
        patogenoService.actualizarPatogeno(patogeno)
        Mockito.verify(patogenoDAO).actualizar(patogeno)
    }


}