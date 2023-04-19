package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.EstadisticaDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner

class HibernateEstadisticaDAO  : HibernateDAO<EstadisticaDAO>(EstadisticaDAO::class.java), EstadisticaDAO {

    override fun especieLider():Especie {
        val session = TransactionRunner.currentSession

        val hql = "SELECT e FROM Vector v JOIN Especie e ON v.especiesContagiadas.id = e.id WHERE  v.TipoDeVector = TipoDeVector.Persona GROUP BY e.id ORDER BY count(e.id) desc"
        // Cambiar --> Query sql =
        // select e. * from (Vector v JOIN Especie_vector es ON v.id = es.vectores_id) JOIN Especie e ON es.Especie_id = e.id where v.tipo = 0 GROUP BY e.id ORDER BY count(e.id) desc limit 1 ;



        val query = session.createQuery(hql, Especie::class.java)
        query.maxResults = 1

        return query.singleResult
    }

}

