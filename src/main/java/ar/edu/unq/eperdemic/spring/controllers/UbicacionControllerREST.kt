package ar.edu.unq.eperdemic.spring.controllers;

import ar.edu.unq.eperdemic.modelo.Camino
import ar.edu.unq.eperdemic.modelo.Ubicacion;
import ar.edu.unq.eperdemic.modelo.UbicacionNeo
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.services.UbicacionService;
import ar.edu.unq.eperdemic.services.VectorService;
import ar.edu.unq.eperdemic.spring.controllers.dto.UbicacionDTO
import ar.edu.unq.eperdemic.spring.controllers.dto.VectorDTO
import kotlin.Suppress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping;

@Suppress("SpringJavaInjectionPointsAutowiringInspection")
@CrossOrigin
@ServiceREST
@RequestMapping("/ubicacion")
class UbicacionControllerREST {

    @Autowired lateinit var ubicacionService : UbicacionService


    @PutMapping("/mover/{vectorId}/{ubicacionid}")
    fun mover(@PathVariable("vectorId")vectorId: Long,@PathVariable("ubicacionid")ubicacionid: Long){
        ubicacionService.mover(vectorId,ubicacionid)
    }

    @PutMapping("/expandir/{id}")
    fun expandir(@PathVariable("id")id: Long) {
        ubicacionService.expandir(id)
    }

    @PostMapping("/crearUbicacion/{nombreUbicacion}")
    fun crearUbicacion(@PathVariable("nombreUbicacion")nombreUbicacion: String): UbicacionDTO {
        return UbicacionDTO.desdeModelo(ubicacionService.crearUbicacion(nombreUbicacion))
    }

    @GetMapping("/{id}")
    fun recuperar(@PathVariable("id") id: Long): UbicacionDTO {
        return UbicacionDTO.desdeModelo(ubicacionService.recuperar(id));
    }

    @GetMapping()
    fun recuperarTodos(): List<UbicacionDTO> {
        return ubicacionService.recuperarTodos().map { ubicacion -> UbicacionDTO.desdeModelo(ubicacion) }
    }

    @GetMapping("/vectoresEn/{id}")
    fun vectoresEn(@PathVariable("id") id: Long): List<VectorDTO> {
        return ubicacionService.vectoresEn(id).map { vector -> VectorDTO.desdeModelo(vector) }
    }

    @PutMapping("/conectar/{nombreDeUbicacion1}/{nombreDeUbicacion2}")
    fun conectar(@PathVariable nombreDeUbicacion1: String,
                 @PathVariable nombreDeUbicacion2: String,
                 @RequestBody  tipoDeCaminoWrapper: TipoDeCaminoWrapper) {

        val tipoDeCamino = when (tipoDeCaminoWrapper.tipoDeCamino) {
            "CaminoAereo"    -> Camino.TipoDeCamino.CaminoAereo
            "CaminoMaritimo" -> Camino.TipoDeCamino.CaminoMaritimo
            "CaminoTerreste" -> Camino.TipoDeCamino.CaminoTerreste
            else -> throw IllegalArgumentException("Valor de tipoDeCamino inválido")
        }
        ubicacionService.conectar(nombreDeUbicacion1,nombreDeUbicacion2,tipoDeCamino)
    }

    @GetMapping("/conectados/{nombreDeUbicacion}")
    fun conectados(@PathVariable nombreDeUbicacion: String): List<UbicacionDTO>
        = ubicacionService.conectados(nombreDeUbicacion).map { u -> UbicacionDTO.desdeModelo(u) }

    @PutMapping("/moverMasCorto/{vectorId}/{nombreDeUbicacion}")
    fun moverMasCorto(@PathVariable vectorId: Long, @PathVariable nombreDeUbicacion: String)
        = ubicacionService.moverMasCorto(vectorId,nombreDeUbicacion)
}

data class TipoDeCaminoWrapper(val tipoDeCamino: String)
