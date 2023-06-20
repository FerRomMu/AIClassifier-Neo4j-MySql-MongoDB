package ar.edu.unq.eperdemic.spring.controllers

import ar.edu.unq.eperdemic.modelo.Distrito
import ar.edu.unq.eperdemic.services.DistritoService
import ar.edu.unq.eperdemic.spring.controllers.dto.DistritoDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Suppress("SpringJavaInjectionPointsAutowiringInspection")
@CrossOrigin
@ServiceREST
@RequestMapping("/distrito")
class DistritoControllerREST {

    @Autowired lateinit var distritoService: DistritoService

    @PostMapping("/crear")
    fun crear(@RequestBody distrito: DistritoDTO) = DistritoDTO.desdeModelo(distritoService.crear(distrito.aModelo()))

    @GetMapping("/distritoMasEnfermo")
    fun distritoMasEnfermo() = DistritoDTO.desdeModelo(distritoService.distritoMasEnfermo())
}