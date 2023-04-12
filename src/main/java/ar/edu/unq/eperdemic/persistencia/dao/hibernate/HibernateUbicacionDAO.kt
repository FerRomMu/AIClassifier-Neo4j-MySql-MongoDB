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


 }

