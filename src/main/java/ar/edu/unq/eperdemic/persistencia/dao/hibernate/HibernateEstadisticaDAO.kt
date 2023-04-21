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


    override fun cantidadVectoresPresentes(nombreDeLaUbicacion: String) : Long {
        val session = TransactionRunner.currentSession

        val hql = "select count(*)\n" +
                "from Vector v\n" +
                "where v.ubicacion.id = (SELECT id FROM Ubicacion WHERE nombre = :nombreUbicacion)"

        val query = session.createQuery(hql, java.lang.Long::class.java)
        query.setParameter("nombreUbicacion", nombreDeLaUbicacion)

        return query.singleResult.toLong()
    }

    override fun cantidadVectoresInfectados(nombreDeLaUbicacion: String) : Long {
        val session = TransactionRunner.currentSession
        
        val hql = "select count(distinct v.id) " +
                "from Vector v " +
                "join v.especiesContagiadas es " +
                "where v.ubicacion.nombre = :nombreUbicacion"

        val query = session.createQuery(hql, java.lang.Long::class.java)
        query.setParameter("nombreUbicacion", nombreDeLaUbicacion)

        return query.singleResult.toLong()
    }

    override fun nombreEspecieQueMasInfectaVectores(nombreDeLaUbicacion: String) : String {
        val session = TransactionRunner.currentSession

        val hql = "select es.nombre " +
                "from Especie es " +
                "join Vector v ON v.especiesContagiadas.id = es.id" +
                "group by v.especiesContagiadas.id " +
                "order by count(es.vectores.id) desc"

        val query = session.createQuery(hql, String::class.java)
        query.setParameter("nombreUbicacion", nombreDeLaUbicacion)
        query.maxResults = 1

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

