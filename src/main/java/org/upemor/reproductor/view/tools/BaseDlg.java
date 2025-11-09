package org.upemor.reproductor.view.tools;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Panel base reutilizable con tabla y botones de acción
 * @author Sistema Reproductor
 */
public abstract class BaseDlg extends JPanel {
    protected JTable tabla;
    protected DefaultTableModel modeloTabla;
    protected JTextField tfBuscar;
    protected JButton btnBuscar;
    protected JButton btnAgregar;
    protected JButton btnEditar;
    protected JButton btnEliminar;
    protected JLabel lbBuscar;
    
    public BaseDlg() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Panel superior - Búsqueda
        JPanel panelSuperior = crearPanelBusqueda();
        add(panelSuperior, BorderLayout.NORTH);
        
        // Panel central - Tabla
        JPanel panelCentral = crearPanelTabla();
        add(panelCentral, BorderLayout.CENTER);
        
        // Panel inferior - Botones
        JPanel panelInferior = crearPanelBotones();
        add(panelInferior, BorderLayout.SOUTH);
        
        configurarEventos();
    }
    
    /**
     * Crea el panel de búsqueda
     */
    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        lbBuscar = new JLabel("Buscar:");
        lbBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        tfBuscar = new JTextField();
        tfBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tfBuscar.setPreferredSize(new Dimension(300, 35));
        
        btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnBuscar.setPreferredSize(new Dimension(120, 35));
        btnBuscar.setFocusPainted(false);
        
        JPanel panelIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelIzq.add(lbBuscar);
        panelIzq.add(tfBuscar);
        panelIzq.add(btnBuscar);
        
        panel.add(panelIzq, BorderLayout.WEST);
        
        return panel;
    }
    
    /**
     * Crea el panel con la tabla
     */
    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabla = new JTable(modeloTabla);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setRowHeight(30);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Crea el panel de botones
     */
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        btnAgregar = crearBoton("➕ Agregar", new Color(76, 175, 80));
        btnEditar = crearBoton("✏️ Editar", new Color(33, 150, 243));
        btnEliminar = crearBoton("🗑️ Eliminar", new Color(244, 67, 54));
        
        panel.add(btnAgregar);
        panel.add(btnEditar);
        panel.add(btnEliminar);
        
        return panel;
    }
    
    /**
     * Crea un botón con estilo
     */
    protected JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setPreferredSize(new Dimension(140, 40));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }
    
    /**
     * Configura los eventos de los botones
     */
    private void configurarEventos() {
        btnBuscar.addActionListener(e -> eventoBotonBuscar());
        btnAgregar.addActionListener(e -> eventoBotonAgregar());
        btnEditar.addActionListener(e -> eventoBotonEditar());
        btnEliminar.addActionListener(e -> eventoBotonEliminar());
        
        tfBuscar.addActionListener(e -> eventoBotonBuscar());
    }
    
    /**
     * Obtiene el ID seleccionado en la tabla
     */
    protected Long seleccionarID() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada >= 0) {
            Object valor = modeloTabla.getValueAt(filaSeleccionada, 0);
            return valor != null ? Long.parseLong(valor.toString()) : null;
        }
        return null;
    }
    
    /**
     * Limpia la tabla
     */
    protected void limpiarTabla() {
        while (modeloTabla.getRowCount() > 0) {
            modeloTabla.removeRow(0);
        }
    }
    
    // Métodos abstractos que deben implementar las clases hijas
    protected abstract void eventoBotonBuscar();
    protected abstract void eventoBotonAgregar();
    protected abstract void eventoBotonEditar();
    protected abstract void eventoBotonEliminar();
}
