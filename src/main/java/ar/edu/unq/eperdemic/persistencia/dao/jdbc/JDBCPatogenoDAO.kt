package ar.edu.unq.eperdemic.persistencia.dao.jdbc

import ar.edu.unq.eperdemic.exceptions.DataDuplicationException
import ar.edu.unq.eperdemic.exceptions.DataNotFoundException
import ar.edu.unq.eperdemic.exceptions.IdNotFoundException
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.jdbc.JDBCConnector.execute
import java.sql.Connection
import java.sql.Statement


class JDBCPatogenoDAO : PatogenoDAO {

    override fun crear(patogeno: Patogeno): Patogeno {
        if (patogeno.id != null) { throw DataDuplicationException("El patogeno ya existe en la base de datos.") }
        execute { conn: Connection ->
            conn.prepareStatement("INSERT INTO patogeno (tipo, cantidadDeEspecies) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS)
                .use  { ps ->
                    ps.setString(1, patogeno.tipo)
                    ps.setInt(2, patogeno.cantidadDeEspecies)
                    ps.execute()
                    val rs = ps.generatedKeys
                    if (rs.next()) {
                        patogeno.id = rs.getLong(1)
                    }
                }
        }
        return patogeno;
    }

    override fun actualizar(patogeno: Patogeno) {
        patogeno.id?: throw IdNotFoundException("No se encontró Id en el patógeno dado.")
        execute { conn: Connection ->
            conn.prepareStatement("UPDATE patogeno SET tipo = ?, cantidadDeEspecies = ? WHERE id = ?")
                .use { ps ->
                    ps.setString(1, patogeno.tipo)
                    ps.setInt(2, patogeno.cantidadDeEspecies)
                    ps.setLong(3,patogeno.id!!)
                    val rowsAffected = ps.executeUpdate()
                    if (rowsAffected == 0) {
                        throw DataNotFoundException("No se encontró ningún patógeno con el ID ${patogeno.id}")
                    }
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
                    patogeno?: throw DataNotFoundException("No hay patogeno con el Id dado.")
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