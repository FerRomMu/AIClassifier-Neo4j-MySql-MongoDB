package ar.edu.unq.eperdemic.spring.controllers

import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.services.VectorService
import ar.edu.unq.eperdemic.spring.controllers.dto.EspecieDTO
import ar.edu.unq.eperdemic.spring.controllers.dto.VectorDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@Suppress("SpringJavaInjectionPointsAutowiringInspection")
@CrossOrigin
@ServiceREST
@RequestMapping("/vector")
class VectorControllerREST {

    @Autowired lateinit var vectorService: VectorService

    @PostMapping("/crearVector/{tipoDeVector}/{ubicacionId}")
    fun crearVector(
        @PathVariable("tipoDeVector") tipoDeVector: TipoDeVector,
        @PathVariable("ubicacionId")  ubicacionId: Long
    ): VectorDTO = VectorDTO
                      .desdeModelo(vectorService.crearVector(tipoDeVector, ubicacionId))

    @GetMapping("/vector/{id}")
    fun recuperarVector(@PathVariable("id") id: Long): VectorDTO
        = VectorDTO.desdeModelo(vectorService.recuperarVector(id))

    @DeleteMapping("/borrarVector/{id}")
    fun borrarVector(@PathVariable("id") id: Long) = vectorService.borrarVector(id)

    @GetMapping
    fun recuperarTodos() = vectorService.recuperarTodos().map { v -> VectorDTO.desdeModelo(v) }

    @PutMapping("/infectar/{vector}/{especie}")
    fun infectar(
        @PathVariable("vector")  vectorDTO: VectorDTO,
        @PathVariable("especie") especieDTO: EspecieDTO
    ) =
        vectorService.infectar(vectorDTO.aModelo(), especieDTO.aModelo())
}