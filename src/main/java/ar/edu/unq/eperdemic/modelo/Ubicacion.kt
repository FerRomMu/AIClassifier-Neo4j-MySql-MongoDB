package ar.edu.unq.eperdemic.modelo

import javax.persistence.*

@Entity
@Table(name = "ubicacion")
class Ubicacion(name: String) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(unique = true)
    var nombre: String = name

}