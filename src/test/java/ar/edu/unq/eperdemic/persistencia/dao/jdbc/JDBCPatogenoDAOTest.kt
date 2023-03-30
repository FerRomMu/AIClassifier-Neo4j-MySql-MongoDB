package ar.edu.unq.eperdemic.persistencia.dao.jdbc

import ar.edu.unq.eperdemic.exceptions.DataDuplicationException
import ar.edu.unq.eperdemic.exceptions.DataNotFoundException
import ar.edu.unq.eperdemic.exceptions.IdNotFoundException
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.utils.jdbc.DataServiceJDBC
import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
internal class JDBCPatogenoDAOTest {

    private val patogenoDAO: PatogenoDAO = JDBCPatogenoDAO()
    lateinit var patogeno: Patogeno

    private val dataDAO = JDBCDataDAO();
    private val dataService = DataServiceJDBC(patogenoDAO, dataDAO)

    @BeforeEach
    fun setUp() {
        patogeno = Patogeno("Gripe");
        patogeno.cantidadDeEspecies = 1

        dataService.crearSetDeDatosIniciales()
    }

    @Test
    fun `Si creo un patogeno me devuelve el mismo`() {

        val patogenoCreado = patogenoDAO.crear(patogeno);

        assertEquals(patogenoCreado.id!! , patogeno.id )
        assertEquals(patogenoCreado.tipo, patogeno.tipo)
        assertEquals(patogenoCreado.cantidadDeEspecies, patogeno.cantidadDeEspecies)

    }

    @Test
    fun `Si persisto un patogeno puedo recuperar una nueva instancia de él`() {

        val patogenoCreado = patogenoDAO.crear(patogeno);
        val patogenoRecuperado = patogenoDAO.recuperar(patogeno.id!!)

        assertEquals(patogenoRecuperado.id!! , patogeno.id )
        assertEquals(patogenoRecuperado.tipo, patogeno.tipo)
        assertEquals(patogenoRecuperado.cantidadDeEspecies, patogeno.cantidadDeEspecies)

        assertTrue(patogenoRecuperado != patogenoCreado)
    }

    @Test
    fun `Si intento crear un patogeno ya creado me devuelve un error`() {

        patogenoDAO.crear(patogeno)
        assertThrows(DataDuplicationException::class.java) { patogenoDAO.crear(patogeno) }

    }

    @Test
    fun `Si creo un patogeno con tipo existente me devuelve un error`() {

        patogeno.tipo = "Tipo 1"
        assertThrows(java.sql.SQLIntegrityConstraintViolationException::class.java) { patogenoDAO.crear(patogeno) }

    }

    @Test
    fun `Si actualizo un patogeno existente este se actualiza`() {

        val patogenoAActualizar = patogenoDAO.crear(patogeno);

        patogenoAActualizar.tipo = "Tipo 1 actualizado"
        patogenoAActualizar.cantidadDeEspecies = 2

        patogenoDAO.actualizar(patogenoAActualizar)

        val patogenoActualizado = patogenoDAO.recuperar(patogenoAActualizar.id!!)

        assertEquals("Tipo 1 actualizado", patogenoActualizado.tipo)
        assertEquals(2, patogenoActualizado.cantidadDeEspecies)

    }

    @Test
    fun `Si actualizo un patogeno inexistente lanza error`() {

        patogeno.id = 80890
        assertThrows(DataNotFoundException::class.java) { patogenoDAO.actualizar(patogeno) }

    }

    @Test
    fun `Si actualizo un patogeno sin id lanza error`() {

        assertThrows(IdNotFoundException::class.java) { patogenoDAO.actualizar(patogeno) }

    }

    @Test
    fun `Si recupero un patogeno existente recibo una instancia de él`() {

        patogenoDAO.crear(patogeno);
        val patogenoObtenido = patogenoDAO.recuperar(patogeno.id!!)

        assertEquals(patogeno.id, patogenoObtenido.id!!)
        assertEquals(1, patogenoObtenido.cantidadDeEspecies)
        assertEquals("Gripe", patogenoObtenido.tipo)
    }

    @Test
    fun `Si recupero un patogeno inexistente lanza error`(){

        assertThrows(DataNotFoundException::class.java) { patogenoDAO.recuperar(9342) }

    }

    @Test
    fun `RecuperarTodos trae todo el set de datos iniciales`() {

        val patogenos = patogenoDAO.recuperarATodos()
        assertEquals(21, patogenos.size)

    }

    @Test
    fun `RecuperarTodos trae los datos en orden`() {

        dataService.eliminarTodo()

        val patogenoA = Patogeno("A")
        val patogenoC = Patogeno("C")
        val patogenoB = Patogeno("B")
        val patogenoD = Patogeno("D")

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

        dataService.eliminarTodo()
        val ningunPatogeno = patogenoDAO.recuperarATodos()

        assertTrue(ningunPatogeno.isEmpty())

    }

    @AfterEach
    fun restartDB() {
        dataService.eliminarTodo()
    }

}