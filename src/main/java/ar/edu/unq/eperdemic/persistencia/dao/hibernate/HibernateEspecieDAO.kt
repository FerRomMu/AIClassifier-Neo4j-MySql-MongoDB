package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.persistencia.dao.EspecieDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner

class HibernateEspecieDAO : HibernateDAO<Especie>(Especie::class.java), EspecieDAO {

    override fun cantidadDeInfectados(especieId: Long): Int {
        val session = TransactionRunner.currentSession

        val hql = "SELECT COUNT(*)\n" +
                "FROM Especie es\n" +
                "JOIN es.vectores e\n" +
                "WHERE es.id = :idDado\n"

        val query = session.createQuery(hql, java.lang.Long::class.java)
        query.setParameter("idDado", especieId)

        return query.uniqueResult().toInt()

    }
}