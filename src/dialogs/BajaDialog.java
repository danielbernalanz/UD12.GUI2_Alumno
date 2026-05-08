/**
 * 
 */
package dialogs;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import dao.AccesoTrabajador;
import excepciones.BDException;
import excepciones.TrabajadorException;
import modelo.Empresa;

/**
 * 
 * @author usuario
 *
 */
public class BajaDialog extends JDialog implements ActionListener {

	JButton aceptar;
	JButton cancelar;
	JLabel identificador;
	JTextField areaIdentificador;
	JPanel panel;
	JPanel panelBotones;
	JLabel texto;

	Empresa empresa;

	public BajaDialog(Empresa empresa) {
		this.empresa = empresa;

		setResizable(false);
		// t�tulo del di�log
		setTitle("Baja Trabajador");
		setSize(300, 200);
		setLayout(new FlowLayout());
		setLocationRelativeTo(null);

		texto = new JLabel("<html>Introduzca el ID del trabajador<br> que desea dar de baja<br><br></html>");
		add(texto);

		panel = new JPanel();
		panelBotones = new JPanel();
		add(panel);
		add(panelBotones);

		identificador = new JLabel("Identificador");
		panel.add(identificador);
		areaIdentificador = new JTextField(15);
		panel.add(areaIdentificador);

		aceptar = new JButton("Aceptar");
		aceptar.addActionListener(this);
		panelBotones.add(aceptar);

		cancelar = new JButton("Cancelar");
		cancelar.addActionListener(this);
		panelBotones.add(cancelar);
		// Visible
		setVisible(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		if (e.getSource() == aceptar) {
			int respuesta = JOptionPane.showConfirmDialog(null, "\u00bfDesea dar de baja el trabajador?", "Borrar",
					JOptionPane.YES_NO_OPTION);
			if (respuesta == JOptionPane.YES_OPTION) {
				try {
					int id = Integer.parseInt(areaIdentificador.getText());
					AccesoTrabajador.eliminarTrabajador(id);
					empresa.bajaTrabajador(id);
					JOptionPane.showMessageDialog(this, "El trabajador se ha eliminado correctamente");
					dispose();
				} catch (TrabajadorException ex) {
					JOptionPane.showMessageDialog(null, "El trabajador no se encuentra en la base de datos", "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (BDException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "El ID debe ser un n\u00famero entero", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		} else if (e.getSource() == cancelar) {
			dispose();
		}

	}

}
