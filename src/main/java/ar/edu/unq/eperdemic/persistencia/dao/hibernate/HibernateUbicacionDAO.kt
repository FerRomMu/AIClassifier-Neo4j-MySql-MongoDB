package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.UbicacionDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner

 open class HibernateUbicacionDAO: HibernateDAO<Ubicacion>(Ubicacion::class.java), UbicacionDAO{

     override fun vectoresEn(ubicacionId: Long?): Collection<Vector> {
         val session = TransactionRunner.currentSession

         val hql = "from Vector v where v.ubicacion.id = :idUbi "

         val query = session.createQuery(hql, Vector::class.java)

         query.setParameter("idUbi", ubicacionId!!)

         return query.resultList
     }


 }

