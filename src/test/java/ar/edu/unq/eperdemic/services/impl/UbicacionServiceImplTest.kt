package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.exceptions.DataDuplicationException
import ar.edu.unq.eperdemic.exceptions.DataNotFoundException
import ar.edu.unq.eperdemic.persistencia.repository.neo.UbicacionNeoRepository
import ar.edu.unq.eperdemic.persistencia.repository.spring.UbicacionRepository
import ar.edu.unq.eperdemic.services.UbicacionService
import ar.edu.unq.eperdemic.services.VectorService
import ar.edu.unq.eperdemic.utils.DataService
import ar.edu.unq.eperdemic.utils.impl.DataServiceImpl
import org.hibernate.exception.ConstraintViolationException
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

    @Autowired lateinit var vectorService: VectorService

    @Autowired lateinit var ubicacionService: UbicacionService
    @Autowired lateinit var ubicacionNeoRepository: UbicacionNeoRepository
    @Autowired lateinit var ubicacionRepository: UbicacionRepository
    @Autowired lateinit var dataService: DataService

    lateinit var dado: Randomizador

    @BeforeEach
    fun setUp() {
        dado = Randomizador.getInstance()
        dado.estado = EstadoRandomizadorDeterminístico()
    }

    @Test
    fun  `mover vector a una ubicacion con un humano y un animal`() {

        val cordoba = ubicacionService.crearUbicacion("Cordoba")
        val chaco = ubicacionService.crearUbicacion("Chaco")

        ubicacionService.conectar("Cordoba", "Chaco", Camino.TipoDeCamino.CaminoTerreste)
        dataService.persistir(cordoba)

        var vectorAMover = vectorService.crearVector(TipoDeVector.Persona,cordoba.id!!)

        var vectorVictima1 = vectorService.crearVector(TipoDeVector.Persona,chaco.id!!)
        var vectorVictima2 = vectorService.crearVector(TipoDeVector.Animal,chaco.id!!)

        val patogeno = Patogeno("Patogeni_SS")
        patogeno.setCapacidadDeContagioHumano(100)
        dataService.persistir(patogeno)


        val especieAContagiar = patogeno.crearEspecie("Especie_Sl","Honduras")
        dataService.persistir(especieAContagiar)

        vectorService.infectar(vectorAMover,especieAContagiar)

        assertEquals(vectorAMover.especiesContagiadas.size,1)
        assertEquals(vectorAMover.especiesContagiadas.first().id, especieAContagiar.id)
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
        assertEquals(vectorAMover.especiesContagiadas.first().id, especieAContagiar.id)
        assertEquals(vectorVictima1.especiesContagiadas.size,1)
        assertEquals(vectorVictima1.especiesContagiadas.first().id, especieAContagiar.id)
        assertEquals(vectorVictima2.especiesContagiadas.size,0)

    }

    @Test
    fun  `mover vector insecto a una ubicacion con solo insectos`() {

        val cordoba = ubicacionService.crearUbicacion("Cordoba")
        val chaco = ubicacionService.crearUbicacion("Chaco")

        ubicacionService.conectar("Cordoba", "Chaco", Camino.TipoDeCamino.CaminoTerreste)
        dataService.persistir(cordoba)

        var vectorAMover = vectorService.crearVector(TipoDeVector.Insecto,cordoba.id!!)

        var vectorVictima1 = vectorService.crearVector(TipoDeVector.Insecto,chaco.id!!)
        var vectorVictima2 = vectorService.crearVector(TipoDeVector.Insecto,chaco.id!!)

        val patogeno = Patogeno("Patogeni_SS")
        patogeno.setCapacidadDeContagioInsecto(100)
        dataService.persistir(patogeno)

        val especieAContagiar = Especie(patogeno,"Especie_Sl","Honduras")
        dataService.persistir(especieAContagiar)

        vectorService.infectar(vectorAMover,especieAContagiar)

        assertEquals(vectorAMover.especiesContagiadas.size,1)
        assertEquals(vectorAMover.especiesContagiadas.first().id, especieAContagiar.id)
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
        assertEquals(vectorAMover.especiesContagiadas.first().id, especieAContagiar.id)
        assertEquals(vectorVictima1.especiesContagiadas.size,0)
        assertEquals(vectorVictima2.especiesContagiadas.size,0)

    }

    @Test
    fun  `mover vector a ubicacion vacia`() {

        val cordoba = ubicacionService.crearUbicacion("Cordoba")
        val chaco = ubicacionService.crearUbicacion("Chaco")

        ubicacionService.conectar("Cordoba", "Chaco", Camino.TipoDeCamino.CaminoTerreste)
        dataService.persistir(cordoba)

        var vectorAMover = vectorService.crearVector(TipoDeVector.Persona,cordoba.id!!)

        val patogeno = Patogeno("Patogeni_SS")
        patogeno.setCapacidadDeContagioInsecto(100)
        dataService.persistir(patogeno)

        val especieAContagiar = patogeno.crearEspecie("Especie_Sl","Honduras")
        dataService.persistir(especieAContagiar)

        vectorService.infectar(vectorAMover,especieAContagiar)
        assertEquals(vectorAMover.especiesContagiadas.size,1)

        val vectoresEnChaco = ubicacionService.vectoresEn(chaco.id!!)
        assertEquals(vectoresEnChaco.size,0)

        ubicacionService.mover(vectorAMover.id!!,chaco.id!!)

        vectorAMover = vectorService.recuperarVector(vectorAMover.id!!)
        assertEquals(vectorAMover.ubicacion.id,chaco.id)

    }

    @Test
    fun `Expandir en una ubicacion`() {
        val cordoba = ubicacionService.crearUbicacion("Cordoba")

        var vectorLocal = vectorService.crearVector(TipoDeVector.Persona,cordoba.id!!)
        var vectorLocal2 = vectorService.crearVector(TipoDeVector.Animal,cordoba.id!!)
        var vectorAExpandir = vectorService.crearVector(TipoDeVector.Persona,cordoba.id!!)


        val patogeno = Patogeno("Patogeni_SS")
        patogeno.setCapacidadDeContagioHumano(100)
        dataService.persistir(patogeno)

        val especieAContagiar = Especie(patogeno,"Especie_Sl","Honduras")
        dataService.persistir(especieAContagiar)

        vectorService.infectar(vectorAExpandir,especieAContagiar)

        assertEquals(vectorAExpandir.especiesContagiadas.size,1)
        assertEquals(vectorAExpandir.especiesContagiadas.first().id, especieAContagiar.id)
        assertEquals(vectorLocal.especiesContagiadas.size,0)
        assertEquals(vectorLocal2.especiesContagiadas.size,0)

        ubicacionService.expandir(cordoba.id!!)

        vectorAExpandir = vectorService.recuperarVector(vectorAExpandir.id!!)
        vectorLocal = vectorService.recuperarVector(vectorLocal.id!!)
        vectorLocal2 =vectorService.recuperarVector(vectorLocal2.id!!)

        assertEquals(vectorAExpandir.especiesContagiadas.size,1)
        assertEquals(vectorAExpandir.especiesContagiadas.first().id, especieAContagiar.id)
        assertEquals(vectorLocal.especiesContagiadas.size,1)
        assertEquals(vectorLocal.especiesContagiadas.first().id, especieAContagiar.id)
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
        val ubicacion = ubicacionService.crearUbicacion("ubicacionTest")
        assertNotNull(ubicacion.id)
    }

    @Test
    fun `si creo una ubicacion se guarda una ubicacionNeo con ese nombre tambien`() {
        val ubicacion = ubicacionService.crearUbicacion("ubicacionTest")

        val ubicacionNeoCreada = ubicacionNeoRepository.findByNombre(ubicacion.nombre)

        assertEquals("ubicacionTest", ubicacionNeoCreada.nombre)
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

    @Test
    fun `se conectan dos ubicaciones existentes por medio terrestre`() {
        val Bera = ubicacionService.crearUbicacion("ubicacion neo 1")
        val ubicacion2 = ubicacionService.crearUbicacion("ubicacion neo 2")

        ubicacionService.conectar(Bera.nombre, ubicacion2.nombre, Camino.TipoDeCamino.CaminoTerreste)

        val ubicacionNeo1 = ubicacionNeoRepository.findByNombre(Bera.nombre)

        assertEquals(ubicacionNeo1.caminos[0].tipo, Camino.TipoDeCamino.CaminoTerreste)
        assertEquals(ubicacionNeo1.caminos[0].ubicacioDestino.nombre, ubicacion2.nombre)
    }

    @Test
    fun `se establecen 2 conexiones unidireccionales entre dos ubicaciones`() {
        val ubicacion1 = ubicacionService.crearUbicacion("ubicacion neo 1")
        val ubicacion2 = ubicacionService.crearUbicacion("ubicacion neo 2")

        ubicacionService.conectar(ubicacion1.nombre, ubicacion2.nombre, Camino.TipoDeCamino.CaminoTerreste)
        ubicacionService.conectar(ubicacion2.nombre, ubicacion1.nombre, Camino.TipoDeCamino.CaminoAereo)

        val ubicacionNeo1 = ubicacionNeoRepository.findByNombre(ubicacion1.nombre)
        val ubicacionNeo2 = ubicacionNeoRepository.findByNombre(ubicacion2.nombre)

        assertEquals(ubicacionNeo1.caminos[0].tipo, Camino.TipoDeCamino.CaminoTerreste)
        assertEquals(ubicacionNeo1.caminos[0].ubicacioDestino.nombre, ubicacion2.nombre)
        assertEquals(ubicacionNeo2.caminos[0].tipo, Camino.TipoDeCamino.CaminoAereo)
        assertEquals(ubicacionNeo2.caminos[0].ubicacioDestino.nombre, ubicacion1.nombre)
    }

    @Test
    fun `si intento conectar dos ubicaciones que no existen falla`() {

       assertThrows(DataNotFoundException::class.java)
            { ubicacionService.conectar("ubicacion inexistente 1",
                                        "ubicacion inexistente 2",
                                        Camino.TipoDeCamino.CaminoTerreste) }

    }

    @Test
    fun `si pido los caminos conectados a la Ubicacion con nombre Quilmes me los devuelve` () {
        ubicacionService.crearUbicacion("Bera")
        ubicacionService.crearUbicacion("Ubicacion2")
        ubicacionService.crearUbicacion("Ubicacion3")
        ubicacionService.crearUbicacion("Ubicacion4")
        ubicacionService.crearUbicacion("Ubicacion5")

        ubicacionService.conectar("Ubicacion2", "Bera", Camino.TipoDeCamino.CaminoTerreste)
        ubicacionService.conectar("Ubicacion2", "Ubicacion3", Camino.TipoDeCamino.CaminoAereo)

        ubicacionService.conectar("Ubicacion3", "Ubicacion4", Camino.TipoDeCamino.CaminoAereo)
        ubicacionService.conectar("Ubicacion4", "Ubicacion3", Camino.TipoDeCamino.CaminoAereo)
        ubicacionService.conectar("Bera", "Ubicacion3", Camino.TipoDeCamino.CaminoAereo)
        ubicacionService.conectar("Ubicacion4", "Ubicacion5", Camino.TipoDeCamino.CaminoAereo)

        val ubicacionesConectadas = ubicacionService.conectados("Ubicacion2")

        val ubicacion2 = ubicacionNeoRepository.findByNombre("Ubicacion2")

        assertEquals(ubicacion2.caminos.size, ubicacionesConectadas.size)

        val nombreUbicacionesConectadas = ubicacionesConectadas.map { u -> u.nombre}
        assertTrue(nombreUbicacionesConectadas.contains("Ubicacion3"))
        assertTrue(nombreUbicacionesConectadas.contains("Bera"))
    }

    @Test
    fun `si pido los caminos conectados a la Ubicacion que no existe devuelve una lista vacia` () {

        assertEquals(0, ubicacionService.conectados("ubicacion inexistente 1").size)
    }

    @Test
    fun `Mover mas corto test` () {
        ubicacionService.crearUbicacion("Bera")
        ubicacionService.crearUbicacion("Quilmes")
        ubicacionService.crearUbicacion("Varela")
        ubicacionService.crearUbicacion("Bernal")
        ubicacionService.crearUbicacion("Solano")

        ubicacionService.conectar("Quilmes", "Bera", Camino.TipoDeCamino.CaminoTerreste)
        ubicacionService.conectar("Quilmes", "Varela", Camino.TipoDeCamino.CaminoTerreste)

        ubicacionService.conectar("Varela", "Bernal", Camino.TipoDeCamino.CaminoTerreste)
        ubicacionService.conectar("Bernal", "Varela", Camino.TipoDeCamino.CaminoTerreste)
        ubicacionService.conectar("Bera", "Varela", Camino.TipoDeCamino.CaminoTerreste)
        ubicacionService.conectar("Bernal", "Solano", Camino.TipoDeCamino.CaminoTerreste)
        ubicacionService.conectar("Varela", "Solano", Camino.TipoDeCamino.CaminoTerreste)
        ubicacionService.conectar("Solano", "Quilmes", Camino.TipoDeCamino.CaminoTerreste)
        ubicacionService.conectar("Bera", "Quilmes", Camino.TipoDeCamino.CaminoTerreste)

        val ubicacionNeo = ubicacionNeoRepository.findByNombre("Quilmes")

    }



    @AfterEach
    fun tearDown() {
        dataService.eliminarTodo()
        //ubicacionNeoRepository.deleteAll()
    }

}