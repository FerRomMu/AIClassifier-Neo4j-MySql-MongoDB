package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.services.UbicacionService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class VectorServiceImplTest {
    lateinit var vectorService: VectorServiceImpl
    lateinit var ubicacionService: UbicacionServiceImpl
    lateinit var vectorDAO: HibernateVectorDAO
    lateinit var ubicacionDAO: HibernateUbicacionDAO
    lateinit var bernal: Ubicacion

    @BeforeEach
    fun setUp() {

        ubicacionDAO = HibernateUbicacionDAO()
        vectorDAO = HibernateVectorDAO()

        vectorService = VectorServiceImpl(vectorDAO,ubicacionDAO)
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

        val fercho = vectorService.crearVector(TipoDeVector.Persona,bernal.id!!)
        val gripe = Patogeno("Gripe")

        val sars = Especie(gripe,"Sars","Argentina")
        val sars_RT = Especie(gripe,"Sars_RT","Argentina")
        val sars_D = Especie(gripe,"Sars_D","Argentina")

        fercho.agregarEspecie(sars)
        fercho.agregarEspecie(sars_RT)
        fercho.agregarEspecie(sars_D)

        assertEquals(vectorService.enfermedades(fercho.id!!).size,3)

    }

    @Test
    fun `crearVector que no existe y lo recupera de la BD`() {
        val fercho = vectorService.crearVector(TipoDeVector.Persona,bernal.id!!)
        val ferchoRecuperado = vectorService.recuperarVector(fercho.id!!)
        assertEquals(fercho.id, ferchoRecuperado.id)
    }

    @Test
    fun borrarVector() {
    }

    @AfterEach
    fun tearDown() {
    }

}