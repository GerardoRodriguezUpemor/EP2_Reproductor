package org.upemor.reproductor.logica;

import org.upemor.reproductor.model.entity.Cancion;
import org.upemor.reproductor.estructuras.MiCola;
import org.upemor.reproductor.estructuras.MiPila;
import org.upemor.reproductor.estructuras.MiLista;

/**
 * Clase que gestiona la lógica del reproductor de música
 * Usa MiCola para cola de reproducción y MiPila para historial
 * @author Sistema Reproductor
 */
public class Reproductor implements ReproductorAudio.ReproductorListener {
    private MiCola<Cancion> colaReproduccion;
    private MiPila<Cancion> historial;
    private Cancion cancionActual;
    private boolean reproduciendo;
    private ReproductorAudio reproductorAudio;
    private ReproductorListener listener;
    
    /**
     * Interfaz para notificar cambios en el reproductor
     */
    public interface ReproductorListener {
        void onCancionCambiada(Cancion cancion);
        void onEstadoCambiado(boolean reproduciendo);
    }
    
    public Reproductor() {
        this.colaReproduccion = new MiCola<>();
        this.historial = new MiPila<>();
        this.cancionActual = null;
        this.reproduciendo = false;
        this.reproductorAudio = new ReproductorAudio();
        this.reproductorAudio.setListener(this);
    }
    
    public void setListener(ReproductorListener listener) {
        this.listener = listener;
    }
    
    /**
     * Agrega una canción a la cola de reproducción
     */
    public void agregarACola(Cancion cancion) {
        colaReproduccion.encolar(cancion);
        System.out.println("✓ Canción agregada a la cola: " + cancion.getTitulo());
    }
    
    /**
     * Reproduce la siguiente canción de la cola
     */
    public Cancion reproducirSiguiente() {
        // Si hay canción actual, agregarla al historial
        if (cancionActual != null) {
            historial.apilar(cancionActual);
        }
        
        // Obtener siguiente canción de la cola
        cancionActual = colaReproduccion.desencolar();
        
        if (cancionActual != null) {
            reproduciendo = true;
            // Reproducir audio real
            reproductorAudio.reproducir(cancionActual);
            System.out.println("▶️ Reproduciendo: " + cancionActual.getTitulo() + " - " + cancionActual.getArtista());
            notificarCancionCambiada(cancionActual);
        } else {
            reproduciendo = false;
            reproductorAudio.detener();
            System.out.println("⏹️ No hay más canciones en la cola");
        }
        
        return cancionActual;
    }
    
    /**
     * Reproduce la canción anterior del historial
     */
    public Cancion reproducirAnterior() {
        Cancion anterior = historial.desapilar();
        
        if (anterior != null) {
            // Si hay canción actual, regresarla a la cola
            if (cancionActual != null) {
                // Crear nueva cola temporal
                MiCola<Cancion> nuevaCola = new MiCola<>();
                nuevaCola.encolar(cancionActual);
                
                // Agregar el resto de canciones
                while (!colaReproduccion.estaVacia()) {
                    nuevaCola.encolar(colaReproduccion.desencolar());
                }
                
                colaReproduccion = nuevaCola;
            }
            
            cancionActual = anterior;
            reproduciendo = true;
            
            // REPRODUCIR REALMENTE LA CANCIÓN ANTERIOR
            reproductorAudio.reproducir(cancionActual);
            
            System.out.println("⏮️ Reproduciendo anterior: " + cancionActual.getTitulo());
            notificarCancionCambiada(cancionActual);
        } else {
            System.out.println("⏹️ No hay canciones en el historial");
        }
        
        return cancionActual;
    }
    
    /**
     * Reproduce una canción específica
     */
    public void reproducir(Cancion cancion) {
        if (cancionActual != null) {
            historial.apilar(cancionActual);
        }
        
        cancionActual = cancion;
        reproduciendo = true;
        reproductorAudio.reproducir(cancion);
        System.out.println("▶️ Reproduciendo: " + cancion.getTitulo());
        notificarCancionCambiada(cancion);
    }
    
    /**
     * Pausa/Resume la reproducción
     */
    public void togglePausa() {
        if (cancionActual != null) {
            if (reproductorAudio.isReproduciendo()) {
                reproductorAudio.pausar();
                reproduciendo = false;
                System.out.println("⏸️ Pausado");
            } else {
                reproductorAudio.reanudar();
                reproduciendo = true;
                System.out.println("▶️ Reproduciendo");
            }
            notificarEstadoCambiado(reproduciendo);
        }
    }
    
    /**
     * Detiene la reproducción
     */
    public void detener() {
        // Si hay canción actual, agregarla al historial antes de detener
        if (cancionActual != null) {
            historial.apilar(cancionActual);
            System.out.println("📜 Canción agregada al historial: " + cancionActual.getTitulo());
        }
        
        reproduciendo = false;
        reproductorAudio.detener();
        cancionActual = null;
        System.out.println("⏹️ Reproducción detenida");
        notificarEstadoCambiado(false);
    }
    
    /**
     * Limpia la cola de reproducción
     */
    public void limpiarCola() {
        colaReproduccion.limpiar();
        System.out.println("🗑️ Cola de reproducción limpiada");
    }
    
    /**
     * Limpia el historial
     */
    public void limpiarHistorial() {
        historial.limpiar();
        System.out.println("🗑️ Historial limpiado");
    }
    
    // Getters
    public Cancion getCancionActual() {
        return cancionActual;
    }
    
    public boolean isReproduciendo() {
        return reproduciendo;
    }
    
    public MiLista<Cancion> obtenerCola() {
        return colaReproduccion.aLista();
    }
    
    public MiLista<Cancion> obtenerHistorial() {
        return historial.aLista();
    }
    
    public int getTamanoCola() {
        return colaReproduccion.tamanio();
    }
    
    public int getTamanoHistorial() {
        return historial.tamanio();
    }
    
    // Implementación de ReproductorAudio.ReproductorListener
    @Override
    public void onCancionTerminada() {
        System.out.println("✅ Canción terminada, reproduciendo siguiente automáticamente...");
        reproducirSiguiente();
    }
    
    @Override
    public void onError(String mensaje) {
        System.err.println("❌ Error en reproductor de audio: " + mensaje);
    }
    
    @Override
    public void onEstadoCambiado(boolean reproduciendo) {
        this.reproduciendo = reproduciendo;
        notificarEstadoCambiado(reproduciendo);
    }
    
    // Métodos de notificación
    private void notificarCancionCambiada(Cancion cancion) {
        if (listener != null) {
            listener.onCancionCambiada(cancion);
        }
    }
    
    private void notificarEstadoCambiado(boolean reproduciendo) {
        if (listener != null) {
            listener.onEstadoCambiado(reproduciendo);
        }
    }
}
