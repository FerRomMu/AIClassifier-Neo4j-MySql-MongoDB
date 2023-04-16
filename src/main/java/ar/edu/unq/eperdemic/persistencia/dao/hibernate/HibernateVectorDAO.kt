package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner

class HibernateVectorDAO: HibernateDAO<Vector>(Vector::class.java), VectorDAO {

    override fun enfermedades(id: Long?): List<Especie> {
        val session = TransactionRunner.currentSession

        val hql = "SELECT es\n" +
                "FROM Vector v\n" +
                "JOIN v.especiesContagiadas es\n" +
                "WHERE v.id = :idDado"

        val query = session.createQuery(hql, Especie::class.java)
        query.setParameter("idDado", id!!)

        return query.resultList
    }

}
