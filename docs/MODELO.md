# 🗄️ CAPA DE MODELO - Entidades y Repositorios

## 📦 Estructura del Paquete `model`

```
org.upemor.reproductor.model/
├── entity/
│   ├── Entity.java          → Clase base abstracta
│   └── Cancion.java         → Entidad Canción
└── repository/
    ├── MiConexion.java      → Conexión singleton a SQLite
    ├── Repository.java       → Interfaz genérica de repositorio
    └── CancionRepository.java → Implementación para canciones
```

---

## 🎯 1. ENTIDADES (entity)

### **Entity.java** - Clase Base Abstracta

**Ubicación:** `org.upemor.reproductor.model.entity.Entity`

#### **Código:**
```java
package org.upemor.reproductor.model.entity;

import lombok.Data;

@Data
public abstract class Entity {
    protected Long id;
    
    public Entity() {
        this.id = null;
    }
    
    public Entity(Long id) {
        this.id = id;
    }
}
```

#### **Características:**
- ✅ **Abstracta:** No se puede instanciar directamente
- ✅ **Lombok `@Data`:** Genera getters, setters, toString, equals, hashCode
- ✅ **Campo `id`:** Identificador único compartido por todas las entidades
- ✅ **Herencia:** Todas las entidades heredan de esta clase

#### **Propósito:**
- Proporcionar comportamiento común a todas las entidades
- Garantizar que todas tengan un ID
- Facilitar operaciones genéricas en repositorios

---

### **Cancion.java** - Entidad Principal

**Ubicación:** `org.upemor.reproductor.model.entity.Cancion`

#### **Código Completo:**
```java
package org.upemor.reproductor.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Cancion extends Entity {
    private String titulo;
    private String artista;
    private String album;
    private Integer duracion; // Duración en segundos
    private String rutaArchivo; // Ruta al archivo MP3
    private LocalDateTime fechaAgregada;
    
    /**
     * Constructor sin ID (para nuevas canciones)
     */
    public Cancion(String titulo, String artista, String album, 
                   Integer duracion, String rutaArchivo) {
        super();
        this.titulo = titulo;
        this.artista = artista;
        this.album = album;
        this.duracion = duracion;
        this.rutaArchivo = rutaArchivo;
        this.fechaAgregada = LocalDateTime.now();
    }
    
    /**
     * Constructor completo con ID (para canciones existentes)
     */
    public Cancion(Long id, String titulo, String artista, String album,
                   Integer duracion, String rutaArchivo, LocalDateTime fechaAgregada) {
        super(id);
        this.titulo = titulo;
        this.artista = artista;
        this.album = album;
        this.duracion = duracion;
        this.rutaArchivo = rutaArchivo;
        this.fechaAgregada = fechaAgregada;
    }
    
    /**
     * Retorna la duración formateada (MM:SS)
     */
    public String getDuracionFormateada() {
        if (duracion == null) return "00:00";
        
        int minutos = duracion / 60;
        int segundos = duracion % 60;
        return String.format("%02d:%02d", minutos, segundos);
    }
    
    /**
     * Retorna la fecha formateada
     */
    public String getFechaFormateada() {
        if (fechaAgregada == null) return "N/A";
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return fechaAgregada.format(formatter);
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s (%s)", titulo, artista, getDuracionFormateada());
    }
}
```

#### **Atributos:**

| Campo | Tipo | Descripción | Obligatorio |
|-------|------|-------------|-------------|
| `id` | Long | Identificador único (heredado) | ✅ (BD) |
| `titulo` | String | Nombre de la canción | ✅ |
| `artista` | String | Nombre del artista | ✅ |
| `album` | String | Nombre del álbum | ❌ |
| `duracion` | Integer | Duración en segundos | ✅ |
| `rutaArchivo` | String | Path al archivo MP3 | ✅ |
| `fechaAgregada` | LocalDateTime | Fecha de creación | ✅ (auto) |

#### **Métodos Adicionales:**

**`getDuracionFormateada()`**
```java
Entrada: duracion = 183
Salida: "03:03"

Entrada: duracion = 3662
Salida: "61:02"
```

**`getFechaFormateada()`**
```java
Entrada: fechaAgregada = 2025-11-08T20:30:45
Salida: "08/11/2025 20:30"
```

**`toString()`**
```java
Entrada: titulo="Bohemian Rhapsody", artista="Queen", duracion=354
Salida: "Bohemian Rhapsody - Queen (05:54)"
```

