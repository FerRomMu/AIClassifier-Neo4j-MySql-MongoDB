package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.exceptions.IdNotFoundException
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner
import ar.edu.unq.eperdemic.utils.impl.DataServiceImpl
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HibernateVectorDAOTest {

    lateinit var vectorDAO: VectorDAO
    lateinit var vector: Vector
    lateinit var dataService: DataServiceImpl

    @BeforeEach
    fun setUp() {

        vectorDAO = HibernateVectorDAO()
        vector = Vector(TipoDeVector.Animal)
    }

    @Test
    fun `si creo un vector al guardarlo se le asigna un id`() {

        Assertions.assertNull(vector.id)
        TransactionRunner.runTrx { vectorDAO.guardar(vector) }

        Assertions.assertNotNull(vector.id)

    }

    @Test
    fun `si guardo un vector con id se actualiza`() {

        TransactionRunner.runTrx { vectorDAO.guardar(vector) }
        Assertions.assertEquals(TipoDeVector.Animal, vector.tipo)

        vector.tipo = TipoDeVector.Insecto
        val vectorActualizado = TransactionRunner.runTrx {
            vectorDAO.guardar(vector)
            val vectorActualizado = vectorDAO.recuperar(vector.id!!)
            vectorActualizado
        }

        Assertions.assertEquals(TipoDeVector.Insecto, vectorActualizado.tipo)
    }

    @Test
    fun `si guardo un vector lo puedo recuperar con su id`() {
        TransactionRunner.runTrx { vectorDAO.guardar(vector) }
        val vectorRecuperado = TransactionRunner.runTrx { vectorDAO.recuperar(vector.id!!) }

        Assertions.assertEquals(vector.id, vectorRecuperado.id)
        Assertions.assertEquals(vector.tipo, vectorRecuperado.tipo)
    }

    @Test
    fun `si trato de recuperar un vector inexistente falla`() {
        Assertions.assertThrows(IdNotFoundException::class.java) {
            TransactionRunner.runTrx {
                vectorDAO.recuperar(
                    10000001
                )
            }
        }
    }

    @Test
    fun `si recupero todos los vectores recibo todos`(){
        TODO("Va a requerir un borrar todo de la db")
    }

    @Test
    fun `si borro un vector se elimina`() {
        Assertions.assertThrows(IdNotFoundException::class.java) {
            TransactionRunner.runTrx {
                vectorDAO.guardar(vector)
                vectorDAO.borrar(vector.id)
                vectorDAO.recuperar(vector.id)
            }
        }
    }

    @Test
    fun `si borro un vector con id invalida falla`() {
        Assertions.assertThrows(IdNotFoundException::class.java) {
            TransactionRunner.runTrx { vectorDAO.borrar(154313) }
        }
    }

    @AfterEach
    fun tearDown() {
        dataService = DataServiceImpl()
        dataService.eliminarTodo()
    }

}