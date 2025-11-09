package org.upemor.reproductor.view.administracion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.upemor.reproductor.logica.Reproductor;
import org.upemor.reproductor.model.entity.Cancion;
import org.upemor.reproductor.estructuras.MiLista;
import java.awt.*;

/**
 * Panel del reproductor con cola y historial
 * @author Sistema Reproductor
 */
public class ReproductorDlg extends JPanel {
    private Reproductor reproductor;
    
    // Componentes de canción actual
    private JLabel lblCancionActual;
    private JLabel lblDetalleCancion;
    private JButton btnAnterior;
    private JButton btnPlayPause;
    private JButton btnSiguiente;
    private JButton btnDetener;
    
    // Tabla de cola
    private JTable tablaCola;
    private DefaultTableModel modeloCola;
    
    public ReproductorDlg(Reproductor reproductor) {
        this.reproductor = reproductor;
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Configurar listener del reproductor
        reproductor.setListener(new Reproductor.ReproductorListener() {
            @Override
            public void onCancionCambiada(Cancion cancion) {
                actualizarInterfaz();
            }
            
            @Override
            public void onEstadoCambiado(boolean reproduciendo) {
                actualizarBotonPlayPause(reproduciendo);
            }
        });
        
        initComponents();
    }
    
    private void initComponents() {
        // Panel superior - Canción actual
        JPanel panelSuperior = crearPanelCancionActual();
        add(panelSuperior, BorderLayout.NORTH);
        
        // Panel central - Solo Cola de reproducción
        JPanel panelCentral = crearPanelCola();
        add(panelCentral, BorderLayout.CENTER);
    }
    
