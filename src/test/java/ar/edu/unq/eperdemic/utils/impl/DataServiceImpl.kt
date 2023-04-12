package ar.edu.unq.eperdemic.utils.impl

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.DataDAO
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateDataDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernatePatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.utils.DataService

class DataServiceImpl(): DataService {

    val patogenoDao: PatogenoDAO = HibernatePatogenoDAO()
    val vectorDao: VectorDAO = HibernateVectorDAO()
    val dataDao: DataDAO = HibernateDataDAO()

    override fun eliminarTodo() {
        dataDao.clear()
    }

    override fun crearSetDeDatosIniciales() {

        val patogeno = Patogeno("Tipo 0")
        for (i in 0..20){
            patogeno.id = null
            patogeno.tipo = "Tipo $i"
            patogeno.cantidadDeEspecies = i
            patogenoDao.guardar(patogeno)
        }

        val vector = Vector(TipoDeVector.Insecto)
        for (i in 0 .. 20) {
            vector.id = null
            vector.ubicacion = Ubicacion("Lugar $i")
            vector.tipo = listOf<TipoDeVector>(TipoDeVector.Insecto, TipoDeVector.Animal, TipoDeVector.Persona)[i % 3]
            vectorDao.guardar(vector)
        }
    }
}
