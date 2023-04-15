package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class UbicacionServiceImplTest {

    lateinit var vectorService: VectorServiceImpl
    lateinit var vectorDAO: HibernateVectorDAO

    lateinit var ubicacionService: UbicacionServiceImpl
    lateinit var ubicacionDAO: HibernateUbicacionDAO
    lateinit var dado: Randomizador

    @BeforeEach
    fun setUp() {
         ubicacionDAO = HibernateUbicacionDAO()
         ubicacionService = UbicacionServiceImpl(ubicacionDAO)

         vectorDAO = HibernateVectorDAO()
         vectorService = VectorServiceImpl(vectorDAO,ubicacionDAO)

         dado.getInstance().estado = EstadoRandomizadorDeterminístico()
    }

    @Test
    fun  `mover vector a una ubicación con un humano y un animal`() {
        var cordoba = ubicacionService.crearUbicacion("Cordoba")
        var chaco = ubicacionService.crearUbicacion("Chaco")

        var vectorAMover = vectorService.crearVector(TipoDeVector.Persona,cordoba.id!!)

        var vectorVictima1 = vectorService.crearVector(TipoDeVector.Persona,chaco.id!!)
        var vectorVictima2 = vectorService.crearVector(TipoDeVector.Animal,chaco.id!!)

        var patogeno = Patogeno("Patogeni_SS")
        var especieAContagiar = Especie(patogeno,"Especie_Sl","Honduras")

        vectorService.infectar(vectorAMover,especieAContagiar)

        assertEquals(vectorAMover.especiesContagiadas.size,1)
        assertEquals(vectorVictima1.especiesContagiadas.size,0)
        assertEquals(vectorVictima2.especiesContagiadas.size,0)

        ubicacionService.mover(vectorAMover.id!!,chaco.id!!)

        assertEquals(vectorAMover.especiesContagiadas.size,1)
        assertEquals(vectorVictima1.especiesContagiadas.size,1)
        assertEquals(vectorVictima2.especiesContagiadas.size,0)

    }

    @Test
    fun expandir() {
    }

    @Test
    fun crearUbicacion() {
    }

    @Test
    fun recuperarTodos() {
        var Cordoba = ubicacionService.crearUbicacion("Cordoba")
        var chaco = ubicacionService.crearUbicacion("Chaco")


        assertEquals(ubicacionService.recuperarTodos().size,2)
    }

    @AfterEach
    fun tearDown() {
    }

}