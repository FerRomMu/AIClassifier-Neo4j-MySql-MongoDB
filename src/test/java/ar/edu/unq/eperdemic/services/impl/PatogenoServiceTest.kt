package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.exceptions.DataNotFoundException
import ar.edu.unq.eperdemic.exceptions.IdNotFoundException
import ar.edu.unq.eperdemic.modelo.*
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
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx
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

    @BeforeEach
    fun crearModelo() {
        patogenoDAO = HibernatePatogenoDAO()
        especieDAO = HibernateEspecieDAO()
        ubicacionDAO = HibernateUbicacionDAO()
        vectorDAO = HibernateVectorDAO()

        vectorService = VectorServiceImpl(vectorDAO, ubicacionDAO, especieDAO)
        patogenoService = PatogenoServiceImpl(patogenoDAO, especieDAO, vectorDAO)
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

        val ubicacion = ubicacionService.crearUbicacion("Ubicacion")
        var vectorInfectado = Vector(TipoDeVector.Persona)
        vectorInfectado.ubicacion = ubicacion
        runTrx { vectorDAO.guardar(vectorInfectado) }
        patogeno = Patogeno("Gripe")

        patogenoService.crearPatogeno(patogeno)
        patogenoService.agregarEspecie(patogeno.id!!, "virusT", ubicacion.id!!)
        val patogenoRecuperado = patogenoService.recuperarPatogeno(patogeno.id!!)

        assertTrue(patogenoRecuperado.especies.map{e -> e.nombre}.contains("virusT"))
    }

    @Test
    fun `si agrego una especie infecta a un vector de la ubicacion`() {

        val ubicacionPatogeno = ubicacionService.crearUbicacion("Ubicacion")
        var vectorInfectado = Vector(TipoDeVector.Persona)
        vectorInfectado.ubicacion = ubicacionPatogeno
        runTrx { vectorDAO.guardar(vectorInfectado) }

        assertFalse(vectorInfectado.especiesContagiadas.map{e -> e.nombre}.contains("virusT"))

        patogeno = Patogeno("Gripe")
        patogenoService.crearPatogeno(patogeno)
        patogenoService.agregarEspecie(patogeno.id!!, "virusT", ubicacionPatogeno.id!!)
        vectorInfectado = vectorService.recuperarVector(vectorInfectado.id!!)

        assertTrue(vectorInfectado.especiesContagiadas.map{e -> e.nombre}.contains("virusT"))
    }

    @Test
    fun `si agrego una especie se persiste la especie`() {

        val especieService = EspecieServiceImpl(HibernateEspecieDAO())
        val ubicacionPatogeno = ubicacionService.crearUbicacion("Ubicacion")
        val vector = Vector(TipoDeVector.Persona)
        vector.ubicacion = ubicacionPatogeno
        runTrx { vectorDAO.guardar(vector) }
        patogeno = Patogeno("Gripe")
        patogenoService.crearPatogeno(patogeno)
        val especieCreada = patogenoService.agregarEspecie(patogeno.id!!, "virus", ubicacionPatogeno.id!!)

        val especiePersistida = especieService.recuperarEspecie(especieCreada.id!!)
        
        assertEquals(especieCreada.id, especiePersistida.id)
        assertEquals(especieCreada.patogeno.id, especiePersistida.patogeno.id)
        assertEquals(especieCreada.nombre, especiePersistida.nombre)

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

    @Test
    fun `si trato de recuperar las especies de un patogeno las devuelve`() {
        patogeno = Patogeno("Gripe")

        patogeno.crearEspecie("virusT", "mansion spencer")
        patogeno.crearEspecie("virusG", "raccoon city")
        patogeno.crearEspecie("virus progenitor", "montanas arklay")

        patogenoService.crearPatogeno(patogeno)

        val especies = patogenoService.especiesDePatogeno(patogeno.id!!).map{e -> e.nombre}

        assertEquals(3, especies.size)

        assertTrue(especies.contains("virusT"))
        assertTrue(especies.contains("virusG"))
        assertTrue(especies.contains("virus progenitor"))
    }

    @Test
    fun `si ejecuto esPandemia devuelve verdadero para una especie en mas de la mitad de ubicaciones`(){
        val especie = dataService.crearPandemiaPositiva()

        assertTrue(patogenoService.esPandemia(especie.id!!))
    }

    @Test
    fun `si ejecuto esPandemia devuelve falso para una especie en menos de la mitad de ubicaciones`(){
        dataService.crearSetDeDatosIniciales()

        var patogeno = Patogeno("Gripe")
        var especie = Especie("21","BR", patogeno)
        var ubicacion = ubicacionService.crearUbicacion("Lugar 21")

        patogenoService.crearPatogeno(patogeno)

        var vector = vectorService.crearVector(TipoDeVector.Persona, ubicacion.id!!)
        vectorService.infectar(vector,especie)

        assertFalse(patogenoService.esPandemia(especie.id!!))
    }

    @AfterEach
    fun deleteAll() {
        dataService.eliminarTodo()
    }

}