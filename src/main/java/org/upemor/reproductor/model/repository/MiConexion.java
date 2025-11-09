package org.upemor.reproductor.model.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Clase Singleton para gestionar la conexión a SQLite
 * @author Sistema Reproductor
 */
public class MiConexion {
    private static MiConexion instancia;
    private Connection conexion;
    private static final String URL = "jdbc:sqlite:canciones.db";
    
    private MiConexion() {
        try {
            Class.forName("org.sqlite.JDBC");
            conexion = DriverManager.getConnection(URL);
            inicializarBaseDatos();
            System.out.println("✓ Conexión a SQLite establecida correctamente");
        } catch (Exception e) {
            System.err.println("Error al conectar con SQLite: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Obtiene la instancia única de la conexión (Singleton)
     */
    public static MiConexion getInstancia() {
        if (instancia == null) {
            instancia = new MiConexion();
        }
        return instancia;
    }
    
    /**
     * Retorna la conexión activa
     */
    public Connection conectar() {
        return conexion;
    }
    
    /**
     * Inicializa la base de datos creando las tablas si no existen
     */
    private void inicializarBaseDatos() {
        try {
            Statement stmt = conexion.createStatement();
            
            // Crear tabla canciones
            String sqlCrearTabla = """
                CREATE TABLE IF NOT EXISTS canciones (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    titulo TEXT NOT NULL,
                    artista TEXT NOT NULL,
                    album TEXT,
                    duracion INTEGER NOT NULL,
                    ruta_archivo TEXT
                )
            """;
            stmt.execute(sqlCrearTabla);
            
            // Verificar si hay datos
            var rs = stmt.executeQuery("SELECT COUNT(*) as total FROM canciones");
            if (rs.next() && rs.getInt("total") == 0) {
                // Insertar datos de ejemplo
                String sqlDatos = """
                    INSERT INTO canciones (titulo, artista, album, duracion, ruta_archivo) VALUES
                    ('Bohemian Rhapsody', 'Queen', 'A Night at the Opera', 354, '/music/queen/bohemian_rhapsody.mp3'),
                    ('Stairway to Heaven', 'Led Zeppelin', 'Led Zeppelin IV', 482, '/music/led_zeppelin/stairway_to_heaven.mp3'),
                    ('Hotel California', 'Eagles', 'Hotel California', 391, '/music/eagles/hotel_california.mp3'),
                    ('Imagine', 'John Lennon', 'Imagine', 183, '/music/john_lennon/imagine.mp3'),
                    ('Sweet Child O Mine', 'Guns N Roses', 'Appetite for Destruction', 356, '/music/guns_n_roses/sweet_child.mp3'),
                    ('Smells Like Teen Spirit', 'Nirvana', 'Nevermind', 301, '/music/nirvana/smells_like_teen_spirit.mp3'),
                    ('Billie Jean', 'Michael Jackson', 'Thriller', 294, '/music/michael_jackson/billie_jean.mp3'),
                    ('Hey Jude', 'The Beatles', 'Hey Jude', 431, '/music/beatles/hey_jude.mp3'),
                    ('Wonderwall', 'Oasis', 'Whats the Story Morning Glory', 258, '/music/oasis/wonderwall.mp3'),
                    ('November Rain', 'Guns N Roses', 'Use Your Illusion I', 537, '/music/guns_n_roses/november_rain.mp3')
                """;
                stmt.execute(sqlDatos);
                System.out.println("✓ Datos de ejemplo insertados");
            }
            
            stmt.close();
        } catch (Exception e) {
            System.err.println("Error al inicializar base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
