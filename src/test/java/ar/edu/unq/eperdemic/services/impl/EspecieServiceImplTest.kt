package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.persistencia.dao.EspecieDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateEspecieDAO
import ar.edu.unq.eperdemic.services.EspecieService
import ar.edu.unq.eperdemic.utils.DataService
import ar.edu.unq.eperdemic.utils.impl.DataServiceImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EspecieServiceImplTest {

    lateinit var especieDAO: EspecieDAO
    lateinit var especieService: EspecieService
    lateinit var especie: Especie
    lateinit var dataService : DataService

    @BeforeEach
    fun setup() {
        especieDAO = HibernateEspecieDAO()
        especieService = EspecieServiceImpl(especieDAO)
        dataService = DataServiceImpl()

    }

    @Test
    fun `creo una especie y recibe un id`() {
        //especie = Especie("especie111","ARG")


    }

}