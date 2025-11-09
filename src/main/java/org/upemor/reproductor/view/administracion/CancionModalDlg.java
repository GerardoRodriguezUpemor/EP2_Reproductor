package org.upemor.reproductor.view.administracion;

import javax.swing.*;
import org.upemor.reproductor.controller.CancionController;
import org.upemor.reproductor.model.entity.Cancion;
import java.awt.*;
import java.io.File;

/**
 * Diálogo modal para agregar/editar canciones
 * @author Sistema Reproductor
 */
public class CancionModalDlg extends JDialog {
    private CancionController controller;
    private Long idCancion;
    
    private JTextField tfTitulo;
    private JTextField tfArtista;
    private JTextField tfAlbum;
    private JSpinner spDuracion;
    private JTextField tfRuta;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private JButton btnSeleccionarArchivo;
    
    /**
     * Constructor para NUEVA canción
     */
    public CancionModalDlg(JFrame parent, boolean modal) {
        this(parent, modal, null);
    }
    
    /**
     * Constructor para EDITAR canción
     */
    public CancionModalDlg(JFrame parent, boolean modal, Long idCancion) {
        super(parent, modal);
        this.idCancion = idCancion;
        
        try {
            controller = new CancionController();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al inicializar: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        initComponents();
        
        if (idCancion != null) {
            cargarDatos();
        }
    }
    
    private void initComponents() {
        setTitle(idCancion == null ? "➕ Agregar Canción" : "✏️ Editar Canción");
        setSize(500, 400);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel del formulario
        JPanel panelFormulario = crearPanelFormulario();
        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel panelBotones = crearPanelBotones();
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        add(panelPrincipal);
    }
    
    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
        
        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);
        
        // Título
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lbTitulo = new JLabel("Título:*");
        lbTitulo.setFont(labelFont);
        panel.add(lbTitulo, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        tfTitulo = new JTextField();
        tfTitulo.setFont(fieldFont);
        tfTitulo.setPreferredSize(new Dimension(300, 30));
        panel.add(tfTitulo, gbc);
        
        // Artista
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        JLabel lbArtista = new JLabel("Artista:*");
        lbArtista.setFont(labelFont);
        panel.add(lbArtista, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        tfArtista = new JTextField();
        tfArtista.setFont(fieldFont);
        tfArtista.setPreferredSize(new Dimension(300, 30));
        panel.add(tfArtista, gbc);
        
        // Álbum
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        JLabel lbAlbum = new JLabel("Álbum:");
        lbAlbum.setFont(labelFont);
        panel.add(lbAlbum, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        tfAlbum = new JTextField();
        tfAlbum.setFont(fieldFont);
        tfAlbum.setPreferredSize(new Dimension(300, 30));
        panel.add(tfAlbum, gbc);
        
        // Duración
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        JLabel lbDuracion = new JLabel("Duración (seg):*");
        lbDuracion.setFont(labelFont);
        panel.add(lbDuracion, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(180, 1, 3600, 1);
        spDuracion = new JSpinner(spinnerModel);
        spDuracion.setFont(fieldFont);
        spDuracion.setPreferredSize(new Dimension(300, 30));
        panel.add(spDuracion, gbc);
        
        // Ruta
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        JLabel lbRuta = new JLabel("Ruta archivo:");
        lbRuta.setFont(labelFont);
        panel.add(lbRuta, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        JPanel panelRuta = new JPanel(new BorderLayout(5, 0));
        
        tfRuta = new JTextField();
        tfRuta.setFont(fieldFont);
        tfRuta.setPreferredSize(new Dimension(200, 30));
        
        btnSeleccionarArchivo = new JButton("📁");
        btnSeleccionarArchivo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSeleccionarArchivo.setPreferredSize(new Dimension(50, 30));
        btnSeleccionarArchivo.setToolTipText("Seleccionar archivo MP3");
        btnSeleccionarArchivo.addActionListener(e -> seleccionarArchivo());
        
        panelRuta.add(tfRuta, BorderLayout.CENTER);
        panelRuta.add(btnSeleccionarArchivo, BorderLayout.EAST);
        
        panel.add(panelRuta, gbc);
        
        return panel;
    }
    
    /**
     * Abre un selector de archivos para elegir un MP3
     */
    private void seleccionarArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar archivo MP3");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) return true;
                String nombre = f.getName().toLowerCase();
                return nombre.endsWith(".mp3") || nombre.endsWith(".wav");
            }
            
            @Override
            public String getDescription() {
                return "Archivos de audio (*.mp3, *.wav)";
            }
        });
        
        // Establecer directorio inicial
        String userHome = System.getProperty("user.home");
        fileChooser.setCurrentDirectory(new File(userHome + "\\Music"));
        
        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            tfRuta.setText(archivo.getAbsolutePath());
            
            // Si el título está vacío, usar el nombre del archivo
            if (tfTitulo.getText().trim().isEmpty()) {
                String nombreArchivo = archivo.getName();
                nombreArchivo = nombreArchivo.substring(0, nombreArchivo.lastIndexOf('.'));
                tfTitulo.setText(nombreArchivo);
            }
        }
    }
    
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        
        btnCancelar = new JButton("❌ Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCancelar.setPreferredSize(new Dimension(120, 35));
        btnCancelar.addActionListener(e -> dispose());
        
        btnGuardar = new JButton("💾 Guardar");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGuardar.setBackground(new Color(76, 175, 80));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setPreferredSize(new Dimension(120, 35));
        btnGuardar.addActionListener(e -> guardar());
        
        panel.add(btnCancelar);
        panel.add(btnGuardar);
        
        return panel;
    }
    
    private void cargarDatos() {
        try {
            Cancion cancion = controller.obtenerPorId(idCancion);
            if (cancion != null) {
                tfTitulo.setText(cancion.getTitulo());
                tfArtista.setText(cancion.getArtista());
                tfAlbum.setText(cancion.getAlbum());
                spDuracion.setValue(cancion.getDuracion());
                tfRuta.setText(cancion.getRutaArchivo());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar datos: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void guardar() {
        try {
            // Validar campos
            if (tfTitulo.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "El título es obligatorio",
                    "Validación", JOptionPane.WARNING_MESSAGE);
                tfTitulo.requestFocus();
                return;
            }
            
            if (tfArtista.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "El artista es obligatorio",
                    "Validación", JOptionPane.WARNING_MESSAGE);
                tfArtista.requestFocus();
                return;
            }
            
            // Crear/actualizar canción
            Cancion cancion = new Cancion();
            cancion.setId(idCancion);
            cancion.setTitulo(tfTitulo.getText().trim());
            cancion.setArtista(tfArtista.getText().trim());
            cancion.setAlbum(tfAlbum.getText().trim());
            cancion.setDuracion((Integer) spDuracion.getValue());
            cancion.setRutaArchivo(tfRuta.getText().trim());
            
            boolean exito;
            if (idCancion == null) {
                exito = controller.crear(cancion);
            } else {
                exito = controller.actualizar(cancion);
            }
            
            if (exito) {
                JOptionPane.showMessageDialog(this,
                    "Canción guardada correctamente",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "No se pudo guardar la canción",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al guardar: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
