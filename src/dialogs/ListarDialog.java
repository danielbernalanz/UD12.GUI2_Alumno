/**
 * 
 */
package dialogs;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

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
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cerrar) {
			dispose();
		}
	}

}
