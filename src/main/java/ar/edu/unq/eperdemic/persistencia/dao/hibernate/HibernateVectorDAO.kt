package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO

class HibernateVectorDAO: HibernateDAO<Vector>(Vector::class.java), VectorDAO {

    override fun recuperarTodos(): List<Vector> {
        TODO("Not yet implemented")
    }

}