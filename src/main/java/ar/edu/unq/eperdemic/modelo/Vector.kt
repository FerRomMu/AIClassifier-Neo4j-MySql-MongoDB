package ar.edu.unq.eperdemic.modelo

@Entity
@Table(name="Vector")
class Vector( var id: Long?,
              var tipo: TipoDeVector,
              var ubicacion: Ubicacion) {
}

enum class TipoDeVector {
    Persona, Insecto, Animal
}