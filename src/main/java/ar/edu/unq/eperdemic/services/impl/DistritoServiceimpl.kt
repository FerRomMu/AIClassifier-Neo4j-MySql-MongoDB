package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.exceptions.DataNotFoundException
import ar.edu.unq.eperdemic.modelo.Distrito
import ar.edu.unq.eperdemic.persistencia.repository.mongo.DistritoMongoRepository
import ar.edu.unq.eperdemic.persistencia.repository.mongo.UbicacionMongoRepository
import ar.edu.unq.eperdemic.services.DistritoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DistritoServiceimpl: DistritoService {

    @Autowired
    lateinit var distritoRepository: DistritoMongoRepository
    @Autowired
    lateinit var ubicacionRepository: UbicacionMongoRepository

    override fun crear(distrito: Distrito): Distrito {
        return distritoRepository.save(distrito)
    }

    override fun distritoMasEnfermo(): Distrito {
        val coordenadasEnfermas = ubicacionRepository.findCoordenadasConInfectados()
        return distritoRepository.distritoMasEnfermo(coordenadasEnfermas) ?:
            throw DataNotFoundException("No hay distrito con enfermos.")
    }

}