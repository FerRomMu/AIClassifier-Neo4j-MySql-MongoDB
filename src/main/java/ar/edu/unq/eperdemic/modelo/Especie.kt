package ar.edu.unq.eperdemic.modelo

@Entity
class Especie(var patogeno: Patogeno,
              var nombre: String,
              var paisDeOrigen: String) {

}