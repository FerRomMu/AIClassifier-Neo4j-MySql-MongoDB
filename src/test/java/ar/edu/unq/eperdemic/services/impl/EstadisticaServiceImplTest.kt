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

    @AfterEach
    fun tearDown() {
       //dataService.eliminarTodo()
    }

}