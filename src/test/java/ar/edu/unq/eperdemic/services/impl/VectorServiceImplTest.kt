package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.exceptions.IdNotFoundException
import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.utils.impl.DataServiceImpl
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VectorServiceImplTest {
    @Autowired lateinit var vectorService: VectorServiceImpl
    @Autowired lateinit var ubicacionService: UbicacionServiceImpl

    //@Autowired lateinit var patogenoRep: PatogenoRepository

    lateinit var bernal: Ubicacion
    lateinit var dataService: DataServiceImpl

    @BeforeEach
    fun setUp() {

        vectorService = VectorServiceImpl()
        // ubicacionService = UbicacionServiceImpl()
        dataService = DataServiceImpl()

        bernal = ubicacionService.crearUbicacion("Bernal")
    }

    @Test
    fun infectar() {
        val vectorAInfectar = Vector(TipoDeVector.Persona)

        val patogenoDeLaEspecie = Patogeno("Gripe")
        dataService.persistir(patogenoDeLaEspecie)

        val especieAContagiar = Especie(patogenoDeLaEspecie,"Especie_AR2T","Francia")

        Assertions.assertEquals(vectorAInfectar.especiesContagiadas.size, 0)

        vectorService.infectar(vectorAInfectar,especieAContagiar)

        Assertions.assertEquals(vectorAInfectar.especiesContagiadas.size, 1)
        Assertions.assertEquals(vectorAInfectar.especiesContagiadas.first().id, especieAContagiar.id)
        Assertions.assertEquals(vectorAInfectar.especiesContagiadas.first().nombre, especieAContagiar.nombre)
        Assertions.assertEquals(
            vectorAInfectar.especiesContagiadas.first().paisDeOrigen,
            especieAContagiar.paisDeOrigen
        )
    }

    @Test
    fun `Si miro las enfermedades de un vector las recibo`() {

        val patogenoDeLaEspecie = Patogeno("Gripe")
        dataService.persistir(patogenoDeLaEspecie)

        val vectorAInfectar = vectorService.crearVector(TipoDeVector.Persona,bernal.id!!)

        val especieAContagiar = Especie(patogenoDeLaEspecie,"Especie_AR2T","Francia")

        vectorService.infectar(vectorAInfectar,especieAContagiar)
        val resultado = vectorService.enfermedades(vectorAInfectar.id!!)

        Assertions.assertEquals(1, resultado.size)
        Assertions.assertEquals(especieAContagiar.id, resultado[0].id)
        Assertions.assertEquals(especieAContagiar.nombre, resultado[0].nombre)
        Assertions.assertEquals(especieAContagiar.paisDeOrigen, resultado[0].paisDeOrigen)

    }

    @Test
    fun `Si pido enfermedad de un vector que no esta infectado recibo 0 enfermedades`() {

        val vectorSano = vectorService.crearVector(TipoDeVector.Persona, bernal.id!!)
        Assertions.assertEquals(0, vectorService.enfermedades(vectorSano.id!!).size)

    }

    @Test
    fun `si creo un vector este recibe un id`() {

        val vector = vectorService.crearVector(TipoDeVector.Persona,bernal.id!!)
        Assertions.assertNotNull(vector.id)

    }

    @Test
    fun `si trato de crear un vector con una ubicacion invalida falla`() {

        Assertions.assertThrows(IdNotFoundException::class.java) {
            vectorService.crearVector(
                TipoDeVector.Persona,
                1554798541
            )
        }

    }

    @Test
    fun `si creo vector lo puedo recuperar`() {

        val vector = vectorService.crearVector(TipoDeVector.Persona,bernal.id!!)
        val vectorRecuperado = vectorService.recuperarVector(vector.id!!)

        Assertions.assertEquals(vector.id, vectorRecuperado.id)
        Assertions.assertEquals(vector.tipo, vectorRecuperado.tipo)
        Assertions.assertEquals(vector.ubicacion.id, vectorRecuperado.ubicacion.id)

    }

    @Test
    fun `si trato de recuperar un vector inexistente falla`() {
        Assertions.assertThrows(IdNotFoundException::class.java) { vectorService.recuperarVector(1) }
    }

    @Test
    fun `si borro un vector y lo quiero recuperar falla`() {

        val vector = vectorService.crearVector(TipoDeVector.Persona,bernal.id!!)
        val vectorRecuperado = vectorService.recuperarVector(vector.id!!)

        Assertions.assertEquals(vector.id, vectorRecuperado.id)

        vectorService.borrarVector(vector.id!!)

        Assertions.assertThrows(IdNotFoundException::class.java) { vectorService.recuperarVector(vector.id!!) }
    }

    @Test
    fun `si trato de borrar un vector con id invalida falla`() {

        Assertions.assertThrows(IdNotFoundException::class.java) { vectorService.borrarVector(123241) }
    }

    @Test
    fun `si trato de recuperar todos llegan todos`() {
        val vectoresPersistidos = dataService.crearSetDeDatosIniciales().filterIsInstance<Vector>()
        val vectores = vectorService.recuperarTodos()

        Assertions.assertEquals(vectoresPersistidos.size, vectores.size)
        Assertions.assertTrue(
            vectores.all { vector ->
                vectoresPersistidos.any {
                    it.id == vector.id &&
                            it.tipo == vector.tipo &&
                            it.especiesContagiadas.size == vector.especiesContagiadas.size &&
                            it.ubicacion.id == it.ubicacion.id
                }
            }
        )
    }

    @Test
    fun `si trato de recuperar todos y no hay nadie simplemente recibo 0`() {

        val vectores = vectorService.recuperarTodos()

        Assertions.assertEquals(0, vectores.size)
    }

    @AfterEach
    fun tearDown() {
        dataService.eliminarTodo()
    }

}