package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Randomizador
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.persistencia.dao.UbicacionDAO
import ar.edu.unq.eperdemic.services.UbicacionService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx

class UbicacionServiceImpl(val ubicacionDAO: UbicacionDAO): UbicacionService {

    override fun mover(vectorId: Long, ubicacionid: Long) {
         runTrx {
            var listaDeVectores = ubicacionDAO.vectoresDeY(ubicacionid, vectorId).toList()
            var vectorAMover = listaDeVectores.find( {it.id == vectorId} )

             if(listaDeVectores.size > 1){
                 vectorAMover!!.ubicacion = listaDeVectores.get(0).ubicacion

                 for (vector in listaDeVectores){
                     vectorAMover.intentarInfectar(vector)
                     // Guardar listaDeVectores
                 }
                 // Guardar vectorAMover
             }
         }

    }

    override fun expandir(ubicacionId: Long) {
        runTrx {
            val vectores = ubicacionDAO.traerVectoresQueEstanEn(ubicacionId).toList()
            if (vectores.isNotEmpty()) {
                val dado = Randomizador().getInstance()
                val numeroAleatorio = dado.valor(0, vectores.size-1)
                val vectorContagioso = vectores.get(numeroAleatorio)
                for(vector in vectores){
                    vectorContagioso.intentarInfectar(vector)
                }
            }
        }

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