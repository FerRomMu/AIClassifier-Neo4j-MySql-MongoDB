package ar.edu.unq.eperdemic.modelo

import javax.persistence.*

@Entity
class Especie(var patogeno: Patogeno,
              var nombre: String,
              var paisDeOrigen: String) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long? = null

}