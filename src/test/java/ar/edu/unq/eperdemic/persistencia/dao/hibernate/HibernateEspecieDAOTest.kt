package ar.edu.unq.eperdemic.persistencia.dao.hibernate

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

    @AfterEach
    fun tearDown() {
        data.eliminarTodo()
    }
}