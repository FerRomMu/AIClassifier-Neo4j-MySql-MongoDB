package ar.edu.unq.eperdemic.modelo

import kotlin.random.Random


class Randomizador {
    private var instance : Randomizador? = null
    var estado = EstadoRandomizadorAlatoria()

    fun  getInstance(): Randomizador {
        if (instance == null)  // NOT thread safe!
            instance = Randomizador()

        return instance!!
    }

    fun  valor(valorMin:Int, valorMax:Int ): Int {
        return estado.valor(valorMin,valorMax)
    }

}

abstract class  EstadoRandomizador (){
    abstract fun valor(valorMin:Int, valorMax:Int) : Int
}

class EstadoRandomizadorDeterminístico () : EstadoRandomizador() {
    override fun valor(valorMin:Int, valorMax:Int) : Int{
        return valorMax
    }
}

class EstadoRandomizadorAlatoria () : EstadoRandomizador(){
    override fun valor(valorMin:Int, valorMax:Int): Int{
        return Random.nextInt(valorMin,valorMax+1)
    }
}