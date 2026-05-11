package dialogs;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
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

public class ModificaDialog extends JDialog implements ActionListener, ItemListener {

    JLabel etiquetaIdentificador;
    JComboBox comboIdentificador;
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
    JComboBox comboPuesto;
    JButton guardar;
    JButton cancelar;

    String dni = "";
    String nombre = "";
    String apellidos = "";
    String direccion = "";
    String telefono = "";
    String puesto = "Elija Puesto";

    JPanel pIdentificador;
    JPanel pDni;
    JPanel pNombre;
    JPanel pApellidos;
    JPanel pDireccion;
    JPanel pTelefono;
    JPanel pPuesto;
    JPanel pBotones;
    JLabel labelError;

    Empresa empresa;
    Trabajador trabajador;
    java.util.List<Trabajador> listaTrabajadores;

    public ModificaDialog(Empresa empresa, Trabajador trabajador) {
        this.empresa = empresa;
        this.trabajador = trabajador;

        setResizable(false);
        setTitle("Modificar Trabajador");
        setSize(300, 350);
        setLayout(new FlowLayout());
        setLocationRelativeTo(null);

        pDni = new JPanel();
        pNombre = new JPanel();
        pApellidos = new JPanel();
        pDireccion = new JPanel();
        pTelefono = new JPanel();
        pPuesto = new JPanel();
        pBotones = new JPanel();

        etiquetaDni = new JLabel("DNI                 ");
        areaDni = new JTextField(15);
        areaDni.setText(trabajador.getDni());
        pDni.add(etiquetaDni);
        pDni.add(areaDni);

        etiquetaNombre = new JLabel("Nombre         ");
        areaNombre = new JTextField(15);
        areaNombre.setText(trabajador.getNombre());
        pNombre.add(etiquetaNombre);
        pNombre.add(areaNombre);

        etiquetaApellidos = new JLabel("Apellidos      ");
        areaApellidos = new JTextField(15);
        areaApellidos.setText(trabajador.getApellidos());
        pApellidos.add(etiquetaApellidos);
        pApellidos.add(areaApellidos);

        etiquetaDireccion = new JLabel("Direccion      ");
        areaDireccion = new JTextField(15);
        areaDireccion.setText(trabajador.getDireccion());
        pDireccion.add(etiquetaDireccion);
        pDireccion.add(areaDireccion);

        etiquetaTelefono = new JLabel("Telefono       ");
        areaTelefono = new JTextField(15);
        areaTelefono.setText(trabajador.getTelefono());
        pTelefono.add(etiquetaTelefono);
        pTelefono.add(areaTelefono);

        etiquetaPuesto = new JLabel("Puesto                         ");
        pPuesto.add(etiquetaPuesto);
        comboPuesto = new JComboBox();
        comboPuesto.addItem("Elija Puesto");
        comboPuesto.addItem("Programador");
        comboPuesto.addItem("Analista");
        comboPuesto.addItem("Arquitecto");
        comboPuesto.addItem("Jefe de Proyecto");
        comboPuesto.setSelectedItem(trabajador.getPuesto());
        comboPuesto.addItemListener(this);
        pPuesto.add(comboPuesto);

        add(pDni);
        add(pNombre);
        add(pApellidos);
        add(pDireccion);
        add(pTelefono);
        add(pPuesto);

        labelError = new JLabel(" ");
        add(labelError);

        guardar = new JButton("Guardar");
        guardar.addActionListener(this);
        pBotones.add(guardar);

        cancelar = new JButton("Cancelar");
        cancelar.addActionListener(this);
        pBotones.add(cancelar);

        add(pBotones);

        setVisible(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    public ModificaDialog(Empresa empresa) {
        this.empresa = empresa;
        this.trabajador = null;

        setResizable(false);
        setTitle("Modificar Trabajador");
        setSize(300, 400);
        setLayout(new FlowLayout());
        setLocationRelativeTo(null);

        pIdentificador = new JPanel();
        pDni = new JPanel();
        pNombre = new JPanel();
        pApellidos = new JPanel();
        pDireccion = new JPanel();
        pTelefono = new JPanel();
        pPuesto = new JPanel();
        pBotones = new JPanel();

        try {
            listaTrabajadores = AccesoTrabajador.consultarTrabajadores();
        } catch (BDException | TrabajadorException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        etiquetaIdentificador = new JLabel("Identificador  ");
        comboIdentificador = new JComboBox();
        comboIdentificador.addItem("Seleccione el identificador");
        for (Trabajador t : listaTrabajadores) {
            comboIdentificador.addItem(String.valueOf(t.getIdentificador()));
        }
        comboIdentificador.addItemListener(this);
        pIdentificador.add(etiquetaIdentificador);
        pIdentificador.add(comboIdentificador);

        etiquetaDni = new JLabel("DNI                 ");
        areaDni = new JTextField(15);
        areaDni.setEditable(false);
        pDni.add(etiquetaDni);
        pDni.add(areaDni);

        etiquetaNombre = new JLabel("Nombre         ");
        areaNombre = new JTextField(15);
        pNombre.add(etiquetaNombre);
        pNombre.add(areaNombre);

        etiquetaApellidos = new JLabel("Apellidos      ");
        areaApellidos = new JTextField(15);
        pApellidos.add(etiquetaApellidos);
        pApellidos.add(areaApellidos);

        etiquetaDireccion = new JLabel("Direccion      ");
        areaDireccion = new JTextField(15);
        pDireccion.add(etiquetaDireccion);
        pDireccion.add(areaDireccion);

        etiquetaTelefono = new JLabel("Telefono       ");
        areaTelefono = new JTextField(15);
        pTelefono.add(etiquetaTelefono);
        pTelefono.add(areaTelefono);

        etiquetaPuesto = new JLabel("Puesto                         ");
        pPuesto.add(etiquetaPuesto);
        comboPuesto = new JComboBox();
        comboPuesto.addItem("Elija Puesto");
        comboPuesto.addItem("Programador");
        comboPuesto.addItem("Analista");
        comboPuesto.addItem("Arquitecto");
        comboPuesto.addItem("Jefe de Proyecto");
        comboPuesto.setEnabled(false);
        comboPuesto.addItemListener(this);
        pPuesto.add(comboPuesto);

        add(pIdentificador);
        add(pDni);
        add(pNombre);
        add(pApellidos);
        add(pDireccion);
        add(pTelefono);
        add(pPuesto);

        labelError = new JLabel(" ");
        add(labelError);

        guardar = new JButton("Guardar");
        guardar.addActionListener(this);
        pBotones.add(guardar);

        cancelar = new JButton("Cancelar");
        cancelar.addActionListener(this);
        pBotones.add(cancelar);

        add(pBotones);

        setVisible(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == comboIdentificador && e.getStateChange() == ItemEvent.SELECTED) {
            int idx = comboIdentificador.getSelectedIndex() - 1;
            if (idx >= 0 && idx < listaTrabajadores.size()) {
                trabajador = listaTrabajadores.get(idx);
                areaDni.setText(trabajador.getDni());
                areaNombre.setText(trabajador.getNombre());
                areaApellidos.setText(trabajador.getApellidos());
                areaDireccion.setText(trabajador.getDireccion());
                areaTelefono.setText(trabajador.getTelefono());
                comboPuesto.setSelectedItem(trabajador.getPuesto());
                areaDni.setEditable(true);
                comboPuesto.setEnabled(true);
                restaurarColores();
                labelError.setText(" ");
            }
        } else if (e.getSource() == comboPuesto) {
            puesto = comboPuesto.getSelectedItem().toString();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == guardar) {
            if (trabajador == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un trabajador primero", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            dni = areaDni.getText();
            nombre = areaNombre.getText();
            apellidos = areaApellidos.getText();
            direccion = areaDireccion.getText();
            telefono = areaTelefono.getText();
            if (comprobarErrores()) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "\u00bfEst\u00e1 seguro de que desea modificar los datos?",
                        "Confirmar modificaci\u00f3n", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
                try {
                    int id = trabajador.getIdentificador();
                    AccesoTrabajador.actualizarTrabajador(id, nombre, apellidos, direccion, telefono, puesto);
                    if (empresa.devolverPosicion(id) != -1) {
                        empresa.modificarTrabajador(id, dni, nombre, apellidos, direccion, telefono, puesto);
                    }
                    JOptionPane.showMessageDialog(null, "Datos modificados correctamente");
                    dispose();
                } catch (BDException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == cancelar) {
            dispose();
        }
    }

    public boolean comprobarErrores() {
        restaurarColores();
        labelError.setText(" ");

        if (dni.equals("") || dni.length() != 9) {
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
        if (nombre.equals("")) {
            areaNombre.setBackground(Color.PINK);
            labelError.setText("Debe introducir el nombre del trabajador");
            return false;
        } else if (apellidos.equals("")) {
            areaApellidos.setBackground(Color.PINK);
            labelError.setText("Debe introducir los apellidos del trabajador");
            return false;
        } else if (direccion.equals("")) {
            areaDireccion.setBackground(Color.PINK);
            labelError.setText("Debe introducir la direcci\u00f3n del trabajador");
            return false;
        } else if (telefono.equals("") || telefono.length() != 9 || !telefono.matches("\\d{9}")) {
            areaTelefono.setBackground(Color.PINK);
            labelError.setText("El tel\u00e9fono debe tener 9 d\u00edgitos");
            return false;
        } else if (puesto.equals("") || puesto.equals("Elija Puesto")) {
            labelError.setText("Debe seleccionar un puesto");
            return false;
        }
        return true;
    }

    private void restaurarColores() {
        areaDni.setBackground(Color.WHITE);
        areaNombre.setBackground(Color.WHITE);
        areaApellidos.setBackground(Color.WHITE);
        areaDireccion.setBackground(Color.WHITE);
        areaTelefono.setBackground(Color.WHITE);
    }
}
