package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.DataDAO
import ar.edu.unq.eperdemic.persistencia.dao.EspecieDAO
import ar.edu.unq.eperdemic.persistencia.dao.UbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.services.VectorService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx

class VectorServiceImpl(
    private val vectorDAO: VectorDAO,
    private val ubicacionDAO: UbicacionDAO,
    private val especieDAO: EspecieDAO
  ): VectorService {

    override fun infectar(vector: Vector, especie: Especie) {
        runTrx {
            vector.agregarEspecie(especie)
            especie.agregarVector(vector)
            especieDAO.guardar(especie)
            vectorDAO.guardar(vector)
        }
    }

    override fun enfermedades(vectorId: Long): List<Especie> {
        return runTrx { vectorDAO.enfermedades(vectorId) }
    }

    override fun crearVector(tipo: TipoDeVector, ubicacionId: Long): Vector {
        return runTrx {
            val ubicacionDelVector = ubicacionDAO.recuperar(ubicacionId);

            val vector = Vector( tipo)
            vector.ubicacion = ubicacionDelVector
            vectorDAO.guardar(vector)

            vector
        }
    }

    override fun recuperarVector(vectorId: Long): Vector {
        return runTrx { vectorDAO.recuperar(vectorId) }
    }

    override fun borrarVector(vectorId: Long) {
        return runTrx { vectorDAO.borrar(vectorId) }
    }

    override fun recuperarTodos(): List<Vector> {
        return runTrx { vectorDAO.recuperarTodos() }
    }

}