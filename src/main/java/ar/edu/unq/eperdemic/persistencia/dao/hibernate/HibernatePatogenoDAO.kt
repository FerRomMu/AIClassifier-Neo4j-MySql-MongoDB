package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.exceptions.DataNotFoundException
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner

class HibernatePatogenoDAO : HibernateDAO<Patogeno>(Patogeno::class.java), PatogenoDAO {

    override fun vectorAleatorioEn(ubicacionId: Long): Vector {
        val session = TransactionRunner.currentSession

        val hql = """
            from Vector v 
            where v.ubicacion.id = :idUbi
            order by rand()
            limit 1
            """

        val query = session.createQuery(hql, Vector::class.java)

        query.setParameter("idUbi", ubicacionId)

        return query.singleResult ?: throw DataNotFoundException("No hay vectores en la ubicacion dada")
    }
}