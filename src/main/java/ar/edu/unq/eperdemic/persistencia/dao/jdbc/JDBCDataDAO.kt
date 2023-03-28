package ar.edu.unq.eperdemic.persistencia.dao.jdbc

import ar.edu.unq.eperdemic.persistencia.dao.DataDAO
import ar.edu.unq.eperdemic.persistencia.dao.jdbc.JDBCConnector.execute
import java.sql.Connection

class JDBCDataDAO: DataDAO {

    override fun clear() {
        execute { conn: Connection ->
            conn.prepareStatement("DELETE FROM patogeno").use { ps ->
                ps.execute()
            }
        }
    }

    init {
        val initializeScript = javaClass.classLoader.getResource("createAll.sql").readText()
        execute {
            it.prepareStatement(initializeScript).use { ps -> ps.execute() }
        }
        execute {
            it.prepareStatement("SET SQL_SAFE_UPDATES=0").use { ps -> ps.execute() }
        }
    }
}