package org.upemor.reproductor.view.administracion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.upemor.reproductor.logica.Reproductor;
import org.upemor.reproductor.model.entity.Cancion;
import org.upemor.reproductor.estructuras.MiLista;
import java.awt.*;

/**
 * Panel del historial de reproducción
 * @author Sistema Reproductor
 */
public class HistorialDlg extends JPanel {
    private Reproductor reproductor;
    private JTable tablaHistorial;
    private DefaultTableModel modeloHistorial;
    private JLabel lblContador;
    
    public HistorialDlg(Reproductor reproductor) {
        this.reproductor = reproductor;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        initComponents();
    }
    
    private void initComponents() {
        // Panel superior con información
        JPanel panelSuperior = crearPanelSuperior();
        add(panelSuperior, BorderLayout.NORTH);
        
        // Panel central con tabla
        JPanel panelCentral = crearPanelCentral();
        add(panelCentral, BorderLayout.CENTER);
        
        // Panel inferior con botones
        JPanel panelInferior = crearPanelInferior();
        add(panelInferior, BorderLayout.SOUTH);
        
        // Cargar datos iniciales
        actualizarInterfaz();
    }
    
    /**
     * Crea el panel superior con información del historial
     */
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setBackground(new Color(245, 245, 247));
        panel.setPreferredSize(new Dimension(0, 80));
        
        // Icono y título
        JLabel lblTitulo = new JLabel("📜 Historial de Reproducción");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        
        // Contador
        lblContador = new JLabel("0 canciones reproducidas");
        lblContador.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblContador.setForeground(Color.GRAY);
        
        JPanel panelTextos = new JPanel(new GridLayout(2, 1));
        panelTextos.setOpaque(false);
        panelTextos.add(lblTitulo);
        panelTextos.add(lblContador);
        
        panel.add(panelTextos, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Crea el panel central con la tabla de historial
     */
    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Crear modelo de tabla
        modeloHistorial = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modeloHistorial.addColumn("#");
        modeloHistorial.addColumn("Título");
        modeloHistorial.addColumn("Artista");
        modeloHistorial.addColumn("Álbum");
        modeloHistorial.addColumn("Duración");
        
        // Crear tabla
        tablaHistorial = new JTable(modeloHistorial);
        tablaHistorial.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaHistorial.setRowHeight(30);
        tablaHistorial.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaHistorial.getTableHeader().setBackground(new Color(60, 120, 180));
        tablaHistorial.getTableHeader().setForeground(Color.WHITE);
        tablaHistorial.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaHistorial.setGridColor(new Color(230, 230, 230));
        
        // Configurar anchos de columnas
        tablaHistorial.getColumnModel().getColumn(0).setPreferredWidth(50);  // #
        tablaHistorial.getColumnModel().getColumn(1).setPreferredWidth(300); // Título
        tablaHistorial.getColumnModel().getColumn(2).setPreferredWidth(200); // Artista
        tablaHistorial.getColumnModel().getColumn(3).setPreferredWidth(200); // Álbum
        tablaHistorial.getColumnModel().getColumn(4).setPreferredWidth(100); // Duración
        
        JScrollPane scroll = new JScrollPane(tablaHistorial);
        scroll.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        panel.add(scroll, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Crea el panel inferior con botones
     */
    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton btnReproducir = crearBoton("▶️ Reproducir", new Color(76, 175, 80));
        btnReproducir.addActionListener(e -> reproducirSeleccionada());
        
        JButton btnActualizar = crearBoton("🔄 Actualizar", new Color(33, 150, 243));
        btnActualizar.addActionListener(e -> actualizarInterfaz());
        
        JButton btnLimpiar = crearBoton("🗑️ Limpiar Historial", new Color(244, 67, 54));
        btnLimpiar.addActionListener(e -> limpiarHistorial());
        
        panel.add(btnReproducir);
        panel.add(btnActualizar);
        panel.add(btnLimpiar);
        
        return panel;
    }
    
    /**
     * Crea un botón estilizado
     */
    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 40));
        
        // Efecto hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(color);
            }
        });
        
        return btn;
    }
    
    /**
     * Reproduce la canción seleccionada del historial
     */
    private void reproducirSeleccionada() {
        int filaSeleccionada = tablaHistorial.getSelectedRow();
        
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this,
                "Debe seleccionar una canción del historial",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Obtener la canción del historial
        MiLista<Cancion> historial = reproductor.obtenerHistorial();
        
        if (filaSeleccionada < historial.tamanio()) {
            Cancion cancion = historial.obtener(filaSeleccionada);
            
            // Agregar a la cola y reproducir
            reproductor.agregarACola(cancion);
            reproductor.reproducirSiguiente();
            
            JOptionPane.showMessageDialog(this,
                "Reproduciendo: " + cancion.getTitulo(),
                "Reproducción Iniciada",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Limpia todo el historial
     */
    private void limpiarHistorial() {
        if (modeloHistorial.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "El historial ya está vacío",
                "Información",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de limpiar todo el historial?\nEsta acción no se puede deshacer.",
            "Confirmar Limpieza",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            reproductor.limpiarHistorial();
            actualizarInterfaz();
            
            JOptionPane.showMessageDialog(this,
                "Historial limpiado correctamente",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Actualiza la interfaz con los datos actuales del historial
     */
    public void actualizarInterfaz() {
        // Limpiar tabla
        while (modeloHistorial.getRowCount() > 0) {
            modeloHistorial.removeRow(0);
        }
        
        // Obtener historial
        MiLista<Cancion> historial = reproductor.obtenerHistorial();
        
        // Actualizar contador
        lblContador.setText(historial.tamanio() + " canción(es) reproducida(s)");
        
        // Llenar tabla (en orden inverso para mostrar la más reciente primero)
        for (int i = historial.tamanio() - 1; i >= 0; i--) {
            Cancion cancion = historial.obtener(i);
            Object[] fila = {
                (historial.tamanio() - i), // Número en orden descendente
                cancion.getTitulo(),
                cancion.getArtista(),
                cancion.getAlbum() != null ? cancion.getAlbum() : "N/A",
                cancion.getDuracionFormateada()
            };
            modeloHistorial.addRow(fila);
        }
    }
}
