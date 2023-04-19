package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.EstadisticaDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner

class HibernateEstadisticaDAO  : HibernateDAO<EstadisticaDAO>(EstadisticaDAO::class.java), EstadisticaDAO {

    override fun especieLider(): Especie {
        val session = TransactionRunner.currentSession

        val hql = "SELECT es FROM Vector v JOIN v.especiesContagiadas es WHERE  v.TipoDeVector = TipoDeVector.Persona GROUP BY es  ORDER BY count(v.id) desc limit 1 "

        val query = session.createQuery(hql, Especie::class.java)

        return query.singleResult
    }

}

