package ar.edu.unq.eperdemic.persistencia.repository.spring

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.utils.DataService
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EspecieRepositoryTest {

    @Autowired lateinit var especieRepository: EspecieRepository
    lateinit var especie: Especie
    @Autowired lateinit var data: DataService

    @BeforeEach
    fun setUp() {
        val patogeno = Patogeno("Gripa")
        especie = Especie(patogeno, "covid", "China")

        data.persistir(patogeno)
    }

    @Test
    fun `si creo un Especie al guardarlo se le asigna un id`() {

        assertNull(especie.id)

        especieRepository.save(especie)

        assertNotNull(especie.id)

    }

    @Test
    fun `si guardo una Especie con id se actualiza`() {

        data.persistir(especie)
        assertEquals("China", especie.paisDeOrigen)
        especie.paisDeOrigen = "USA"

        especieRepository.save(especie)
        val especieActualizado = especieRepository.findById(especie.id!!).get()

        assertEquals("USA", especieActualizado.paisDeOrigen)
    }

    @Test
    fun `si trato de recuperar una Especie existente con su id lo obtengo`() {
        data.persistir(especie)
        val especieRecuperado =  especieRepository.findById(especie.id!!).get()

        assertEquals(especie.id, especieRecuperado.id)
        assertEquals(especie.nombre, especieRecuperado.nombre)
        assertEquals(especie.paisDeOrigen, especieRecuperado.paisDeOrigen)
    }

    @Test
    fun `si trato de recuperar un Especie inexistente devuelve null`() {
        assertTrue(especieRepository.findById(10000001).isEmpty)
    }

    @Test
    fun `si recupero todos los especies recibo todos`(){

        val especiesPersistidos = data.crearSetDeDatosIniciales().filterIsInstance<Especie>()
        val recuperados = especieRepository.findAll().toList()
        assertEquals(especiesPersistidos.size, recuperados.size)
        assertTrue(
            recuperados.all {especie ->
                especiesPersistidos.any {
                    it.id == especie.id &&
                            it.nombre == especie.nombre &&
                            it.paisDeOrigen == especie.paisDeOrigen
                }
            }
        )
    }

    @Test
    fun `si pido la cantidad de infectados de una especie me la da`() {
        val especieContagiada = data.crearPandemiaPositiva()

        assertEquals(21, especieRepository.cantidadDeInfectados(especieContagiada.id!!))
    }

    @Test
    fun `si pido la especie lider me da la especie que infecto a mas vectores del tipo dado`(){

        val patogeno2 = Patogeno("tipo444444")
        val especie2 = patogeno2.crearEspecie("nombre444444", "lugar34444")
        data.persistir(listOf(especie2, patogeno2))
        val setInicial = data.crearSetDeDatosIniciales()
        val animales = setInicial
            .filterIsInstance<Vector>()
            .filter {
                it.tipo == TipoDeVector.Animal
            }
        animales.forEach{ it.agregarEspecie(especie2) }
        data.persistir(animales)

        val especieLider = especieRepository.especieLider(TipoDeVector.Animal)

        assertEquals(especie2.id, especieLider.id)
        assertEquals(especie2.paisDeOrigen, especieLider.paisDeOrigen)
        assertEquals(especie2.nombre, especieLider.nombre)

    }

    @Test
    fun `si pido la especie lider sin aclarar tipo me da la especie que infecto a mas humanos`(){
        val patogeno2 = Patogeno("tipo444444")
        val especie2 = patogeno2.crearEspecie("nombre444444", "lugar34444")
        data.persistir(listOf(especie2, patogeno2))

        val setInicial = data.crearSetDeDatosIniciales()
        val personas = setInicial
            .filterIsInstance<Vector>()
            .filter {
                it.tipo == TipoDeVector.Persona
            }
        personas.forEach{ it.agregarEspecie(especie2) }
        data.persistir(personas)

        val especieLider = especieRepository.especieLider()

        assertEquals(especie2.id, especieLider.id)
        assertEquals(especie2.paisDeOrigen, especieLider.paisDeOrigen)
        assertEquals(especie2.nombre, especieLider.nombre)
    }

    @AfterEach
    fun tearDown() {
        data.eliminarTodo()
    }

}