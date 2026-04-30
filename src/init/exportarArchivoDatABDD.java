package init;

import config.ConfigMySql;
import dao.AccesoTrabajador;
import excepciones.BDException;
import excepciones.TrabajadorException;
import ficheros.FicheroDatos;
import modelo.Trabajador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class exportarArchivoDatABDD {
    static void main(String[] args) throws BDException {
        AccesoTrabajador.crearTablaConDatosTrabajador();
    }
}