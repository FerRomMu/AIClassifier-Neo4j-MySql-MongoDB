package ar.edu.unq.eperdemic.utils.impl

import ar.edu.unq.eperdemic.exceptions.InvalidDataTypeException
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

    override fun persistir(entidades: List<Any>): List<Any> {
        return TransactionRunner.runTrx {
            entidades.forEach { entidad ->
                when (entidad) {
                    is Patogeno -> patogenoDao.guardar(entidad)
                    is Vector -> vectorDao.guardar(entidad)
                    is Especie -> especieDAO.guardar(entidad)
                    is Ubicacion -> ubicacionDao.guardar(entidad)
                    else -> throw InvalidDataTypeException("El dato no es persistible.")
                }
            }
            entidades
        }
    }

    override fun persistir(entidad: Any): Any {
        return this.persistir(listOf(entidad)).first()
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
                var especie = patogeno.crearEspecie("Especie $i", "Pais $i")
                vector.especiesContagiadas.add(especie)
                ubicacionDao.guardar(ubicacion)
                especieDAO.guardar(especie)
                vectorDao.guardar(vector)
            }
        }

    }
    override fun crearPandemiaPositiva(): Especie {
        return TransactionRunner.runTrx {
            var patogeno = Patogeno("Gripe")
            patogenoDao.guardar(patogeno)
            var especie1 = Especie(patogeno, "virusT", "raccoon city")
            especieDAO.guardar(especie1)

            for (i in 0..20){
                var vector = Vector(
                    listOf(TipoDeVector.Insecto, TipoDeVector.Animal, TipoDeVector.Persona)[i % 3]
                )
                var ubicacion = Ubicacion("Lugar $i")
                vector.ubicacion = ubicacion

                vector.especiesContagiadas.add(especie1)
                ubicacionDao.guardar(ubicacion)
                vectorDao.guardar(vector)
            }
            especie1
        }
    }
}
