package ar.edu.unq.eperdemic.modelo

import java.io.Serializable
import javax.persistence.*

@Entity
class Especie(var patogeno: Patogeno,
              var nombre: String,
              var paisDeOrigen: String):Serializable  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long? = null


    fun capacidadDeContagioA(tipoVictima : TipoDeVector): Int {
        return this.patogeno.capacidadDeContagioA(tipoVictima)
    }


}