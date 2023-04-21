package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.*
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.*
import ar.edu.unq.eperdemic.services.EstadisticaService
import ar.edu.unq.eperdemic.services.UbicacionService
import ar.edu.unq.eperdemic.services.VectorService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx
import ar.edu.unq.eperdemic.utils.impl.DataServiceImpl
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance
import javax.persistence.NoResultException

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class EstadisticaServiceImplTest {

    lateinit var estadisticaDAO: EstadisticaDAO
    lateinit var patogenoDAO : PatogenoDAO
    lateinit var ubicacionDAO: UbicacionDAO
    lateinit var vectorDAO: VectorDAO
    lateinit var especieDAO: EspecieDAO
    lateinit var dataService: DataServiceImpl


    lateinit var estadisticaService: EstadisticaService
    lateinit var ubicacionService : UbicacionService
    lateinit var vectorService : VectorService
    lateinit var patogenoService : PatogenoServiceImpl
    lateinit var especieService: EspecieServiceImpl

    @BeforeEach
    fun setUp() {
        estadisticaDAO = HibernateEstadisticaDAO()
        patogenoDAO = HibernatePatogenoDAO()
        ubicacionDAO = HibernateUbicacionDAO()
        vectorDAO = HibernateVectorDAO()
        especieDAO = HibernateEspecieDAO()

        vectorService = VectorServiceImpl(vectorDAO,ubicacionDAO,especieDAO)
        estadisticaService = EstadisticaServiceImpl(estadisticaDAO)
        ubicacionService = UbicacionServiceImpl(ubicacionDAO)
        patogenoService = PatogenoServiceImpl(patogenoDAO,especieDAO,vectorDAO)
        dataService = DataServiceImpl()
        especieService = EspecieServiceImpl(especieDAO)

    }

    @Test
    fun especieLider() {

        ubicacionService.crearUbicacion("ubicacionTest")

        var patogenoDeLaEspecie1 = Patogeno("Gripe")
        dataService.persistir(patogenoDeLaEspecie1)

        var especie1 = patogenoDeLaEspecie1.crearEspecie("Especie1","Rusia")
        var especie2 = patogenoDeLaEspecie1.crearEspecie("Especie2","Rusia")

        var vectorHumano1 = Vector(TipoDeVector.Persona)
        var vectorHumano2 = Vector(TipoDeVector.Persona)
        var vectorHumano3 = Vector(TipoDeVector.Persona)
        var vectorAnimal = Vector(TipoDeVector.Animal)
        var vectorInsecto = Vector(TipoDeVector.Insecto)

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

        val especiesLideresIds = estadisticaService.lideres()

        assertEquals(especie10.id, especiesLideresIds[0].id)
        assertEquals(especie9.id, especiesLideresIds[1].id)
        assertEquals(especie8.id, especiesLideresIds[2].id)
        assertEquals(especie7.id, especiesLideresIds[3].id)
        assertEquals(especie6.id, especiesLideresIds[4].id)
        assertEquals(especie5.id, especiesLideresIds[5].id)
        assertEquals(especie4.id, especiesLideresIds[6].id)
        assertEquals(especie3.id, especiesLideresIds[7].id)
        assertEquals(especie2.id, especiesLideresIds[8].id)
        assertEquals(especie1.id, especiesLideresIds[9].id)
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

        val especiesLideresIds = estadisticaService.lideres()

        assertEquals(especie4.id, especiesLideresIds[0].id)
        assertEquals(especie3.id, especiesLideresIds[1].id)
        assertEquals(especie2.id, especiesLideresIds[2].id)
        assertEquals(especie1.id, especiesLideresIds[3].id)

        assertEquals(4, especiesLideresIds.size)
    }

    @Test
    fun `no hay especieLider`(){

        val vectorHumano1 = Vector(TipoDeVector.Persona)
        dataService.persistir(vectorHumano1)

        assertThrows(NoResultException::class.java) {
            estadisticaService.especieLider()
        }

    }

    @AfterEach
    fun tearDown() {
       dataService.eliminarTodo()
    }

}