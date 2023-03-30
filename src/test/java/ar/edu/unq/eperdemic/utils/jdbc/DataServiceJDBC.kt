package ar.edu.unq.eperdemic.utils.jdbc

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.DataDAO
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.utils.DataService

class DataServiceJDBC (val patogenoDao: PatogenoDAO, val dataDao: DataDAO): DataService {

    override fun eliminarTodo() {
        dataDao.clear()
    }

    override fun crearSetDeDatosIniciales() {
        val patogeno = Patogeno("Tipo 0")
        for (i in 0..20){
            patogeno.tipo = "Tipo $i"
            patogeno.cantidadDeEspecies = i
            patogenoDao.crear(patogeno)
        }
    }
}