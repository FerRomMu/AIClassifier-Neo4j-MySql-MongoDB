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
        ubicacionService.crearUbicacion("ubicacionTest")

        val patogenoDeLaEspecie1 = Patogeno("Gripe")
        dataService.persistir(patogenoDeLaEspecie1)

        var especie1 = patogenoDeLaEspecie1.crearEspecie("Especie1","Rusia")
        val especie2 = patogenoDeLaEspecie1.crearEspecie("Especie2","Rusia")

        val vectorHumano1 = Vector(TipoDeVector.Persona)
        val vectorHumano2 = Vector(TipoDeVector.Persona)
        val vectorHumano3 = Vector(TipoDeVector.Persona)
        val vectorAnimal = Vector(TipoDeVector.Animal)
        val vectorInsecto = Vector(TipoDeVector.Insecto)

        vectorService.infectar(vectorHumano1,especie1)
        vectorService.infectar(vectorHumano2,especie1)

        vectorService.infectar(vectorHumano3,especie2)
        vectorService.infectar(vectorAnimal,especie2)
        vectorService.infectar(vectorInsecto,especie2)

        especie1 = especieService.recuperarEspecie(especie1.id!!)

        assertEquals(especie1.id,estadisticaService.especieLider().id)
    }

    @Test
    fun `si devuelvo los lideres obtengo las 10 especies con mayores infectados en orden descendente`(){
        val patogenoDeLaEspecie1 = Patogeno("Gripe")
        dataService.persistir(patogenoDeLaEspecie1)

        val especie1 = patogenoDeLaEspecie1.crearEspecie("Especie1","Rusia")
        val especie2 = patogenoDeLaEspecie1.crearEspecie("Especie2","Rusia")
        val especie3 = patogenoDeLaEspecie1.crearEspecie("Especie3","Rusia")
        val especie4 = patogenoDeLaEspecie1.crearEspecie("Especie4","Rusia")
        val especie5 = patogenoDeLaEspecie1.crearEspecie("Especie5","Rusia")
        val especie6 = patogenoDeLaEspecie1.crearEspecie("Especie6","Rusia")
        val especie7 = patogenoDeLaEspecie1.crearEspecie("Especie7","Rusia")
        val especie8 = patogenoDeLaEspecie1.crearEspecie("Especie8","Rusia")
        val especie9 = patogenoDeLaEspecie1.crearEspecie("Especie9","Rusia")
        val especie10 = patogenoDeLaEspecie1.crearEspecie("Especie10","Rusia")
        patogenoDeLaEspecie1.crearEspecie("Especie11","Rusia")
        patogenoDeLaEspecie1.crearEspecie("Especie12","Rusia")

        val vectorHumano1 = Vector(TipoDeVector.Persona)
        val vectorHumano2 = Vector(TipoDeVector.Persona)
        val vectorHumano3 = Vector(TipoDeVector.Persona)
        val vectorHumano4 = Vector(TipoDeVector.Persona)
        val vectorHumano5 = Vector(TipoDeVector.Persona)
        val vectorHumano6 = Vector(TipoDeVector.Persona)
        val vectorHumano7 = Vector(TipoDeVector.Persona)
        val vectorHumano8 = Vector(TipoDeVector.Persona)
        val vectorHumano9 = Vector(TipoDeVector.Persona)

        val vectorAnimal1 = Vector(TipoDeVector.Animal)
        val vectorAnimal2 = Vector(TipoDeVector.Animal)
        val vectorAnimal3 = Vector(TipoDeVector.Animal)
        val vectorAnimal4 = Vector(TipoDeVector.Animal)

        val vectorInsecto1 = Vector(TipoDeVector.Insecto)
        val vectorInsecto2 = Vector(TipoDeVector.Insecto)

        vectorService.infectar(vectorHumano1,especie9)
        vectorService.infectar(vectorHumano2,especie9)
        vectorService.infectar(vectorHumano3,especie9)
        vectorService.infectar(vectorHumano4,especie9)
        vectorService.infectar(vectorHumano5,especie9)
        vectorService.infectar(vectorHumano6,especie9)
        vectorService.infectar(vectorHumano7,especie9)
        vectorService.infectar(vectorHumano8,especie9)
        vectorService.infectar(vectorHumano9,especie9)

        vectorService.infectar(vectorHumano3,especie2)
        vectorService.infectar(vectorAnimal1,especie2)

        vectorService.infectar(vectorHumano4,especie3)
        vectorService.infectar(vectorHumano5,especie3)
        vectorService.infectar(vectorHumano6,especie3)

        vectorService.infectar(vectorHumano5,especie4)
        vectorService.infectar(vectorHumano4,especie4)
        vectorService.infectar(vectorHumano3,especie4)
        vectorService.infectar(vectorHumano2,especie4)

        vectorService.infectar(vectorAnimal2,especie5)
        vectorService.infectar(vectorAnimal1,especie5)
        vectorService.infectar(vectorAnimal3,especie5)
        vectorService.infectar(vectorAnimal4,especie5)
        vectorService.infectar(vectorHumano7,especie5)

        vectorService.infectar(vectorHumano9,especie7)
        vectorService.infectar(vectorHumano1,especie7)
        vectorService.infectar(vectorHumano2,especie7)
        vectorService.infectar(vectorHumano3,especie7)
        vectorService.infectar(vectorHumano5,especie7)
        vectorService.infectar(vectorAnimal2,especie7)
        vectorService.infectar(vectorAnimal4,especie7)

        vectorService.infectar(vectorAnimal4,especie6)
        vectorService.infectar(vectorAnimal3,especie6)
        vectorService.infectar(vectorAnimal2,especie6)
        vectorService.infectar(vectorAnimal1,especie6)
        vectorService.infectar(vectorHumano5,especie6)
        vectorService.infectar(vectorHumano6,especie6)

        vectorService.infectar(vectorHumano1,especie8)
        vectorService.infectar(vectorHumano2,especie8)
        vectorService.infectar(vectorHumano3,especie8)
        vectorService.infectar(vectorHumano4,especie8)
        vectorService.infectar(vectorHumano5,especie8)
        vectorService.infectar(vectorHumano6,especie8)
        vectorService.infectar(vectorHumano7,especie8)
        vectorService.infectar(vectorHumano8,especie8)

        vectorService.infectar(vectorHumano9,especie1)

        vectorService.infectar(vectorAnimal3,especie10)
        vectorService.infectar(vectorAnimal2,especie10)
        vectorService.infectar(vectorAnimal1,especie10)
        vectorService.infectar(vectorHumano4,especie10)
        vectorService.infectar(vectorHumano2,especie10)
        vectorService.infectar(vectorHumano1,especie10)
        vectorService.infectar(vectorHumano3,especie10)
        vectorService.infectar(vectorHumano8,especie10)
        vectorService.infectar(vectorHumano5,especie10)
        vectorService.infectar(vectorHumano6,especie10)


        vectorService.infectar(vectorHumano6,especie1)

        vectorService.infectar(vectorInsecto1,especie2)
        vectorService.infectar(vectorInsecto2,especie2)

        assertEquals(10,estadisticaService.lideres().size)

        val especiesLideres = estadisticaService.lideres()

        assertEquals(especie10.id, especiesLideres[0].id)
        assertEquals(especie10.patogeno.id, especiesLideres[0].patogeno.id)
        assertEquals(especie10.nombre, especiesLideres[0].nombre)
        assertEquals(especie10.paisDeOrigen, especiesLideres[0].paisDeOrigen)

        assertEquals(especie9.id, especiesLideres[1].id)
        assertEquals(especie9.patogeno.id, especiesLideres[1].patogeno.id)
        assertEquals(especie9.nombre, especiesLideres[1].nombre)
        assertEquals(especie9.paisDeOrigen, especiesLideres[1].paisDeOrigen)

        assertEquals(especie8.id, especiesLideres[2].id)
        assertEquals(especie8.patogeno.id, especiesLideres[2].patogeno.id)
        assertEquals(especie8.nombre, especiesLideres[2].nombre)
        assertEquals(especie8.paisDeOrigen, especiesLideres[2].paisDeOrigen)

        assertEquals(especie7.id, especiesLideres[3].id)
        assertEquals(especie7.patogeno.id, especiesLideres[3].patogeno.id)
        assertEquals(especie7.nombre, especiesLideres[3].nombre)
        assertEquals(especie7.paisDeOrigen, especiesLideres[3].paisDeOrigen)

        assertEquals(especie6.id, especiesLideres[4].id)
        assertEquals(especie6.patogeno.id, especiesLideres[4].patogeno.id)
        assertEquals(especie6.nombre, especiesLideres[4].nombre)
        assertEquals(especie6.paisDeOrigen, especiesLideres[4].paisDeOrigen)

        assertEquals(especie5.id, especiesLideres[5].id)
        assertEquals(especie5.patogeno.id, especiesLideres[5].patogeno.id)
        assertEquals(especie5.nombre, especiesLideres[5].nombre)
        assertEquals(especie5.paisDeOrigen, especiesLideres[5].paisDeOrigen)

        assertEquals(especie4.id, especiesLideres[6].id)
        assertEquals(especie4.patogeno.id, especiesLideres[6].patogeno.id)
        assertEquals(especie4.nombre, especiesLideres[6].nombre)
        assertEquals(especie4.paisDeOrigen, especiesLideres[6].paisDeOrigen)

        assertEquals(especie3.id, especiesLideres[7].id)
        assertEquals(especie3.patogeno.id, especiesLideres[7].patogeno.id)
        assertEquals(especie3.nombre, especiesLideres[7].nombre)
        assertEquals(especie3.paisDeOrigen, especiesLideres[7].paisDeOrigen)

        assertEquals(especie2.id, especiesLideres[8].id)
        assertEquals(especie2.patogeno.id, especiesLideres[8].patogeno.id)
        assertEquals(especie2.nombre, especiesLideres[8].nombre)
        assertEquals(especie2.paisDeOrigen, especiesLideres[8].paisDeOrigen)

        assertEquals(especie1.id, especiesLideres[9].id)
        assertEquals(especie1.patogeno.id, especiesLideres[9].patogeno.id)
        assertEquals(especie1.nombre, especiesLideres[9].nombre)
        assertEquals(especie1.paisDeOrigen, especiesLideres[9].paisDeOrigen)
    }

    @Test
    fun `si devuelvo los lideres y no hay 10 especies obtengo las especies con mayores infectados en orden descendente que haya`(){
        val patogenoDeLaEspecie1 = Patogeno("Gripe")
        dataService.persistir(patogenoDeLaEspecie1)

        val especie1 = patogenoDeLaEspecie1.crearEspecie("Especie1","Rusia")
        val especie2 = patogenoDeLaEspecie1.crearEspecie("Especie2","Rusia")
        val especie3 = patogenoDeLaEspecie1.crearEspecie("Especie3","Rusia")
        val especie4 = patogenoDeLaEspecie1.crearEspecie("Especie4","Rusia")

        val vectorHumano1 = Vector(TipoDeVector.Persona)
        val vectorHumano2 = Vector(TipoDeVector.Persona)

        val vectorAnimal1 = Vector(TipoDeVector.Animal)
        val vectorAnimal2 = Vector(TipoDeVector.Animal)

        val vectorInsecto1 = Vector(TipoDeVector.Insecto)

        vectorService.infectar(vectorHumano1,especie1)

        vectorService.infectar(vectorHumano1,especie2)
        vectorService.infectar(vectorAnimal1,especie2)

        vectorService.infectar(vectorHumano1,especie3)
        vectorService.infectar(vectorHumano2,especie3)
        vectorService.infectar(vectorAnimal1,especie3)

        vectorService.infectar(vectorHumano1,especie4)
        vectorService.infectar(vectorHumano2,especie4)
        vectorService.infectar(vectorAnimal1,especie4)
        vectorService.infectar(vectorAnimal2,especie4)

        vectorService.infectar(vectorInsecto1,especie2)

        assertEquals(4,estadisticaService.lideres().size)

        val especiesLideres = estadisticaService.lideres()

        assertEquals(especie4.id, especiesLideres[0].id)
        assertEquals(especie4.patogeno.id, especiesLideres[0].patogeno.id)
        assertEquals(especie4.nombre, especiesLideres[0].nombre)
        assertEquals(especie4.paisDeOrigen, especiesLideres[0].paisDeOrigen)

        assertEquals(especie3.id, especiesLideres[1].id)
        assertEquals(especie3.patogeno.id, especiesLideres[1].patogeno.id)
        assertEquals(especie3.nombre, especiesLideres[1].nombre)
        assertEquals(especie3.paisDeOrigen, especiesLideres[1].paisDeOrigen)

        assertEquals(especie2.id, especiesLideres[2].id)
        assertEquals(especie2.patogeno.id, especiesLideres[2].patogeno.id)
        assertEquals(especie2.nombre, especiesLideres[2].nombre)
        assertEquals(especie2.paisDeOrigen, especiesLideres[2].paisDeOrigen)

        assertEquals(especie1.id, especiesLideres[3].id)
        assertEquals(especie1.patogeno.id, especiesLideres[3].patogeno.id)
        assertEquals(especie1.nombre, especiesLideres[3].nombre)
        assertEquals(especie1.paisDeOrigen, especiesLideres[3].paisDeOrigen)

        assertEquals(4, especiesLideres.size)
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