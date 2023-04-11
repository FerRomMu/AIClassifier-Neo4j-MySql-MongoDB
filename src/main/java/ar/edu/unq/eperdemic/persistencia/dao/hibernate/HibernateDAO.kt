package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.exceptions.IdNotFoundException
import ar.edu.unq.eperdemic.services.runner.TransactionRunner


open class HibernateDAO<T>(private val entityType: Class<T>) {

    fun guardar(entity: T) {
        val session = TransactionRunner.currentSession
        session.save(entity)
    }

    fun recuperar(id: Long?): T {
        val session = TransactionRunner.currentSession
        return session.get(entityType, id) ?: throw IdNotFoundException("El id no fue encontrado")
    }
}