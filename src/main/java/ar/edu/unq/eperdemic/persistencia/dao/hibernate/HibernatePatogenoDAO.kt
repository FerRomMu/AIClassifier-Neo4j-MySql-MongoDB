package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.exceptions.DataNotFoundException
import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner

class HibernatePatogenoDAO : HibernateDAO<Patogeno>(Patogeno::class.java), PatogenoDAO {
    override fun especiesDePatogeno(id: Long?): List<Especie> {
        val session = TransactionRunner.currentSession

        val hql = "select es\n " +
                "from Patogeno p " +
                "join p.especies es\n" +
                "where p.id = :idPatogeno"

        val query = session.createQuery(hql, Especie::class.java)
        query.setParameter("idPatogeno", id)

        return query.resultList
    }
}