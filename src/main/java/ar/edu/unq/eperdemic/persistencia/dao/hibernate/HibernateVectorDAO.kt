package ar.edu.unq.eperdemic.persistencia.dao.hibernate


import ar.edu.unq.eperdemic.exceptions.DataNotFoundException
import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner

class HibernateVectorDAO: HibernateDAO<Vector>(Vector::class.java), VectorDAO {

    override fun vectorAleatorioEn(ubicacionId: Long): Vector {
        val session = TransactionRunner.currentSession

        val hql = """
            from Vector v 
            where v.ubicacion.id = :idUbi
            order by rand()
            """

        val query = session.createQuery(hql, Vector::class.java)

        query.setParameter("idUbi", ubicacionId)

        return query.singleResult ?: throw DataNotFoundException("No hay vectores en la ubicacion dada")
    }

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

