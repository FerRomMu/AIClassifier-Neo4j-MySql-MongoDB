package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.*
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.*
import ar.edu.unq.eperdemic.services.EstadisticaService
import ar.edu.unq.eperdemic.services.UbicacionService
import ar.edu.unq.eperdemic.services.VectorService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx
import ar.edu.unq.eperdemic.utils.impl.DataServiceImpl
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

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

    }

    @Test
    fun especieLider() {

        val ubicacion = ubicacionService.crearUbicacion("ubicacionTest")

        var patogenoDeLaEspecie1 = Patogeno("Gripe")
        runTrx { patogenoDAO.guardar(patogenoDeLaEspecie1)}

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

        especie1 = runTrx {especieDAO.recuperar(especie1.id!!) }

        assertEquals(especie1.id,estadisticaService.especieLider().id)
    }

    @Test
    fun lideres(){
        var patogenoDeLaEspecie1 = Patogeno("Gripe")
        runTrx { patogenoDAO.guardar(patogenoDeLaEspecie1)}

        var especie1 = patogenoDeLaEspecie1.crearEspecie("Especie1","Rusia")
        var especie2 = patogenoDeLaEspecie1.crearEspecie("Especie2","Rusia")
        var especie3 = patogenoDeLaEspecie1.crearEspecie("Especie3","Rusia")
        var especie4 = patogenoDeLaEspecie1.crearEspecie("Especie4","Rusia")
        var especie5 = patogenoDeLaEspecie1.crearEspecie("Especie5","Rusia")
        var especie6 = patogenoDeLaEspecie1.crearEspecie("Especie6","Rusia")
        var especie7 = patogenoDeLaEspecie1.crearEspecie("Especie7","Rusia")
        var especie8 = patogenoDeLaEspecie1.crearEspecie("Especie8","Rusia")
        var especie9 = patogenoDeLaEspecie1.crearEspecie("Especie9","Rusia")
        var especie10 = patogenoDeLaEspecie1.crearEspecie("Especie10","Rusia")

        var vectorHumano1 = Vector(TipoDeVector.Persona)
        var vectorHumano2 = Vector(TipoDeVector.Persona)
        var vectorHumano3 = Vector(TipoDeVector.Persona)
        var vectorHumano4 = Vector(TipoDeVector.Persona)
        var vectorHumano5 = Vector(TipoDeVector.Persona)
        var vectorHumano6 = Vector(TipoDeVector.Persona)
        var vectorHumano7 = Vector(TipoDeVector.Persona)
        var vectorHumano8 = Vector(TipoDeVector.Persona)
        var vectorHumano9 = Vector(TipoDeVector.Persona)

        var vectorAnimal1 = Vector(TipoDeVector.Animal)
        var vectorAnimal2 = Vector(TipoDeVector.Animal)
        var vectorAnimal3 = Vector(TipoDeVector.Animal)
        var vectorAnimal4 = Vector(TipoDeVector.Animal)

        var vectorInsecto = Vector(TipoDeVector.Insecto)

        vectorService.infectar(vectorHumano1,especie1)
        vectorService.infectar(vectorHumano2,especie1)

        vectorService.infectar(vectorHumano3,especie2)
        vectorService.infectar(vectorAnimal1,especie2)
        vectorService.infectar(vectorInsecto,especie2)

        vectorService.infectar(vectorHumano4,especie3)

        vectorService.infectar(vectorHumano5,especie4)

        vectorService.infectar(vectorHumano6,especie5)

        vectorService.infectar(vectorAnimal2,especie6)

        vectorService.infectar(vectorHumano7,especie7)
        vectorService.infectar(vectorAnimal4,especie7)

        vectorService.infectar(vectorHumano8,especie8)

        vectorService.infectar(vectorHumano9,especie9)

        vectorService.infectar(vectorAnimal3,especie10)


        assertEquals(10,estadisticaService.lideres().size)

        val especiesLideresIds = estadisticaService.lideres().map { especie -> especie.id }

        assertTrue(especiesLideresIds.contains(especie1.id))
        assertTrue(especiesLideresIds.contains(especie2.id))
        assertTrue(especiesLideresIds.contains(especie3.id))
        assertTrue(especiesLideresIds.contains(especie4.id))
        assertTrue(especiesLideresIds.contains(especie5.id))
        assertTrue(especiesLideresIds.contains(especie6.id))
        assertTrue(especiesLideresIds.contains(especie7.id))
        assertTrue(especiesLideresIds.contains(especie8.id))
        assertTrue(especiesLideresIds.contains(especie9.id))
        assertTrue(especiesLideresIds.contains(especie10.id))


    }

    @AfterEach
    fun tearDown() {
       dataService.eliminarTodo()
    }

}