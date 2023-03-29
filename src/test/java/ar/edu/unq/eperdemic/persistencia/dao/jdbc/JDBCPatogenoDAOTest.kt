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
    fun recuperarTest() {

        val patogenoCreado = patogenoDAO.crear(patogeno);
        val patogenoObtenido = patogenoDAO.recuperar(10000000)

        assertEquals(10000000, patogenoObtenido.id!!)
        assertEquals(1, patogenoObtenido.cantidadDeEspecies)
        assertEquals("Gripe", patogenoObtenido.tipo)

        assertTrue(patogenoObtenido !== patogeno)
    }

    @Test
    fun recuperar2IdIgualesTest() {


        val patogenoDuplicado = Patogeno("sarampion");
        patogenoDuplicado.cantidadDeEspecies = 250
        patogenoDuplicado.id = 10000000;

        val patogenoCreado = patogenoDAO.crear(patogeno);
        val patogenoCreadoDuplicado = patogenoDAO.crear(patogenoDuplicado);


        val exception = assertThrows(RuntimeException::class.java) {
            val mensaje = patogenoDAO.recuperar(10000000)
        }
        assertEquals("Existe mas de un patogeno con el id 10000000", exception)

    }

    @Test
    fun recuperarATodos() {
        val patogeno2 = Patogeno("sarampion");
        patogeno2.cantidadDeEspecies = 250
        patogeno2.id = 200;

        val patogeno3 = Patogeno("sarampion");
        patogeno2.cantidadDeEspecies = 250
        patogeno2.id = 150;

        val patogenoCreado1 = patogenoDAO.crear(patogeno);
        val patogenoCreado2 = patogenoDAO.crear(patogeno2);
        val patogenoCreado3 = patogenoDAO.crear(patogeno3);

        val patogenos = patogenoDAO.recuperarATodos()
        assertEquals(3, patogenos.size)
    }

    @AfterEach
    fun restartDB() {
        dataService.deleteAll()
    }


}