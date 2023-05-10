package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.exceptions.IdNotFoundException
import ar.edu.unq.eperdemic.modelo.BioalteracionGenetica
import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.SupresionBiomecanica
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.services.EspecieService
import ar.edu.unq.eperdemic.services.MutacionService
import ar.edu.unq.eperdemic.utils.DataService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MutacionServiceImplTest {

    @Autowired
    lateinit var mutacionService: MutacionService
    @Autowired
    lateinit var especieService: EspecieService
    @Autowired
    lateinit var data : DataService

    @Test
    fun `Si intento agregar una mutación a una especie con id invalido falla`(){
        assertThrows(IdNotFoundException::class.java) {
            mutacionService.agregarMutacion(
                3242828234131,
                SupresionBiomecanica(21)
            )
        }
    }

    @Test
    fun `Si intento agregar una mutación a una especie esta se agrega correctamente`(){
        val unaEspecie = data.crearSetDeDatosIniciales().first { it is Especie } as Especie

        assertEquals(unaEspecie.mutacionesPosibles.size, 0)

        mutacionService.agregarMutacion(unaEspecie.id!!, SupresionBiomecanica(21))
        mutacionService.agregarMutacion(unaEspecie.id!!, BioalteracionGenetica(TipoDeVector.Persona))

        val especieActualizada = especieService.recuperarEspecie(unaEspecie.id!!)

        assertTrue(especieActualizada.mutacionesPosibles.first{ it is SupresionBiomecanica }.equals(SupresionBiomecanica(21)))
        assertTrue(especieActualizada.mutacionesPosibles.first{ it is BioalteracionGenetica }.equals(BioalteracionGenetica(TipoDeVector.Persona)))
        assertEquals(especieActualizada.mutacionesPosibles.size, 2)
    }

    @AfterEach
    fun deleteAll() {
        data.eliminarTodo()
    }
}