#### **Anotaciones Lombok:**

- **`@Data`:** Genera getters, setters, toString, equals, hashCode
- **`@NoArgsConstructor`:** Constructor sin argumentos
- **`@AllArgsConstructor`:** Constructor con todos los argumentos
- **`@EqualsAndHashCode(callSuper = true)`:** Incluye campos de la clase padre en equals/hashCode

---

## 🗄️ 2. REPOSITORIOS (repository)

### **Repository.java** - Interfaz Genérica

**Ubicación:** `org.upemor.reproductor.model.repository.Repository`

#### **Código:**
```java
package org.upemor.reproductor.model.repository;

import org.upemor.reproductor.estructuras.MiLista;

public interface Repository<T> {
    /**
     * Busca entidades que coincidan con el filtro
     * @param filtro Texto de búsqueda
     * @return Lista de entidades encontradas
     */
    MiLista<T> buscar(String filtro);
    
    /**
     * Obtiene una entidad por su ID
     * @param id Identificador único
     * @return Entidad encontrada o null
     */
    T obtenerPorId(Long id);
    
    /**
     * Guarda una nueva entidad
     * @param entidad Entidad a guardar
     * @return true si se guardó correctamente
     */
    boolean guardar(T entidad);
    
    /**
     * Actualiza una entidad existente
     * @param entidad Entidad con datos actualizados
     * @return true si se actualizó correctamente
     */
    boolean actualizar(T entidad);
    
    /**
     * Elimina una entidad por su ID
     * @param id Identificador de la entidad
     * @return true si se eliminó correctamente
     */
    boolean eliminar(Long id);
}
```

#### **Características:**
- ✅ **Genérica:** `Repository<T>` funciona con cualquier tipo
- ✅ **Contrato:** Define operaciones CRUD estándar
- ✅ **Retorno:** Usa `MiLista<T>` en lugar de colecciones Java
- ✅ **Abstracción:** No depende de implementación específica

---

### **MiConexion.java** - Singleton de Conexión SQLite

**Ubicación:** `org.upemor.reproductor.model.repository.MiConexion`

#### **Código Completo:**
```java
package org.upemor.reproductor.model.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MiConexion {
    private static MiConexion instancia;
    private Connection conexion;
    private static final String DB_URL = "jdbc:sqlite:canciones.db";
    
    /**
     * Constructor privado (Singleton)
     */
    private MiConexion() {
        try {
            // Cargar driver de SQLite
            Class.forName("org.sqlite.JDBC");
            
            // Establecer conexión
            conexion = DriverManager.getConnection(DB_URL);
            
            System.out.println("✅ Conexión a base de datos establecida");
            
            // Crear tablas si no existen
            crearTablas();
            
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Error: Driver SQLite no encontrado");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar a base de datos");
            e.printStackTrace();
        }
    }
    
    /**
     * Obtiene la instancia única (Singleton)
     */
    public static synchronized MiConexion getInstancia() {
        if (instancia == null) {
            instancia = new MiConexion();
        }
        return instancia;
    }
    
    /**
     * Retorna la conexión activa
     */
    public Connection getConexion() {
        try {
            // Verificar si la conexión está cerrada
            if (conexion == null || conexion.isClosed()) {
                conexion = DriverManager.getConnection(DB_URL);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al verificar conexión");
            e.printStackTrace();
        }
        return conexion;
    }
    
    /**
     * Crea las tablas necesarias si no existen
     */
    private void crearTablas() {
        String sqlCanciones = """
            CREATE TABLE IF NOT EXISTS canciones (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo VARCHAR(200) NOT NULL,
                artista VARCHAR(100) NOT NULL,
                album VARCHAR(100),
                duracion INTEGER NOT NULL,
                ruta_archivo VARCHAR(500) NOT NULL,
                fecha_agregada DATETIME DEFAULT CURRENT_TIMESTAMP
            )
        """;
        
        try (Statement stmt = conexion.createStatement()) {
            stmt.execute(sqlCanciones);
            System.out.println("✅ Tabla 'canciones' verificada/creada");
        } catch (SQLException e) {
            System.err.println("❌ Error al crear tablas");
            e.printStackTrace();
        }
    }
    
    /**
     * Cierra la conexión
     */
    public void cerrar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("✅ Conexión cerrada");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al cerrar conexión");
            e.printStackTrace();
        }
    }
}
```

#### **Patrón Singleton:**

