package org.upemor.reproductor.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Entidad Cancion
 * @author Sistema Reproductor
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cancion {
    private Long id;
    private String titulo;
    private String artista;
    private String album;
    private Integer duracion; // en segundos
    private String rutaArchivo;
    
    /**
     * Convierte la duración de segundos a formato mm:ss
     */
    public String getDuracionFormateada() {
        if (duracion == null) return "00:00";
        int minutos = duracion / 60;
        int segundos = duracion % 60;
        return String.format("%02d:%02d", minutos, segundos);
    }
    
    @Override
    public String toString() {
        return titulo + " - " + artista;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cancion cancion = (Cancion) obj;
        return id != null && id.equals(cancion.id);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
