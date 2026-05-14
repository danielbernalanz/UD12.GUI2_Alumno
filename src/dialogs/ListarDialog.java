/**
 * 
 */
package dialogs;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;

import dao.AccesoTrabajador;
import excepciones.BDException;
import excepciones.TrabajadorException;
import modelo.Empresa;
import modelo.Trabajador;

/**
 * 
 * @author usuario
 *
 */
public class ListarDialog extends JDialog implements ActionListener {

	Empresa empresa;
	JTable tabla;
	JButton cerrar;
	JButton exportarCSV;
	JButton exportarJSON;
	java.util.List<Trabajador> trabajadores;

	public ListarDialog(Empresa empresa) {
		this.empresa = empresa;

		setResizable(false);
		setTitle("Listado Trabajadores");
		setSize(750, 700);
		setLayout(new FlowLayout());
		setLocationRelativeTo(null);

		String[] columnas = { "Identificador", "DNI", "Nombre", "Apellidos", "Direcci\u00f3n", "Tel\u00e9fono", "Puesto" };
		String[][] datos = null;

		try {
			trabajadores = AccesoTrabajador.consultarTrabajadores();
			datos = new String[trabajadores.size()][7];
			for (int i = 0; i < trabajadores.size(); i++) {
				Trabajador t = trabajadores.get(i);
				datos[i][0] = String.valueOf(t.getIdentificador());
				datos[i][1] = t.getDni();
				datos[i][2] = t.getNombre();
				datos[i][3] = t.getApellidos();
				datos[i][4] = t.getDireccion();
				datos[i][5] = t.getTelefono();
				datos[i][6] = t.getPuesto();
			}
		} catch (BDException | TrabajadorException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			dispose();
			return;
		}

		tabla = new JTable(datos, columnas);
		JScrollPane jsp = new JScrollPane(tabla);
		jsp.setPreferredSize(new Dimension(700, 600));
		add(jsp);

		cerrar = new JButton("Cerrar");
		cerrar.addActionListener(this);
		add(cerrar);

		exportarCSV = new JButton("Exportar CSV");
		exportarCSV.addActionListener(this);
		add(exportarCSV);

		exportarJSON = new JButton("Exportar JSON");
		exportarJSON.addActionListener(this);
		add(exportarJSON);

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cerrar) {
			dispose();
		} else if (e.getSource() == exportarCSV) {
			exportarCSV();
		} else if (e.getSource() == exportarJSON) {
			exportarJSON();
		}
	}

	private void exportarCSV() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Exportar a CSV");
		fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos CSV (*.csv)", "csv"));

		int resultado = fileChooser.showSaveDialog(this);
		if (resultado != JFileChooser.APPROVE_OPTION) return;

		String ruta = fileChooser.getSelectedFile().getAbsolutePath();
		if (!ruta.endsWith(".csv")) ruta += ".csv";

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta, java.nio.charset.StandardCharsets.UTF_8))) {
			bw.write("Identificador;DNI;Nombre;Apellidos;Direccion;Telefono;Puesto");
			bw.newLine();
			for (Trabajador t : trabajadores) {
				bw.write(String.join(";",
					t.getIdentificador() + "",
					"\"" + t.getDni() + "\"",
					"\"" + t.getNombre() + "\"",
					"\"" + t.getApellidos() + "\"",
					"\"" + t.getDireccion() + "\"",
					"\"" + t.getTelefono() + "\"",
					"\"" + t.getPuesto() + "\""
				));
				bw.newLine();
			}
			JOptionPane.showMessageDialog(this, "CSV exportado correctamente.", "\u00c9xito", JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this, "Error al exportar CSV: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void exportarJSON() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Exportar a JSON");
		fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos JSON (*.json)", "json"));

		int resultado = fileChooser.showSaveDialog(this);
		if (resultado != JFileChooser.APPROVE_OPTION) return;

		String ruta = fileChooser.getSelectedFile().getAbsolutePath();
		if (!ruta.endsWith(".json")) ruta += ".json";

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta, java.nio.charset.StandardCharsets.UTF_8))) {
			StringWriter sw = new StringWriter();
			sw.write("[\n");
			for (int i = 0; i < trabajadores.size(); i++) {
				Trabajador t = trabajadores.get(i);
				sw.write("  {\n");
				sw.write("    \"identificador\": " + t.getIdentificador() + ",\n");
				sw.write("    \"dni\": \"" + escaparJSON(t.getDni()) + "\",\n");
				sw.write("    \"nombre\": \"" + escaparJSON(t.getNombre()) + "\",\n");
				sw.write("    \"apellidos\": \"" + escaparJSON(t.getApellidos()) + "\",\n");
				sw.write("    \"direccion\": \"" + escaparJSON(t.getDireccion()) + "\",\n");
				sw.write("    \"telefono\": \"" + escaparJSON(t.getTelefono()) + "\",\n");
				sw.write("    \"puesto\": \"" + escaparJSON(t.getPuesto()) + "\"\n");
				sw.write("  }");
				if (i < trabajadores.size() - 1) sw.write(",");
				sw.write("\n");
			}
			sw.write("]\n");
			bw.write(sw.toString());
			JOptionPane.showMessageDialog(this, "JSON exportado correctamente.", "\u00c9xito", JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this, "Error al exportar JSON: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private String escaparJSON(String valor) {
		return valor.replace("\\", "\\\\")
					.replace("\"", "\\\"")
					.replace("\n", "\\n")
					.replace("\r", "\\r")
					.replace("\t", "\\t");
	}

}
