package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.DataDAO
import ar.edu.unq.eperdemic.persistencia.dao.UbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.services.VectorService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx

class VectorServiceImpl(
    private val vectorDAO: VectorDAO,
    private val ubicacionDAO: UbicacionDAO,
  ): VectorService {



    override fun contagiar(vectorInfectado: Vector, vectores: List<Vector>) {
        TODO("Not yet implemented")
    }

    override fun infectar(vector: Vector, especie: Especie) {
        TODO("Not yet implemented")
    }

    override fun enfermedades(vectorId: Long): List<Especie> {
        TODO("Not yet implemented")
    }

    override fun crearVector(tipo: TipoDeVector, ubicacionId: Long): Vector {
        return runTrx {
            var ubicacionDelVector = ubicacionDAO.recuperar(ubicacionId);

            var vector = Vector(null, tipo, ubicacionDelVector)
            vectorDAO.guardar(vector)

            vector
        }
    }

    override fun recuperarVector(vectorId: Long): Vector {
        TODO("Not yet implemented")
    }

    override fun borrarVector(vectorId: Long) {
        TODO("Not yet implemented")
    }
}