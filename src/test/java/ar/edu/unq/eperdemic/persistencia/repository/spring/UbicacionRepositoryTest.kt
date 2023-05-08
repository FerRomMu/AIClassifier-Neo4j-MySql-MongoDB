package ar.edu.unq.eperdemic.persistencia.repository.spring

import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.utils.DataService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UbicacionRepositoryTest {
  
    lateinit var ubicacion: Ubicacion
    @Autowired
    lateinit var ubicacionRepository: UbicacionRepository
    @Autowired
    lateinit var data: DataService

    @BeforeEach
    fun setUp() {
        ubicacion = Ubicacion("unaUbicacion")
    }

    @Test
    fun `si creo una ubicacion al guardarla se le asigna un id`() {

        assertNull(ubicacion.id)

        ubicacionRepository.save(ubicacion)

        assertNotNull(ubicacion.id)

    }

    @Test
    fun `si trato de recuperar una ubicacion existente con su id la obtengo`() {
        data.persistir(ubicacion)
        val ubicacionRecuperada =  ubicacionRepository.findById(ubicacion.id!!).get()

        assertEquals(ubicacion.id, ubicacionRecuperada.id)
        assertEquals(ubicacion.nombre, ubicacionRecuperada.nombre)
    }

    @Test
    fun `si trato de recuperar una ubicacion inexistente devuelve null`() {
        assertTrue(ubicacionRepository.findById(10000001).isEmpty)
    }

    @Test
    fun `si recupero todas las ubicaciones recibo todas`(){

        val ubicacionPersistidos = data.crearSetDeDatosIniciales().filterIsInstance<Ubicacion>()
        val recuperados = ubicacionRepository.findAll().toList()

        assertEquals(21, recuperados.size)
        assertTrue(
            recuperados.all { ubicacion ->
                ubicacionPersistidos.any {
                    it.id == ubicacion.id &&
                            it.nombre == ubicacion.nombre
                }
            }
        )
    }

    @Test
    fun `si borro una ubicacion esta deja de estar`(){
        data.persistir(ubicacion)

        ubicacionRepository.deleteById(ubicacion.id)

        assertTrue(ubicacionRepository.findById(ubicacion.id!!).isEmpty)
    }

    @Test
    fun `si borro una ubicacion que es referenciada por uno o mas vectores falla`(){

        val ubicacions = data.crearSetDeDatosIniciales().filterIsInstance<Ubicacion>()
        val ubicacionABorrar = ubicacions.first()

        assertThrows(DataIntegrityViolationException::class.java) { ubicacionRepository.deleteById(ubicacionABorrar.id!!) }
    }

    @Test
    fun `si borro una ubicacion con id invalida no devuelve nada`() {

        assertThrows(NullPointerException::class.java) { ubicacionRepository.deleteById(ubicacion.id!!) }
    }

    @Test
    fun `si pido los vectores de un lugar recibo esos vectores`(){

        val vector = Vector(TipoDeVector.Persona)
        val vector2 = Vector(TipoDeVector.Persona)
        val vectorEnOtroLado = Vector(TipoDeVector.Animal)
        vector.ubicacion = ubicacion
        vector2.ubicacion = ubicacion
        val unLugar = Ubicacion("unLugar")
        vectorEnOtroLado.ubicacion = unLugar

        data.persistir(listOf(ubicacion, unLugar, vector, vector2, vectorEnOtroLado))

        val vectoresDelLugar = ubicacionRepository.vectoresEn(ubicacion.id)

        assertEquals(2, vectoresDelLugar.size)
        assertEquals(vector.id, vectoresDelLugar[0].id)
        assertEquals(vector2.id, vectoresDelLugar[1].id)
    }

    @Test
    fun `cantidad de vectores infectados en una ubicacion`()  {
        val patogeno = Patogeno("patogenoSSS")

        val vector1 = Vector(TipoDeVector.Insecto) ; vector1.ubicacion = ubicacion
        val vector2 = Vector(TipoDeVector.Insecto) ; vector2.ubicacion = ubicacion
        val vector3 = Vector(TipoDeVector.Insecto) ; vector3.ubicacion = ubicacion
        val vector4 = Vector(TipoDeVector.Insecto) ; vector4.ubicacion = ubicacion
        val vector5 = Vector(TipoDeVector.Insecto) ; vector5.ubicacion = ubicacion

        val especie1 = Especie(patogeno, "especie111", "arg")
        val especie2 = Especie(patogeno, "especie222", "arg")
        val especie3 = Especie(patogeno, "especie333", "arg")

        vector1.agregarEspecie(especie1) ; vector2.agregarEspecie(especie1); vector3.agregarEspecie(especie1)
        vector4.agregarEspecie(especie2)

        data.persistir(listOf(especie1, especie2, especie3, vector1, vector2, vector3, vector4, vector5, ubicacion, patogeno))

        val cantidad = ubicacionRepository.cantidadVectoresInfectados(ubicacion.nombre)

        assertEquals(4, cantidad)
    }

    @AfterEach
    fun tearDown() {
        data.eliminarTodo()
    }
}
