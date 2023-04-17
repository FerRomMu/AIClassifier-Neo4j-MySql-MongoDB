package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.EspecieDAO
import ar.edu.unq.eperdemic.services.EspecieService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx

class EspecieServiceImpl(var especieDAO: EspecieDAO) : EspecieService {

    override fun recuperarEspecie(id: Long): Especie {
        return runTrx {
            val especieRecuperada = especieDAO.recuperar(id)
            especieRecuperada
        }
    }

    override fun recuperarTodos(): List<Especie> {
        return runTrx {
            val recuperadas = especieDAO.recuperarTodos()
            recuperadas
        }
    }

    override fun cantidadDeInfectados(especieId: Long): Int {
        TODO("Not yet implemented")
    }

}