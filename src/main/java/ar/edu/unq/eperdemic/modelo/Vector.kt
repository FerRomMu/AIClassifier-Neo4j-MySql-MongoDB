package ar.edu.unq.eperdemic.modelo

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.OneToMany

@Entity
class Vector( var id: Long?,
              var tipo: TipoDeVector,
              var ubicacion: Ubicacion) {

    @OneToMany(mappedBy = "owner", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var especiesContagiadas: HashSet<Especie> = HashSet()

    fun agregarEspecie(especie: Especie) {
        especiesContagiadas.add(especie)
    }
}

enum class TipoDeVector {
    Persona, Insecto, Animal
}