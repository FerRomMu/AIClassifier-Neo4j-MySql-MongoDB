package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernatePatogenoDAO
import ar.edu.unq.eperdemic.services.impl.PatogenoServiceImpl
import ar.edu.unq.eperdemic.utils.DataService
import ar.edu.unq.eperdemic.utils.jdbc.DataServiceJDBC
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension


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
    fun testCrearUnPatogenoConIdYSinEspecies(){
        patogeno = Patogeno("Gripe")
        assertNull(patogeno.id)

        patogenoService.crearPatogeno(patogeno)
        assertNotNull(patogeno.id)
    }

    @Test
    fun testRecuperarUnPatogenoYaCreado() {

    }
    
    @Test
    fun testRecuperarATodosLosPatogenos(){

    }

    @Test
    fun testAgregarEspecie() {
        /*patogeno = Patogeno("Gripe")
        var id : Long = 1;
        `when`(patogenoDAO.recuperar(id)).thenReturn(patogeno)

        patogenoService.agregarEspecie(id,"Gripe","Chile")
        Mockito.verify(patogenoDAO).recuperar(id)
        Mockito.verify(patogenoDAO).actualizar(patogeno)
        assertEquals(1, patogeno.cantidadDeEspecies)*/
    }

    @AfterEach
    fun deleteAll() {

    }

}