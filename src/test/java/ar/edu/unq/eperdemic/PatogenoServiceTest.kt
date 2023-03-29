package ar.edu.unq.eperdemic

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.services.impl.PatogenoServiceImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension


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