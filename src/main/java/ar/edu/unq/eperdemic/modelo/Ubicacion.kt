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

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var caminos : MutableSet<Camino> = HashSet()

    fun caminosA(ubicacionAMover: Ubicacion): MutableSet<Camino> {
        return caminos.filter { c -> c.llegaA(ubicacionAMover) }.toMutableSet()
    }


}