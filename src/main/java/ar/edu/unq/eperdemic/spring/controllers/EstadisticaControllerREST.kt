package ar.edu.unq.eperdemic.spring.controllers

import ar.edu.unq.eperdemic.services.EstadisticaService
import ar.edu.unq.eperdemic.services.PatogenoService
import ar.edu.unq.eperdemic.services.impl.EstadisticaServiceImpl
import ar.edu.unq.eperdemic.spring.controllers.dto.EspecieDTO
import ar.edu.unq.eperdemic.spring.controllers.dto.EspecieLiderDTO
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

    @Autowired lateinit var estadisticaService: EstadisticaService
    @Autowired lateinit var patogenoService : PatogenoService

    @GetMapping("/especieLider")
    fun especieLider() : EspecieLiderDTO{
        val especieLider = estadisticaService.especieLider()
        val esPandemia = patogenoService.esPandemia(especieLider.id!!)

        return EspecieLiderDTO.desdeModelo(especieLider.nombre, especieLider.patogeno.id, especieLider.vectores.size, esPandemia)
    }

    @GetMapping("/lideres")
    fun lideres(): List<EspecieLiderDTO> {
        return estadisticaService.lideres().map {
            val esPandemia = patogenoService.esPandemia(it.id!!)

            EspecieLiderDTO.desdeModelo(it.nombre, it.patogeno.id, it.vectores.size, esPandemia)
        }
    }

    @GetMapping("/reporteDeContagios/{nombre}/{equipo}")
    fun repoteDeContagios(
        @PathVariable nombre: String,
        @PathVariable equipo: String
    ): ReporteDeContagiosDTO = ReporteDeContagiosDTO
                                    .desdeModelo(estadisticaService.reporteDeContagios(nombre), nombre, equipo)
}