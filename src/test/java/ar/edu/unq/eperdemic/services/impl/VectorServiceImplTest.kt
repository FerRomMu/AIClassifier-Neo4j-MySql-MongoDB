package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class VectorServiceImplTest {
    lateinit var vector: Vector
    lateinit var vectorService: VectorServiceImpl
    lateinit var  vectorDAO: HibernateVectorDAO
    lateinit var ubicacionDAO: HibernateUbicacionDAO

    @BeforeEach
    fun setUp() {

        ubicacionDAO = HibernateUbicacionDAO()
        vectorDAO = HibernateVectorDAO()

        vectorService = VectorServiceImpl(vectorDAO,ubicacionDAO)


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
    fun `crearVector que no existe`() {
        var ubicacion = Ubicacion("Quilmes",null)
        ubicacionDAO.guardar(ubicacion)

        vectorService.crearVector(TipoDeVector.Persona,ubicacion.id!!)
    }

    @Test
    fun recuperarVector() {
    }

    @Test
    fun borrarVector() {
    }

    @AfterEach
    fun tearDown() {
    }

}