package ar.edu.unq.eperdemic.modelo

import javax.persistence.Entity

@Entity
class ReporteDeContagios(val vectoresPresentes:Int,
                         val vectoresInfecatods:Int,
                         val nombreDeEspecieMasInfecciosa: String) {
}