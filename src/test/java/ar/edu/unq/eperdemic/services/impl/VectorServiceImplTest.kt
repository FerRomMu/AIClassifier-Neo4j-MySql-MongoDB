package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.exceptions.IdNotFoundException
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.EspecieDAO
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateEspecieDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.services.UbicacionService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VectorServiceImplTest {
    lateinit var vectorService: VectorServiceImpl
    lateinit var ubicacionService: UbicacionServiceImpl

    lateinit var vectorDAO: HibernateVectorDAO
    lateinit var ubicacionDAO: HibernateUbicacionDAO
    lateinit var especieDAO: EspecieDAO

    lateinit var bernal: Ubicacion

    @BeforeEach
    fun setUp() {

        ubicacionDAO = HibernateUbicacionDAO()
        vectorDAO = HibernateVectorDAO()

        especieDAO = HibernateEspecieDAO()

        vectorService = VectorServiceImpl(vectorDAO,ubicacionDAO,especieDAO)
        ubicacionService = UbicacionServiceImpl(ubicacionDAO)

        bernal = ubicacionService.crearUbicacion("Bernal")
    }

    @Test
    fun contagiar() {
    }

    @Test
    fun infectar() {
    }

    @Test
    fun enfermedades() {
    }

    @Test
    fun `si creo un vector este recibe un id`() {

        val vector = vectorService.crearVector(TipoDeVector.Persona,bernal.id!!)
        assertNotNull(vector.id)

    }

    @Test
    fun `si trato de crear un vector con una ubicacion invalida falla`() {

        assertThrows(IdNotFoundException::class.java){ vectorService.crearVector(TipoDeVector.Persona,1554798541) }

    }

    @Test
    fun `si creo vector lo puedo recuperar`() {

        val vector = vectorService.crearVector(TipoDeVector.Persona,bernal.id!!)
        val vectorRecuperado = vectorService.recuperarVector(vector.id!!)

        assertEquals(vector.id, vectorRecuperado.id)
        assertEquals(vector.tipo, vectorRecuperado.tipo)
        assertEquals(vector.ubicacion.id, vectorRecuperado.ubicacion.id)

    }

    @Test
    fun borrarVector() {
    }

    @AfterEach
    fun tearDown() {
    }

}