package ar.edu.unq.eperdemic.modelo

import javax.persistence.*

@Entity
@Table(name = "reporteDeContagios")
class ReporteDeContagios(val vectoresPresentes:Int,
                         val vectoresInfecatods:Int,
                         val nombreDeEspecieMasInfecciosa: String) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long? = null
}