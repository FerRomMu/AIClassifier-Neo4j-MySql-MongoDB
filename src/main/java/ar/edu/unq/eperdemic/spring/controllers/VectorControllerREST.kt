package ar.edu.unq.eperdemic.spring.controllers

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.services.EspecieService
import ar.edu.unq.eperdemic.services.PatogenoService
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
    @Autowired lateinit var especieService: EspecieService

    @PostMapping("/crearVector/{ubicacionId}")
    fun crearVector(@RequestBody tipoDeVectorWrapper: TipoDeVectorWrapper, @PathVariable("ubicacionId")  ubicacionId: Long): VectorDTO {
        val tipoDeVector = when (tipoDeVectorWrapper.tipoDeVector) {
            "Persona" -> TipoDeVector.Persona
            "Insecto" -> TipoDeVector.Insecto
            "Animal" -> TipoDeVector.Animal
            else -> throw IllegalArgumentException("Valor de tipoDeVector inválido")
        }
        return  VectorDTO.desdeModelo(vectorService.crearVector(tipoDeVector, ubicacionId))
    }
    @GetMapping("/{id}")
    fun recuperarVector(@PathVariable("id") id: Long): VectorDTO
        = VectorDTO.desdeModelo(vectorService.recuperarVector(id))

    @DeleteMapping("/borrarVector/{id}")
    fun borrarVector(@PathVariable("id") id: Long) = vectorService.borrarVector(id)

    @GetMapping
    fun recuperarTodos() = vectorService.recuperarTodos().map { v -> VectorDTO.desdeModelo(v) }

    @PutMapping("/infectar/{vectorId}/{especieId}")
    fun infectar(
        @PathVariable("vectorId") vectorId: Long,
        @PathVariable("especieId") especieId: Long)
    {
        val especieAInfectar = especieService.recuperarEspecie(especieId)
        val vectorAInfectar = vectorService.recuperarVector(vectorId)
        vectorService.infectar(vectorAInfectar, especieAInfectar)

    }

    @GetMapping("/enfermedades/{vectorId}")
    fun enfermedades(@PathVariable("vectorId") vectorId: Long): List<EspecieDTO>
        = vectorService.enfermedades(vectorId).map { e -> EspecieDTO.desdeModelo(e) }

}
data class TipoDeVectorWrapper(val tipoDeVector: String)