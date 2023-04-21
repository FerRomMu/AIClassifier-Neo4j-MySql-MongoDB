package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.persistencia.dao.EspecieDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateEspecieDAO
import ar.edu.unq.eperdemic.exceptions.DataDuplicationException
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernatePatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx
import ar.edu.unq.eperdemic.utils.DataService
import ar.edu.unq.eperdemic.utils.impl.DataServiceImpl
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class UbicacionServiceImplTest {

    lateinit var vectorService: VectorServiceImpl
    lateinit var vectorDAO: HibernateVectorDAO
    lateinit var patogenoDAO: PatogenoDAO
    lateinit var especieDAO: EspecieDAO

    lateinit var ubicacionService: UbicacionServiceImpl
    lateinit var ubicacionDAO: HibernateUbicacionDAO
    lateinit var dado: Randomizador
    lateinit var dataService: DataService

    @BeforeEach
    fun setUp() {
        ubicacionDAO = HibernateUbicacionDAO()
        ubicacionService = UbicacionServiceImpl(ubicacionDAO)
        dataService = DataServiceImpl()
         ubicacionDAO = HibernateUbicacionDAO()
         ubicacionService = UbicacionServiceImpl(ubicacionDAO)
        patogenoDAO = HibernatePatogenoDAO()

        especieDAO = HibernateEspecieDAO()

         vectorDAO = HibernateVectorDAO()
         vectorService = VectorServiceImpl(vectorDAO,ubicacionDAO,especieDAO)

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
        runTrx {patogenoDAO.guardar(patogeno)}


        var especieAContagiar = patogeno.crearEspecie("Especie_Sl","Honduras")

        vectorService.infectar(vectorAMover,especieAContagiar)

        assertEquals(vectorAMover.especiesContagiadas.size,1)
        assertEquals(vectorVictima1.especiesContagiadas.size,0)
        assertEquals(vectorVictima2.especiesContagiadas.size,0)

        var vectoresEnChaco = runTrx { ubicacionDAO.vectoresEn(chaco.id!!) }

        assertEquals(vectoresEnChaco.size,2)

        ubicacionService.mover(vectorAMover.id!!,chaco.id!!)

        vectoresEnChaco = runTrx { ubicacionDAO.vectoresEn(chaco.id!!) }
        assertEquals(vectoresEnChaco.size,3)

        vectorAMover = vectorService.recuperarVector(vectorAMover.id!!)
        vectorVictima1 = vectorService.recuperarVector(vectorVictima1.id!!)
        vectorVictima2 =vectorService.recuperarVector(vectorVictima2.id!!)


        assertEquals(vectorAMover.especiesContagiadas.size,1)
        assertEquals(vectorVictima1.especiesContagiadas.size,1)
        assertEquals(vectorVictima2.especiesContagiadas.size,0)
    }

    @Test
    fun `Expandir en una ubicacion`() {
        var cordoba = ubicacionService.crearUbicacion("Cordoba")


        var vectorLocal = vectorService.crearVector(TipoDeVector.Persona,cordoba.id!!)
        var vectorLocal2 = vectorService.crearVector(TipoDeVector.Animal,cordoba.id!!)
        var vectorAExpandir = vectorService.crearVector(TipoDeVector.Persona,cordoba.id!!)


        var patogeno = Patogeno("Patogeni_SS")
        patogeno.setCapacidadDeContagioHumano(100)
        runTrx {patogenoDAO.guardar(patogeno)}


        var especieAContagiar = Especie(patogeno,"Especie_Sl","Honduras")

        vectorService.infectar(vectorAExpandir,especieAContagiar)

        assertEquals(vectorAExpandir.especiesContagiadas.size,1)
        assertEquals(vectorLocal.especiesContagiadas.size,0)
        assertEquals(vectorLocal2.especiesContagiadas.size,0)

        ubicacionService.expandir(cordoba.id!!)

        vectorAExpandir = vectorService.recuperarVector(vectorAExpandir.id!!)
        vectorLocal = vectorService.recuperarVector(vectorLocal.id!!)
        vectorLocal2 =vectorService.recuperarVector(vectorLocal2.id!!)

        assertEquals(vectorAExpandir.especiesContagiadas.size,1)
        assertEquals(vectorLocal.especiesContagiadas.size,1)
        assertEquals(vectorLocal2.especiesContagiadas.size,0)
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
        dataService.crearSetDeDatosIniciales()
        val ubicaciones = ubicacionService.recuperarTodos()

        assertEquals(21, ubicaciones.size)
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