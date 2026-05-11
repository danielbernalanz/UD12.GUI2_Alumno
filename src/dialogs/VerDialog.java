package dialogs;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import dao.AccesoTrabajador;
import excepciones.BDException;
import excepciones.TrabajadorException;
import modelo.Empresa;
import modelo.Trabajador;

public class VerDialog extends JDialog implements ActionListener, ItemListener {

    JTextField areaId;
    JButton buscar;
    JComboBox comboIdentificador;
    JTable tabla;
    JTextField campoBusqueda;
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
    List<Trabajador> trabajadores;
    TableRowSorter<DefaultTableModel> sorter;

    public VerDialog(Empresa empresa) {
        this.empresa = empresa;

        setResizable(false);
        setTitle("Buscar Trabajador");
        setSize(750, 700);
        setLayout(new FlowLayout());
        setLocationRelativeTo(null);

        try {
            trabajadores = AccesoTrabajador.consultarTrabajadores();
        } catch (BDException | TrabajadorException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        JPanel pBusqueda = new JPanel();
        pBusqueda.add(new JLabel("ID:"));
        areaId = new JTextField(8);
        pBusqueda.add(areaId);
        buscar = new JButton("Buscar");
        buscar.addActionListener(this);
        pBusqueda.add(buscar);
        add(pBusqueda);

        JPanel pCombo = new JPanel();
        pCombo.add(new JLabel("Seleccione identificador:"));
        comboIdentificador = new JComboBox();
        comboIdentificador.addItem("Elija un identificador");
        for (Trabajador t : trabajadores) {
            comboIdentificador.addItem(String.valueOf(t.getIdentificador()));
        }
        comboIdentificador.addItemListener(this);
        pCombo.add(comboIdentificador);
        add(pCombo);

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

        add(new JLabel("Listado de trabajadores:"));
        add(new JLabel("Buscar: "));
        campoBusqueda = new JTextField(15);
        campoBusqueda.addActionListener(this);
        add(campoBusqueda);

        String[] columnas = { "Identificador", "DNI", "Nombre", "Apellidos", "Direcci\u00f3n", "Tel\u00e9fono", "Puesto" };
        String[][] datos = new String[trabajadores.size()][7];
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

        DefaultTableModel modeloTabla = new DefaultTableModel(datos, columnas);
        tabla = new JTable(modeloTabla);
        sorter = new TableRowSorter<>(modeloTabla);
        tabla.setRowSorter(sorter);
        JScrollPane jsp = new JScrollPane(tabla);
        jsp.setPreferredSize(new Dimension(700, 200));
        add(jsp);

        cerrar = new JButton("Cerrar");
        cerrar.addActionListener(this);
        add(cerrar);

        setVisible(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void mostrarTrabajador(Trabajador t) {
        areaDni.setText(t.getDni());
        areaNombre.setText(t.getNombre());
        areaApellidos.setText(t.getApellidos());
        areaDireccion.setText(t.getDireccion());
        areaTelefono.setText(t.getTelefono());
        areaPuesto.setText(t.getPuesto());
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == comboIdentificador && e.getStateChange() == ItemEvent.SELECTED) {
            int idx = comboIdentificador.getSelectedIndex() - 1;
            if (idx >= 0 && idx < trabajadores.size()) {
                mostrarTrabajador(trabajadores.get(idx));
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buscar) {
            try {
                int id = Integer.parseInt(areaId.getText());
                if (id <= 0) {
                    JOptionPane.showMessageDialog(this, "El ID debe ser un n\u00famero entero mayor que cero",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                List<Trabajador> resultados = AccesoTrabajador.consultarTrabajadorPorIdentificador(id);
                if (resultados.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Trabajador no encontrado", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                mostrarTrabajador(resultados.get(0));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El ID debe ser un n\u00famero entero", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (BDException | TrabajadorException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == campoBusqueda) {
            String texto = campoBusqueda.getText();
            if (texto.isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
            }
        } else if (e.getSource() == cerrar) {
            dispose();
        }
    }
}
