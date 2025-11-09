package org.upemor.reproductor.controller;

import org.upemor.reproductor.model.entity.Cancion;
import org.upemor.reproductor.model.repository.CancionRepository;
import org.upemor.reproductor.estructuras.MiLista;

/**
 * Controlador para gestionar operaciones de canciones
 * @author Sistema Reproductor
 */
public class CancionController {
    private CancionRepository repository;
    
    public CancionController() throws Exception {
        this.repository = new CancionRepository();
    }
    
    /**
     * Obtiene todas las canciones
     */
    public MiLista<Cancion> obtenerTodas() throws Exception {
        try {
            return repository.obtenerTodas();
        } catch (Exception e) {
            System.err.println("Error en controller al obtener canciones: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Busca canciones por texto
     */
    public MiLista<Cancion> buscar(String texto) throws Exception {
        try {
            if (texto == null || texto.trim().isEmpty()) {
                return obtenerTodas();
            }
            return repository.buscar(texto);
        } catch (Exception e) {
            System.err.println("Error en controller al buscar: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Obtiene una canción por ID
     */
    public Cancion obtenerPorId(Long id) throws Exception {
        try {
            return repository.obtenerPorId(id);
        } catch (Exception e) {
            System.err.println("Error en controller al obtener por ID: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Crea una nueva canción
     */
    public boolean crear(Cancion cancion) throws Exception {
        if (!validar(cancion)) {
            throw new Exception("Datos de canción inválidos");
        }
        
        try {
            return repository.crear(cancion);
        } catch (Exception e) {
            System.err.println("Error en controller al crear: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Actualiza una canción
     */
    public boolean actualizar(Cancion cancion) throws Exception {
        if (!validar(cancion)) {
            throw new Exception("Datos de canción inválidos");
        }
        
        try {
            return repository.actualizar(cancion);
        } catch (Exception e) {
            System.err.println("Error en controller al actualizar: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Elimina una canción
     */
    public boolean eliminar(Long id) throws Exception {
        try {
            return repository.eliminar(id);
        } catch (Exception e) {
            System.err.println("Error en controller al eliminar: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Valida los datos de una canción
     */
    private boolean validar(Cancion cancion) throws Exception {
        if (cancion == null) {
            throw new Exception("La canción no puede ser nula");
        }
        if (cancion.getTitulo() == null || cancion.getTitulo().trim().isEmpty()) {
            throw new Exception("El título no puede estar vacío");
        }
        if (cancion.getArtista() == null || cancion.getArtista().trim().isEmpty()) {
            throw new Exception("El artista no puede estar vacío");
        }
        if (cancion.getDuracion() == null || cancion.getDuracion() <= 0) {
            throw new Exception("La duración debe ser mayor a 0");
        }
        return true;
    }
}
