package ar.edu.unq.eperdemic.spring.controllers

import ar.edu.unq.eperdemic.exceptions.IdNotFoundException
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.services.PatogenoService
import ar.edu.unq.eperdemic.spring.controllers.dto.EspecieDTO
import ar.edu.unq.eperdemic.spring.controllers.dto.PatogenoDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Suppress("SpringJavaInjectionPointsAutowiringInspection")
@CrossOrigin
@ServiceREST
@RequestMapping("/patogeno")
class PatogenoControllerREST() {

    @Autowired lateinit var patogenoService: PatogenoService

    @PostMapping
    fun create(@RequestBody patogenoDTO: PatogenoDTO): PatogenoDTO {

      return PatogenoDTO.desdeModelo(patogenoService.crearPatogeno(patogenoDTO.aModelo()))
    }

    @PostMapping("/{id}/{idUbicacion}")
    fun agregarEspecie(@PathVariable id: Long, @PathVariable("idUbicacion") idUbicacion: Long, @RequestBody especieDTO: EspecieDTO): EspecieDTO {
      val especie = patogenoService.agregarEspecie(id, especieDTO.nombre, idUbicacion)
      return EspecieDTO.desdeModelo(especie)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long) : PatogenoDTO{
        return PatogenoDTO.desdeModelo(patogenoService.recuperarPatogeno(id))
    }

    @GetMapping
    fun getAll() = patogenoService.recuperarATodosLosPatogenos().map { PatogenoDTO.desdeModelo(it) }

    @GetMapping("/especies/{id}")
    fun especiesDePatogeno(@PathVariable id: Long) : List<EspecieDTO>{
      val especies = patogenoService.especiesDePatogeno(id)

      return especies.map { especie -> EspecieDTO(especie.nombre, especie.paisDeOrigen, especie.patogeno.id) }
    }

    @GetMapping("/esPandemia/{idEspecie}")
    fun esPandemia(@PathVariable("idEspecie") idEspecie: Long) : Boolean{
      return patogenoService.esPandemia(idEspecie)
    }

}