**Diagrama:**
```
Primera llamada:              Llamadas posteriores:
  ↓                                  ↓
getInstancia()                  getInstancia()
  ↓                                  ↓
instancia == null?              instancia == null?
  ↓ (SÍ)                            ↓ (NO)
new MiConexion()                Retorna instancia existente
  ↓                                  ↓
Retorna nueva instancia         ✅ Misma instancia
```

**Ventajas:**
- ✅ Una sola conexión a BD en toda la aplicación
- ✅ Evita crear múltiples conexiones
- ✅ Thread-safe con `synchronized`
- ✅ Lazy initialization (se crea al primer uso)

#### **Método `crearTablas()`:**

Ejecuta SQL DDL para crear la estructura:

```sql
CREATE TABLE IF NOT EXISTS canciones (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    titulo VARCHAR(200) NOT NULL,
    artista VARCHAR(100) NOT NULL,
    album VARCHAR(100),
    duracion INTEGER NOT NULL,
    ruta_archivo VARCHAR(500) NOT NULL,
    fecha_agregada DATETIME DEFAULT CURRENT_TIMESTAMP
)
```

---

### **CancionRepository.java** - Repositorio de Canciones

**Ubicación:** `org.upemor.reproductor.model.repository.CancionRepository`

#### **Código Completo:**
```java
package org.upemor.reproductor.model.repository;

import org.upemor.reproductor.model.entity.Cancion;
import org.upemor.reproductor.estructuras.MiLista;
import java.sql.*;
import java.time.LocalDateTime;

public class CancionRepository implements Repository<Cancion> {
    private Connection conexion;
    
    public CancionRepository() {
        this.conexion = MiConexion.getInstancia().getConexion();
    }
    
    @Override
    public MiLista<Cancion> buscar(String filtro) {
        MiLista<Cancion> canciones = new MiLista<>();
        
        String sql = filtro == null || filtro.trim().isEmpty()
            ? "SELECT * FROM canciones ORDER BY fecha_agregada DESC"
            : "SELECT * FROM canciones WHERE titulo LIKE ? OR artista LIKE ? ORDER BY fecha_agregada DESC";
        
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            if (filtro != null && !filtro.trim().isEmpty()) {
                String patron = "%" + filtro + "%";
                pstmt.setString(1, patron);
                pstmt.setString(2, patron);
            }
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Cancion cancion = convertirResultSetACancion(rs);
                canciones.agregar(cancion);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar canciones: " + e.getMessage());
            e.printStackTrace();
        }
        
        return canciones;
    }
    
    @Override
    public Cancion obtenerPorId(Long id) {
        String sql = "SELECT * FROM canciones WHERE id = ?";
        
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return convertirResultSetACancion(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener canción: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    @Override
    public boolean guardar(Cancion cancion) {
        String sql = "INSERT INTO canciones (titulo, artista, album, duracion, ruta_archivo) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, cancion.getTitulo());
            pstmt.setString(2, cancion.getArtista());
            pstmt.setString(3, cancion.getAlbum());
            pstmt.setInt(4, cancion.getDuracion());
            pstmt.setString(5, cancion.getRutaArchivo());
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                // Obtener ID generado
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    cancion.setId(generatedKeys.getLong(1));
                }
                System.out.println("✅ Canción guardada: " + cancion.getTitulo());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al guardar canción: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    public boolean actualizar(Cancion cancion) {
        String sql = "UPDATE canciones SET titulo = ?, artista = ?, album = ?, duracion = ?, ruta_archivo = ? WHERE id = ?";
        
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, cancion.getTitulo());
            pstmt.setString(2, cancion.getArtista());
            pstmt.setString(3, cancion.getAlbum());
            pstmt.setInt(4, cancion.getDuracion());
            pstmt.setString(5, cancion.getRutaArchivo());
            pstmt.setLong(6, cancion.getId());
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                System.out.println("✅ Canción actualizada: " + cancion.getTitulo());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar canción: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    public boolean eliminar(Long id) {
        String sql = "DELETE FROM canciones WHERE id = ?";
        
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                System.out.println("✅ Canción eliminada (ID: " + id + ")");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar canción: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Convierte un ResultSet a objeto Cancion
     */
    private Cancion convertirResultSetACancion(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String titulo = rs.getString("titulo");
        String artista = rs.getString("artista");
        String album = rs.getString("album");
        Integer duracion = rs.getInt("duracion");
        String rutaArchivo = rs.getString("ruta_archivo");
        
        // Convertir timestamp a LocalDateTime
        Timestamp timestamp = rs.getTimestamp("fecha_agregada");
        LocalDateTime fechaAgregada = timestamp != null ? timestamp.toLocalDateTime() : LocalDateTime.now();
        
        return new Cancion(id, titulo, artista, album, duracion, rutaArchivo, fechaAgregada);
    }
}
```

