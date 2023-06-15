package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.exceptions.IdNotFoundException
import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.persistencia.repository.mongo.UbicacionMongoRepository
import ar.edu.unq.eperdemic.services.UbicacionService
import ar.edu.unq.eperdemic.services.VectorService
import ar.edu.unq.eperdemic.utils.DataService
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VectorServiceImplTest {
    @Autowired lateinit var vectorService: VectorService
    @Autowired lateinit var ubicacionService: UbicacionService
    @Autowired lateinit var dataService: DataService
    @Autowired lateinit var ubicacionMongoRepository: UbicacionMongoRepository

    lateinit var bernal: Ubicacion

    lateinit var coordenada : Coordenada

    @BeforeEach
    fun setUp() {
        coordenada = Coordenada(1.0, 2.0)
        bernal = ubicacionService.crearUbicacion("Bernal", coordenada)
    }

    @Test
    fun infectar() {
        val vectorAInfectar = vectorService.crearVector(TipoDeVector.Persona, bernal.id!!)

        val patogenoDeLaEspecie = Patogeno("Gripe")
        dataService.persistir(patogenoDeLaEspecie)

        val especieAContagiar = Especie(patogenoDeLaEspecie,"Especie_AR2T","Francia")
        dataService.persistir(especieAContagiar)

        assertEquals(vectorAInfectar.especiesContagiadas.size, 0)

        vectorService.infectar(vectorAInfectar,especieAContagiar)

        assertEquals(vectorAInfectar.especiesContagiadas.size, 1)
        assertEquals(vectorAInfectar.especiesContagiadas.first().id, especieAContagiar.id)
        assertEquals(vectorAInfectar.especiesContagiadas.first().nombre, especieAContagiar.nombre)
        assertEquals(
            vectorAInfectar.especiesContagiadas.first().paisDeOrigen,
            especieAContagiar.paisDeOrigen
        )
    }

    @Test
    fun `Si miro las enfermedades de un vector las recibo`() {
        var ubicacion = ubicacionService.crearUbicacion("ubicacion1",coordenada)

        val vectorAInfectar = vectorService.crearVector(TipoDeVector.Persona, ubicacion.id!!)

        val patogenoDeLaEspecie = Patogeno("Gripe")
        dataService.persistir(patogenoDeLaEspecie)

        val especieAContagiar = Especie(patogenoDeLaEspecie,"Especie_AR2T","Francia")
        dataService.persistir(especieAContagiar)

        vectorService.infectar(vectorAInfectar,especieAContagiar)
        val resultado = vectorService.enfermedades(vectorAInfectar.id!!)

        assertEquals(1, resultado.size)
        assertEquals(especieAContagiar.id, resultado[0].id)
        assertEquals(especieAContagiar.nombre, resultado[0].nombre)
        assertEquals(especieAContagiar.paisDeOrigen, resultado[0].paisDeOrigen)

    }

    @Test
    fun `Si pido enfermedad de un vector que no esta infectado recibo 0 enfermedades`() {

        val vectorSano = vectorService.crearVector(TipoDeVector.Persona, bernal.id!!)
        assertEquals(0, vectorService.enfermedades(vectorSano.id!!).size)

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

        assertEquals(vector.id, vectorRecuperado.id)
        assertEquals(vector.tipo, vectorRecuperado.tipo)
        assertEquals(vector.ubicacion.id, vectorRecuperado.ubicacion.id)

    }

    @Test
    fun `si trato de recuperar un vector inexistente falla`() {
        Assertions.assertThrows(IdNotFoundException::class.java) { vectorService.recuperarVector(1) }
    }

    @Test
    fun `si borro un vector y lo quiero recuperar falla`() {

        val vector = vectorService.crearVector(TipoDeVector.Persona,bernal.id!!)
        val vectorRecuperado = vectorService.recuperarVector(vector.id!!)

        assertEquals(vector.id, vectorRecuperado.id)

        vectorService.borrarVector(vector.id!!)

        Assertions.assertThrows(IdNotFoundException::class.java) { vectorService.recuperarVector(vector.id!!) }
    }

    @Test
    fun `si trato de borrar un vector con id invalida falla`() {
        Assertions.assertThrows(EmptyResultDataAccessException::class.java) { vectorService.borrarVector(123241) }
    }

    @Test
    fun `si trato de recuperar todos llegan todos`() {
        val vectoresPersistidos = dataService.crearSetDeDatosIniciales().filterIsInstance<Vector>()
        val vectores = vectorService.recuperarTodos()

        assertEquals(vectoresPersistidos.size, vectores.size)
        assertTrue(
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

        assertEquals(0, vectores.size)
    }

    @Test
    fun `si infecto a un vector sano actualiza ubicacionMongo `() {
        var ubicacion = ubicacionService.crearUbicacion("ubicacion1",coordenada)

        val vectorAInfectar = vectorService.crearVector(TipoDeVector.Persona, ubicacion.id!!)

        val patogenoDeLaEspecie = Patogeno("Gripe")
        dataService.persistir(patogenoDeLaEspecie)

        val especieAContagiar = Especie(patogenoDeLaEspecie,"Especie_AR2T","Francia")
        dataService.persistir(especieAContagiar)

        assertEquals(vectorAInfectar.especiesContagiadas.size, 0)
        assertFalse(ubicacionMongoRepository.findByNombre(ubicacion.nombre).hayAlgunInfectado)

        vectorService.infectar(vectorAInfectar,especieAContagiar)

        assertEquals(vectorAInfectar.especiesContagiadas.size, 1)
        assertEquals(vectorAInfectar.especiesContagiadas.first().id, especieAContagiar.id)
        assertEquals(vectorAInfectar.especiesContagiadas.first().nombre, especieAContagiar.nombre)

        assertTrue(ubicacionMongoRepository.findByNombre(ubicacion.nombre).hayAlgunInfectado)

    }

    @AfterEach
    fun tearDown() {
        dataService.eliminarTodo()
        ubicacionMongoRepository.deleteAll()
    }

}