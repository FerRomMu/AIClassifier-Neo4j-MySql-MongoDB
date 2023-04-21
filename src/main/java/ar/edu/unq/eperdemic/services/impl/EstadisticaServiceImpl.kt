package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.exceptions.DataNotFoundException
import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.ReporteDeContagios
import ar.edu.unq.eperdemic.persistencia.dao.EstadisticaDAO
import ar.edu.unq.eperdemic.services.EstadisticaService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx

class EstadisticaServiceImpl(var estadisticaDAO: EstadisticaDAO) : EstadisticaService {

    override fun especieLider(): Especie {
        return runTrx { estadisticaDAO.especieLider()}
    }

    override fun reporteDeContagios(nombreDeLaUbicacion: String): ReporteDeContagios {
        return runTrx {
                ReporteDeContagios(
                    estadisticaDAO.cantidadVectoresPresentes(nombreDeLaUbicacion).toInt(),
                    estadisticaDAO.cantidadVectoresInfectados(nombreDeLaUbicacion).toInt(),
                    estadisticaDAO.nombreEspecieQueMasInfectaVectores(nombreDeLaUbicacion)
                )
        }
    }

    override fun lideres(): List<Especie>{
        return runTrx { estadisticaDAO.lideres()}
    }

}