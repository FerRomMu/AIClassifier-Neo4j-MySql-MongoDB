package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.UbicacionDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner

 open class HibernateUbicacionDAO: HibernateDAO<Ubicacion>(Ubicacion::class.java), UbicacionDAO{

  override fun recuperarTodos(): List<Ubicacion> {
      val session = TransactionRunner.currentSession

      val hql = "from Ubicacion"

      val query = session.createQuery(hql, Ubicacion::class.java)

   return query.resultList
  }

     override fun vectoresDeY(ubicacionId: Long?,vectorId: Long?): Collection<Vector> {
         val session = TransactionRunner.currentSession

         val hql = "from Vector v where v.id = :idVec or v.ubicacion.id = :idUbi "

         val query = session.createQuery(hql, Vector::class.java)

         query.setParameter("idUbi", ubicacionId!!)
         query.setParameter("idVec", vectorId!!)

         return query.resultList
     }


 }

