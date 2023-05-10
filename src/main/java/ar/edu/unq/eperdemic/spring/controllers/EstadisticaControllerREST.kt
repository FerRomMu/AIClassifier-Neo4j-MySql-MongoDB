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
    fun especieLider() = EspecieDTO.desdeModelo(estadisticaService.especieLider())

    @GetMapping("/lideres")
    fun lideres(): List<EspecieDTO> = estadisticaService.lideres().map { EspecieDTO.desdeModelo(it) }

    @GetMapping("/reporteDeContagios/{nombre}/{equipo}")
    fun repoteDeContagios(
        @PathVariable nombre: String,
        @PathVariable equipo: String
    ): ReporteDeContagiosDTO = ReporteDeContagiosDTO
                                    .desdeModelo(estadisticaService.reporteDeContagios(nombre), nombre, equipo)
}