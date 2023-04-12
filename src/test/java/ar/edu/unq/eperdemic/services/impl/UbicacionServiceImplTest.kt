package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class UbicacionServiceImplTest {

    lateinit var ubicacionService: UbicacionServiceImpl
    lateinit var ubicacionDAO: HibernateUbicacionDAO

    @BeforeEach
    fun setUp() {
         ubicacionDAO = HibernateUbicacionDAO()
         ubicacionService = UbicacionServiceImpl(ubicacionDAO)
    }

    @Test
    fun mover() {
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