package ar.edu.unq.eperdemic.modelo

@Entity
@Table(name="Especie")
class Especie(var patogeno: Patogeno,
              var nombre: String,
              var paisDeOrigen: String) {

}