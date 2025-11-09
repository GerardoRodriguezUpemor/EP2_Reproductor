package org.upemor.reproductor.view;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import javax.swing.*;
import org.upemor.reproductor.logica.Reproductor;
import org.upemor.reproductor.view.administracion.BibliotecaDlg;
import org.upemor.reproductor.view.administracion.ReproductorDlg;
import org.upemor.reproductor.view.administracion.HistorialDlg;
import java.awt.*;

/**
 * Ventana principal del reproductor de canciones
 * @author Sistema Reproductor
 */
public class PrincipalDlg extends JFrame {
    private Reproductor reproductor;
    private BibliotecaDlg bibliotecaDlg;
    private ReproductorDlg reproductorDlg;
    private HistorialDlg historialDlg;
    private JTabbedPane tabbedPane;
    
    public PrincipalDlg() {
        initComponents();
        setVisible(true);
    }
    
    private void initComponents() {
        setTitle("🎵 Reproductor de Canciones - Sistema de Estructuras de Datos");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // Crear instancia del reproductor
        reproductor = new Reproductor();
        
        // Crear panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Crear header
        JPanel panelHeader = crearHeader();
        panelPrincipal.add(panelHeader, BorderLayout.NORTH);
        
        // Crear tabs
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Tab de Reproductor (PRIMERO)
        reproductorDlg = new ReproductorDlg(reproductor);
        tabbedPane.addTab("🎵 Reproductor", null, reproductorDlg, "Reproductor y cola de reproducción");
        
        // Tab de Biblioteca (SEGUNDO)
        bibliotecaDlg = new BibliotecaDlg(reproductor);
        tabbedPane.addTab("📚 Biblioteca", null, bibliotecaDlg, "Gestiona tu biblioteca de canciones");
        
        // Tab de Historial (TERCERO)
        historialDlg = new HistorialDlg(reproductor);
        tabbedPane.addTab("📜 Historial", null, historialDlg, "Historial de reproducción");
        
        // Listener para actualizar cuando se cambie de tab
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            if (selectedIndex == 0) { // Reproductor
                reproductorDlg.actualizarInterfaz();
            } else if (selectedIndex == 2) { // Historial
                historialDlg.actualizarInterfaz();
            }
        });
        
        panelPrincipal.add(tabbedPane, BorderLayout.CENTER);
        
        // Crear footer
        JPanel panelFooter = crearFooter();
        panelPrincipal.add(panelFooter, BorderLayout.SOUTH);
        
        add(panelPrincipal);
    }
    
    /**
     * Crea el header de la aplicación
     */
    private JPanel crearHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(100, 100, 100)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel lblTitulo = new JLabel("🎵 REPRODUCTOR DE CANCIONES");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(63, 81, 181));
        
        JLabel lblSubtitulo = new JLabel("Sistema con Estructuras de Datos: Lista, Pila y Cola");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblSubtitulo.setForeground(Color.GRAY);
        
        JPanel panelTextos = new JPanel(new GridLayout(2, 1));
        panelTextos.add(lblTitulo);
        panelTextos.add(lblSubtitulo);
        
        panel.add(panelTextos, BorderLayout.WEST);
        
        return panel;
    }
    
    /**
     * Crea el footer de la aplicación
     */
    private JPanel crearFooter() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        JLabel lblInfo = new JLabel("📊 Estructuras de Datos Implementadas: MiLista (Lista Enlazada) | MiPila (LIFO) | MiCola (FIFO)");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblInfo.setForeground(Color.GRAY);
        
        JLabel lblVersion = new JLabel("v1.0 - 2025");
        lblVersion.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblVersion.setForeground(Color.GRAY);
        
        panel.add(lblInfo, BorderLayout.WEST);
        panel.add(lblVersion, BorderLayout.EAST);
        
        return panel;
    }
    
    public static void main(String[] args) {
        try {
            // Configurar FlatLaf
            FlatMacLightLaf.setup();
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);
            UIManager.put("TextComponent.arc", 10);
            UIManager.setLookAndFeel(new FlatMacLightLaf());
        } catch (Exception e) {
            System.err.println("Error al configurar Look and Feel: " + e.getMessage());
        }
        
        SwingUtilities.invokeLater(() -> new PrincipalDlg());
    }
}
