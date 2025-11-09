package org.upemor.reproductor.logica;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.upemor.reproductor.model.entity.Cancion;

import java.io.File;

/**
 * Reproductor de audio MP3 con soporte completo de pause/resume
 * Usa JavaFX MediaPlayer que soporta todas las operaciones de control
 * @author Sistema Reproductor
 */
public class ReproductorAudio {
    private MediaPlayer mediaPlayer;
    private Thread hiloReproduccion;
    private boolean reproduciendo;
    private boolean pausado;
    private Cancion cancionActual;
    private ReproductorListener listener;
    
    /**
     * Interfaz para escuchar eventos del reproductor
     */
    public interface ReproductorListener {
        void onCancionTerminada();
        void onError(String mensaje);
        void onEstadoCambiado(boolean reproduciendo);
    }
    
    public ReproductorAudio() {
        this.reproduciendo = false;
        this.pausado = false;
    }
    
    public void setListener(ReproductorListener listener) {
        this.listener = listener;
    }
    
    /**
     * Reproduce un archivo MP3 usando JavaFX MediaPlayer
     * Soporta pause/resume completo
     */
    public void reproducir(Cancion cancion) {
        // Detener reproducción actual si existe
        detener();
        
        this.cancionActual = cancion;
        
        // Verificar que existe la ruta del archivo
        if (cancion.getRutaArchivo() == null || cancion.getRutaArchivo().isEmpty()) {
            notificarError("La canción no tiene ruta de archivo especificada");
            // Simular reproducción sin archivo
            simularReproduccion(cancion);
            return;
        }
        
        File archivoMp3 = new File(cancion.getRutaArchivo());
        
        // Si el archivo no existe, simular reproducción
        if (!archivoMp3.exists()) {
            System.out.println("⚠️  Archivo no encontrado: " + cancion.getRutaArchivo());
            System.out.println("▶️  Simulando reproducción de: " + cancion.getTitulo());
            simularReproduccion(cancion);
            return;
        }
        
        // Reproducir archivo real con JavaFX MediaPlayer
        try {
            String uriArchivo = archivoMp3.toURI().toString();
            Media media = new Media(uriArchivo);
            mediaPlayer = new MediaPlayer(media);
            
            // Configurar listeners
            mediaPlayer.setOnReady(() -> {
                reproduciendo = true;
                pausado = false;
                notificarEstadoCambiado(true);
                System.out.println("▶️  Reproduciendo archivo: " + archivoMp3.getName());
                mediaPlayer.play();
            });
            
            mediaPlayer.setOnEndOfMedia(() -> {
                reproduciendo = false;
                pausado = false;
                notificarEstadoCambiado(false);
                System.out.println("✅ Canción terminada: " + cancion.getTitulo());
                notificarCancionTerminada();
            });
            
            mediaPlayer.setOnError(() -> {
                System.err.println("❌ Error al reproducir: " + mediaPlayer.getError().getMessage());
                notificarError("Error al reproducir: " + mediaPlayer.getError().getMessage());
                reproduciendo = false;
                notificarEstadoCambiado(false);
            });
            
        } catch (Exception e) {
            System.err.println("❌ Error al inicializar MediaPlayer: " + e.getMessage());
            notificarError("Error al inicializar: " + e.getMessage());
            // Intentar con simulación
            simularReproduccion(cancion);
        }
    }
    
    /**
     * Simula la reproducción cuando no hay archivo MP3
     */
    private void simularReproduccion(Cancion cancion) {
        hiloReproduccion = new Thread(() -> {
            try {
                reproduciendo = true;
                pausado = false;
                notificarEstadoCambiado(true);
                
                System.out.println("🎵 [SIMULACIÓN] Reproduciendo: " + cancion.getTitulo() + " - " + cancion.getArtista());
                System.out.println("⏱️  Duración: " + cancion.getDuracionFormateada());
                
                // Simular duración de la canción
                int duracionMs = cancion.getDuracion() * 1000;
                int tiempoTranscurrido = 0;
                
                while (tiempoTranscurrido < duracionMs && reproduciendo) {
                    if (!pausado) {
                        Thread.sleep(1000); // 1 segundo
                        tiempoTranscurrido += 1000;
                        
                        // Mostrar progreso cada 10 segundos
                        if (tiempoTranscurrido % 10000 == 0) {
                            int segundosTranscurridos = tiempoTranscurrido / 1000;
                            System.out.println("   ⏱️  " + formatearTiempo(segundosTranscurridos) + " / " + cancion.getDuracionFormateada());
                        }
                    } else {
                        Thread.sleep(100); // Esperar mientras está pausado
                    }
                }
                
                if (reproduciendo) {
                    System.out.println("✅ Canción terminada: " + cancion.getTitulo());
                    reproduciendo = false;
                    notificarEstadoCambiado(false);
                    notificarCancionTerminada();
                }
                
            } catch (InterruptedException e) {
                System.out.println("⏹️  Reproducción interrumpida");
                reproduciendo = false;
                notificarEstadoCambiado(false);
            }
        });
        
        hiloReproduccion.start();
    }
    
    /**
     * Pausa la reproducción
     * Ahora funciona correctamente con JavaFX MediaPlayer
     */
    public void pausar() {
        if (reproduciendo && !pausado) {
            pausado = true;
            
            // Si es reproducción real con MediaPlayer
            if (mediaPlayer != null) {
                mediaPlayer.pause();
                System.out.println("⏸️  Reproducción pausada");
                notificarEstadoCambiado(false);
            } else {
                // En simulación también pausamos
                System.out.println("⏸️  Simulación pausada");
                notificarEstadoCambiado(false);
            }
        }
    }
    
    /**
     * Reanuda la reproducción desde donde se pausó
     * Ahora funciona correctamente con JavaFX MediaPlayer
     */
    public void reanudar() {
        if (pausado) {
            pausado = false;
            
            // Si es reproducción real con MediaPlayer
            if (mediaPlayer != null) {
                mediaPlayer.play();
                reproduciendo = true;
                System.out.println("▶️  Reproducción reanudada");
                notificarEstadoCambiado(true);
            } else {
                // En simulación también reanudamos
                reproduciendo = true;
                System.out.println("▶️  Simulación reanudada");
                notificarEstadoCambiado(true);
            }
        }
    }
    
    /**
     * Detiene completamente la reproducción
     */
    public void detener() {
        reproduciendo = false;
        pausado = false;
        
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
        
        if (hiloReproduccion != null && hiloReproduccion.isAlive()) {
            hiloReproduccion.interrupt();
        }
        
        notificarEstadoCambiado(false);
    }
    
    /**
     * Formatea segundos a formato mm:ss
     */
    private String formatearTiempo(int segundos) {
        int minutos = segundos / 60;
        int segs = segundos % 60;
        return String.format("%02d:%02d", minutos, segs);
    }
    
    // Métodos de notificación
    private void notificarCancionTerminada() {
        if (listener != null) {
            listener.onCancionTerminada();
        }
    }
    
    private void notificarError(String mensaje) {
        if (listener != null) {
            listener.onError(mensaje);
        }
    }
    
    private void notificarEstadoCambiado(boolean reproduciendo) {
        if (listener != null) {
            listener.onEstadoCambiado(reproduciendo);
        }
    }
    
    // Getters
    public boolean isReproduciendo() {
        return reproduciendo && !pausado;
    }
    
    public boolean isPausado() {
        return pausado;
    }
    
    public Cancion getCancionActual() {
        return cancionActual;
    }
}
