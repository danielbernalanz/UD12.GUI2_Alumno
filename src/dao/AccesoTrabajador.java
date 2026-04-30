package dao;

import config.ConfigMySql;
import excepciones.BDException;
import excepciones.TrabajadorException;
import ficheros.FicheroDatos;
import modelo.Trabajador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccesoTrabajador {
    public static void crearTablaConDatosTrabajador() throws BDException {
        Connection conexion = null;
        ArrayList<Trabajador> trabajadores = FicheroDatos.obtenerTrabajadores("ficheroDatos/empresa.dat");
        try {
            conexion = ConfigMySql.abrirConexion();

            String tabla = "CREATE TABLE IF NOT EXISTS trabajador(" +
                    "identificador INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "dni VARCHAR(20) NOT NULL," +
                    "nombre VARCHAR(20) NOT NULL," +
                    "apellidos VARCHAR(20) NOT NULL," +
                    "direccion VARCHAR(20) NOT NULL," +
                    "telefono VARCHAR(20) NOT NULL," +
                    "puesto VARCHAR(20) NOT NULL)";
            PreparedStatement sentencia = conexion.prepareStatement(tabla);
            sentencia.execute();
            System.out.println("Tabla creada");
            for (Trabajador trabajador : trabajadores) {
                if (AccesoTrabajador.consultarTrabajadorPorIdentificador(trabajador.getIdentificador()).isEmpty()){
                    insertarTrabajador(conexion, trabajador);
                } else {
                    AccesoTrabajador.actualizarTrabajador(trabajador.getIdentificador(),trabajador.getNombre(),trabajador.getApellidos(),trabajador.getDireccion(),trabajador.getTelefono(), trabajador.getPuesto());
                }
            }
            System.out.println("Trabajadores insertados");
        } catch (BDException | SQLException | TrabajadorException e) {
            throw new RuntimeException(e);
        } finally {
            if (conexion != null) {
                ConfigMySql.cerrarConexion(conexion);
            }
        }
    }

    public static void insertarTrabajador(Connection conexion, Trabajador trabajador) throws SQLException, TrabajadorException {
        String sql = "INSERT INTO trabajador(dni,nombre,apellidos,direccion,telefono,puesto) VALUES(?,?,?,?,?,?)";

        PreparedStatement sentencia = conexion.prepareStatement(sql);

            sentencia.setString(1, trabajador.getDni());
            sentencia.setString(2, trabajador.getNombre());
            sentencia.setString(3, trabajador.getApellidos());
            sentencia.setString(4, trabajador.getDireccion());
            sentencia.setString(5, trabajador.getTelefono());
            sentencia.setString(6, trabajador.getPuesto());

        int filas = sentencia.executeUpdate();
        if (filas == 0) {
            throw new TrabajadorException(TrabajadorException.NO_ANADIDO);
        }
    }

    public static void eliminarTrabajador(String identificador) throws BDException, TrabajadorException {
        Connection conexion = null;
        int columnasEliminadas;

        try {
            conexion = ConfigMySql.abrirConexion();
            String sentenciaEliminarTrabajador = "DELETE FROM trabajador WHERE identificador = ?;";
            PreparedStatement sentencia = conexion.prepareStatement(sentenciaEliminarTrabajador);
            sentencia.setString(1, identificador);
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

    public static boolean actualizarTrabajador(int identificador, String nombre, String apellidos,
                                               String direccion, String telefono, String puesto) throws BDException {

        String sql = "UPDATE trabajador " +
                "SET nombre = ?, apellidos = ?, direccion = ?, telefono = ?, puesto = ? " +
                "WHERE identificador = ?";

        try (Connection conexion = ConfigMySql.abrirConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, apellidos);
            ps.setString(3, direccion);
            ps.setString(4, telefono);
            ps.setString(5, puesto);
            ps.setInt(6, identificador);

            int filasActualizadas = ps.executeUpdate();
            return filasActualizadas > 0;

        } catch (SQLException e) {
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        }
    }
    public static List<Trabajador> consultarTrabajadorPorIdentificador(int identificador) throws BDException, TrabajadorException {
        Connection conexion = null;
        List<Trabajador> listaTrabajadores = new ArrayList<>();

        try {
            conexion = ConfigMySql.abrirConexion();

            String sql = "SELECT * FROM trabajador where identificador = ? ;";
            PreparedStatement sentencia = conexion.prepareStatement(sql);
            sentencia.setInt(1, identificador);
            ResultSet rs = sentencia.executeQuery();

            while (rs.next()) {
                Trabajador Trabajador = new Trabajador(
                        rs.getInt("identificador"),
                        rs.getString("dni"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getString("puesto")

                        );
                listaTrabajadores.add(Trabajador);
            }
            rs.close();
            sentencia.close();
            if (listaTrabajadores.isEmpty()){
                throw new TrabajadorException(TrabajadorException.SIN_RESULTADOS);
            }
        } catch (SQLException e) {
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        } finally {
            if (conexion != null) {
                ConfigMySql.cerrarConexion(conexion);
            }
        }

        return listaTrabajadores;
    }
    public static List<Trabajador> consultarTrabajadores() throws BDException, TrabajadorException {
        Connection conexion = null;
        List<Trabajador> listaTrabajador = new ArrayList<>();

        try {
            // TODO Test git pushes
            conexion = ConfigMySql.abrirConexion();

            String sql = "SELECT * FROM trabajador;";
            PreparedStatement sentencia = conexion.prepareStatement(sql);
            ResultSet rs = sentencia.executeQuery();
            while (rs.next()) {
                Trabajador trabajador = new Trabajador(
                        rs.getInt("identificador"),
                        rs.getString("dni"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getString("puesto")

                );
                listaTrabajador.add(trabajador);
            }
            rs.close();
            sentencia.close();
            if (listaTrabajador.isEmpty()){
                throw new TrabajadorException(TrabajadorException.SIN_RESULTADOS);
            }
        } catch (SQLException e) {
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        }  finally {
            if (conexion != null) {
                ConfigMySql.cerrarConexion(conexion);
            }
        }

        return listaTrabajador;
    }
}