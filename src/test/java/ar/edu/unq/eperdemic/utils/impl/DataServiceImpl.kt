package ar.edu.unq.eperdemic.utils.impl

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.DataDAO
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.UbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateDataDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernatePatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner
import ar.edu.unq.eperdemic.utils.DataService

class DataServiceImpl(): DataService {

    val patogenoDao: PatogenoDAO = HibernatePatogenoDAO()
    val vectorDao: VectorDAO = HibernateVectorDAO()
    val ubicacionDao: UbicacionDAO = HibernateUbicacionDAO()
    val dataDao: DataDAO = HibernateDataDAO()

    override fun eliminarTodo() {
        TransactionRunner.runTrx { dataDao.clear() }
    }

    override fun crearSetDeDatosIniciales() {

        TransactionRunner.runTrx {
            for (i in 0..20){
                var patogeno = Patogeno("Tipo $i")
                patogeno.tipo = "Tipo $i"
                patogeno.cantidadDeEspecies = i
                patogenoDao.guardar(patogeno)
            }

            for (i in 0 .. 20) {
                var vector = Vector(
                    listOf(TipoDeVector.Insecto, TipoDeVector.Animal, TipoDeVector.Persona)[i % 3]
                )
                var ubicacion = Ubicacion("Lugar $i")
                vector.ubicacion = ubicacion
                ubicacionDao.guardar(ubicacion)
                vectorDao.guardar(vector)
            }
        }

    }
}
