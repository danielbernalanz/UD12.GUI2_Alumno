package debug;

import config.ConfigMySql;
import excepciones.BDException;
import excepciones.TrabajadorException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Debug {
    public static void eliminarTodo() throws BDException, TrabajadorException {
        Connection conexion = null;
        int columnasEliminadas;

        try {
            conexion = ConfigMySql.abrirConexion();
            String sentenciaEliminarTrabajador = "DELETE FROM trabajador ;";
            PreparedStatement sentencia = conexion.prepareStatement(sentenciaEliminarTrabajador);
            columnasEliminadas = sentencia.executeUpdate();
            sentencia.close();
            if (columnasEliminadas == 0) {
                throw new TrabajadorException(TrabajadorException.NO_ELIMINADO);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        } finally {
            if (conexion != null) {
                ConfigMySql.cerrarConexion(conexion);
            }
        }

    }
}
