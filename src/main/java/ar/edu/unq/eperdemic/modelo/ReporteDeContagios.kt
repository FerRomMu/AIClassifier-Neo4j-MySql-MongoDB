package ar.edu.unq.eperdemic.modelo

@Entity
class ReporteDeContagios(val vectoresPresentes:Int,
                         val vectoresInfecatods:Int,
                         val nombreDeEspecieMasInfecciosa: String) {
}