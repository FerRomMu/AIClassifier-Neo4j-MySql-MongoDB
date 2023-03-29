package ar.edu.unq.eperdemic.persistencia.dao.jdbc

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.jdbc.JDBCConnector.execute
import java.sql.Connection


class JDBCPatogenoDAO : PatogenoDAO {

    override fun crear(patogeno: Patogeno): Patogeno {
        execute { conn: Connection ->
            conn.prepareStatement("INSERT INTO patogeno (tipo, cantidadDeEspecies) VALUES (?,?)")
                .use  { ps ->
                    ps.setString(1, patogeno.tipo)
                    ps.setInt(2, patogeno.cantidadDeEspecies)
                    ps.execute()
                }
        }
        return patogeno;
    }

    override fun actualizar(patogeno: Patogeno) {
        execute { conn: Connection ->
            conn.prepareStatement("UPDATE patogeno SET tipo = ?, cantidadDeEspecies = ? WHERE id = ?")
                .use { ps ->
                    ps.setString(1, patogeno.tipo)
                    ps.setInt(2, patogeno.cantidadDeEspecies)
                    ps.setLong(3,patogeno.id!!)
                    ps.execute()
                }
        }
    }

    override fun recuperar(patogenoId: Long): Patogeno {
        var pID =  patogenoId.toInt();
        return execute { conn: Connection ->
            conn.prepareStatement("SELECT * FROM patogeno WHERE id = ?")
                .use { ps ->
                    ps.setInt(1, pID)
                    val resultSet = ps.executeQuery()
                    var patogeno: Patogeno? = null
                    while (resultSet.next()) {
                        if (patogeno != null) {
                            throw RuntimeException("Existe mas de un patogeno con el id $pID")
                        }
                        patogeno = Patogeno(resultSet.getString("tipo"))
                        patogeno.cantidadDeEspecies = resultSet.getInt("cantidadDeEspecies")
                        patogeno.id = patogenoId;
                    }
                    patogeno!!
                }
        }

    }

    override fun recuperarATodos(): List<Patogeno> {


            return JDBCConnector.execute { conn: Connection ->
                conn.prepareStatement("SELECT id, tipo, cantidadDeEspecies FROM patogeno ORDER BY tipo ASC")
                    .use { ps ->
                        val resultSet = ps.executeQuery()
                        var patogeno: Patogeno? = null
                        val patogenos = mutableListOf<Patogeno>();
                        while (resultSet.next()) {
                            patogeno = Patogeno(resultSet.getString("tipo"))

                            var pID =  resultSet.getInt("id").toLong();
                            patogeno.id = pID;

                            patogeno.cantidadDeEspecies = resultSet.getInt("cantidadDeEspecies")
                            patogenos.add(patogeno)
                        }
                        patogenos.toList()
                    }
            }
        }


    init {
        val initializeScript = javaClass.classLoader.getResource("createAll.sql").readText()
        execute {
            it.prepareStatement(initializeScript).use { ps -> ps.execute() }
        }
    }
}