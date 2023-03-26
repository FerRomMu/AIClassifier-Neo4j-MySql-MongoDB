package ar.edu.unq.eperdemic.persistencia.dao.jdbc

import ar.edu.unq.eperdemic.persistencia.dao.DataDAO
import ar.edu.unq.eperdemic.persistencia.dao.jdbc.JDBCConnector.execute
import java.sql.Connection

class JDBCDataDAO: DataDAO {

    override fun clear() {
        execute { conn: Connection ->
            conn.prepareStatement("DELETE FROM patogeno WHERE id!=0").use { ps ->
                ps.execute()
            }
        }
        execute { conn: Connection ->
            conn.prepareStatement("DELETE FROM patogeno WHERE id=0").use { ps ->
                ps.execute()
            }
        }
    }

    init {
        val initializeScript = javaClass.classLoader.getResource("createAll.sql").readText()
        JDBCConnector.execute {
            it.prepareStatement(initializeScript).use { ps -> ps.execute() }
        }
    }
}