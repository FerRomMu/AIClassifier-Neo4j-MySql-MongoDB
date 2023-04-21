package ar.edu.unq.eperdemic.modelo

import kotlin.random.Random


class Randomizador private constructor() {
    var estado : EstadoRandomizador = EstadoRandomizadorAlatorio()

    companion object {
        private var instance: Randomizador? = null

        fun  getInstance(): Randomizador {

            if (instance == null)
                instance = Randomizador()

            return instance!!
        }
    }

    fun  valor(valorMin:Int, valorMax:Int ): Int {
        return estado.valor(valorMin,valorMax)
    }


}

abstract class  EstadoRandomizador(){
    abstract fun valor(valorMin:Int, valorMax:Int) : Int
}

class EstadoRandomizadorDeterminístico() : EstadoRandomizador() {
    override fun valor(valorMin:Int, valorMax:Int) : Int{
        return valorMax
    }
}

class EstadoRandomizadorAlatorio() : EstadoRandomizador(){
    override fun valor(valorMin:Int, valorMax:Int): Int{
        return Random.nextInt(valorMin,valorMax+1)
    }
}