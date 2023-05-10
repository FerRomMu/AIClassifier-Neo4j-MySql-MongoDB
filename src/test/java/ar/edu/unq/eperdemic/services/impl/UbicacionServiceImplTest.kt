package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.exceptions.DataDuplicationException
import ar.edu.unq.eperdemic.utils.DataService
import ar.edu.unq.eperdemic.utils.impl.DataServiceImpl
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UbicacionServiceImplTest {


    @Autowired
    lateinit var vectorService: VectorServiceImpl

    lateinit var ubicacionService: UbicacionServiceImpl
    lateinit var dado: Randomizador
    lateinit var dataService: DataService

    @BeforeEach
    fun setUp() {
        dataService = DataServiceImpl()
        ubicacionService = UbicacionServiceImpl()

        dado = Randomizador.getInstance()
        dado.estado = EstadoRandomizadorDeterminístico()
    }

    @Test
    fun  `mover vector a una ubicacion con un humano y un animal`() {
        var cordoba = ubicacionService.crearUbicacion("Cordoba")
        var chaco = ubicacionService.crearUbicacion("Chaco")

        var vectorAMover = vectorService.crearVector(TipoDeVector.Persona,cordoba.id!!)

        var vectorVictima1 = vectorService.crearVector(TipoDeVector.Persona,chaco.id!!)
        var vectorVictima2 = vectorService.crearVector(TipoDeVector.Animal,chaco.id!!)

        var patogeno = Patogeno("Patogeni_SS")
        patogeno.setCapacidadDeContagioHumano(100)
        dataService.persistir(patogeno)


        var especieAContagiar = patogeno.crearEspecie("Especie_Sl","Honduras")

        vectorService.infectar(vectorAMover,especieAContagiar)

        assertEquals(vectorAMover.especiesContagiadas.size,1)
        assertEquals(vectorAMover.especiesContagiadas.first().id, patogeno.id)
        assertEquals(vectorVictima1.especiesContagiadas.size,0)
        assertEquals(vectorVictima2.especiesContagiadas.size,0)

        var vectoresEnChaco = ubicacionService.vectoresEn(chaco.id!!)

        assertEquals(vectoresEnChaco.size,2)

        ubicacionService.mover(vectorAMover.id!!,chaco.id!!)

        vectoresEnChaco = ubicacionService.vectoresEn(chaco.id!!)
        assertEquals(vectoresEnChaco.size,3)

        vectorAMover = vectorService.recuperarVector(vectorAMover.id!!)
        vectorVictima1 = vectorService.recuperarVector(vectorVictima1.id!!)
        vectorVictima2 =vectorService.recuperarVector(vectorVictima2.id!!)


        assertEquals(vectorAMover.especiesContagiadas.size,1)
        assertEquals(vectorAMover.especiesContagiadas.first().id, patogeno.id)
        assertEquals(vectorVictima1.especiesContagiadas.size,1)
        assertEquals(vectorVictima1.especiesContagiadas.first().id, patogeno.id)
        assertEquals(vectorVictima2.especiesContagiadas.size,0)
    }

    @Test
    fun  `mover vector insecto a una ubicacion con solo insectos`() {
        var cordoba = ubicacionService.crearUbicacion("Cordoba")
        var chaco = ubicacionService.crearUbicacion("Chaco")

        var vectorAMover = vectorService.crearVector(TipoDeVector.Insecto,cordoba.id!!)

        var vectorVictima1 = vectorService.crearVector(TipoDeVector.Insecto,chaco.id!!)
        var vectorVictima2 = vectorService.crearVector(TipoDeVector.Insecto,chaco.id!!)

        var patogeno = Patogeno("Patogeni_SS")
        patogeno.setCapacidadDeContagioInsecto(100)
        dataService.persistir(patogeno)

        var especieAContagiar = patogeno.crearEspecie("Especie_Sl","Honduras")

        vectorService.infectar(vectorAMover,especieAContagiar)

        assertEquals(vectorAMover.especiesContagiadas.size,1)
        assertEquals(vectorAMover.especiesContagiadas.first().id, patogeno.id)
        assertEquals(vectorVictima1.especiesContagiadas.size,0)
        assertEquals(vectorVictima2.especiesContagiadas.size,0)

        var vectoresEnChaco = ubicacionService.vectoresEn(chaco.id!!)

        assertEquals(vectoresEnChaco.size,2)

        ubicacionService.mover(vectorAMover.id!!,chaco.id!!)

        vectoresEnChaco = ubicacionService.vectoresEn(chaco.id!!)
        assertEquals(vectoresEnChaco.size,3)

        vectorAMover = vectorService.recuperarVector(vectorAMover.id!!)
        vectorVictima1 = vectorService.recuperarVector(vectorVictima1.id!!)
        vectorVictima2 =vectorService.recuperarVector(vectorVictima2.id!!)

        assertEquals(vectorAMover.especiesContagiadas.size,1)
        assertEquals(vectorAMover.especiesContagiadas.first().id, patogeno.id)
        assertEquals(vectorVictima1.especiesContagiadas.size,0)
        assertEquals(vectorVictima2.especiesContagiadas.size,0)
    }

    @Test
    fun  `mover vector a ubicacion vacia`() {
        var cordoba = ubicacionService.crearUbicacion("Cordoba")
        var chaco = ubicacionService.crearUbicacion("Chaco")

        var vectorAMover = vectorService.crearVector(TipoDeVector.Persona,cordoba.id!!)

        var patogeno = Patogeno("Patogeni_SS")
        patogeno.setCapacidadDeContagioInsecto(100)
        dataService.persistir(patogeno)

        var especieAContagiar = patogeno.crearEspecie("Especie_Sl","Honduras")

        vectorService.infectar(vectorAMover,especieAContagiar)
        assertEquals(vectorAMover.especiesContagiadas.size,1)

        var vectoresEnChaco = ubicacionService.vectoresEn(chaco.id!!)
        assertEquals(vectoresEnChaco.size,0)

        ubicacionService.mover(vectorAMover.id!!,chaco.id!!)

        vectorAMover = vectorService.recuperarVector(vectorAMover.id!!)
        assertEquals(vectorAMover.ubicacion.id,chaco.id)
    }

    @Test
    fun `Expandir en una ubicacion`() {
        var cordoba = ubicacionService.crearUbicacion("Cordoba")


        var vectorLocal = vectorService.crearVector(TipoDeVector.Persona,cordoba.id!!)
        var vectorLocal2 = vectorService.crearVector(TipoDeVector.Animal,cordoba.id!!)
        var vectorAExpandir = vectorService.crearVector(TipoDeVector.Persona,cordoba.id!!)


        var patogeno = Patogeno("Patogeni_SS")
        patogeno.setCapacidadDeContagioHumano(100)
        dataService.persistir(patogeno)

        var especieAContagiar = Especie(patogeno,"Especie_Sl","Honduras")

        vectorService.infectar(vectorAExpandir,especieAContagiar)

        assertEquals(vectorAExpandir.especiesContagiadas.size,1)
        assertEquals(vectorAExpandir.especiesContagiadas.first().id, patogeno.id)
        assertEquals(vectorLocal.especiesContagiadas.size,0)
        assertEquals(vectorLocal2.especiesContagiadas.size,0)

        ubicacionService.expandir(cordoba.id!!)

        vectorAExpandir = vectorService.recuperarVector(vectorAExpandir.id!!)
        vectorLocal = vectorService.recuperarVector(vectorLocal.id!!)
        vectorLocal2 =vectorService.recuperarVector(vectorLocal2.id!!)

        assertEquals(vectorAExpandir.especiesContagiadas.size,1)
        assertEquals(vectorAExpandir.especiesContagiadas.first().id, patogeno.id)
        assertEquals(vectorLocal.especiesContagiadas.size,1)
        assertEquals(vectorLocal.especiesContagiadas.first().id, patogeno.id)
        assertEquals(vectorLocal2.especiesContagiadas.size,0)
    }

    @Test
    fun `Expandir en una ubicacion sin contagios no hace nada`() {
        val cordoba = ubicacionService.crearUbicacion("Cordoba")

        val vectorSinContagiar = vectorService.crearVector(TipoDeVector.Persona,cordoba.id!!)
        val vectorSinContagiar2 = vectorService.crearVector(TipoDeVector.Animal,cordoba.id!!)
        val vectorSinContagiar3 = vectorService.crearVector(TipoDeVector.Persona,cordoba.id!!)

        ubicacionService.expandir(cordoba.id!!)

        assertEquals(vectorSinContagiar.especiesContagiadas.size,0)
        assertEquals(vectorSinContagiar2.especiesContagiadas.size,0)
        assertEquals(vectorSinContagiar3.especiesContagiadas.size,0)
    }


    @Test
    fun `si creo una ubicacion esta recibe un id`() {
        var ubicacion = ubicacionService.crearUbicacion("ubicacionTest")
        assertNotNull(ubicacion.id)
    }

    @Test
    fun `si creo una ubicacion la puedo recuperar`() {
        val ubicacion = ubicacionService.crearUbicacion("ubicacionTest")
        val ubicacionRecuperada = ubicacionService.recuperar(ubicacion.id!!)

        assertEquals(ubicacion.nombre, ubicacionRecuperada.nombre)
        assertEquals(ubicacion.id, ubicacionRecuperada.id)
    }

    @Test
    fun `si trato de crear dos ubicaciones con el mismo nombre recibo error`() {
        ubicacionService.crearUbicacion("ubicacionRepetida")

        assertThrows(DataDuplicationException::class.java) { ubicacionService.crearUbicacion("ubicacionRepetida") }
    }

    @Test
    fun `si trato de recuperar todos llegan todos`() {
        val ubicacionesPersistidas = dataService.crearSetDeDatosIniciales().filterIsInstance<Ubicacion>()
        val ubicaciones = ubicacionService.recuperarTodos()

        assertEquals(ubicacionesPersistidas.size, ubicaciones.size)
        assertTrue(
            ubicaciones.all { ubicacion ->
                ubicacionesPersistidas.any{
                    it.id == ubicacion.id &&
                            it.nombre == ubicacion.nombre
                }
            }
        )
    }

    @Test
    fun `si trato de recuperar todos y no hay nadie simplemente recibo 0`() {

        val ubicaciones = ubicacionService.recuperarTodos()

        assertEquals(0, ubicaciones.size)
    }

    @AfterEach
    fun tearDown() {
        dataService.eliminarTodo()
    }

}