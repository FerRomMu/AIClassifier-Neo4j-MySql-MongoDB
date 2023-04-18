package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.exceptions.IdNotFoundException
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.services.runner.TransactionRunner


open class HibernateDAO<T>(private val entityType: Class<T>) {

    fun guardar(entity: T) {
        val session = TransactionRunner.currentSession
        session.saveOrUpdate(entity)
    }

    fun recuperar(id: Long?): T {
        val session = TransactionRunner.currentSession
        return session.get(entityType, id) ?: throw IdNotFoundException("El id no fue encontrado")
    }

    fun borrar(id: Long?) {
        val session = TransactionRunner.currentSession

        val entity = session.get(entityType, id) ?: throw IdNotFoundException("El id no fue encontrado")

        session.delete(entity)
    }

    open fun recuperarTodos(): List<T> {
        val session = TransactionRunner.currentSession
        val name = entityType.simpleName
        val hql = "from $name"
        val query = session.createQuery(hql, entityType)
        return query.resultList

    }
}