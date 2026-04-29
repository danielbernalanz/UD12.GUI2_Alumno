package init;

import config.ConfigMySql;
import excepciones.BDException;
import ficheros.FicheroDatos;
import modelo.Trabajador;

import java.sql.Connection;
import java.util.ArrayList;

public class exportarArchivoDatABDD {
    static void main(String[] args) throws BDException {
        Connection conexion = null;
        ArrayList<Trabajador> trabajadores = FicheroDatos.obtenerTrabajadores("ficheroDatos/empresa.dat");
        for (Trabajador trabajador : trabajadores) {
            try {
                conexion = ConfigMySql.abrirConexion();

               // String sentenciaInsertarLibro = "INSERT INTO libro(isbn,titulo,escritor,anio_publicacion,puntuacion) VALUES(?,?,?,?,?);";
                String tabla = "CREATE TABLE TRABAJADOR(" +
                        "identificador INT NOT NULL AUTOINCREMENT," +
                        "dni VARCHAR(20) NOT NULL," +
                        "nombre VARCHAR(20) NOT NULL," +
                        "apellidos VARCHAR(20) NOT NULL," +
                        "direccion VARCHAR(20) NOT NULL," +
                        "telefono VARCHAR(20) NOT NULL," +
                        "puesto VARCHAR(20) NOT NULL,";

            } catch (BDException e) {
                throw new RuntimeException(e);
            }
            finally {
                if (conexion != null){
                    ConfigMySql.cerrarConexion(conexion);
                }
            }
        }
    }
}
