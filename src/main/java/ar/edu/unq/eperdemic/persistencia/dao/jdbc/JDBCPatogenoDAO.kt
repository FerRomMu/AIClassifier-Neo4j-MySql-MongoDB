package ar.edu.unq.eperdemic.persistencia.dao.jdbc

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.jdbc.JDBCConnector.execute
import java.sql.Connection


class JDBCPatogenoDAO : PatogenoDAO {

    override fun crear(patogeno: Patogeno): Patogeno {
        execute { conn: Connection ->
            conn.prepareStatement("INSERT INTO patogeno (tipo, id, cantidadDeEspecies) VALUES (?,?,?)")
                .use  { ps ->
                    ps.setString(1, patogeno.tipo)
                    ps.setLong(2, patogeno.id!!)
                    ps.setInt(3, patogeno.cantidadDeEspecies)
                    ps.execute()
                }
        }
        return  patogeno;
    }

    override fun actualizar(patogeno: Patogeno) {
        TODO("not implemented")
    }

    override fun recuperar(patogenoId: Long): Patogeno {
        TODO("not implemented")
    }

    override fun recuperarATodos(): List<Patogeno> {
        TODO("not implemented")
    }

    init {
        val initializeScript = javaClass.classLoader.getResource("createAll.sql").readText()
        execute {connection ->
            val scripts = initializeScript.split(";")
            scripts.forEach{ script ->
                connection.prepareStatement(script)
                    .use {
                        it.execute()
                    }
            }
            null
        }
    }
}