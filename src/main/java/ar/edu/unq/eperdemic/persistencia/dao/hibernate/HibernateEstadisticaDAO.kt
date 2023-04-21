package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.ReporteDeContagios
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.EstadisticaDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner

class HibernateEstadisticaDAO  : HibernateDAO<EstadisticaDAO>(EstadisticaDAO::class.java), EstadisticaDAO {

    override fun especieLider():Especie {
        val session = TransactionRunner.currentSession

        val hql =
            "select es \n" +
            "from Vector v \n" +
            "join v.especiesContagiadas es \n" +
            "where v.tipo = 0\n" +
            "group by es.id \n" +
            "order by count(es.id) \n" +
            "desc"

        val query = session.createQuery(hql, Especie::class.java)
        query.maxResults = 1

        return query.singleResult
    }


    fun cantidadVectoresPresentes(nombreDeLaUbicacion: String) : Long {
        val session = TransactionRunner.currentSession

        val hql = "select count(*) " +
                "from Vector v\n" +
                "where v.ubicacion.id = (SELECT id FROM UBICACION WHERE nombre = :nombreUbicacion)"

        val query = session.createQuery(hql, Long::class.java)
        query.setParameter("nombreUbicacion", nombreDeLaUbicacion)

        return query.singleResult
    }

    fun cantidadVectoresInfectados(nombreDeLaUbicacion: String) : Long {
        val session = TransactionRunner.currentSession

        val hql = "select * \n" +
                "from Especie es \n" +
                "join es.vectores v on "

        val query = session.createQuery(hql, Long::class.java)
        query.setParameter("nombreUbicacion", nombreDeLaUbicacion)

        return query.singleResult
    }

    override fun reporteDeContagios(nombreDeLaUbicacion: String): ReporteDeContagios {
        val session = TransactionRunner.currentSession

        val hql = ""

        val query = session.createQuery(hql, ReporteDeContagios::class.java)

        return query.singleResult
    }

    override fun lideres(): List<Especie>{
        val session = TransactionRunner.currentSession

        val hql =
            "select es \n" +
            "from Vector v \n" +
            "join v.especiesContagiadas es \n" +
            "where v.tipo = 0 or v.tipo = 2\n" +
            "group by es.id \n" +
            "order by count(es.id) \n" +
            "desc"

        val query = session.createQuery(hql, Especie::class.java)
        query.maxResults = 10

        return query.resultList
    }


}

