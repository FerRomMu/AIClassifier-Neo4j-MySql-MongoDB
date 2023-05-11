package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.exceptions.DataNotFoundException
import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.ReporteDeContagios
import ar.edu.unq.eperdemic.persistencia.dao.EstadisticaDAO
import ar.edu.unq.eperdemic.persistencia.repository.spring.EspecieRepository
import ar.edu.unq.eperdemic.persistencia.repository.spring.UbicacionRepository
import ar.edu.unq.eperdemic.services.EstadisticaService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service

@Service
class EstadisticaServiceImpl() : EstadisticaService {

    @Autowired
    lateinit var especieRepository: EspecieRepository
    @Autowired
    lateinit var ubicacionRepository: UbicacionRepository

    override fun especieLider(): Especie {
        try {
            return  especieRepository.especieLider()
        } catch(e: EmptyResultDataAccessException) {
            throw DataNotFoundException("No hay especie lider.")
        }
    }

    override fun reporteDeContagios(nombreDeLaUbicacion: String): ReporteDeContagios {
        try {
            return ReporteDeContagios(
                ubicacionRepository.cantidadVectoresPresentes(nombreDeLaUbicacion).toInt(),
                ubicacionRepository.cantidadVectoresInfectados(nombreDeLaUbicacion).toInt(),
                ubicacionRepository.nombreEspecieQueMasInfectaVectores(nombreDeLaUbicacion)
            )
        } catch(e: EmptyResultDataAccessException) {
            throw DataNotFoundException("No hay ubicacion o especie en dicha ubicacion.")
        }
    }

    override fun lideres(): List<Especie>{
        return TODO("especieRepository.lideres() falta")
    }

}