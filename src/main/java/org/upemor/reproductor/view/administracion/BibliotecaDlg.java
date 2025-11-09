package org.upemor.reproductor.view.administracion;

import javax.swing.*;
import org.upemor.reproductor.controller.CancionController;
import org.upemor.reproductor.estructuras.MiLista;
import org.upemor.reproductor.model.entity.Cancion;
import org.upemor.reproductor.view.tools.BaseDlg;
import org.upemor.reproductor.logica.Reproductor;
import java.awt.*;

/**
 * Panel para gestionar la biblioteca de canciones
 * @author Sistema Reproductor
 */
public class BibliotecaDlg extends BaseDlg {
    private CancionController controller;
    private Reproductor reproductor;
    private JButton btnAgregarACola;
    private JButton btnReproducirTodo;
    private MiLista<Cancion> cancionesActuales; // Lista de canciones mostradas
    
    public BibliotecaDlg(Reproductor reproductor) {
        super();
        this.reproductor = reproductor;
        this.cancionesActuales = new MiLista<>();
        inicializar();
    }
    
    private void inicializar() {
        try {
            controller = new CancionController();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al inicializar controlador: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        // Configurar columnas
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Título");
        modeloTabla.addColumn("Artista");
        modeloTabla.addColumn("Álbum");
        modeloTabla.addColumn("Duración");
        
        // Configurar anchos de columnas
        tabla.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(250);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(200);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(200);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(80);
        
        // Personalizar textos
        lbBuscar.setText("Buscar canción (título o artista):");
        btnBuscar.setText("🔍 Buscar");
        btnAgregar.setText("+Canción");
        btnEditar.setText("✏️ Editar");
        btnEliminar.setText("-Canción");
        
        // Agregar botón adicional
        agregarBotonCola();
        
        // Cargar datos iniciales
        eventoBotonBuscar();
    }
    
    /**
     * Agrega botón para agregar a cola de reproducción
     */
    private void agregarBotonCola() {
        JPanel panelBotones = (JPanel) getComponent(2); // Panel inferior
        
        // Cambiar el layout a BorderLayout para separar botones
        panelBotones.setLayout(new BorderLayout(10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // Panel izquierdo con botones de gestión
        JPanel panelIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelIzquierdo.add(btnAgregar);
        panelIzquierdo.add(btnEditar);
        panelIzquierdo.add(btnEliminar);
        
        // Panel derecho con botones de reproducción
        JPanel panelDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        
        btnAgregarACola = crearBoton("+Cola", new Color(156, 39, 176));
        btnAgregarACola.addActionListener(e -> agregarAColaReproduccion());
        
        btnReproducirTodo = crearBoton("▶", new Color(76, 175, 80));
        btnReproducirTodo.addActionListener(e -> reproducirTodasLasCanciones());
        
        panelDerecho.add(btnAgregarACola);
        panelDerecho.add(btnReproducirTodo);
        
        // Limpiar panel y agregar los nuevos sub-paneles
        panelBotones.removeAll();
        panelBotones.add(panelIzquierdo, BorderLayout.WEST);
        panelBotones.add(panelDerecho, BorderLayout.EAST);
    }
    
    @Override
    protected void eventoBotonBuscar() {
        try {
            limpiarTabla();
            
            String textoBusqueda = tfBuscar.getText().trim();
            MiLista<Cancion> canciones = controller.buscar(textoBusqueda);
            
            // Guardar las canciones actuales
            cancionesActuales = canciones;
            
            // Iterar usando nuestra estructura MiLista
            for (int i = 0; i < canciones.tamanio(); i++) {
                Cancion cancion = canciones.obtener(i);
                Object[] fila = {
                    cancion.getId(),
                    cancion.getTitulo(),
                    cancion.getArtista(),
                    cancion.getAlbum() != null ? cancion.getAlbum() : "N/A",
                    cancion.getDuracionFormateada()
                };
                modeloTabla.addRow(fila);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al buscar canciones: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    protected void eventoBotonAgregar() {
        try {
            CancionModalDlg modal = new CancionModalDlg(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                true
            );
            modal.setVisible(true);
            
            // Actualizar tabla
            eventoBotonBuscar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al abrir formulario: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    protected void eventoBotonEditar() {
        try {
            Long id = seleccionarID();
            if (id == null) {
                JOptionPane.showMessageDialog(this,
                    "Debe seleccionar una canción para editar",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            CancionModalDlg modal = new CancionModalDlg(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                true,
                id
            );
            modal.setVisible(true);
            
            // Actualizar tabla
            eventoBotonBuscar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al editar canción: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    protected void eventoBotonEliminar() {
        try {
            Long id = seleccionarID();
            if (id == null) {
                JOptionPane.showMessageDialog(this,
                    "Debe seleccionar una canción para eliminar",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar esta canción?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                boolean eliminado = controller.eliminar(id);
                if (eliminado) {
                    JOptionPane.showMessageDialog(this,
                        "Canción eliminada correctamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    eventoBotonBuscar();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "No se pudo eliminar la canción",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al eliminar canción: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Agrega la canción seleccionada a la cola de reproducción
     */
    private void agregarAColaReproduccion() {
        try {
            Long id = seleccionarID();
            if (id == null) {
                JOptionPane.showMessageDialog(this,
                    "Debe seleccionar una canción",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            Cancion cancion = controller.obtenerPorId(id);
            if (cancion != null) {
                reproductor.agregarACola(cancion);
                JOptionPane.showMessageDialog(this,
                    "Canción agregada a la cola: " + cancion.getTitulo(),
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al agregar a la cola: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Reproduce todas las canciones de la biblioteca en orden
     */
    private void reproducirTodasLasCanciones() {
        try {
            if (cancionesActuales == null || cancionesActuales.tamanio() == 0) {
                JOptionPane.showMessageDialog(this,
                    "No hay canciones para reproducir",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Limpiar la cola actual
            reproductor.limpiarCola();
            
            // Agregar todas las canciones a la cola
            for (int i = 0; i < cancionesActuales.tamanio(); i++) {
                Cancion cancion = cancionesActuales.obtener(i);
                reproductor.agregarACola(cancion);
            }
            
            // Iniciar reproducción
            reproductor.reproducirSiguiente();
            
            JOptionPane.showMessageDialog(this,
                "Reproduciendo " + cancionesActuales.tamanio() + " canciones",
                "Reproducción Iniciada", JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al reproducir canciones: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

