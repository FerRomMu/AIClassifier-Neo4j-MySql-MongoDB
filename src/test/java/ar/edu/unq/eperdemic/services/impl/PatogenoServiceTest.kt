package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.exceptions.DataNotFoundException
import ar.edu.unq.eperdemic.exceptions.IdNotFoundException
import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.persistencia.repository.mongo.DistritoMongoRepository
import ar.edu.unq.eperdemic.persistencia.repository.mongo.UbicacionMongoRepository
import ar.edu.unq.eperdemic.services.EspecieService
import ar.edu.unq.eperdemic.services.PatogenoService
import ar.edu.unq.eperdemic.services.UbicacionService
import ar.edu.unq.eperdemic.services.VectorService
import ar.edu.unq.eperdemic.utils.DataService
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import javax.persistence.NoResultException

@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PatogenoServiceTest {

    @Autowired lateinit var patogenoService: PatogenoService
    @Autowired lateinit var ubicacionService: UbicacionService
    @Autowired lateinit var vectorService: VectorService
    @Autowired lateinit var dataService: DataService
    @Autowired lateinit var especieService : EspecieService
    @Autowired lateinit var ubicacionMongoRepository: UbicacionMongoRepository
    @Autowired lateinit var distritoMongoRepository: DistritoMongoRepository

    lateinit var patogeno: Patogeno
    lateinit var coordenada: Coordenada
    lateinit var distrito: Distrito

    @BeforeEach
    fun crearModelo() {
        coordenada = Coordenada(1.0, 2.0)
        distrito = Distrito("distritoA", listOf(coordenada, Coordenada(2.0, 1.0), Coordenada(2.2, 2.2)))

        distritoMongoRepository.save(distrito)
    }

    @Test
    fun `si creo un patogeno este recibe un id`(){
        patogeno = Patogeno("Gripe")
        assertNull(patogeno.id)

        patogenoService.crearPatogeno(patogeno)
        assertNotNull(patogeno.id)
    }

    @Test
    fun `si creo un patogeno lo puedo recuperar con sus datos`() {
        patogeno = Patogeno("Gripe")

        patogenoService.crearPatogeno(patogeno)
        val patogenoRecuperado = patogenoService.recuperarPatogeno(patogeno.id!!)

        assertEquals(patogenoRecuperado.id!!, patogeno.id)
        assertEquals(patogenoRecuperado.tipo, patogeno.tipo)
        assertEquals(patogenoRecuperado.cantidadDeEspecies(), patogeno.cantidadDeEspecies())
    }

    @Test
    fun `si trato de recuperar un patogeno inexistente falla`() {
        assertThrows(IdNotFoundException::class.java) { patogenoService.recuperarPatogeno(10000001) }
    }

    @Test
    fun `si trato de recuperar todos llegan todos`() {
        dataService.crearSetDeDatosIniciales()
        val patogenos = patogenoService.recuperarATodosLosPatogenos()

        assertEquals(21, patogenos.size)
    }

    @Test
    fun `si trato de recuperar todos y no hay nadie simplemente recibo 0`() {

        val patogenos = patogenoService.recuperarATodosLosPatogenos()

        assertEquals(0, patogenos.size)
    }

    @Test
    fun `si agrego una especie se persiste en el patogeno`() {
        val ubicacion = ubicacionService.crearUbicacion("Ubicacion", coordenada)
        val vectorInfectado = Vector(TipoDeVector.Persona)
        vectorInfectado.ubicacion = ubicacion
        dataService.persistir(vectorInfectado)
        patogeno = Patogeno("Gripe")

        patogenoService.crearPatogeno(patogeno)
        patogenoService.agregarEspecie(patogeno.id!!, "virusT", ubicacion.id!!)
        val patogenoRecuperado = patogenoService.recuperarPatogeno(patogeno.id!!)

        assertTrue(patogenoRecuperado.especies.map{e -> e.nombre}.contains("virusT"))
    }

    @Test
    fun `si agrego una especie infecta a un vector de la ubicacion`() {
        val ubicacionPatogeno = ubicacionService.crearUbicacion("Ubicacion",coordenada)
        var vectorInfectado = Vector(TipoDeVector.Persona)
        vectorInfectado.ubicacion = ubicacionPatogeno
        dataService.persistir(vectorInfectado)

        assertFalse(vectorInfectado.especiesContagiadas.map{e -> e.nombre}.contains("virusT"))

        patogeno = Patogeno("Gripe")
        patogenoService.crearPatogeno(patogeno)
        patogenoService.agregarEspecie(patogeno.id!!, "virusT", ubicacionPatogeno.id!!)
        vectorInfectado = vectorService.recuperarVector(vectorInfectado.id!!)

        assertTrue(vectorInfectado.especiesContagiadas.map{e -> e.nombre}.contains("virusT"))
    }

    @Test
    fun `si agrego una especie se persiste la especie`() {
        val ubicacionPatogeno = ubicacionService.crearUbicacion("Ubicacion",coordenada)
        val vector = Vector(TipoDeVector.Persona)
        vector.ubicacion = ubicacionPatogeno
        dataService.persistir(vector)
        patogeno = Patogeno("Gripe")
        patogenoService.crearPatogeno(patogeno)
        val especieCreada = patogenoService.agregarEspecie(patogeno.id!!, "virus", ubicacionPatogeno.id!!)

        val especiePersistida = especieService.recuperarEspecie(especieCreada.id!!)
        
        assertEquals(especieCreada.id, especiePersistida.id)
        assertEquals(especieCreada.patogeno.id, especiePersistida.patogeno.id)
        assertEquals(especieCreada.nombre, especiePersistida.nombre)

    }

    @Test
    fun `si agrego una especie y no hay vectores a infectar en la ubicacion falla`() {
        patogeno = Patogeno("Gripe")

        patogenoService.crearPatogeno(patogeno)
        val ubicacionSinVectores = ubicacionService.crearUbicacion("bernal",coordenada)

        assertThrows(DataNotFoundException::class.java) {
            patogenoService.agregarEspecie(patogeno.id!!, "virusT", ubicacionSinVectores.id!!)
        }
    }

    @Test
    fun `si trato de recuperar las especies de un patogeno las devuelve`() {
        patogeno = Patogeno("Gripe")

        patogeno.crearEspecie("virusT", "mansion spencer")
        patogeno.crearEspecie("virusG", "raccoon city")
        patogeno.crearEspecie("virus progenitor", "montanas arklay")

        patogenoService.crearPatogeno(patogeno)

        val especies = patogenoService.especiesDePatogeno(patogeno.id!!).map{e -> e.nombre}

        assertEquals(3, especies.size)

        assertTrue(especies.contains("virusT"))
        assertTrue(especies.contains("virusG"))
        assertTrue(especies.contains("virus progenitor"))
    }

    @Test
    fun `si ejecuto esPandemia devuelve verdadero para una especie en mas de la mitad de ubicaciones`(){
        val especie = dataService.crearPandemiaPositiva()

        assertTrue(patogenoService.esPandemia(especie.id!!))
    }

    @Test
    fun `si ejecuto esPandemia devuelve falso para una especie en menos de la mitad de ubicaciones`(){
        dataService.crearSetDeDatosIniciales()

        val patogeno = Patogeno("Gripe")
        val ubicacion = ubicacionService.crearUbicacion("Lugar 21",coordenada)

        patogenoService.crearPatogeno(patogeno)
        val vector = vectorService.crearVector(TipoDeVector.Persona, ubicacion.id!!)
        val especie = Especie(patogeno, "21", "BR")
        dataService.persistir(especie)

        vectorService.infectar(vector,especie)

        assertFalse(patogenoService.esPandemia(especie.id!!))
    }

    @Test
    fun `si agrego una especie se actualiza ubicacionMongo`() {
        val ubicacion = ubicacionService.crearUbicacion("Ubicacion", coordenada)
        val vectorInfectado = Vector(TipoDeVector.Persona)
        vectorInfectado.ubicacion = ubicacion
        dataService.persistir(vectorInfectado)

        assertFalse(ubicacionMongoRepository.findByNombre(ubicacion.nombre).hayAlgunInfectado)

        patogeno = Patogeno("Gripe")
        patogenoService.crearPatogeno(patogeno)
        patogenoService.agregarEspecie(patogeno.id!!, "virusT", ubicacion.id!!)
        val patogenoRecuperado = patogenoService.recuperarPatogeno(patogeno.id!!)

        assertTrue(patogenoRecuperado.especies.map{e -> e.nombre}.contains("virusT"))

        assertTrue(ubicacionMongoRepository.findByNombre(ubicacion.nombre).hayAlgunInfectado)
    }

    @AfterEach
    fun deleteAll() {
        dataService.eliminarTodo()
        ubicacionMongoRepository.deleteAll()
    }

}