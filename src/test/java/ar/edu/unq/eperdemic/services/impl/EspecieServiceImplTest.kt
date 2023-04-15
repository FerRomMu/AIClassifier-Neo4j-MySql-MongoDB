package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.EspecieDAO
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateEspecieDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernatePatogenoDAO
import ar.edu.unq.eperdemic.services.EspecieService
import ar.edu.unq.eperdemic.services.PatogenoService
import ar.edu.unq.eperdemic.utils.DataService
import ar.edu.unq.eperdemic.utils.impl.DataServiceImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EspecieServiceImplTest {

    lateinit var especieDAO: EspecieDAO
    lateinit var patogenoDAO: PatogenoDAO
    lateinit var especieService: EspecieService
    lateinit var patogenoService: PatogenoService
    lateinit var especie: Especie
    lateinit var patogeno: Patogeno
    lateinit var dataService : DataService

    @BeforeEach
    fun setup() {
        especieDAO = HibernateEspecieDAO()
        patogenoDAO = HibernatePatogenoDAO()
        especieService = EspecieServiceImpl(especieDAO)
        patogenoService = PatogenoServiceImpl(patogenoDAO)
        dataService = DataServiceImpl()

    }

    @Test
    fun `si creo una especie la recupero por su id`() {
        patogeno = Patogeno("Gripe")
        especie = Especie("especie11", "ARG", patogeno)

        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
       // val especieCreada = patogenoService.agregarEspecie()

    }

    @Test
    fun `recuperar todas las especies`() {
        dataService.crearSetDeDatosIniciales()

        val especies = especieService.recuperarTodos()

        assertEquals(21, especies.size)
    }

}