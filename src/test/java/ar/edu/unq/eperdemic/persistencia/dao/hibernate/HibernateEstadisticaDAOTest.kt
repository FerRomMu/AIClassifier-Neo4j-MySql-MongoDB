package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.persistencia.dao.EstadisticaDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx
import ar.edu.unq.eperdemic.utils.DataService
import ar.edu.unq.eperdemic.utils.impl.DataServiceImpl
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HibernateEstadisticaDAOTest {

    lateinit var data: DataService
    lateinit var estadisticaDAO: EstadisticaDAO
    lateinit var ubicacion1: Ubicacion

    @BeforeEach
    fun setUp() {
        estadisticaDAO = HibernateEstadisticaDAO()
        data = DataServiceImpl()

        ubicacion1 = Ubicacion("ubicacion 1")
        val patogeno = Patogeno("patogenoSSS")

        val vector1 = Vector(TipoDeVector.Insecto) ; vector1.ubicacion = ubicacion1
        val vector2 = Vector(TipoDeVector.Insecto) ; vector2.ubicacion = ubicacion1
        val vector3 = Vector(TipoDeVector.Insecto) ; vector3.ubicacion = ubicacion1
        val vector4 = Vector(TipoDeVector.Insecto) ; vector4.ubicacion = ubicacion1
        val vector5 = Vector(TipoDeVector.Insecto) ; vector5.ubicacion = ubicacion1

        val especie1 = Especie(patogeno, "especie111", "arg")
        val especie2 = Especie(patogeno, "especie222", "arg")
        val especie3 = Especie(patogeno, "especie333", "arg")

        vector1.agregarEspecie(especie1) ; vector2.agregarEspecie(especie1); vector3.agregarEspecie(especie1)
        vector4.agregarEspecie(especie2)

        data.persistir(listOf(ubicacion1, patogeno, especie1, especie2, especie3, vector1, vector2, vector3, vector4, vector5))
    }

    @Test
    fun `cantidad de vectores presentes en una ubicacion`() {
        val cantidadVectoresTotal = runTrx { estadisticaDAO.cantidadVectoresPresentes(ubicacion1.nombre) }

        assertEquals(5, cantidadVectoresTotal)
    }

    @Test
    fun `cantidad de vectores que no estan presentes en una ubicacion`() {
        val cantidadVectoresTotal = runTrx { estadisticaDAO.cantidadVectoresPresentes("ubicacion 222") }

        assertEquals(0, cantidadVectoresTotal)
    }

    @Test
    fun `cantidad de vectores infectados en una ubicacion`() {
        val cantidadVectoresInfectados = runTrx { estadisticaDAO.cantidadVectoresInfectados(ubicacion1.nombre) }

        assertEquals(4, cantidadVectoresInfectados)
    }

    @Test
    fun `nombre de la especie que esta infectando a mas vectores`() {
        val nombreEspecieQueMasInfecta = runTrx { estadisticaDAO.nombreEspecieQueMasInfectaVectores(ubicacion1.nombre) }

        assertEquals("especie111", nombreEspecieQueMasInfecta)

    }

    @AfterEach
    fun tearDown() {
        data.eliminarTodo()
    }
}