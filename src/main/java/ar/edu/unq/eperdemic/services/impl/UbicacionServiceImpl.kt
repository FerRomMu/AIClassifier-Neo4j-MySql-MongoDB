package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Randomizador
import ar.edu.unq.eperdemic.exceptions.DataDuplicationException
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.UbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.services.UbicacionService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx
import org.hibernate.exception.ConstraintViolationException

class UbicacionServiceImpl(val ubicacionDAO: UbicacionDAO): UbicacionService {

    val vectorDAO : VectorDAO = HibernateVectorDAO()

    override fun mover(vectorId: Long, ubicacionid: Long) {
         runTrx {
            var listaDeVectores = ubicacionDAO.vectoresEn(ubicacionid).toList()
            var vectorAMover = vectorDAO.recuperar(vectorId)

             if(listaDeVectores.isNotEmpty()){
                 vectorAMover.ubicacion = listaDeVectores[0].ubicacion

                 for (vector in listaDeVectores){
                     vectorAMover.intentarInfectar(vector)
                     vectorDAO.guardar(vector)
                 }
                 vectorDAO.guardar(vectorAMover)
             }else{
                 val ubicacionAMover = ubicacionDAO.recuperar(ubicacionid)
                 vectorAMover.ubicacion = ubicacionAMover
                 vectorDAO.guardar(vectorAMover)
             }
         }
    }

    override fun expandir(ubicacionId: Long) {
        runTrx {
            val vectores = ubicacionDAO.vectoresEn(ubicacionId).toMutableList()
            if (vectores.isNotEmpty()) {
                val dado = Randomizador.getInstance()
                val numeroAleatorio = dado.valor(0, vectores.size-1)
                val vectorContagioso = vectores.removeAt(numeroAleatorio)
                for(vector in vectores){
                    vectorContagioso.intentarInfectar(vector)
                }
            }
        }

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

    override fun vectoresEn(id: Long): List<Vector> {
        return runTrx {
            ubicacionDAO.vectoresEn(id).toList()
        }
    }

}