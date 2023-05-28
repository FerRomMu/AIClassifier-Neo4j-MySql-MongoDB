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
    fun `si pido los lideres y hay menos de diez me da los que haya en orden de mas contagios`(){

        val patogeno2 = Patogeno("tipo444444")
        val especie2 = patogeno2.crearEspecie("nombre444444", "lugar34444")
        val especie3 = patogeno2.crearEspecie("nombre34243", "lugar34243")
        data.persistir(listOf(especie2, especie3, patogeno2))

        val setInicial = data.crearSetDeDatosIniciales()
        val animalesYPersonas = setInicial
            .filterIsInstance<Vector>()
            .filter {
                it.tipo == TipoDeVector.Animal ||
                        it.tipo == TipoDeVector.Persona
            }

        for (i in animalesYPersonas.indices) {
            if (i < 5) {
                animalesYPersonas[i].agregarEspecie(especie2)
                animalesYPersonas[i].agregarEspecie(especie3)
            } else {
                animalesYPersonas[i].agregarEspecie(especie2)
            }
        }
        val especiesLideresOrdenadas = mutableListOf(especie2, especie3)

        data.persistir(animalesYPersonas)

        val lideres = especieRepository.lideres()

        assertEquals(2, lideres.size)
        for (i in lideres.indices) {
            assertEquals(especiesLideresOrdenadas[i].id, lideres[i].id)
            assertEquals(especiesLideresOrdenadas[i].nombre, lideres[i].nombre)
            assertEquals(especiesLideresOrdenadas[i].paisDeOrigen, lideres[i].paisDeOrigen)
            assertEquals(especiesLideresOrdenadas[i].patogeno.id, lideres[i].patogeno.id)
        }
    }

    @Test
    fun `si pido los lideres me da los primeros diez con mas contagios`(){

        val patogeno2 = Patogeno("tipo444444")

        val especiesNoLideres: MutableList<Especie> = mutableListOf()
        for (i in 1..100) {
            especiesNoLideres.add(patogeno2.crearEspecie("nombre$i", "lugar$i"))
        }

        val especiesQueSeranLideres: MutableList<Especie> = mutableListOf()
        for (i in 101..110) {
            especiesQueSeranLideres.add(patogeno2.crearEspecie("nombre$i", "lugar$i"))
        }

        data.persistir(especiesNoLideres + especiesQueSeranLideres + patogeno2)

        val setInicial = data.crearSetDeDatosIniciales()
        val animalesYPersonas = setInicial
            .filterIsInstance<Vector>()
            .filter {
                it.tipo == TipoDeVector.Animal ||
                        it.tipo == TipoDeVector.Persona
            }

        val especiesLideresOrdenadas = mutableListOf<Especie>()
        for (i in animalesYPersonas.indices) {
            if (i < 4) {
                especiesNoLideres.forEach{ animalesYPersonas[i].agregarEspecie(it) }
                especiesQueSeranLideres.forEach{ animalesYPersonas[i].agregarEspecie(it) }
            } else {
                especiesQueSeranLideres.forEach{ animalesYPersonas[i].agregarEspecie(it) }
                val nuevoLider = especiesQueSeranLideres.removeFirst()
                especiesLideresOrdenadas.add(nuevoLider)
            }
        }
        especiesLideresOrdenadas.reverse()

        data.persistir(animalesYPersonas)

        val lideres = especieRepository.lideres()

        assertEquals(10, lideres.size)
        for (i in lideres.indices) {
            assertEquals(especiesLideresOrdenadas[i].id, lideres[i].id)
            assertEquals(especiesLideresOrdenadas[i].nombre, lideres[i].nombre)
            assertEquals(especiesLideresOrdenadas[i].paisDeOrigen, lideres[i].paisDeOrigen)
            assertEquals(especiesLideresOrdenadas[i].patogeno.id, lideres[i].patogeno.id)
        }
    }

    @Test
    fun `si pido los lideres aclarando tipos y hay menos de 10 me da los que haya en orden de mas contagios para esos tipos de vector`(){

        val patogeno2 = Patogeno("tipo444444")
        val especie2 = patogeno2.crearEspecie("nombre444444", "lugar34444")
        val especie3 = patogeno2.crearEspecie("nombre34243", "lugar34243")
        data.persistir(listOf(especie2, especie3, patogeno2))

        val setInicial = data.crearSetDeDatosIniciales()
        val insectosYAnimales = setInicial
            .filterIsInstance<Vector>()
            .filter {
                it.tipo == TipoDeVector.Insecto ||
                        it.tipo == TipoDeVector.Animal
            }

        for (i in insectosYAnimales.indices) {
            if (i < 5) {
                insectosYAnimales[i].agregarEspecie(especie2)
                insectosYAnimales[i].agregarEspecie(especie3)
            } else {
                insectosYAnimales[i].agregarEspecie(especie2)
            }
        }
        val especiesLideresOrdenadas = mutableListOf(especie2, especie3)

        data.persistir(insectosYAnimales)

        val lideres = especieRepository.lideres(TipoDeVector.Insecto, TipoDeVector.Animal)

        assertEquals(2, lideres.size)
        for (i in lideres.indices) {
            assertEquals(especiesLideresOrdenadas[i].id, lideres[i].id)
            assertEquals(especiesLideresOrdenadas[i].nombre, lideres[i].nombre)
            assertEquals(especiesLideresOrdenadas[i].paisDeOrigen, lideres[i].paisDeOrigen)
            assertEquals(especiesLideresOrdenadas[i].patogeno.id, lideres[i].patogeno.id)
        }
    }

    @Test
    fun `si pido los lideres aclarando tipos me da los primeros 10 que haya en orden de mas contagios para esos tipos de vector`(){

        val patogeno2 = Patogeno("tipo444444")

        val especiesNoLideres: MutableList<Especie> = mutableListOf()
        for (i in 1..100) {
            especiesNoLideres.add(patogeno2.crearEspecie("nombre$i", "lugar$i"))
        }

        val especiesQueSeranLideres: MutableList<Especie> = mutableListOf()
        for (i in 101..110) {
            especiesQueSeranLideres.add(patogeno2.crearEspecie("nombre$i", "lugar$i"))
        }

        data.persistir(especiesNoLideres + especiesQueSeranLideres + patogeno2)

        val setInicial = data.crearSetDeDatosIniciales()
        val insectosYAnimales = setInicial
            .filterIsInstance<Vector>()
            .filter {
                it.tipo == TipoDeVector.Insecto ||
                        it.tipo == TipoDeVector.Animal
            }

        val especiesLideresOrdenadas = mutableListOf<Especie>()
        for (i in insectosYAnimales.indices) {
            if (i < 4) {
                especiesNoLideres.forEach{ insectosYAnimales[i].agregarEspecie(it) }
                especiesQueSeranLideres.forEach{ insectosYAnimales[i].agregarEspecie(it) }
            } else {
                especiesQueSeranLideres.forEach{ insectosYAnimales[i].agregarEspecie(it) }
                val nuevoLider = especiesQueSeranLideres.removeFirst()
                especiesLideresOrdenadas.add(nuevoLider)
            }
        }
        especiesLideresOrdenadas.reverse()

        data.persistir(insectosYAnimales)

        val lideres = especieRepository.lideres(TipoDeVector.Insecto, TipoDeVector.Animal)

        assertEquals(10, lideres.size)
        for (i in lideres.indices) {
            assertEquals(especiesLideresOrdenadas[i].id, lideres[i].id)
            assertEquals(especiesLideresOrdenadas[i].nombre, lideres[i].nombre)
            assertEquals(especiesLideresOrdenadas[i].paisDeOrigen, lideres[i].paisDeOrigen)
            assertEquals(especiesLideresOrdenadas[i].patogeno.id, lideres[i].patogeno.id)
        }
    }

    @Test
    fun `si pido los lideres pero ninguna especie contagio tanto humanos como animales no recibo nada`(){

        data.crearSetDeDatosIniciales()
        val lideres = especieRepository.lideres()

        assertEquals(0, lideres.size)
    }

    @AfterEach
    fun tearDown() {
        data.eliminarTodo()
    }

}