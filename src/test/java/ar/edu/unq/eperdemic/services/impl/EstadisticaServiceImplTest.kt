package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.exceptions.DataNotFoundException
import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.services.*
import ar.edu.unq.eperdemic.utils.DataService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import javax.persistence.NoResultException

@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class EstadisticaServiceImplTest {

    @Autowired lateinit var dataService: DataService
    @Autowired lateinit var estadisticaService: EstadisticaService
    @Autowired lateinit var ubicacionService : UbicacionService
    @Autowired lateinit var vectorService : VectorService
    @Autowired lateinit var especieService: EspecieService

    @BeforeEach
    fun setUp() {

    }

    @Test
    fun especieLider() {
        val patogeno2 = Patogeno("tipo444444")
        val especie2 = patogeno2.crearEspecie("nombre444444", "lugar34444")
        dataService.persistir(listOf(especie2, patogeno2))

        val setInicial = dataService.crearSetDeDatosIniciales()
        val personas = setInicial
            .filterIsInstance<Vector>()
            .filter {
                it.tipo == TipoDeVector.Persona
            }
        personas.forEach{ it.agregarEspecie(especie2) }
        dataService.persistir(personas)

        val especieLider = estadisticaService.especieLider()

        assertEquals(especie2.id, especieLider.id)
        assertEquals(especie2.paisDeOrigen, especieLider.paisDeOrigen)
        assertEquals(especie2.nombre, especieLider.nombre)
    }

    @Test
    fun `si pido los lideres y hay menos de diez me da los que haya en orden de mas contagios`(){

        val patogeno2 = Patogeno("tipo444444")
        val especie2 = patogeno2.crearEspecie("nombre444444", "lugar34444")
        val especie3 = patogeno2.crearEspecie("nombre34243", "lugar34243")
        dataService.persistir(listOf(especie2, especie3, patogeno2))

        val setInicial = dataService.crearSetDeDatosIniciales()
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

        dataService.persistir(animalesYPersonas)

        val lideres = estadisticaService.lideres()

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

        dataService.persistir(especiesNoLideres + especiesQueSeranLideres + patogeno2)

        val setInicial = dataService.crearSetDeDatosIniciales()
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

        dataService.persistir(animalesYPersonas)

        val lideres = estadisticaService.lideres()

        assertEquals(10, lideres.size)
        for (i in lideres.indices) {
            assertEquals(especiesLideresOrdenadas[i].id, lideres[i].id)
            assertEquals(especiesLideresOrdenadas[i].nombre, lideres[i].nombre)
            assertEquals(especiesLideresOrdenadas[i].paisDeOrigen, lideres[i].paisDeOrigen)
            assertEquals(especiesLideresOrdenadas[i].patogeno.id, lideres[i].patogeno.id)
        }
    }

    @Test
    fun `si pido los lideres pero ninguna especie contagio tanto humanos como animales no recibo anda`(){

        dataService.crearSetDeDatosIniciales()
        val lideres = estadisticaService.lideres()

        assertEquals(0, lideres.size)
    }

    @Test
    fun `no hay especieLider`(){

        val vectorHumano1 = Vector(TipoDeVector.Persona)
        dataService.persistir(vectorHumano1)

        assertThrows(DataNotFoundException::class.java) {
            estadisticaService.especieLider()
        }

    }

    @Test
    fun `devolver el reporte de contagios de una ubicacion`() {
        val ubicacion1 = Ubicacion("ubicacion 1")
        val patogeno = Patogeno("patogenoSSS")

        val vector1 = Vector(TipoDeVector.Insecto) ; vector1.ubicacion = ubicacion1
        val vector2 = Vector(TipoDeVector.Insecto) ; vector2.ubicacion = ubicacion1
        val vector3 = Vector(TipoDeVector.Insecto) ; vector3.ubicacion = ubicacion1
        val vector4 = Vector(TipoDeVector.Insecto) ; vector4.ubicacion = ubicacion1
        val vector5 = Vector(TipoDeVector.Insecto) ; vector5.ubicacion = ubicacion1

        val especie1 = Especie(patogeno, "especie111", "arg")
        val especie2 = Especie(patogeno, "especie222", "arg")
        val especie3 = Especie(patogeno, "especie333", "arg")

        vector1.agregarEspecie(especie1) ; vector2.agregarEspecie(especie1); vector3.agregarEspecie(especie1)
        vector4.agregarEspecie(especie2)

        dataService.persistir(listOf(ubicacion1, patogeno, especie1, especie2, especie3, vector1, vector2, vector3, vector4, vector5))

        val reporte = estadisticaService.reporteDeContagios(ubicacion1.nombre)

        assertEquals(5, reporte.vectoresPresentes)
        assertEquals(4, reporte.vectoresInfecatods)
        assertEquals("especie111", reporte.nombreDeEspecieMasInfecciosa)
    }

    @Test
    fun `devolver el reporte de contagios de una ubicacion que no esta`() {
        val ubicacion1 = Ubicacion("ubicacion 1")
        val patogeno = Patogeno("patogenoSSS")

        val vector1 = Vector(TipoDeVector.Insecto) ; vector1.ubicacion = ubicacion1
        val vector2 = Vector(TipoDeVector.Insecto) ; vector2.ubicacion = ubicacion1
        val vector3 = Vector(TipoDeVector.Insecto) ; vector3.ubicacion = ubicacion1
        val vector4 = Vector(TipoDeVector.Insecto) ; vector4.ubicacion = ubicacion1
        val vector5 = Vector(TipoDeVector.Insecto) ; vector5.ubicacion = ubicacion1

        val especie1 = Especie(patogeno, "especie111", "arg")
        val especie2 = Especie(patogeno, "especie222", "arg")
        val especie3 = Especie(patogeno, "especie333", "arg")

        vector1.agregarEspecie(especie1) ; vector2.agregarEspecie(especie1); vector3.agregarEspecie(especie1)
        vector4.agregarEspecie(especie2)

        dataService.persistir(listOf(ubicacion1, patogeno, especie1, especie2, especie3, vector1, vector2, vector3, vector4, vector5))

        assertThrows(DataNotFoundException::class.java) { estadisticaService.reporteDeContagios("ubicacion 2") }
    }

    @AfterEach
    fun tearDown() {
       dataService.eliminarTodo()
    }

}