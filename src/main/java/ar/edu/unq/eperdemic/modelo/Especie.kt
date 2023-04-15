package ar.edu.unq.eperdemic.modelo

import javax.persistence.*

@Entity
class Especie(var patogeno: Patogeno,
              var nombre: String,
              var paisDeOrigen: String) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long? = null


    fun capacidadDeContagioA(tipoVictima : TipoDeVector): Int {
        return 0 // Ver que tipo es tipoVictima y usar su variable
    }
}