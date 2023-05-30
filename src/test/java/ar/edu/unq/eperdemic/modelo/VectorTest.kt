package ar.edu.unq.eperdemic.modelo

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VectorTest {
    lateinit var vectorHumano1: Vector
    lateinit var vectorHumano2: Vector
    lateinit var vectorAnimal1: Vector
    lateinit var vectorAnimal2: Vector
    lateinit var vectorInsecto1: Vector
    lateinit var vectorInsecto2: Vector
    lateinit var especie: Especie
    lateinit var patogeno: Patogeno
    lateinit var ubicacion: Ubicacion
    lateinit var dado: Randomizador
    lateinit var supresion: SupresionBiomecanica
    lateinit var bioalteracionAnimal: BioalteracionGenetica

    @BeforeEach
    fun setup() {
        vectorHumano1 = Vector(TipoDeVector.Persona)
        vectorHumano2 = Vector(TipoDeVector.Persona)

        vectorAnimal1 = Vector(TipoDeVector.Animal)
        vectorAnimal2 = Vector(TipoDeVector.Animal)

        vectorInsecto1 = Vector(TipoDeVector.Insecto)
        vectorInsecto2 = Vector(TipoDeVector.Insecto)

        ubicacion = Ubicacion("bernal")
        patogeno = Patogeno("Gripe")
        patogeno.setCapacidadDeContagioHumano(100)
        patogeno.setCapacidadDeContagioAnimal(100)
        patogeno.setCapacidadDeContagioInsecto(100)
        patogeno.setCapacidadDeBiomecanizacion(100)
        especie = Especie(patogeno, "virusT", "raccoon city")

        dado = Randomizador.getInstance()
        dado.estado = EstadoRandomizadorDeterminístico()

        supresion = SupresionBiomecanica(50)
        bioalteracionAnimal = BioalteracionGenetica(TipoDeVector.Animal)
        bioalteracionAnimal.especie = especie

    }

    @Test
    fun `si agrego una especie se agrega al set de especies contagiadas`(){
        assertEquals(0, vectorHumano1.especiesContagiadas.size)

        vectorHumano1.agregarEspecie(especie)
        val especieContagiada = vectorHumano1.especiesContagiadas.first()

        assertEquals(especie,especieContagiada)

    }

    @Test
    fun `si un vector humano intenta infectar a un vector humano lo contagia`() {
        assertEquals(0, vectorHumano2.especiesContagiadas.size)

        vectorHumano1.agregarEspecie(especie)
        vectorHumano1.intentarInfectar(vectorHumano2)

        val especieQueFueContagiada = vectorHumano2.especiesContagiadas.first()
        val especieQueContagia = vectorHumano1.especiesContagiadas.first()

        assertEquals(1, vectorHumano2.especiesContagiadas.size)
        assertEquals(especieQueContagia,especieQueFueContagiada)
    }

    @Test
    fun `si un vector humano intenta infectar a un vector insecto lo contagia`() {
        assertEquals(0, vectorInsecto1.especiesContagiadas.size)

        vectorHumano1.agregarEspecie(especie)
        vectorHumano1.intentarInfectar(vectorInsecto1)

        val especieQueFueContagiada = vectorInsecto1.especiesContagiadas.first()
        val especieQueContagia = vectorHumano1.especiesContagiadas.first()

        assertEquals(1, vectorInsecto1.especiesContagiadas.size)
        assertEquals(especieQueContagia,especieQueFueContagiada)
    }

    @Test
    fun `si un vector humano intenta infectar a un vector animal, no pasa nada`() {
        assertEquals(0, vectorAnimal1.especiesContagiadas.size)

        vectorHumano1.agregarEspecie(especie)
        vectorHumano1.intentarInfectar(vectorAnimal1)

        assertEquals(0, vectorAnimal1.especiesContagiadas.size)
    }

    @Test
    fun `si un vector insecto intenta infectar a un vector animal lo contagia`() {
        assertEquals(0, vectorAnimal1.especiesContagiadas.size)

        vectorInsecto1.agregarEspecie(especie)
        vectorInsecto1.intentarInfectar(vectorAnimal1)

        val especieQueFueContagiada = vectorAnimal1.especiesContagiadas.first()
        val especieQueContagia = vectorInsecto1.especiesContagiadas.first()

        assertEquals(1, vectorAnimal1.especiesContagiadas.size)
        assertEquals(especieQueContagia,especieQueFueContagiada)
    }

    @Test
    fun `si un vector insecto intenta infectar a un vector humano lo contagia`() {
        assertEquals(0, vectorHumano1.especiesContagiadas.size)

        vectorInsecto1.agregarEspecie(especie)
        vectorInsecto1.intentarInfectar(vectorHumano1)

        val especieQueFueContagiada = vectorHumano1.especiesContagiadas.first()
        val especieQueContagia = vectorInsecto1.especiesContagiadas.first()

        assertEquals(1, vectorHumano1.especiesContagiadas.size)
        assertEquals(especieQueContagia,especieQueFueContagiada)
    }

    @Test
    fun `si un vector insecto intenta infectar a un vector insecto, no pasa nada`() {
        assertEquals(0, vectorInsecto2.especiesContagiadas.size)

        vectorInsecto1.agregarEspecie(especie)
        vectorInsecto1.intentarInfectar(vectorInsecto2)

        assertEquals(0, vectorInsecto2.especiesContagiadas.size)
    }

    @Test
    fun `si un vector animal intenta infectar a un vector humano lo contagia`() {
        assertEquals(0, vectorHumano1.especiesContagiadas.size)

        vectorAnimal1.agregarEspecie(especie)
        vectorAnimal1.intentarInfectar(vectorHumano1)

        val especieQueFueContagiada = vectorHumano1.especiesContagiadas.first()
        val especieQueContagia = vectorAnimal1.especiesContagiadas.first()

        assertEquals(1, vectorHumano1.especiesContagiadas.size)
        assertEquals(especieQueContagia,especieQueFueContagiada)
    }

    @Test
    fun `si un vector animal intenta infectar a un vector insecto lo contagia`() {
        assertEquals(0, vectorInsecto1.especiesContagiadas.size)

        vectorAnimal1.agregarEspecie(especie)
        vectorAnimal1.intentarInfectar(vectorInsecto1)

        val especieQueFueContagiada = vectorInsecto1.especiesContagiadas.first()
        val especieQueContagia = vectorAnimal1.especiesContagiadas.first()

        assertEquals(1, vectorInsecto1.especiesContagiadas.size)
        assertEquals(especieQueContagia,especieQueFueContagiada)
    }

    @Test
    fun `si un vector animal intenta infectar a un vector animal, no pasa nada`() {
        assertEquals(0, vectorAnimal2.especiesContagiadas.size)

        vectorAnimal1.agregarEspecie(especie)
        vectorAnimal1.intentarInfectar(vectorAnimal2)

        assertEquals(0, vectorAnimal2.especiesContagiadas.size)
    }

    @Test
    fun `si un vector animal con bioalteracion animal intenta infectar otro vector animal, lo contagia`() {
        assertEquals(0, vectorAnimal2.especiesContagiadas.size)

        vectorAnimal1.agregarEspecie(especie)
        vectorAnimal1.mutacionesSufridas.add(bioalteracionAnimal)
        vectorAnimal1.intentarInfectar(vectorAnimal2)

        assertTrue(vectorAnimal2.especiesContagiadas.contains(especie))
    }

    @Test
    fun `si un vector animal con bioalteracion animal intenta infectar otro vector animal con una especie sin esa mutacion, no contagia`() {
        val especieAInfectar = Especie(patogeno,"especie2", "Peru")

        assertEquals(0, vectorAnimal2.especiesContagiadas.size)

        vectorAnimal1.agregarEspecie(especieAInfectar)
        vectorAnimal1.mutacionesSufridas.add(bioalteracionAnimal)
        vectorAnimal1.intentarInfectar(vectorAnimal2)

        assertFalse(vectorAnimal2.especiesContagiadas.contains(especie))
    }

    @Test
    fun `si un vector animal intenta infectar a un vector insecto con suprecion biomecanica que lo cancela, no lo contagia`() {
        assertEquals(0, vectorInsecto1.especiesContagiadas.size)
        patogeno.setCapacidadDeDefensa(20)

        vectorAnimal1.agregarEspecie(especie)
        vectorInsecto1.mutacionesSufridas.add(supresion)
        vectorAnimal1.intentarInfectar(vectorInsecto1)

        assertEquals(0, vectorInsecto1.especiesContagiadas.size)
    }

    @Test
    fun `si un vector animal intenta infectar a un vector insecto con suprecion biomecanica de misma potencia que defensa, lo contagia`() {
        assertEquals(0, vectorInsecto1.especiesContagiadas.size)
        patogeno.setCapacidadDeDefensa(50)

        vectorAnimal1.agregarEspecie(especie)
        vectorInsecto1.mutacionesSufridas.add(supresion)
        vectorAnimal1.intentarInfectar(vectorInsecto1)

        assertTrue(vectorInsecto1.especiesContagiadas.contains(especie))
    }

    @Test
    fun `si la especie con la que un vector humano contagia a otro existosamente no tiene mutaciones posibles, entonces no muta`() {
        assertEquals(0, vectorHumano2.especiesContagiadas.size)

        vectorHumano1.agregarEspecie(especie)
        assertEquals(0, vectorHumano1.mutacionesSufridas.size)
        assertEquals(0, especie.mutacionesPosibles.size)

        vectorHumano1.intentarInfectar(vectorHumano2)

        val especieQueFueContagiada = vectorHumano2.especiesContagiadas.first()
        val especieQueContagia = vectorHumano1.especiesContagiadas.first()

        assertEquals(1, vectorHumano2.especiesContagiadas.size)
        assertEquals(especieQueContagia,especieQueFueContagiada)
        assertEquals(0, vectorHumano1.mutacionesSufridas.size)
    }


    @Test
    fun `si un vector humano intenta infectar a un vector humano lo contagia y si hay suerte el primero muta porque hay mutaciones posibles`() {
        assertEquals(0, vectorHumano2.especiesContagiadas.size)
        especie.agregarMutacion(bioalteracionAnimal)

        vectorHumano1.agregarEspecie(especie)
        assertEquals(0, vectorHumano1.mutacionesSufridas.size)
        assertEquals(1, especie.mutacionesPosibles.size)

        vectorHumano1.intentarInfectar(vectorHumano2)

        val especieQueFueContagiada = vectorHumano2.especiesContagiadas.first()
        val especieQueContagia = vectorHumano1.especiesContagiadas.first()

        assertEquals(1, vectorHumano2.especiesContagiadas.size)
        assertEquals(especieQueContagia,especieQueFueContagiada)

        assertEquals(1, vectorHumano1.mutacionesSufridas.size)
        assertTrue(bioalteracionAnimal.equals(vectorHumano1.mutacionesSufridas.first()))
    }

    @Test
    fun `si un vector humano intenta infectar a un vector humano lo contagia y si hay suerte el primero no muta porque el vector ya tiene todas las mutaciones posibles`() {
        assertEquals(0, vectorHumano2.especiesContagiadas.size)
        especie.agregarMutacion(bioalteracionAnimal)

        vectorHumano1.agregarEspecie(especie)
        vectorHumano1.mutacionesSufridas.add(bioalteracionAnimal)

        assertEquals(1, vectorHumano1.mutacionesSufridas.size)
        assertEquals(1, especie.mutacionesPosibles.size)

        vectorHumano1.intentarInfectar(vectorHumano2)

        val especieQueFueContagiada = vectorHumano2.especiesContagiadas.first()
        val especieQueContagia = vectorHumano1.especiesContagiadas.first()

        assertEquals(1, vectorHumano2.especiesContagiadas.size)
        assertEquals(especieQueContagia,especieQueFueContagiada)

        assertEquals(1, vectorHumano1.mutacionesSufridas.size)
        assertTrue(vectorHumano1.mutacionesSufridas.contains(bioalteracionAnimal))
    }

}