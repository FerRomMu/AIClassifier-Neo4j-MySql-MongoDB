package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.exceptions.IdNotFoundException
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.services.impl.UbicacionServiceImpl
import ar.edu.unq.eperdemic.services.impl.VectorServiceImpl
import ar.edu.unq.eperdemic.services.runner.TransactionRunner
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx
import ar.edu.unq.eperdemic.utils.DataService
import ar.edu.unq.eperdemic.utils.impl.DataServiceImpl
import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HibernatePatogenoDAOTest {

    lateinit var patogenoDAO: PatogenoDAO
    lateinit var patogeno: Patogeno
    lateinit var data: DataService

    @BeforeEach
    fun setUp() {

        patogenoDAO = HibernatePatogenoDAO()
        patogeno = Patogeno("Gripe")
        data = DataServiceImpl()

    }

    @Test
    fun `si creo un Patogeno al guardarlo se le asigna un id`() {

        assertNull(patogeno.id)

        runTrx { patogenoDAO.guardar(patogeno) }

        assertNotNull(patogeno.id)

    }

    @Test
    fun `si guardo un Patogeno con id se actualiza`() {

        data.persistir(patogeno)
        assertEquals(0, patogeno.cantidadDeEspecies())

        patogeno.crearEspecie("especieA", "Japon")

        val patogenoActualizado = runTrx {
            patogenoDAO.guardar(patogeno)
            val patogenoActualizado = patogenoDAO.recuperar(patogeno.id!!)
            patogenoActualizado
        }

        assertEquals(1, patogenoActualizado.cantidadDeEspecies())
    }

    @Test
    fun `si guardo un Patogeno lo puedo recuperar con su id`() {
        data.persistir(patogeno)
        val patogenoRecuperado = runTrx { patogenoDAO.recuperar(patogeno.id!!) }

        assertEquals(patogeno.id, patogenoRecuperado.id)
        assertEquals(patogeno.tipo, patogenoRecuperado.tipo)
        assertEquals(patogeno.cantidadDeEspecies(), patogenoRecuperado.cantidadDeEspecies())
    }

    @Test
    fun `si trato de recuperar un Patogeno inexistente falla`() {
        assertThrows(IdNotFoundException::class.java) { runTrx { patogenoDAO.recuperar(10000001) } }
    }

    @Test
    fun `si recupero todos los vectores recibo todos`(){

        data.crearSetDeDatosIniciales()
        val recuperados = runTrx { patogenoDAO.recuperarTodos() }
        assertEquals(21, recuperados.size)

    }

    @Test
    fun `si trato de recuperar las especies de un patogeno las devuelve`() {
        patogeno = Patogeno("Gripe")

        patogeno.crearEspecie("virusT", "mansion spencer")
        patogeno.crearEspecie("virusG", "raccoon city")
        patogeno.crearEspecie("virus progenitor", "montanas arklay")
        data.persistir(patogeno)

        var especies: List<String> = runTrx {
            patogenoDAO.especiesDePatogeno(patogeno.id!!).map{e -> e.nombre}
        }

        assertEquals(3, especies.size)
        assertTrue(especies.contains("virusT"))
        assertTrue(especies.contains("virusG"))
        assertTrue(especies.contains("virus progenitor"))
    }

    @AfterEach
    fun tearDown() {
        data.eliminarTodo()
    }

}