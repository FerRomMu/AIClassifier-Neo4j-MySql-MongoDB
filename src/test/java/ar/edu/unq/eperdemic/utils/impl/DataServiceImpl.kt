package ar.edu.unq.eperdemic.utils.impl

import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.persistencia.dao.*
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.*
import ar.edu.unq.eperdemic.services.runner.TransactionRunner
import ar.edu.unq.eperdemic.utils.DataService

class DataServiceImpl(): DataService {

    val patogenoDao: PatogenoDAO = HibernatePatogenoDAO()
    val vectorDao: VectorDAO = HibernateVectorDAO()
    val ubicacionDao: UbicacionDAO = HibernateUbicacionDAO()
    val dataDao: DataDAO = HibernateDataDAO()
    val especieDAO: EspecieDAO = HibernateEspecieDAO()

    override fun eliminarTodo() {
        TransactionRunner.runTrx { dataDao.clear() }
    }

    override fun crearSetDeDatosIniciales() {

        TransactionRunner.runTrx {
            for (i in 0..20){
                var patogeno = Patogeno("Tipo $i")
                patogeno.tipo = "Tipo $i"
                patogenoDao.guardar(patogeno)

                var vector = Vector(
                    listOf(TipoDeVector.Insecto, TipoDeVector.Animal, TipoDeVector.Persona)[i % 3]
                )
                var ubicacion = Ubicacion("Lugar $i")
                vector.ubicacion = ubicacion
                var especie = Especie("nombre $i", "pais $i", patogeno)
                vector.especiesContagiadas.add(especie)
                ubicacionDao.guardar(ubicacion)
                vectorDao.guardar(vector)

            }
        }

    }
}
