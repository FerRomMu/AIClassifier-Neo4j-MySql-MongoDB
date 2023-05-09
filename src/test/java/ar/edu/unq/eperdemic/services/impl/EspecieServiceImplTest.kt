package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.exceptions.IdNotFoundException
import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.services.EspecieService
import ar.edu.unq.eperdemic.utils.DataService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EspecieServiceImplTest {

    @Autowired lateinit var especieService: EspecieService
    @Autowired lateinit var data : DataService
    //@Autowired lateinit var vectorService: VectorServiceImpl
    lateinit var especie: Especie
    lateinit var patogeno: Patogeno

    @BeforeEach
    fun setup() {

    }

    @Test
    fun `puedo recuperar una especie guardada con su id`() {

        patogeno = Patogeno("Gripe")
        especie = Especie(patogeno,"especie11", "ARG")

        data.persistir(listOf(patogeno, especie))

        val recuperado = especieService.recuperarEspecie(especie.id!!)

        assertEquals(especie.id, recuperado.id)
        assertEquals("especie11", recuperado.nombre)
        assertEquals("ARG", recuperado.paisDeOrigen)
        assertEquals(patogeno.id, recuperado.patogeno.id)
    }

    @Test
    fun `si intento recuperar una especie con un id inexistente falla`() {
        assertThrows(IdNotFoundException::class.java) { especieService.recuperarEspecie(100000) }
    }

    @Test
    fun `recuperar todas las especies`() {
        val especiesPersistidas = data.crearSetDeDatosIniciales().filterIsInstance<Especie>()

        val especies = especieService.recuperarTodos()

        assertEquals(especiesPersistidas.size, especies.size)
        assertTrue(
            especies.all { especie ->
                especiesPersistidas.any{
                    it.id == especie.id &&
                    it.nombre == especie.nombre &&
                    it.paisDeOrigen == especie.paisDeOrigen &&
                    it.patogeno.id == especie.patogeno.id
                }
            }
        )
    }

    @Test
    fun `obtener la cantidad de infectados de una especie que no infecto a ningun vector`() {
        patogeno = Patogeno("Gripe")
        especie = Especie(patogeno,"especie11", "ARG")
        val vector = Vector(TipoDeVector.Animal)
        val vector2 = Vector(TipoDeVector.Persona)

        data.persistir(listOf(patogeno,especie,vector,vector2))

        val cantidad = especieService.cantidadDeInfectados(especie.id!!)

        assertEquals(0, cantidad)
    }
/*
    @Test
    fun `obtener la cantidad de infectados de una especie`() {
        patogeno = Patogeno("Gripe")
        especie = Especie(patogeno,"especie11", "ARG")
        val vector = Vector(TipoDeVector.Animal)
        val vector2 = Vector(TipoDeVector.Persona)

        data.persistir(patogeno)
        vectorService.infectar(vector, especie)
        vectorService.infectar(vector2, especie)

        val cantidad = especieService.cantidadDeInfectados(especie.id!!)

        assertEquals(2, cantidad)
    }

 */

    @AfterEach
    fun deleteAll() {
        data.eliminarTodo()
    }
}