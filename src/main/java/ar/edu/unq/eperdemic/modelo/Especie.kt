package ar.edu.unq.eperdemic.modelo

import javax.persistence.Entity
import javax.persistence.ManyToOne

@Entity
class Especie(var patogeno: Patogeno,
              var nombre: String,
              var paisDeOrigen: String) {

    @ManyToOne
    var owner: Vector? = null

}