package init;

import dao.AccesoTrabajador;
import excepciones.BDException;

public class ExportarArchivoDatABDD {
    public static void init() throws BDException {
        AccesoTrabajador.crearTablaConDatosTrabajador();
    }
}