    /**
     * Crea el panel de la canción actual con controles
     */
    private JPanel crearPanelCancionActual() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 120, 180), 3),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panel.setBackground(new Color(245, 245, 247));
        panel.setPreferredSize(new Dimension(0, 200));
        
        // Información de la canción
        JPanel panelInfo = new JPanel(new GridLayout(2, 1, 10, 10));
        panelInfo.setOpaque(false);
        
        lblCancionActual = new JLabel("Ninguna canción reproduciendo");
        lblCancionActual.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblCancionActual.setHorizontalAlignment(SwingConstants.CENTER);
        lblCancionActual.setForeground(new Color(33, 33, 33));
        
        lblDetalleCancion = new JLabel("Agrega canciones a la cola para comenzar");
        lblDetalleCancion.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblDetalleCancion.setForeground(new Color(100, 100, 100));
        lblDetalleCancion.setHorizontalAlignment(SwingConstants.CENTER);
        
        panelInfo.add(lblCancionActual);
        panelInfo.add(lblDetalleCancion);
        
        panel.add(panelInfo, BorderLayout.CENTER);
        
        // Controles de reproducción
        JPanel panelControles = crearPanelControles();
        panel.add(panelControles, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Crea el panel de controles de reproducción
     */
    private JPanel crearPanelControles() {
        JPanel panel = new JPanel(new BorderLayout(10, 15));
        panel.setOpaque(false);
        
        // Botones de control
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setOpaque(false);
        
        btnAnterior = crearBotonControl("⏮️ Anterior", new Color(156, 39, 176));
        btnPlayPause = crearBotonControl("▶️ Play / Pausa", new Color(76, 175, 80));
        btnSiguiente = crearBotonControl("⏭️ Siguiente", new Color(33, 150, 243));
        btnDetener = crearBotonControl("⏹️ Detener", new Color(244, 67, 54));
        
        btnAnterior.addActionListener(e -> reproducirAnterior());
        btnPlayPause.addActionListener(e -> togglePlayPause());
        btnSiguiente.addActionListener(e -> reproducirSiguiente());
        btnDetener.addActionListener(e -> detenerReproduccion());
        
        panelBotones.add(btnAnterior);
        panelBotones.add(btnPlayPause);
        panelBotones.add(btnSiguiente);
        panelBotones.add(btnDetener);
        
        panel.add(panelBotones, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JButton crearBotonControl(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(160, 45));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        
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
     * Crea el panel de cola de reproducción
     */
    private JPanel crearPanelCola() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100), 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Título del panel
        JLabel lblTitulo = new JLabel("📋 Cola de Reproducción");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(lblTitulo, BorderLayout.NORTH);
        
        // Crear tabla
        modeloCola = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modeloCola.addColumn("#");
        modeloCola.addColumn("Título");
        modeloCola.addColumn("Artista");
        modeloCola.addColumn("Álbum");
        modeloCola.addColumn("Duración");
        
        tablaCola = new JTable(modeloCola);
        tablaCola.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaCola.setRowHeight(30);
        tablaCola.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaCola.getTableHeader().setBackground(new Color(76, 175, 80));
        tablaCola.getTableHeader().setForeground(Color.WHITE);
        tablaCola.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaCola.setGridColor(new Color(230, 230, 230));
        
        // Configurar anchos de columnas
        tablaCola.getColumnModel().getColumn(0).setPreferredWidth(50);  // #
        tablaCola.getColumnModel().getColumn(1).setPreferredWidth(300); // Título
        tablaCola.getColumnModel().getColumn(2).setPreferredWidth(200); // Artista
        tablaCola.getColumnModel().getColumn(3).setPreferredWidth(200); // Álbum
        tablaCola.getColumnModel().getColumn(4).setPreferredWidth(100); // Duración
        
        JScrollPane scroll = new JScrollPane(tablaCola);
        scroll.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.add(scroll, BorderLayout.CENTER);
        
        // Botón limpiar cola
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnLimpiarCola = new JButton("🗑️ Limpiar Cola");
        btnLimpiarCola.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLimpiarCola.setBackground(new Color(244, 67, 54));
        btnLimpiarCola.setForeground(Color.WHITE);
        btnLimpiarCola.setFocusPainted(false);
        btnLimpiarCola.setBorderPainted(false);
        btnLimpiarCola.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLimpiarCola.setPreferredSize(new Dimension(170, 40));
        btnLimpiarCola.addActionListener(e -> limpiarCola());
        
        panelBoton.add(btnLimpiarCola);
        panel.add(panelBoton, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Reproduce la siguiente canción
     */
    private void reproducirSiguiente() {
        Cancion cancion = reproductor.reproducirSiguiente();
        
        if (cancion == null) {
            JOptionPane.showMessageDialog(this,
                "No hay más canciones en la cola",
                "Información",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Toggle Play/Pausa
     * Ahora funciona correctamente con pause/resume real
     */
    private void togglePlayPause() {
        if (reproductor.getCancionActual() == null) {
            // Si no hay canción actual, reproducir siguiente
            reproducirSiguiente();
        } else {
            // Si hay canción, alternar pause/resume
            reproductor.togglePausa();
            actualizarInterfaz();
        }
    }
    
    /**
     * Detiene completamente la reproducción
     */
    private void detenerReproduccion() {
        reproductor.detener();
        actualizarInterfaz();
    }
    
    /**
     * Actualiza el botón play/pause según el estado
     */
    private void actualizarBotonPlayPause(boolean reproduciendo) {
        SwingUtilities.invokeLater(() -> {
            if (reproduciendo) {
                btnPlayPause.setText("⏸️ Pausar");
            } else {
                btnPlayPause.setText("▶️ Reproducir");
            }
        });
    }
    
    /**
     * Reproduce la canción anterior
     */
    private void reproducirAnterior() {
        Cancion cancion = reproductor.reproducirAnterior();
        actualizarInterfaz();
        
        if (cancion == null) {
            JOptionPane.showMessageDialog(this,
                "No hay canciones en el historial",
                "Información",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Limpia la cola de reproducción
     */
    private void limpiarCola() {
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Desea limpiar toda la cola de reproducción?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            reproductor.limpiarCola();
            actualizarInterfaz();
        }
    }
    
    /**
     * Actualiza toda la interfaz con los datos actuales
     */
    public void actualizarInterfaz() {
        // Actualizar canción actual
        Cancion actual = reproductor.getCancionActual();
        if (actual != null) {
            lblCancionActual.setText(actual.getTitulo());
            lblDetalleCancion.setText(actual.getArtista() + 
                (actual.getAlbum() != null ? " • " + actual.getAlbum() : "") +
                " • " + actual.getDuracionFormateada());
        } else {
            lblCancionActual.setText("Ninguna canción reproduciendo");
            lblDetalleCancion.setText("Agrega canciones a la cola para comenzar");
        }
        
        // Actualizar cola
        actualizarTablaCola();
    }
    
    /**
     * Actualiza la tabla de cola de reproducción
     */
    private void actualizarTablaCola() {
        // Limpiar tabla
        while (modeloCola.getRowCount() > 0) {
            modeloCola.removeRow(0);
        }
        
        // Obtener canciones de la cola
        MiLista<Cancion> cola = reproductor.obtenerCola();
        
        // Llenar tabla con numeración
        for (int i = 0; i < cola.tamanio(); i++) {
            Cancion cancion = cola.obtener(i);
            Object[] fila = {
                (i + 1), // Número en la cola
                cancion.getTitulo(),
                cancion.getArtista(),
                cancion.getAlbum() != null ? cancion.getAlbum() : "N/A",
                cancion.getDuracionFormateada()
            };
            modeloCola.addRow(fila);
        }
    }
}
