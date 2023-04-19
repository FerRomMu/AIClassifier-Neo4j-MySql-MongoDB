package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.exceptions.IdNotFoundException
import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.EspecieDAO
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.UbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateEspecieDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernatePatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.services.EspecieService
import ar.edu.unq.eperdemic.services.PatogenoService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx
import ar.edu.unq.eperdemic.utils.DataService
import ar.edu.unq.eperdemic.utils.impl.DataServiceImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EspecieServiceImplTest {

    lateinit var especieDAO: EspecieDAO
    lateinit var patogenoDAO: PatogenoDAO
    lateinit var especieService: EspecieService
    lateinit var patogenoService: PatogenoService
    lateinit var especie: Especie
    lateinit var patogeno: Patogeno
    lateinit var dataService : DataService
    lateinit var vectorDAO: VectorDAO
    lateinit var ubicacionDAO: UbicacionDAO
    lateinit var vectorService: VectorServiceImpl

    @BeforeEach
    fun setup() {
        especieDAO = HibernateEspecieDAO()
        patogenoDAO = HibernatePatogenoDAO()
        ubicacionDAO = HibernateUbicacionDAO()
        vectorDAO = HibernateVectorDAO()

        especieService = EspecieServiceImpl(especieDAO)
        dataService = DataServiceImpl()
        vectorService = VectorServiceImpl(vectorDAO,ubicacionDAO,especieDAO )

    }

    @Test
    fun `puedo recuperar una especie guardada con su id`() {

        patogeno = Patogeno("Gripe")
        especie = Especie(patogeno,"especie11", "ARG")

        runTrx {
            patogenoDAO.guardar(patogeno)
            especieDAO.guardar(especie)
        }

        val recuperado = especieService.recuperarEspecie(especie.id!!)

        assertEquals(especie.id, recuperado.id)
        assertEquals("especie11", recuperado.nombre)
        assertEquals("ARG", recuperado.paisDeOrigen)
        assertEquals(patogeno.id, recuperado.patogeno.id)
    }

    @Test
    fun `si intento recuperar una especie con un id inexistente falla`() {
        assertThrows(IdNotFoundException::class.java) { especieService.recuperarEspecie(100000) }
    }

    @Test
    fun `recuperar todas las especies`() {
        dataService.crearSetDeDatosIniciales()

        val especies = especieService.recuperarTodos()

        assertEquals(21, especies.size)
    }

    @Test
    fun `obtener la cantidad de infectados de una especie que no infecto a ningun vector`() {
        patogeno = Patogeno("Gripe")
        especie = Especie(patogeno,"especie11", "ARG")
        var vector = Vector(TipoDeVector.Animal)
        var vector2 = Vector(TipoDeVector.Persona)

        runTrx {
            patogenoDAO.guardar(patogeno)
            especieDAO.guardar(especie)
            vectorDAO.guardar(vector)
            vectorDAO.guardar(vector2)
        }

        val cantidad = especieService.cantidadDeInfectados(especie.id!!)

        assertEquals(0, cantidad)
    }

    @Test
    fun `obtener la cantidad de infectados de una especie`() {
        patogeno = Patogeno("Gripe")
        especie = Especie(patogeno,"especie11", "ARG")
        var vector = Vector(TipoDeVector.Animal)
        var vector2 = Vector(TipoDeVector.Persona)

        runTrx { patogenoDAO.guardar(patogeno) }

        vectorService.infectar(vector, especie)
        vectorService.infectar(vector2, especie)

        val cantidad = especieService.cantidadDeInfectados(especie.id!!)

        assertEquals(2, cantidad)
    }

    @AfterEach
    fun deleteAll() {
        dataService.eliminarTodo()
    }
}