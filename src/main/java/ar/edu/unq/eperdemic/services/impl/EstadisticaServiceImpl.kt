package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.persistencia.dao.EstadisticaDAO
import ar.edu.unq.eperdemic.services.EstadisticaService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx

class EstadisticaServiceImpl(var estadisticaDAO: EstadisticaDAO) : EstadisticaService {

    override fun especieLider(): Especie {
        return runTrx { estadisticaDAO.especieLider()}
    }


}