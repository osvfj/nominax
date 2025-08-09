package com.nominax.view.consultas;

import com.nominax.dao.EmpleadoDAO;
import com.nominax.dao.impl.EmpleadoDAOFile;
import com.nominax.dao.DepartamentoDAO;
import com.nominax.dao.PuestoDAO;
import com.nominax.dao.impl.DepartamentoDAOFile;
import com.nominax.dao.impl.PuestoDAOFile;
import com.nominax.entity.Empleado;
import com.nominax.entity.Departamento;
import com.nominax.entity.Puesto;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Empleados extends javax.swing.JFrame {

    static final EmpleadoDAO empleadoDAO = new EmpleadoDAOFile();
    static final DepartamentoDAO departamentoDAO = new DepartamentoDAOFile();
    static final PuestoDAO puestoDAO = new PuestoDAOFile();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
    public Empleados() {
        initComponents();
        this.setTitle("Lista de empleados");
        configurarEventos();
        cargarDatos();
    }
    
    private void configurarEventos() {
        filtrarPor.addActionListener(e -> aplicarFiltro());
    }
    
    private void aplicarFiltro() {
        String filtroSeleccionado = (String) filtrarPor.getSelectedItem();
        
        switch (filtroSeleccionado) {
            case "Por departamento":
                mostrarPorDepartamento();
                break;
            case "Por puestos":
                mostrarPorPuestos();
                break;
            case "Por fecha de ingreso":
                mostrarPorFechaIngreso();
                break;
            case "Por género":
                mostrarPorGenero();
                break;
            default:
                cargarDatos();
        }
    }
    
    private void cargarDatos() {
        try {
            DefaultTableModel modelo = (DefaultTableModel) modeloTabla.getModel();
            modelo.setRowCount(0);

            List<Empleado> empleados = empleadoDAO.obtenerTodos();
            
            if (empleados != null) {
                for (Empleado empleado : empleados) {
                    agregarFilaEmpleado(modelo, empleado);
                }
            }
            
        } catch (IOException e) {
            mostrarError("Error al cargar los empleados: " + e.getMessage());
        }
    }
    
    private void agregarFilaEmpleado(DefaultTableModel modelo, Empleado empleado) {
        try {
            String nombreDepartamento = "N/A";
            Optional<Departamento> depto = departamentoDAO.buscarPorId(empleado.getIdDepto());
            if (depto.isPresent()) {
                nombreDepartamento = depto.get().getDescripcion();
            }
            
            String nombrePuesto = "N/A";
            Optional<Puesto> puesto = puestoDAO.buscarPorId(empleado.getIdPuesto());
            if (puesto.isPresent()) {
                nombrePuesto = puesto.get().getNombre();
            }
            
            Object[] fila = {
                empleado.getId(),
                empleado.getNombre() + " " + empleado.getApellidoPat() + " " + empleado.getApellidoMat(),
                nombreDepartamento,
                nombrePuesto,
                empleado.getSexo() ? "Masculino" : "Femenino",
                dateFormat.format(empleado.getFechaIngreso()),
                String.format("$%.2f", empleado.getSalario()),
                empleado.getCoop() ? "Sí" : "No",
                empleado.getTelefono().toString()
            };
            modelo.addRow(fila);
            
        } catch (IOException e) {
            Object[] fila = {
                empleado.getId(),
                empleado.getNombre() + " " + empleado.getApellidoPat() + " " + empleado.getApellidoMat(),
                "N/A",
                "N/A",
                empleado.getSexo() ? "Masculino" : "Femenino",
                dateFormat.format(empleado.getFechaIngreso()),
                String.format("$%.2f", empleado.getSalario()),
                empleado.getCoop() ? "Sí" : "No",
                empleado.getTelefono().toString()
            };
            modelo.addRow(fila);
        }
    }
    
    private void mostrarPorDepartamento() {
        try {
            DefaultTableModel modelo = (DefaultTableModel) modeloTabla.getModel();
            modelo.setRowCount(0);

            List<Empleado> empleados = empleadoDAO.obtenerTodos();
            List<Departamento> departamentos = departamentoDAO.obtenerTodos();
            
            Map<Integer, String> mapaDepartamentos = departamentos.stream()
                .collect(Collectors.toMap(Departamento::getId, Departamento::getDescripcion));
            
            Map<Integer, List<Empleado>> empleadosPorDepto = empleados.stream()
                .collect(Collectors.groupingBy(Empleado::getIdDepto));
            
            empleadosPorDepto.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    Integer idDepto = entry.getKey();
                    String nombreDepto = mapaDepartamentos.getOrDefault(idDepto, "Departamento " + idDepto);
                    List<Empleado> empleadosDepto = entry.getValue();
                    
                    Object[] separador = {
                        "---", "=== " + nombreDepto.toUpperCase() + " (" + empleadosDepto.size() + " empleados) ===", 
                        "---", "---", "---", "---", "---", "---", "---"
                    };
                    modelo.addRow(separador);
                    
                    empleadosDepto.sort((e1, e2) -> (e1.getNombre() + e1.getApellidoPat()).compareTo(e2.getNombre() + e2.getApellidoPat()));
                    
                    for (Empleado empleado : empleadosDepto) {
                        agregarFilaEmpleado(modelo, empleado);
                    }
                });
            
        } catch (IOException e) {
            mostrarError("Error al filtrar por departamento: " + e.getMessage());
        }
    }
    
    private void mostrarPorPuestos() {
        try {
            DefaultTableModel modelo = (DefaultTableModel) modeloTabla.getModel();
            modelo.setRowCount(0);

            List<Empleado> empleados = empleadoDAO.obtenerTodos();
            List<Puesto> puestos = puestoDAO.obtenerTodos();
            
            Map<Integer, String> mapaPuestos = puestos.stream()
                .collect(Collectors.toMap(Puesto::getId, Puesto::getNombre));
            
            Map<Integer, List<Empleado>> empleadosPorPuesto = empleados.stream()
                .collect(Collectors.groupingBy(Empleado::getIdPuesto));
            
            empleadosPorPuesto.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    Integer idPuesto = entry.getKey();
                    String nombrePuesto = mapaPuestos.getOrDefault(idPuesto, "Puesto " + idPuesto);
                    List<Empleado> empleadosPuesto = entry.getValue();
                    
                    double salarioPromedio = empleadosPuesto.stream()
                        .mapToDouble(Empleado::getSalario)
                        .average()
                        .orElse(0.0);
                    
                    Object[] separador = {
                        "---", "=== " + nombrePuesto.toUpperCase() + " (" + empleadosPuesto.size() + " empleados) - Promedio: $" + String.format("%.2f", salarioPromedio) + " ===", 
                        "---", "---", "---", "---", "---", "---", "---"
                    };
                    modelo.addRow(separador);
                    
                    empleadosPuesto.sort((e1, e2) -> Double.compare(e2.getSalario(), e1.getSalario()));
                    
                    for (Empleado empleado : empleadosPuesto) {
                        agregarFilaEmpleado(modelo, empleado);
                    }
                });
            
        } catch (IOException e) {
            mostrarError("Error al filtrar por puesto: " + e.getMessage());
        }
    }
    
    private void mostrarPorFechaIngreso() {
        try {
            DefaultTableModel modelo = (DefaultTableModel) modeloTabla.getModel();
            modelo.setRowCount(0);

            List<Empleado> empleados = empleadoDAO.obtenerTodos();
            
            Map<Integer, List<Empleado>> empleadosPorAno = empleados.stream()
                .collect(Collectors.groupingBy(empleado -> {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(empleado.getFechaIngreso());
                    return cal.get(Calendar.YEAR);
                }));
            
            empleadosPorAno.entrySet().stream()
                .sorted(Map.Entry.<Integer, List<Empleado>>comparingByKey().reversed())
                .forEach(entry -> {
                    Integer ano = entry.getKey();
                    List<Empleado> empleadosAno = entry.getValue();
                    
                    Object[] separador = {
                        "---", "=== AÑO " + ano + " (" + empleadosAno.size() + " ingresos) ===", 
                        "---", "---", "---", "---", "---", "---", "---"
                    };
                    modelo.addRow(separador);
                    
                    empleadosAno.sort((e1, e2) -> e2.getFechaIngreso().compareTo(e1.getFechaIngreso()));
                    
                    for (Empleado empleado : empleadosAno) {
                        agregarFilaEmpleado(modelo, empleado);
                    }
                });
            
        } catch (IOException e) {
            mostrarError("Error al filtrar por fecha de ingreso: " + e.getMessage());
        }
    }
    
    private void mostrarPorGenero() {
        try {
            DefaultTableModel modelo = (DefaultTableModel) modeloTabla.getModel();
            modelo.setRowCount(0);

            List<Empleado> empleados = empleadoDAO.obtenerTodos();
            
            List<Empleado> hombres = empleados.stream()
                .filter(Empleado::getSexo)
                .sorted((e1, e2) -> (e1.getNombre() + e1.getApellidoPat()).compareTo(e2.getNombre() + e2.getApellidoPat()))
                .collect(Collectors.toList());
                
            List<Empleado> mujeres = empleados.stream()
                .filter(empleado -> !empleado.getSexo())
                .sorted((e1, e2) -> (e1.getNombre() + e1.getApellidoPat()).compareTo(e2.getNombre() + e2.getApellidoPat()))
                .collect(Collectors.toList());
            
            if (!hombres.isEmpty()) {
                double salarioPromedioHombres = hombres.stream()
                    .mapToDouble(Empleado::getSalario)
                    .average()
                    .orElse(0.0);
                
                Object[] encabezadoHombres = {
                    "---", "=== EMPLEADOS MASCULINOS (" + hombres.size() + ") - Promedio: $" + String.format("%.2f", salarioPromedioHombres) + " ===", 
                    "---", "---", "---", "---", "---", "---", "---"
                };
                modelo.addRow(encabezadoHombres);
                
                for (Empleado empleado : hombres) {
                    agregarFilaEmpleado(modelo, empleado);
                }
            }
            
            if (!mujeres.isEmpty()) {
                double salarioPromedioMujeres = mujeres.stream()
                    .mapToDouble(Empleado::getSalario)
                    .average()
                    .orElse(0.0);
                
                Object[] encabezadoMujeres = {
                    "---", "=== EMPLEADAS FEMENINAS (" + mujeres.size() + ") - Promedio: $" + String.format("%.2f", salarioPromedioMujeres) + " ===", 
                    "---", "---", "---", "---", "---", "---", "---"
                };
                modelo.addRow(encabezadoMujeres);
                
                for (Empleado empleado : mujeres) {
                    agregarFilaEmpleado(modelo, empleado);
                }
            }
            
        } catch (IOException e) {
            mostrarError("Error al filtrar por género: " + e.getMessage());
        }
    }
    
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        modeloTabla = new javax.swing.JTable();
        filtrarPor = new javax.swing.JComboBox<>();
        filtrarLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        modeloTabla.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        modeloTabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre Completo", "Departamento", "Puesto", "Género", "Fecha Ingreso", "Salario", "Cooperativa", "Teléfono"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        modeloTabla.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        modeloTabla.setRowHeight(40);
        modeloTabla.setRowMargin(2);
        modeloTabla.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(modeloTabla);

        filtrarPor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Por departamento", "Por puestos", "Por fecha de ingreso", "Por género" }));

        filtrarLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        filtrarLabel.setText("Filtrar:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(filtrarLabel)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(filtrarPor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 940, Short.MAX_VALUE)))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(88, Short.MAX_VALUE)
                .addComponent(filtrarLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filtrarPor, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Empleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Empleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Empleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Empleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Empleados().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel filtrarLabel;
    private javax.swing.JComboBox<String> filtrarPor;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable modeloTabla;
    // End of variables declaration//GEN-END:variables
}