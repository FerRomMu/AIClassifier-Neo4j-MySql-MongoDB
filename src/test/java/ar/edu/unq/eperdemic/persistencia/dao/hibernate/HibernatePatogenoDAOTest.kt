package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.exceptions.IdNotFoundException
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.services.impl.UbicacionServiceImpl
import ar.edu.unq.eperdemic.services.impl.VectorServiceImpl
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HibernatePatogenoDAOTest {

    lateinit var patogenoDAO: PatogenoDAO
    lateinit var patogeno: Patogeno

    @BeforeEach
    fun setUp() {

        patogenoDAO = HibernatePatogenoDAO()
        patogeno = Patogeno("Gripe")
    }

    @Test
    fun `si creo un Patogeno al guardarlo se le asigna un id`() {

        assertNull(patogeno.id)
        runTrx { patogenoDAO.guardar(patogeno) }

        assertNotNull(patogeno.id)

    }

    @Test
    fun `si guardo un Patogeno con id se actualiza`() {

        runTrx { patogenoDAO.guardar(patogeno) }
        assertEquals(0, patogeno.cantidadDeEspecies)

        patogeno.crearEspecie("especieA", "Japon")
        val patogenoActualizado = runTrx {
            patogenoDAO.guardar(patogeno)
            val patogenoActualizado = patogenoDAO.recuperar(patogeno.id!!)
            patogenoActualizado
        }

        assertEquals(1, patogenoActualizado.cantidadDeEspecies)
    }

    @Test
    fun `si guardo un Patogeno lo puedo recuperar con su id`() {
        runTrx { patogenoDAO.guardar(patogeno) }
        val patogenoRecuperado = runTrx { patogenoDAO.recuperar(patogeno.id!!) }

        assertEquals(patogeno.id, patogenoRecuperado.id)
        assertEquals(patogeno.tipo, patogenoRecuperado.tipo)
        assertEquals(patogeno.cantidadDeEspecies, patogenoRecuperado.cantidadDeEspecies)
    }

    @Test
    fun `si trato de recuperar un Patogeno inexistente falla`() {
        assertThrows(IdNotFoundException::class.java) { runTrx { patogenoDAO.recuperar(10000001) } }
    }

    @AfterEach
    fun tearDown() {
    }

}