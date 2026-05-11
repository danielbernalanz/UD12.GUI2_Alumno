package dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import dao.AccesoTrabajador;
import excepciones.BDException;
import excepciones.TrabajadorException;
import modelo.Empresa;
import modelo.Trabajador;

public class VerDialog extends JDialog implements ActionListener, ItemListener {

    JTextField areaIdentificador;
    JTextField areaDni;
    JTextField areaNombre;
    JTextField areaApellidos;
    JTextField areaDireccion;
    JTextField areaTelefono;
    JComboBox comboPuesto;
    JButton buscar;
    JButton cancelar;
    JLabel labelError;
    JTable tabla;
    DefaultTableModel modeloTabla;

    String dni = "";
    String nombre = "";
    String apellidos = "";
    String direccion = "";
    String telefono = "";
    String puesto = "";

    List<Trabajador> trabajadores;

    private static final String[] COLUMNAS = {
        "Identificador", "DNI", "Nombre", "Apellidos", "Direcci\u00f3n", "Tel\u00e9fono", "Puesto"
    };

    public VerDialog(Empresa empresa) {
        setResizable(false);
        setTitle("Buscar Trabajador");
        setSize(650, 550);
        setLayout(new FlowLayout());
        setLocationRelativeTo(null);

        try {
            trabajadores = AccesoTrabajador.consultarTrabajadores();
        } catch (BDException | TrabajadorException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        JPanel pIdentificador = new JPanel();
        pIdentificador.add(new JLabel("Identificador:"));
        areaIdentificador = new JTextField(15);
        pIdentificador.add(areaIdentificador);
        add(pIdentificador);

        JPanel pDni = new JPanel();
        pDni.add(new JLabel("DNI:"));
        areaDni = new JTextField(15);
        pDni.add(areaDni);
        add(pDni);

        JPanel pNombre = new JPanel();
        pNombre.add(new JLabel("Nombre:"));
        areaNombre = new JTextField(15);
        pNombre.add(areaNombre);
        add(pNombre);

        JPanel pApellidos = new JPanel();
        pApellidos.add(new JLabel("Apellidos:"));
        areaApellidos = new JTextField(15);
        pApellidos.add(areaApellidos);
        add(pApellidos);

        JPanel pDireccion = new JPanel();
        pDireccion.add(new JLabel("Direccion:"));
        areaDireccion = new JTextField(15);
        pDireccion.add(areaDireccion);
        add(pDireccion);

        JPanel pTelefono = new JPanel();
        pTelefono.add(new JLabel("Telefono:"));
        areaTelefono = new JTextField(15);
        pTelefono.add(areaTelefono);
        add(pTelefono);

        JPanel pPuesto = new JPanel();
        pPuesto.add(new JLabel("Puesto:"));
        comboPuesto = new JComboBox();
        comboPuesto.addItem("Elija Puesto");
        comboPuesto.addItem("Programador");
        comboPuesto.addItem("Analista");
        comboPuesto.addItem("Arquitecto");
        comboPuesto.addItem("Jefe de Proyecto");
        comboPuesto.addItemListener(this);
        pPuesto.add(comboPuesto);
        add(pPuesto);

        labelError = new JLabel(" ");
        add(labelError);

        JPanel pBotones = new JPanel();
        buscar = new JButton("Buscar");
        buscar.addActionListener(this);
        pBotones.add(buscar);
        cancelar = new JButton("Cancelar");
        cancelar.addActionListener(this);
        pBotones.add(cancelar);
        add(pBotones);

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

        modeloTabla = new DefaultTableModel(datos, COLUMNAS);
        tabla = new JTable(modeloTabla);
        tabla.setRowSorter(new TableRowSorter<>(modeloTabla));
        JScrollPane jsp = new JScrollPane(tabla);
        jsp.setPreferredSize(new Dimension(600, 200));
        add(jsp);

        setVisible(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private boolean comprobarErrores() {
        restaurarColores();
        labelError.setText(" ");

        if (!areaIdentificador.getText().equals("")) {
            try {
                int id = Integer.parseInt(areaIdentificador.getText());
                if (id <= 0) {
                    areaIdentificador.setBackground(Color.PINK);
                    labelError.setText("El identificador debe ser un n\u00famero entero mayor que cero");
                    return false;
                }
            } catch (NumberFormatException ex) {
                areaIdentificador.setBackground(Color.PINK);
                labelError.setText("El identificador debe ser un n\u00famero entero");
                return false;
            }
        }
        if (!dni.equals("")) {
            if (dni.length() != 9) {
                areaDni.setBackground(Color.PINK);
                labelError.setText("El DNI debe tener longitud 9");
                return false;
            }
            String numeroStr = dni.substring(0, 8);
            char letra = Character.toUpperCase(dni.charAt(8));
            if (!numeroStr.matches("\\d{8}")) {
                areaDni.setBackground(Color.PINK);
                labelError.setText("El DNI debe tener 8 d\u00edgitos seguidos de una letra");
                return false;
            }
            String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
            int numero = Integer.parseInt(numeroStr);
            if (letras.charAt(numero % 23) != letra) {
                areaDni.setBackground(Color.PINK);
                labelError.setText("La letra del DNI no es v\u00e1lida");
                return false;
            }
        }
        if (!telefono.equals("") && (telefono.length() != 9 || !telefono.matches("\\d{9}"))) {
            areaTelefono.setBackground(Color.PINK);
            labelError.setText("El tel\u00e9fono debe tener 9 d\u00edgitos");
            return false;
        }
        if (!puesto.equals("") && puesto.equals("Elija Puesto")) {
            labelError.setText("Seleccione un puesto v\u00e1lido");
            return false;
        }
        return true;
    }

    private void filtrar() {
        List<Trabajador> datosFiltrados = new ArrayList<>();
        for (Trabajador t : trabajadores) {
            boolean coincide = true;
            String idText = areaIdentificador.getText();
            if (!idText.equals("")) {
                try {
                    int id = Integer.parseInt(idText);
                    if (t.getIdentificador() != id) {
                        coincide = false;
                    }
                } catch (NumberFormatException e) {
                    coincide = false;
                }
            }
            if (coincide && !dni.equals("") && !t.getDni().equalsIgnoreCase(dni)) {
                coincide = false;
            }
            if (!nombre.equals("") && !t.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                coincide = false;
            }
            if (!apellidos.equals("") && !t.getApellidos().toLowerCase().contains(apellidos.toLowerCase())) {
                coincide = false;
            }
            if (!direccion.equals("") && !t.getDireccion().toLowerCase().contains(direccion.toLowerCase())) {
                coincide = false;
            }
            if (!telefono.equals("") && !t.getTelefono().equals(telefono)) {
                coincide = false;
            }
            if (!puesto.equals("") && !puesto.equals("Elija Puesto") && !t.getPuesto().equalsIgnoreCase(puesto)) {
                coincide = false;
            }
            if (coincide) {
                datosFiltrados.add(t);
            }
        }

        modeloTabla.setRowCount(0);
        for (Trabajador t : datosFiltrados) {
            modeloTabla.addRow(new Object[]{
                t.getIdentificador(), t.getDni(), t.getNombre(), t.getApellidos(),
                t.getDireccion(), t.getTelefono(), t.getPuesto()
            });
        }
    }

    private void restaurarColores() {
        areaIdentificador.setBackground(Color.WHITE);
        areaDni.setBackground(Color.WHITE);
        areaNombre.setBackground(Color.WHITE);
        areaApellidos.setBackground(Color.WHITE);
        areaDireccion.setBackground(Color.WHITE);
        areaTelefono.setBackground(Color.WHITE);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == comboPuesto && e.getStateChange() == ItemEvent.SELECTED) {
            puesto = comboPuesto.getSelectedItem().toString();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buscar) {
            dni = areaDni.getText();
            nombre = areaNombre.getText();
            apellidos = areaApellidos.getText();
            direccion = areaDireccion.getText();
            telefono = areaTelefono.getText();
            if (comprobarErrores()) {
                filtrar();
            }
        } else if (e.getSource() == cancelar) {
            dispose();
        }
    }
}
