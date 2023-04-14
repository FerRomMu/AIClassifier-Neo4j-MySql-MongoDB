package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.exceptions.DataDuplicationException
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.persistencia.dao.UbicacionDAO
import ar.edu.unq.eperdemic.services.UbicacionService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx
import org.hibernate.exception.ConstraintViolationException

class UbicacionServiceImpl(val ubicacionDAO: UbicacionDAO): UbicacionService {

    override fun mover(vectorId: Long, ubicacionid: Long) {
        TODO("Not yet implemented")
    }

    override fun expandir(ubicacionId: Long) {
        TODO("Not yet implemented")
    }

    override fun crearUbicacion(nombreUbicacion: String): Ubicacion {
        val ubicacion = Ubicacion(nombreUbicacion)
        try {
            return runTrx {
                ubicacionDAO.guardar(ubicacion)
                ubicacion
            }
        } catch (e: ConstraintViolationException) {
            throw DataDuplicationException("Ya existe una ubicación con ese nombre.")
        }
    }

    override fun recuperar(id: Long): Ubicacion {
        return runTrx { ubicacionDAO.recuperar(id) }
    }

    override fun recuperarTodos(): List<Ubicacion> {
        return runTrx {
            ubicacionDAO.recuperarTodos()
        }
    }

}