package ar.edu.unq.eperdemic.modelo

import javax.persistence.*

@Entity
class Especie(var nombre: String,
              var paisDeOrigen: String, patogenoParam: Patogeno) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long? = null

    @ManyToOne
    @JoinColumn(name="id_patogeno")
    val patogeno: Patogeno = patogenoParam

    @ManyToMany(mappedBy = "especiesContagiadas")
    val vectores: MutableSet<Vector> = HashSet()

}
