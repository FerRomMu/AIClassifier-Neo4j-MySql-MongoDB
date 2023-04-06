package ar.edu.unq.eperdemic.modelo

import javax.persistence.Entity

@Entity
class Especie(var patogeno: Patogeno,
              var nombre: String,
              var paisDeOrigen: String) {

}