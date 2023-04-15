package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.services.PatogenoService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx

class PatogenoServiceImpl(var patogenoDAO: PatogenoDAO) : PatogenoService {

    override fun crearPatogeno(patogeno: Patogeno): Patogeno {
        return runTrx {
           patogenoDAO.guardar(patogeno)
            patogeno
        }
    }

    override fun recuperarPatogeno(id: Long): Patogeno {
        return runTrx {
            val patogenoRecuperado = patogenoDAO.recuperar(id)
            patogenoRecuperado
        }
    }

    override fun recuperarATodosLosPatogenos(): List<Patogeno> {
        return runTrx { patogenoDAO.recuperarTodos() }
    }

    override fun agregarEspecie(id: Long, nombre: String, ubicacionId: Long): Especie {
        TODO("not implemented")
    }

    override fun cantidadDeInfectados(especieId: Long): Int {
        TODO("Not yet implemented")
    }

    override fun esPandemia(especieId: Long): Boolean {
        TODO("Not yet implemented")
    }

    override fun recuperarEspecie(id: Long): Especie {
        TODO("Not yet implemented")
    }

}