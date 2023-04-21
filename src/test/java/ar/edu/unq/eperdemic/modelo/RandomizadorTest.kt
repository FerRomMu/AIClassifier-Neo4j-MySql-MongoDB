package ar.edu.unq.eperdemic.modelo

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RandomizadorTest {
    lateinit var randomizador: Randomizador

    @BeforeEach
    fun setUp() {
        randomizador = Randomizador.getInstance()

    }

    @Test
    fun `si le pido un valor al randomizador aleatorio me devuelve uno entre minimo y maximo establecidos`(){
        randomizador.estado = EstadoRandomizadorAlatorio()

        val valorRandom = randomizador.valor(1,10)

        assertTrue(valorRandom in 1..10)

        val valorRandom2 = randomizador.valor(1,1)

        assertEquals(1,valorRandom2)
    }

    @Test
    fun `si le pido un valor al randomizador deterministico me devuelve el maximo establecidos`(){
        randomizador.estado = EstadoRandomizadorDeterminístico()

        val valorRandom = randomizador.valor(1,10)

        assertEquals(10,valorRandom)
    }
}