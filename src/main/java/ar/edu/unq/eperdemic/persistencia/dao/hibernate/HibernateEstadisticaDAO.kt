package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.exceptions.DataNotFoundException
import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.ReporteDeContagios
import ar.edu.unq.eperdemic.modelo.TipoDeVector
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
            "where v.tipo = :tipoPersona\n" +
            "group by es.id \n" +
            "order by count(es.id) \n" +
            "desc"

        val query = session.createQuery(hql, Especie::class.java)
        query.setParameter("tipoPersona", TipoDeVector.Persona)
        query.maxResults = 1

        return query.singleResult
    }


    override fun cantidadVectoresPresentes(nombreDeLaUbicacion: String) : Long {
        val session = TransactionRunner.currentSession

        val hql = "select count(*)\n" +
                "from Vector v\n" +
                "where v.ubicacion.nombre = :nombreUbicacion"

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
        try {
            val session = TransactionRunner.currentSession

            val hql = "select es.nombre " +
                    "from Vector v " +
                    "join v.especiesContagiadas es " +
                    "where v.ubicacion.nombre = :nombreUbicacion " +
                    "group by es.nombre " +
                    "order by count(v) desc"

            val query = session.createQuery(hql, String::class.java)
            query.setParameter("nombreUbicacion", nombreDeLaUbicacion)
            query.maxResults = 1

            return query.singleResult
        } catch (e: Exception) {
            throw DataNotFoundException("No existe un dato válido a devolver.")
        }
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