#### **Métodos CRUD:**

**1. `buscar(String filtro)`**
```java
// Buscar todas
MiLista<Cancion> todas = repository.buscar(null);

// Buscar por filtro
MiLista<Cancion> resultados = repository.buscar("Queen");
// SQL: SELECT * FROM canciones WHERE titulo LIKE '%Queen%' OR artista LIKE '%Queen%'
```

**2. `obtenerPorId(Long id)`**
```java
Cancion cancion = repository.obtenerPorId(5L);
// SQL: SELECT * FROM canciones WHERE id = 5
```

**3. `guardar(Cancion cancion)`**
```java
Cancion nueva = new Cancion("Imagine", "John Lennon", "Imagine", 183, "/music/imagine.mp3");
boolean guardada = repository.guardar(nueva);
// SQL: INSERT INTO canciones (...) VALUES (...)
// Genera ID automáticamente
```

**4. `actualizar(Cancion cancion)`**
```java
cancion.setTitulo("Imagine (Remastered)");
boolean actualizada = repository.actualizar(cancion);
// SQL: UPDATE canciones SET titulo = ... WHERE id = ...
```

**5. `eliminar(Long id)`**
```java
boolean eliminada = repository.eliminar(5L);
// SQL: DELETE FROM canciones WHERE id = 5
```

---

## 📊 Diagrama de Flujo - Operación de Búsqueda

```
[Usuario escribe "Queen" en búsqueda]
            ↓
[BibliotecaDlg captura evento]
            ↓
[CancionController.buscar("Queen")]
            ↓
[CancionRepository.buscar("Queen")]
            ↓
[Prepara SQL con filtro LIKE]
            ↓
[PreparedStatement.executeQuery()]
            ↓
[SQLite ejecuta consulta]
            ↓
[ResultSet con filas encontradas]
            ↓
[convertirResultSetACancion() × N]
            ↓
[MiLista<Cancion> con resultados]
            ↓
[Retorna a BibliotecaDlg]
            ↓
[Actualiza tabla en UI]
            ↓
[Usuario ve resultados]
```

---

## 🔐 Seguridad y Buenas Prácticas

### **PreparedStatement vs Statement:**

❌ **MAL (SQL Injection vulnerable):**
```java
String sql = "SELECT * FROM canciones WHERE titulo = '" + titulo + "'";
Statement stmt = conexion.createStatement();
stmt.executeQuery(sql);
```

✅ **BIEN (Protegido):**
```java
String sql = "SELECT * FROM canciones WHERE titulo = ?";
PreparedStatement pstmt = conexion.prepareStatement(sql);
pstmt.setString(1, titulo);
pstmt.executeQuery();
```

### **Try-with-resources:**

✅ Cierra automáticamente recursos:
```java
try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
    // Usar pstmt...
} // Se cierra automáticamente
```

### **Manejo de Excepciones:**

✅ Captura y log de errores:
```java
try {
    // Operación
} catch (SQLException e) {
    System.err.println("❌ Error: " + e.getMessage());
    e.printStackTrace();
}
```

---

## 💡 Ejemplo de Uso Completo

```java
// 1. Crear repositorio
CancionRepository repository = new CancionRepository();

// 2. Buscar todas las canciones
MiLista<Cancion> todas = repository.buscar(null);
System.out.println("Total de canciones: " + todas.tamanio());

// 3. Buscar por filtro
MiLista<Cancion> queen = repository.buscar("Queen");
queen.recorrer((cancion, i) -> {
    System.out.println(cancion.getTitulo() + " - " + cancion.getArtista());
});

// 4. Crear nueva canción
Cancion nueva = new Cancion(
    "Stairway to Heaven",
    "Led Zeppelin",
    "Led Zeppelin IV",
    482,
    "/music/stairway.mp3"
);
repository.guardar(nueva);

// 5. Obtener por ID
Cancion cancion = repository.obtenerPorId(nueva.getId());

// 6. Actualizar
cancion.setAlbum("Led Zeppelin IV (Remastered)");
repository.actualizar(cancion);

// 7. Eliminar
repository.eliminar(cancion.getId());
```
