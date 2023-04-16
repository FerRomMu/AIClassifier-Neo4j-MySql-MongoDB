package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.exceptions.DataNotFoundException
import ar.edu.unq.eperdemic.exceptions.IdNotFoundException
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.persistencia.dao.EspecieDAO
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.UbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateEspecieDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernatePatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.services.PatogenoService
import ar.edu.unq.eperdemic.services.UbicacionService
import ar.edu.unq.eperdemic.services.VectorService
import ar.edu.unq.eperdemic.utils.DataService
import ar.edu.unq.eperdemic.utils.impl.DataServiceImpl
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import javax.persistence.NoResultException

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PatogenoServiceTest {

    lateinit var patogenoDAO: PatogenoDAO
    lateinit var especieDAO: EspecieDAO
    lateinit var ubicacionDAO: UbicacionDAO
    lateinit var vectorDAO: VectorDAO

    lateinit var patogenoService: PatogenoService
    lateinit var ubicacionService: UbicacionService
    lateinit var vectorService: VectorService
    lateinit var dataService: DataService

    lateinit var patogeno: Patogeno
    lateinit var ubicacion: Ubicacion

    @BeforeEach
    fun crearModelo() {
        patogenoDAO = HibernatePatogenoDAO()
        especieDAO = HibernateEspecieDAO()
        ubicacionDAO = HibernateUbicacionDAO()
        vectorDAO = HibernateVectorDAO()

        vectorService = VectorServiceImpl(vectorDAO, ubicacionDAO)
        patogenoService = PatogenoServiceImpl(patogenoDAO, ubicacionDAO, especieDAO, vectorDAO)
        ubicacionService = UbicacionServiceImpl(ubicacionDAO)
        dataService = DataServiceImpl()

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
        assertEquals(patogenoRecuperado.cantidadDeEspecies(), patogeno.cantidadDeEspecies())
    }

    @Test
    fun `si trato de recuperar un patogeno inexistente falla`() {
        assertThrows(IdNotFoundException::class.java) { patogenoService.recuperarPatogeno(10000001) }
    }

    @Test
    fun `si trato de recuperar todos llegan todos`() {
        dataService.crearSetDeDatosIniciales()
        val patogenos = patogenoService.recuperarATodosLosPatogenos()

        assertEquals(21, patogenos.size)
    }

    @Test
    fun `si trato de recuperar todos y no hay nadie simplemente recibo 0`() {

        val patogenos = patogenoService.recuperarATodosLosPatogenos()

        assertEquals(0, patogenos.size)
    }

    @Test
    fun `si agrego una especie se persiste en el patogeno`() {
        dataService.crearSetDeDatosIniciales()
        patogeno = Patogeno("Gripe")

        val ubicacion = ubicacionService.recuperar(1)

        patogenoService.crearPatogeno(patogeno)
        patogenoService.agregarEspecie(patogeno.id!!, "virusT", ubicacion.id!!)

        val patogenoRecuperado = patogenoService.recuperarPatogeno(patogeno.id!!)

        assertTrue(patogenoRecuperado.especies.map{e -> e.nombre}.contains("virusT"))
    }

    @Test
    fun `si agrego una especie infecta a un vector de la ubicacion`() {
        dataService.crearSetDeDatosIniciales()

        var vectorInfectado = vectorService.recuperarVector(1)
        assertEquals(vectorInfectado.especiesContagiadas.size,0)

        val ubicacion = ubicacionService.recuperar(1)
        patogeno = Patogeno("Gripe")

        patogenoService.crearPatogeno(patogeno)
        patogenoService.agregarEspecie(patogeno.id!!, "virusT", ubicacion.id!!)
        vectorInfectado = vectorService.recuperarVector(1)

        assertEquals(vectorInfectado.especiesContagiadas.size,1)
    }

    @Test
    fun `si agrego una especie y no hay vectores a infectar en la ubicacion falla`() {
        patogeno = Patogeno("Gripe")

        patogenoService.crearPatogeno(patogeno)
        val ubicacionSinVectores = ubicacionService.crearUbicacion("bernal")

        assertThrows(NoResultException::class.java) {
            patogenoService.agregarEspecie(patogeno.id!!, "virusT", ubicacionSinVectores.id!!)
        }
    }

    @AfterEach
    fun deleteAll() {
        dataService.eliminarTodo()
    }

}