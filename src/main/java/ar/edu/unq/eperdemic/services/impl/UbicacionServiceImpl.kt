package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.persistencia.dao.UbicacionDAO
import ar.edu.unq.eperdemic.services.UbicacionService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx

class UbicacionServiceImpl(val ubicacionDAO: UbicacionDAO): UbicacionService {

    override fun mover(vectorId: Long, ubicacionid: Long) {
        TODO("Not yet implemented")
    }

    override fun expandir(ubicacionId: Long) {
        TODO("Not yet implemented")
    }

    override fun crearUbicacion(nombreUbicacion: String): Ubicacion {
        val ubicacion = Ubicacion(nombreUbicacion)
        return runTrx {
            ubicacionDAO.guardar(ubicacion)
            ubicacion
        }
    }

    override fun recuperarTodos(): List<Ubicacion> {
        return runTrx {
            ubicacionDAO.recuperarTodos()
        }
    }

}