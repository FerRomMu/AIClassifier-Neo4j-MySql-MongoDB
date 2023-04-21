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

    override fun esPandemia(especieId: Long): Boolean {
        val session = TransactionRunner.currentSession

        val hql =
            "select \n" +
                    "(count(distinct v.ubicacion.id) * 2) >= (select count(*) from Ubicacion) as cantidad_ubicaciones_mayor_a_la_mitad \n" +
                    "from Vector v \n" +
                    "join v.especiesContagiadas es \n" +
                    "where es.id = :idEsp \n"

        val query = session.createQuery(hql, Boolean::class.javaObjectType)
        query.setParameter("idEsp", especieId)

        return query.singleResult
    }
}