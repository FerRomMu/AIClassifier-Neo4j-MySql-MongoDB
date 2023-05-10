package ar.edu.unq.eperdemic.spring.controllers

import ar.edu.unq.eperdemic.services.MutacionService
import ar.edu.unq.eperdemic.spring.controllers.dto.MutacionDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@Suppress("SpringJavaInjectionPointsAutowiringInspection")
@CrossOrigin
@ServiceREST
@RequestMapping("/especie")
class MutacionControllerREST {
    @Autowired
    lateinit var mutacionService: MutacionService

    @PostMapping("/{id}")
    fun agregarMutacion(@PathVariable id: Long, @RequestBody mutacion: MutacionDTO): MutacionDTO {
        return MutacionDTO.desdeModelo(mutacionService.agregarMutacion(id, MutacionDTO.aModelo(mutacion)))
    }
}