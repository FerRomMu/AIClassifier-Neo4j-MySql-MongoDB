package ar.edu.unq.eperdemic.modelo

class Camino(val ubicacioOrigen: Ubicacion,
             val ubicacioDestino: Ubicacion,
             val tipo: TipoDeCamino) {

    fun puedePasar(vector: Vector) : Boolean{
        return this.tipo.puedeTransitar(vector.tipo)
    }

    fun llegaA(ubicacionAMover: Ubicacion): Boolean {
        return this.ubicacioDestino.equals(ubicacionAMover)
    }

     fun equals(caminoAComparar : Camino): Boolean{
        return this.ubicacioOrigen == caminoAComparar.ubicacioOrigen    &&
                this.ubicacioDestino == caminoAComparar.ubicacioDestino &&
                this.tipo == caminoAComparar.tipo
    }

}


enum class TipoDeCamino{
    CaminoTerreste,CaminoMaritimo,CaminoAereo;

    fun puedeTransitar(tipo : TipoDeVector) : Boolean{
        return when(this){
            TipoDeCamino.CaminoTerreste -> tipo.esPersona() || tipo.esAnimal() || tipo.esInsecto()
            TipoDeCamino.CaminoMaritimo -> tipo.esPersona() || tipo.esAnimal()
            TipoDeCamino.CaminoAereo -> tipo.esInsecto() || tipo.esAnimal()
        }
    }




}