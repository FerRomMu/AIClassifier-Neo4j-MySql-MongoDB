package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.DataDAO
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.services.DataService

class DataServiceImpl(val patogenoDao: PatogenoDAO, val dataDao: DataDAO): DataService {

    override fun deleteAll() {
        dataDao.clear()
    }

    override fun crearSetDatosIniciales() {
        val patogeno = Patogeno("Tipo 0")
        for (i in 0..50){
            patogeno.id = i.toLong()
            patogeno.tipo = "Tipo $i"
            patogeno.cantidadDeEspecies = i
            patogenoDao.crear(patogeno)
        }
    }

}