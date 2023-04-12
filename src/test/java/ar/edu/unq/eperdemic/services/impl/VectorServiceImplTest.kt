package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.exceptions.IdNotFoundException
import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.services.UbicacionService
import ar.edu.unq.eperdemic.utils.impl.DataServiceImpl
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
    lateinit var bernal: Ubicacion
    lateinit var dataService: DataServiceImpl

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

        var vectorAInfectar = Vector(TipoDeVector.Persona)

        var patogenoDeLaEspecie = Patogeno("Gripe")
        var especieAContagiar = Especie(patogenoDeLaEspecie,"Especie_AR2T","Francia")

        assertEquals(vectorAInfectar.especiesContagiadas.size,0)

        vectorService.infectar(vectorAInfectar,especieAContagiar)

        assertEquals(vectorAInfectar.especiesContagiadas.size,1)

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
    fun `si borro un vector y lo quiero recuperar falla`() {

        val vector = vectorService.crearVector(TipoDeVector.Persona,bernal.id!!)
        val vectorRecuperado = vectorService.recuperarVector(vector.id!!)

        assertEquals(vector.id, vectorRecuperado.id)

        vectorService.borrarVector(vector.id!!)

        assertThrows(IdNotFoundException::class.java) { vectorService.recuperarVector(vector.id!!) }
    }

    @Test
    fun `si trato de borrar un vector con id invalida falla`() {

        assertThrows(IdNotFoundException::class.java) {  vectorService.borrarVector(123241) }
    }

    @AfterEach
    fun tearDown() {
        dataService = DataServiceImpl()
        dataService.eliminarTodo()
    }

}