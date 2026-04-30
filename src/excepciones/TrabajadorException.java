package excepciones;

public class TrabajadorException extends Exception{

	public static final String NO_ANADIDO = "No se ha podido añadir el trabajador";	public static final String ERROR_QUERY = "Error en la consulta ";
	public static final String NO_ELIMINADO = "No se ha podido eliminar el trabajador ";
	public static final String SIN_RESULTADOS = "No se ha encontrado ningun resultado en la consulta";

	public TrabajadorException(String mensaje) {
		super("Error: " + mensaje);
	}
	
}
