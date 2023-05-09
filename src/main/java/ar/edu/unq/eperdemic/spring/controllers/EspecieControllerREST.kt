package ar.edu.unq.eperdemic.spring.controllers

import ar.edu.unq.eperdemic.exceptions.IdNotFoundException
import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.services.EspecieService
import ar.edu.unq.eperdemic.spring.controllers.dto.EspecieDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Suppress("SpringJavaInjectionPointsAutowiringInspection")
@CrossOrigin
@ServiceREST
@RequestMapping("/especie")
class EspecieControllerREST {

    @Autowired lateinit var especieService : EspecieService

    @GetMapping("/{id}")
    fun recuperarEspecie(@PathVariable("id") id: Long): EspecieDTO {
        return EspecieDTO.desdeModelo(especieService.recuperarEspecie(id))
    }

    @GetMapping()
    fun recuperarTodos() : List<EspecieDTO> {
        return especieService.recuperarTodos().map { especie -> EspecieDTO.desdeModelo(especie) }
    }

    @GetMapping("/cantidadInfectados/{id}")
    fun cantidadDeInfectados(@PathVariable("id") especieId: Long): Int {
        TODO()
    }

}