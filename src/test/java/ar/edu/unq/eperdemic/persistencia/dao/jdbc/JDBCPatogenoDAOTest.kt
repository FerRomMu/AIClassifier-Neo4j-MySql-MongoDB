package ar.edu.unq.eperdemic.persistencia.dao.jdbc

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.services.PatogenoService
import ar.edu.unq.eperdemic.services.impl.DataServiceImpl
import ar.edu.unq.eperdemic.services.impl.PatogenoServiceImpl
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
internal class JDBCPatogenoDAOTest {

    private val patogenoDAO: PatogenoDAO = JDBCPatogenoDAO()
    lateinit var patogeno: Patogeno

    private val patogenoService: PatogenoService = PatogenoServiceImpl(patogenoDAO);
    private val dataDAO = JDBCDataDAO();
    private val dataService = DataServiceImpl(patogenoDAO, dataDAO)

    @BeforeEach
    fun setUp() {
        patogeno = Patogeno("Gripe");
        patogeno.cantidadDeEspecies = 1
        patogeno.id = 10000000;

        dataService.crearSetDatosIniciales()

    }

    @Test
    fun `Si creo un patogeno me devuelve el mismo`() {

        val patogenoCreado = patogenoDAO.crear(patogeno);

        assertEquals(patogenoCreado.id!! , patogeno.id!! )
        assertEquals(patogenoCreado.tipo!!, patogeno.tipo!!)
        assertEquals(patogenoCreado.cantidadDeEspecies!!, patogeno.cantidadDeEspecies!!)

    }

    @Test
    fun `Si persisto un patogeno puedo recuperar una nueva instancia de él`() {

        val patogenoCreado = patogenoDAO.crear(patogeno);
        val patogenoRecuperado = patogenoDAO.recuperar(patogeno.id!!)

        assertEquals(patogenoRecuperado.id!! , patogeno.id!! )
        assertEquals(patogenoRecuperado.tipo!!, patogeno.tipo!!)
        assertEquals(patogenoRecuperado.cantidadDeEspecies!!, patogeno.cantidadDeEspecies!!)

        assertTrue(patogenoRecuperado != patogenoCreado)
    }

    @Test
    fun `Si creo un patogeno con id existente me devuelve un error`() {

        TODO("")

    }

    @Test
    fun `Si actualizo un patogeno existente este se actualiza`() {

        val patogenoAActualizar = patogenoDAO.recuperar(1)
        patogenoAActualizar.tipo = "Tipo 1 actualizado"
        patogenoAActualizar.cantidadDeEspecies = 2

        patogenoService.actualizarPatogeno(patogenoAActualizar)

        val patogenoActualizado = patogenoDAO.recuperar(patogenoAActualizar.id!!)

        assertEquals("Tipo 1 actualizado", patogenoActualizado.tipo)
        assertEquals(2, patogenoActualizado.cantidadDeEspecies)
        assertTrue(patogenoAActualizar !== patogeno)
    }

    @Test
    fun `Si actualizo un patogeno inexistente lanza error`() {

        TODO("hay que implementar esta logica")

    }

    @Test
    fun `Si recupero un patogeno existente recibo una instancia de él`() {

        val patogenoObtenido = patogenoDAO.recuperar(1)

        assertEquals(1, patogenoObtenido.id!!)
        assertEquals(1, patogenoObtenido.cantidadDeEspecies)
        assertEquals("Tipo 1", patogenoObtenido.tipo)

    }

    @Test
    fun `Si recupero un patogeno inexistente lanza error`(){

        TODO("hay que implementar esta logica")

    }

    @Test
    fun `RecuperarTodos trae todo el set de datos iniciales`() {

        val patogenos = patogenoDAO.recuperarATodos()
        assertEquals(21, patogenos.size)

    }

    @Test
    fun `RecuperarTodos trae los datos en orden`() {

        dataService.deleteAll()

        val patogenoA = Patogeno("A")
        val patogenoC = Patogeno("C")
        val patogenoB = Patogeno("B")
        val patogenoD = Patogeno("D")

        patogenoA.id = 1
        patogenoC.id = 2
        patogenoB.id = 3
        patogenoD.id = 4

        patogenoDAO.crear(patogenoA)
        patogenoDAO.crear(patogenoC)
        patogenoDAO.crear(patogenoB)
        patogenoDAO.crear(patogenoD)

        val nombres = patogenoDAO.recuperarATodos().map { it -> it.tipo }

        assertEquals("A", nombres[0])
        assertEquals("B", nombres[1])
        assertEquals("C", nombres[2])
        assertEquals("D", nombres[3])

    }

    @Test
    fun `si no hay datos en la BD recuperarTodos devuelve una lista vacia`() {

        dataService.deleteAll()
        val ningunPatogeno = patogenoDAO.recuperarATodos()

        assertTrue(ningunPatogeno.isEmpty())

    }

    @AfterEach
    fun restartDB() {
        dataService.deleteAll()
    }

}