package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.exceptions.DataDuplicationException
import ar.edu.unq.eperdemic.exceptions.IdNotFoundException
import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
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
    lateinit var patogenoDAO: PatogenoDAO
    lateinit var especie: Especie
    lateinit var patogeno: Patogeno
    lateinit var data: DataService

    @BeforeEach
    fun setUp() {
        especieDAO = HibernateEspecieDAO()
        patogenoDAO = HibernatePatogenoDAO()

        data = DataServiceImpl()

        patogeno = Patogeno("Gripe")
        especie = Especie("especie111", "ARG", patogeno)
    }

    @Test
    fun `creo una Especie y se le asigna un id`() {
        assertNull(especie.id)

        runTrx { patogenoDAO.guardar(patogeno) }

        runTrx { especieDAO.guardar(especie) }
        assertNotNull(especie.id)

    }

    @Test
    fun `guardo una especie con id en la db este se actualiza` () {
        runTrx { patogenoDAO.guardar(patogeno) }
        runTrx { especieDAO.guardar(especie) }

        assertEquals("especie111", especie.nombre)

        especie.nombre = "especie222"
        val especieActualizada = runTrx {
            especieDAO.guardar(especie)
            val especie = especieDAO.recuperar(especie.id)

            especie
        }
        assertEquals("especie222", especieActualizada.nombre)
    }

    @Test
    fun `guardo una especie y la recupero con su id`() {
        runTrx { patogenoDAO.guardar(patogeno) }
        runTrx { especieDAO.guardar(especie) }

        val especieRecuperada = runTrx {
            val especie = especieDAO.recuperar(especie.id)
            especie
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

    @AfterEach
    fun tearDown() {
        data.eliminarTodo()
    }
}