package ar.edu.unq.eperdemic.modelo

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class Ubicacion(var nombre: String) {

    @Id
    @Column(name="Id")
    var id: Long? = null
}