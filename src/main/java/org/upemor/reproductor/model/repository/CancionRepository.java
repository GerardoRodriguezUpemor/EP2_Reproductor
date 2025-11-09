package org.upemor.reproductor.model.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.upemor.reproductor.model.entity.Cancion;
import org.upemor.reproductor.estructuras.MiLista;

/**
 * Repositorio para operaciones CRUD de Canciones
 * Retorna MiLista en lugar de List de Java
 * @author Sistema Reproductor
 */
public class CancionRepository {
    private MiConexion miConexion;
    
    public CancionRepository() {
        this.miConexion = MiConexion.getInstancia();
    }
    
    /**
     * Obtiene todas las canciones de la base de datos
     * @return MiLista con todas las canciones
     */
    public MiLista<Cancion> obtenerTodas() throws Exception {
        MiLista<Cancion> canciones = new MiLista<>();
        
        try {
            String query = "SELECT * FROM canciones ORDER BY id";
            PreparedStatement stmt = miConexion.conectar().prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Cancion cancion = mapearCancion(rs);
                canciones.agregar(cancion);
            }
            
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.err.println("Error al obtener todas las canciones: " + e.getMessage());
            throw e;
        }
        
        return canciones;
    }
    
    /**
     * Busca canciones por título o artista
     * @return MiLista con canciones que coinciden con la búsqueda
     */
    public MiLista<Cancion> buscar(String texto) throws Exception {
        MiLista<Cancion> canciones = new MiLista<>();
        
        try {
            String query = "SELECT * FROM canciones WHERE titulo LIKE ? OR artista LIKE ? ORDER BY id";
            PreparedStatement stmt = miConexion.conectar().prepareStatement(query);
            stmt.setString(1, "%" + texto + "%");
            stmt.setString(2, "%" + texto + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Cancion cancion = mapearCancion(rs);
                canciones.agregar(cancion);
            }
            
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.err.println("Error al buscar canciones: " + e.getMessage());
            throw e;
        }
        
        return canciones;
    }
    
    /**
     * Obtiene una canción por su ID
     */
    public Cancion obtenerPorId(Long id) throws Exception {
        try {
            String query = "SELECT * FROM canciones WHERE id = ?";
            PreparedStatement stmt = miConexion.conectar().prepareStatement(query);
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            Cancion cancion = null;
            if (rs.next()) {
                cancion = mapearCancion(rs);
            }
            
            rs.close();
            stmt.close();
            return cancion;
        } catch (Exception e) {
            System.err.println("Error al obtener canción por ID: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Crea una nueva canción en la base de datos
     */
    public boolean crear(Cancion cancion) throws Exception {
        try {
            String query = "INSERT INTO canciones (titulo, artista, album, duracion, ruta_archivo) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = miConexion.conectar().prepareStatement(query);
            stmt.setString(1, cancion.getTitulo());
            stmt.setString(2, cancion.getArtista());
            stmt.setString(3, cancion.getAlbum());
            stmt.setInt(4, cancion.getDuracion());
            stmt.setString(5, cancion.getRutaArchivo());
            
            int filasAfectadas = stmt.executeUpdate();
            stmt.close();
            return filasAfectadas > 0;
        } catch (Exception e) {
            System.err.println("Error al crear canción: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Actualiza una canción existente
     */
    public boolean actualizar(Cancion cancion) throws Exception {
        try {
            String query = "UPDATE canciones SET titulo = ?, artista = ?, album = ?, duracion = ?, ruta_archivo = ? WHERE id = ?";
            PreparedStatement stmt = miConexion.conectar().prepareStatement(query);
            stmt.setString(1, cancion.getTitulo());
            stmt.setString(2, cancion.getArtista());
            stmt.setString(3, cancion.getAlbum());
            stmt.setInt(4, cancion.getDuracion());
            stmt.setString(5, cancion.getRutaArchivo());
            stmt.setLong(6, cancion.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            stmt.close();
            return filasAfectadas > 0;
        } catch (Exception e) {
            System.err.println("Error al actualizar canción: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Elimina una canción por su ID
     */
    public boolean eliminar(Long id) throws Exception {
        try {
            String query = "DELETE FROM canciones WHERE id = ?";
            PreparedStatement stmt = miConexion.conectar().prepareStatement(query);
            stmt.setLong(1, id);
            
            int filasAfectadas = stmt.executeUpdate();
            stmt.close();
            return filasAfectadas > 0;
        } catch (Exception e) {
            System.err.println("Error al eliminar canción: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Mapea un ResultSet a un objeto Cancion
     */
    private Cancion mapearCancion(ResultSet rs) throws Exception {
        Cancion cancion = new Cancion();
        cancion.setId(rs.getLong("id"));
        cancion.setTitulo(rs.getString("titulo"));
        cancion.setArtista(rs.getString("artista"));
        cancion.setAlbum(rs.getString("album"));
        cancion.setDuracion(rs.getInt("duracion"));
        cancion.setRutaArchivo(rs.getString("ruta_archivo"));
        return cancion;
    }
}
