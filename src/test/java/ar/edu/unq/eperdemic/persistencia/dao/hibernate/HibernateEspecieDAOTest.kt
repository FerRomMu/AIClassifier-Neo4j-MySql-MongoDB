package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.exceptions.IdNotFoundException
import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.EspecieDAO
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx
import ar.edu.unq.eperdemic.utils.DataService
import ar.edu.unq.eperdemic.utils.impl.DataServiceImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HibernateEspecieDAOTest {

    lateinit var especieDAO: EspecieDAO
    lateinit var especie: Especie
    lateinit var patogeno: Patogeno
    lateinit var data: DataService

    @BeforeEach
    fun setUp() {
        especieDAO = HibernateEspecieDAO()

        data = DataServiceImpl()

        patogeno = Patogeno("Gripe")
        especie =  patogeno.crearEspecie("especie111", "ARG")
    }

    @Test
    fun `creo una Especie y se le asigna un id`() {
        assertNull(especie.id)

        data.persistir(listOf(patogeno))

        runTrx { especieDAO.guardar(especie) }

        assertNotNull(especie.id)
    }

    @Test
    fun `guardo una especie con id en la db este se actualiza` () {
        data.persistir(listOf(patogeno, especie))

        assertEquals("especie111", especie.nombre)
        especie.nombre = "especie222"
        data.persistir(especie)

        val especieActualizada = runTrx { especieDAO.recuperar(especie.id) }
        assertEquals("especie222", especieActualizada.nombre)
    }

    @Test
    fun `guardo una especie y la recupero con su id`() {

        data.persistir(listOf(patogeno, especie))

        val especieRecuperada = runTrx {
            especieDAO.recuperar(especie.id)
        }
        assertEquals(especie.id, especieRecuperada.id)
        assertEquals(especie.nombre, especieRecuperada.nombre)
        assertEquals(especie.paisDeOrigen, especieRecuperada.paisDeOrigen)
        assertEquals(especie.patogeno.id, especieRecuperada.patogeno.id)

    }

    @Test
    fun `intento recuperar una especie que no existe en la db y falla`() {
        assertThrows(IdNotFoundException::class.java) { runTrx { especieDAO.recuperar(100000) } }
    }

    @Test
    fun `recuperar todas las especies`() {
        data.crearSetDeDatosIniciales()

        val recuperados = runTrx { especieDAO.recuperarTodos() }

        assertEquals(21,recuperados.size)

    }

    @Test
    fun `obtener la cantidad de infectados de una especie`() {
        val vector1 = Vector(TipoDeVector.Animal)
        val vector2 = Vector(TipoDeVector.Persona)
        val patogenoDeLaEspecie = Patogeno("Gripe")

        val especieAContagiar = Especie(patogenoDeLaEspecie, "sarasa", "ARG")
        val especieAContagiar2 = Especie(patogenoDeLaEspecie,"Especie_AR2T","Francia")

        especieAContagiar2.vectores.add(vector1)
        vector1.especiesContagiadas.add(especieAContagiar2)

        especieAContagiar2.vectores.add(vector2)
        vector2.especiesContagiadas.add(especieAContagiar2)

        especieAContagiar.vectores.add(vector1)
        vector1.especiesContagiadas.add(especieAContagiar)

        data.persistir(listOf(patogenoDeLaEspecie, especieAContagiar, especieAContagiar2, vector1, vector2))
        val cantidad = runTrx {
            especieDAO.cantidadDeInfectados(especieAContagiar2.id!!)
        }

        assertEquals(2, cantidad)
    }

    @AfterEach
    fun tearDown() {
        data.eliminarTodo()
    }
}