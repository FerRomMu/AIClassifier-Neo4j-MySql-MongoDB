package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner

class HibernateVectorDAO: HibernateDAO<Vector>(Vector::class.java), VectorDAO {

    override fun recuperarTodos(): List<Vector> {
        val session = TransactionRunner.currentSession

        val hql = "from vector"

        val query = session.createQuery(hql, Vector::class.java)

        return query.resultList
    }

}