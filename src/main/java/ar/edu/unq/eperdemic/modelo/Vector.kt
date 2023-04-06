package ar.edu.unq.eperdemic.modelo

import javax.persistence.Entity

@Entity
class Vector( var id: Long?,
              var tipo: TipoDeVector,
              var ubicacion: Ubicacion) {
}

enum class TipoDeVector {
    Persona, Insecto, Animal
}