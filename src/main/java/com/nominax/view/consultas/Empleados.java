package com.nominax.view.consultas;

import com.nominax.dao.EmpleadoDAO;
import com.nominax.dao.PuestoDAO;
import com.nominax.dao.impl.EmpleadoDAOFile;
import com.nominax.dao.impl.PuestoDAOFile;
import com.nominax.entity.Empleado;
import com.nominax.entity.Puesto;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.stream.Collectors;


public class Empleados extends javax.swing.JFrame {

    /**
     * Creates new form empleados
     */
    static final EmpleadoDAO db = new EmpleadoDAOFile();
    static final PuestoDAO puestoDb = new PuestoDAOFile();
    static final EmpleadoDAO departamentoDAO = new EmpleadoDAOFile();
    static final PuestoDAO empleadoDAO = new PuestoDAOFile();
    static List<Empleado> empleados;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
    public Empleados() {
        initComponents();
        this.setTitle("Lista de empleados");
        this.setResizable(false);
        try {
            empleados = db.obtenerTodos();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, 
                    "Error al cargar los empleados: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
        
    }

    
  
   
    private void aplicarFiltro(String query, Integer limite) {
        String filtroSeleccionado = (String) filtrarPor.getSelectedItem();
        DefaultTableModel modelo = (DefaultTableModel) modeloTabla.getModel();
        modelo.setRowCount(0);
        
        switch (filtroSeleccionado) {
            case "Por nombre":
                    if(!query.isEmpty() || limite != null){
                         List<Empleado> empleadosFiltrados = empleados.subList(0, !limite.equals(0) ? limite : empleados.size());
                         for (Empleado empleado : empleadosFiltrados) {
                            if(empleado.getNombre().toLowerCase().contains(query.toLowerCase())){
                                agregarFilaEmpleado(modelo, empleado);
                            }  
                        }
                    }
                break;
          case "Por primer apellido":
                    if(!query.isEmpty() || limite != null){
                         List<Empleado> empleadosFiltrados = empleados.subList(0, !limite.equals(0) ? limite : empleados.size());
                         for (Empleado empleado : empleadosFiltrados) {
                            if(empleado.getApellidoPat().toLowerCase().contains(query.toLowerCase())){
                                agregarFilaEmpleado(modelo, empleado);
                            }  
                        }
                    }
                break;
            case "Por segundo apellido":
                    if(!query.isEmpty() || limite != null){
                         List<Empleado> empleadosFiltrados = empleados.subList(0, !limite.equals(0) ? limite : empleados.size());
                         for (Empleado empleado : empleadosFiltrados) {
                            if(empleado.getApellidoMat().toLowerCase().contains(query.toLowerCase())){
                                agregarFilaEmpleado(modelo, empleado);
                            }  
                        }
                    }
                break;
            case "Por puestos":
                    if(!query.isEmpty() || limite != null){
                    try {
                        List<Puesto> puestos = puestoDb.obtenerTodos();
                      
                        List<Empleado> empleadosFiltrados = puestos
                                                            .stream()
                                                            .filter(puesto -> puesto.getNombre().toLowerCase().contains(query.toLowerCase()))
                                                            .flatMap(puesto -> 
                                                                empleados
                                                                    .stream()
                                                                    .filter(empleado -> empleado.getIdPuesto() == puesto.getId())
                                                            )
                                                            .collect(Collectors.toList());
                        
                      
                        System.out.println(empleadosFiltrados);
                        
                        for (Empleado empleado : empleadosFiltrados) { 
                            agregarFilaEmpleado(modelo, empleado);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(Empleados.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            
        }
    }
    
       private void agregarFilaEmpleado(DefaultTableModel modelo, Empleado empleado) {
        try {
            String nombrePuesto = "N/A";
            Optional<Puesto> puesto = puestoDb.buscarPorId(empleado.getIdPuesto());
            if (puesto.isPresent()) {
                nombrePuesto = puesto.get().getNombre();
            }
            Object[] fila = {
                empleado.getId(),
                empleado.getNombre(),
                empleado.getApellidoPat(),
                empleado.getApellidoMat(),
                nombrePuesto,
                empleado.getSexo() ? "Masculino" : "Femenino",
                dateFormat.format(empleado.getFechaIngreso()),
                empleado.getSalario(),
                empleado.getCoop(),
                empleado.getTelefono().toString()
            };
            modelo.addRow(fila);
        } catch (IOException ex) {
            Logger.getLogger(Empleados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
  
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        modeloTabla = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        txtUsuario = new javax.swing.JTextField();
        limitador = new javax.swing.JSpinner();
        btnConsultar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        filtrarPor = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        modeloTabla.setBackground(new java.awt.Color(165, 153, 249));
        modeloTabla.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        modeloTabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre", "Primer Apellido", "Segundo Apellido", "Puesto", "Género", "Ingreso", "Salario", "Cooperativa", "Teléfono"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Boolean.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        modeloTabla.setRowHeight(40);
        modeloTabla.setSelectionBackground(new java.awt.Color(153, 153, 255));
        modeloTabla.setShowVerticalLines(true);
        modeloTabla.getTableHeader().setResizingAllowed(false);
        jScrollPane2.setViewportView(modeloTabla);

        txtUsuario.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtUsuario.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, null, new java.awt.Color(102, 102, 255)));
        txtUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsuarioActionPerformed(evt);
            }
        });

        limitador.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 255), 1, true));
        limitador.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                limitadorStateChanged(evt);
            }
        });

        btnConsultar.setBackground(new java.awt.Color(102, 102, 255));
        btnConsultar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnConsultar.setForeground(new java.awt.Color(255, 255, 255));
        btnConsultar.setText("Consultar");
        btnConsultar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, null, new java.awt.Color(102, 102, 255), new java.awt.Color(102, 102, 255)));
        btnConsultar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnConsultar.setDefaultCapable(false);
        btnConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsultarActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Limitador");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Buscar");

        filtrarPor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Por nombre", "Por primer apellido", "Por segundo apellido", "Por puestos", "Por fecha de ingreso", "Por género" }));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Filtrar");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(limitador, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(filtrarPor, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnConsultar, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(32, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnConsultar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(limitador, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(filtrarPor, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(90, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsuarioActionPerformed

    private void btnConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultarActionPerformed
        String busqueda = txtUsuario.getText();
        Integer limite = Integer.parseInt(limitador.getValue().toString());
        aplicarFiltro(busqueda, limite);
    }//GEN-LAST:event_btnConsultarActionPerformed

    private void limitadorStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_limitadorStateChanged
        char c = limitador.getValue().toString().charAt(0);
        if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
            limitador.setValue(0);
        }
        
        if(Integer.parseInt(limitador.getValue().toString()) > empleados.size()){
            limitador.setValue(Integer.valueOf(limitador.getValue().toString()) - 1);
        }
    }//GEN-LAST:event_limitadorStateChanged

   
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
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
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Empleados().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConsultar;
    private javax.swing.JComboBox<String> filtrarPor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSpinner limitador;
    private javax.swing.JTable modeloTabla;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}