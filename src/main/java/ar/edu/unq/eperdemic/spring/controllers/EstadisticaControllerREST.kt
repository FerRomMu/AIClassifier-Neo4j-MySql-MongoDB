package ar.edu.unq.eperdemic.spring.controllers

import ar.edu.unq.eperdemic.services.impl.EstadisticaServiceImpl
import ar.edu.unq.eperdemic.spring.controllers.dto.EspecieDTO
import ar.edu.unq.eperdemic.spring.controllers.dto.ReporteDeContagiosDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Suppress("SpringJavaInjectionPointsAutowiringInspection")
@CrossOrigin
@ServiceREST
@RequestMapping("/estadistica")
class EstadisticaControllerREST {

    @Autowired lateinit var estadisticaService: EstadisticaServiceImpl

    @GetMapping("/especieLider")
    fun especieLider() = EspecieDTO.from(estadisticaService.especieLider())

    @GetMapping("/lideres")
    fun lideres(): List<EspecieDTO> = estadisticaService.lideres().map { EspecieDTO.from(it) }

    @GetMapping("/reporteDeContagios/{nombre}")
    fun repoteDeContagios(
        @PathVariable nombre: String
    ): ReporteDeContagiosDTO = ReporteDeContagiosDTO.from(estadisticaService.reporteDeContagios(nombre))
    /*

    fun especieLider(): Especie
    fun reporteDeContagios(nombreDeLaUbicacion: String): ReporteDeContagios
    fun lideres(): List<Especie>
    * */
}