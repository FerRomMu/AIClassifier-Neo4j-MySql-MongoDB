package ar.edu.unq.eperdemic.persistencia.repository.spring

import ar.edu.unq.eperdemic.exceptions.IdNotFoundException
import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.services.runner.TransactionRunner
import ar.edu.unq.eperdemic.utils.DataService
import ar.edu.unq.eperdemic.utils.impl.DataServiceImpl
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Repository
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VectorRepositoryTest {

    @Autowired lateinit var vectorRepository: VectorRepository
    @Autowired lateinit var data: DataService
    lateinit var vector: Vector

    @BeforeEach
    fun setUp() {
        vector = Vector(TipoDeVector.Animal)
    }

    @Test
    fun `si creo un vector al guardarlo se le asigna un id`() {

        assertNull(vector.id)

        vectorRepository.save(vector)

        assertNotNull(vector.id)

    }

    @Test
    fun `si guardo un vector con id se actualiza`() {
        data.persistir(vector)

        assertEquals(TipoDeVector.Animal, vector.tipo)
        vector.tipo = TipoDeVector.Insecto

        vectorRepository.save(vector)
        val vectorActualizado = vectorRepository.findById(vector.id!!).get()

        assertEquals(TipoDeVector.Insecto, vectorActualizado.tipo)
    }

    @Test
    fun `si trato de recuperar un vector existente con su id lo obtengo`() {
        data.persistir(vector)

        val vectorRecuperado = vectorRepository.findById(vector.id!!).get()

        assertEquals(vector.id, vectorRecuperado.id)
        assertEquals(vector.tipo, vectorRecuperado.tipo)
    }

    @Test
    fun `si trato de recuperar un vector devuelve vacio`() {
        assertTrue(vectorRepository.findById(10000001).isEmpty)
    }

    @Test
    fun `si recupero todos los vectores recibo todos`(){
        val vectoresPersistidos = data.crearSetDeDatosIniciales().filterIsInstance<Vector>()

        val recuperados =  vectorRepository.findAll().toList()

        assertEquals(21, recuperados.size)
        assertTrue(
            recuperados.all { vector ->
                vectoresPersistidos.any {
                    it.id == vector.id &&
                            it.tipo == vector.tipo &&
                            it.ubicacion.id == vector.ubicacion.id &&
                            it.especiesContagiadas.size == vector.especiesContagiadas.size
                }
            }
        )

    }

    @Test
    fun `si borro un vector se elimina`() {
        data.persistir(vector)
        vectorRepository.delete(vector)

        assertTrue(vectorRepository.findById(vector.id!!).isEmpty)
    }

    @Test
    fun `si borro un vector con id invalida falla`() {

        assertThrows(NullPointerException::class.java) { vectorRepository.deleteById(vector.id!!) }
    }

    @Test
    fun `devolver todas las enfermedades de un vector`() {
        val patogeno = Patogeno("Tipo 111")
        val especie = Especie(patogeno, "Especie 111", "ARG")
        val especie2 = Especie(patogeno, "Especie 222", "ARG")
        vector.agregarEspecie(especie) ; vector.agregarEspecie(especie2)

        data.persistir(listOf(patogeno, especie, especie2, vector))

        val enfermedades = vectorRepository.enfermedades(vector.id)

        assertEquals(enfermedades[0].id, especie.id)
        assertEquals(enfermedades[0].nombre, especie.nombre)
        assertEquals(enfermedades[0].paisDeOrigen, especie.paisDeOrigen)
        assertEquals(enfermedades[1].id, especie2.id)
        assertEquals(enfermedades[1].nombre, especie2.nombre)
        assertEquals(enfermedades[1].paisDeOrigen, especie2.paisDeOrigen)
    }

    @Test
    fun `devolver un vector aleatorio en una ubicacion devuelve un vector aleatorio de esa ubicacion`() {
        val vector2 = Vector(TipoDeVector.Insecto)
        val vector3 = Vector(TipoDeVector.Persona)
        val ubicacion = Ubicacion("ubicacion 1")
        val vectorOtroLado = Vector(TipoDeVector.Persona)
        val otroLugar = Ubicacion("Otro lado")

        vector.ubicacion = ubicacion
        vector2.ubicacion = ubicacion
        vector3.ubicacion = ubicacion
        vectorOtroLado.ubicacion = otroLugar

        data.persistir(listOf(ubicacion, otroLugar, vector, vector2, vector3, vectorOtroLado))

        val vectorAleatorio = vectorRepository.vectorAleatorioEn(ubicacion.id!!)

        assertTrue(vectorAleatorio.id == vector.id || vectorAleatorio.id == vector2.id || vectorAleatorio.id == vector3.id)
    }

    @Test
    fun `devolver un vector aleatorio en una ubicacion sin vectores da null` () {
        val ubicacion = Ubicacion("desierto")
        data.persistir(ubicacion)

        assertThrows(NullPointerException::class.java) { vectorRepository.vectorAleatorioEn(vector.id!!) }
    }

    @AfterEach
    fun tearDown() {
        data.eliminarTodo()
    }

}