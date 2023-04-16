package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.persistencia.dao.UbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.services.UbicacionService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx

class UbicacionServiceImpl(val ubicacionDAO: UbicacionDAO): UbicacionService {

    val vectorDAO : VectorDAO = HibernateVectorDAO()

    override fun mover(vectorId: Long, ubicacionid: Long) {
         runTrx {
            var listaDeVectores = ubicacionDAO.vectoresDeY(ubicacionid, vectorId).toList()
            var vectorAMover = listaDeVectores.find( {it.id == vectorId} )

             if(listaDeVectores.size > 1){
                 vectorAMover!!.ubicacion = listaDeVectores.get(0).ubicacion

                 for (vector in listaDeVectores){
                     vectorAMover.intentarInfectar(vector)
                     vectorDAO.guardar(vector)
                 }
                 vectorDAO.guardar(vectorAMover)
             }
         }

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