package ar.edu.unq.eperdemic.modelo

@Entity
@Table(name="Reporte de Contagios")
class ReporteDeContagios(val vectoresPresentes:Int,
                         val vectoresInfecatods:Int,
                         val nombreDeEspecieMasInfecciosa: String) {
}