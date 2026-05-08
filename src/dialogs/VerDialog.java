package dialogs;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dao.AccesoTrabajador;
import excepciones.BDException;
import excepciones.TrabajadorException;
import modelo.Empresa;
import modelo.Trabajador;

public class VerDialog extends JDialog implements ActionListener {

    JLabel etiquetaId;
    JTextField areaId;
    JButton buscar;
    JButton cerrar;

    JLabel etiquetaDni;
    JTextField areaDni;
    JLabel etiquetaNombre;
    JTextField areaNombre;
    JLabel etiquetaApellidos;
    JTextField areaApellidos;
    JLabel etiquetaDireccion;
    JTextField areaDireccion;
    JLabel etiquetaTelefono;
    JTextField areaTelefono;
    JLabel etiquetaPuesto;
    JTextField areaPuesto;

    Empresa empresa;

    public VerDialog(Empresa empresa) {
        this.empresa = empresa;

        setResizable(false);
        setTitle("Buscar Trabajador");
        setSize(320, 350);
        setLayout(new FlowLayout());
        setLocationRelativeTo(null);

        JPanel pBusqueda = new JPanel();
        etiquetaId = new JLabel("ID                 ");
        areaId = new JTextField(10);
        buscar = new JButton("Buscar");
        buscar.addActionListener(this);
        pBusqueda.add(etiquetaId);
        pBusqueda.add(areaId);
        pBusqueda.add(buscar);
        add(pBusqueda);

        etiquetaDni = new JLabel("DNI                 ");
        areaDni = new JTextField(15);
        areaDni.setEditable(false);
        add(etiquetaDni);
        add(areaDni);

        etiquetaNombre = new JLabel("Nombre         ");
        areaNombre = new JTextField(15);
        areaNombre.setEditable(false);
        add(etiquetaNombre);
        add(areaNombre);

        etiquetaApellidos = new JLabel("Apellidos      ");
        areaApellidos = new JTextField(15);
        areaApellidos.setEditable(false);
        add(etiquetaApellidos);
        add(areaApellidos);

        etiquetaDireccion = new JLabel("Direccion      ");
        areaDireccion = new JTextField(15);
        areaDireccion.setEditable(false);
        add(etiquetaDireccion);
        add(areaDireccion);

        etiquetaTelefono = new JLabel("Telefono       ");
        areaTelefono = new JTextField(15);
        areaTelefono.setEditable(false);
        add(etiquetaTelefono);
        add(areaTelefono);

        etiquetaPuesto = new JLabel("Puesto           ");
        areaPuesto = new JTextField(15);
        areaPuesto.setEditable(false);
        add(etiquetaPuesto);
        add(areaPuesto);

        cerrar = new JButton("Cerrar");
        cerrar.addActionListener(this);
        add(cerrar);

        setVisible(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buscar) {
            try {
                int id = Integer.parseInt(areaId.getText());
                List<Trabajador> resultados = AccesoTrabajador.consultarTrabajadorPorIdentificador(id);
                if (!resultados.isEmpty()) {
                    Trabajador t = resultados.get(0);
                    areaDni.setText(t.getDni());
                    areaNombre.setText(t.getNombre());
                    areaApellidos.setText(t.getApellidos());
                    areaDireccion.setText(t.getDireccion());
                    areaTelefono.setText(t.getTelefono());
                    areaPuesto.setText(t.getPuesto());
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El ID debe ser un n\u00famero entero", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (BDException | TrabajadorException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == cerrar) {
            dispose();
        }
    }
}
