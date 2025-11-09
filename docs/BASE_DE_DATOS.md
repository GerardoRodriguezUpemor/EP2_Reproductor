# 🗄️ BASE DE DATOS - SQLite

## 📊 Esquema de la Base de Datos

### **Nombre:** `canciones.db`
### **Motor:** SQLite 3.42.0.0
### **Ubicación:** Raíz del proyecto

---

## 📋 Tabla: canciones

### **Definición DDL:**

```sql
CREATE TABLE IF NOT EXISTS canciones (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    titulo VARCHAR(200) NOT NULL,
    artista VARCHAR(100) NOT NULL,
    album VARCHAR(100),
    duracion INTEGER NOT NULL,
    ruta_archivo VARCHAR(500) NOT NULL,
    fecha_agregada DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

### **Estructura de Columnas:**

| Columna | Tipo | Restricción | Descripción |
|---------|------|-------------|-------------|
| `id` | INTEGER | PRIMARY KEY AUTOINCREMENT | Identificador único autoincremental |
| `titulo` | VARCHAR(200) | NOT NULL | Nombre de la canción |
| `artista` | VARCHAR(100) | NOT NULL | Nombre del artista/banda |
| `album` | VARCHAR(100) | NULL | Nombre del álbum (opcional) |
| `duracion` | INTEGER | NOT NULL | Duración en segundos |
| `ruta_archivo` | VARCHAR(500) | NOT NULL | Path absoluto al archivo MP3 |
| `fecha_agregada` | DATETIME | DEFAULT CURRENT_TIMESTAMP | Fecha de inserción |

---

## 🔑 Tipos de Datos

### **INTEGER:**
- Números enteros
- `id`: Autoincremental (1, 2, 3, ...)
- `duracion`: Segundos (183 = 3:03 minutos)

### **VARCHAR(n):**
- Cadenas de texto variable
- SQLite no valida longitud, es solo referencia

### **DATETIME:**
- Timestamp de fecha y hora
- Formato: `2025-11-08 20:30:45`
- Se establece automáticamente con `CURRENT_TIMESTAMP`

---

## 💾 Operaciones CRUD

### **1. CREATE - Insertar Canción**

```sql
INSERT INTO canciones (titulo, artista, album, duracion, ruta_archivo) 
VALUES (
    'Bohemian Rhapsody',
    'Queen',
    'A Night at the Opera',
    354,
    'C:/music/bohemian_rhapsody.mp3'
);
```

**PreparedStatement en Java:**
```java
String sql = "INSERT INTO canciones (titulo, artista, album, duracion, ruta_archivo) VALUES (?, ?, ?, ?, ?)";
PreparedStatement pstmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
pstmt.setString(1, "Bohemian Rhapsody");
pstmt.setString(2, "Queen");
pstmt.setString(3, "A Night at the Opera");
pstmt.setInt(4, 354);
pstmt.setString(5, "C:/music/bohemian_rhapsody.mp3");
pstmt.executeUpdate();

// Obtener ID generado
ResultSet rs = pstmt.getGeneratedKeys();
if (rs.next()) {
    long id = rs.getLong(1);
}
```

---

### **2. READ - Consultar Canciones**

#### **Todas las canciones:**
```sql
SELECT * FROM canciones ORDER BY fecha_agregada DESC;
```

#### **Buscar por título o artista:**
```sql
SELECT * FROM canciones 
WHERE titulo LIKE '%Queen%' OR artista LIKE '%Queen%' 
ORDER BY fecha_agregada DESC;
```

#### **Obtener por ID:**
```sql
SELECT * FROM canciones WHERE id = 5;
```

**PreparedStatement en Java:**
```java
String sql = "SELECT * FROM canciones WHERE titulo LIKE ? OR artista LIKE ?";
PreparedStatement pstmt = conexion.prepareStatement(sql);
pstmt.setString(1, "%Queen%");
pstmt.setString(2, "%Queen%");
ResultSet rs = pstmt.executeQuery();

while (rs.next()) {
    Long id = rs.getLong("id");
    String titulo = rs.getString("titulo");
    String artista = rs.getString("artista");
    // ...
}
```

---

### **3. UPDATE - Actualizar Canción**

```sql
UPDATE canciones 
SET titulo = 'Bohemian Rhapsody (Remastered)',
    artista = 'Queen',
    album = 'A Night at the Opera (Remastered)',
    duracion = 354,
    ruta_archivo = 'C:/music/bohemian_remastered.mp3'
WHERE id = 1;
```

**PreparedStatement en Java:**
```java
String sql = "UPDATE canciones SET titulo = ?, artista = ?, album = ?, duracion = ?, ruta_archivo = ? WHERE id = ?";
PreparedStatement pstmt = conexion.prepareStatement(sql);
pstmt.setString(1, "Bohemian Rhapsody (Remastered)");
pstmt.setString(2, "Queen");
pstmt.setString(3, "A Night at the Opera (Remastered)");
pstmt.setInt(4, 354);
pstmt.setString(5, "C:/music/bohemian_remastered.mp3");
pstmt.setLong(6, 1L);
int filas = pstmt.executeUpdate();
```

---

### **4. DELETE - Eliminar Canción**

```sql
DELETE FROM canciones WHERE id = 5;
```

**PreparedStatement en Java:**
```java
String sql = "DELETE FROM canciones WHERE id = ?";
PreparedStatement pstmt = conexion.prepareStatement(sql);
pstmt.setLong(1, 5L);
int filas = pstmt.executeUpdate();
```

---

## 🔗 Conexión a la Base de Datos

### **Clase MiConexion (Singleton):**

```java
public class MiConexion {
    private static MiConexion instancia;
    private Connection conexion;
    private static final String DB_URL = "jdbc:sqlite:canciones.db";
    
    private MiConexion() {
        Class.forName("org.sqlite.JDBC");
        conexion = DriverManager.getConnection(DB_URL);
        crearTablas();
    }
    
    public static synchronized MiConexion getInstancia() {
        if (instancia == null) {
            instancia = new MiConexion();
        }
        return instancia;
    }
    
    public Connection getConexion() {
        return conexion;
    }
}
```

### **URL de Conexión:**
```
jdbc:sqlite:canciones.db
```

**Desglose:**
- `jdbc:` - Protocolo JDBC
- `sqlite:` - Driver de SQLite
- `canciones.db` - Archivo de base de datos (ruta relativa)

---

## 📂 Ubicación del Archivo

```
EP2_Reproductor/
├── pom.xml
├── canciones.db          ← Archivo de base de datos
├── src/
├── target/
└── docs/
```

**Ruta absoluta:**
```
C:\Users\gera_\Desktop\Universidad\VSC\EP2_Reproductor\canciones.db
```

---

## 🎵 Datos de Ejemplo

### **Script de Inserción:**

```sql
INSERT INTO canciones (titulo, artista, album, duracion, ruta_archivo) VALUES
('Bohemian Rhapsody', 'Queen', 'A Night at the Opera', 354, 'C:/music/bohemian_rhapsody.mp3'),
('Stairway to Heaven', 'Led Zeppelin', 'Led Zeppelin IV', 482, 'C:/music/stairway_to_heaven.mp3'),
('Hotel California', 'Eagles', 'Hotel California', 391, 'C:/music/hotel_california.mp3'),
('Imagine', 'John Lennon', 'Imagine', 183, 'C:/music/imagine.mp3'),
('Sweet Child O Mine', 'Guns N Roses', 'Appetite for Destruction', 356, 'C:/music/sweet_child.mp3'),
('Smells Like Teen Spirit', 'Nirvana', 'Nevermind', 301, 'C:/music/smells_like_teen_spirit.mp3'),
('Billie Jean', 'Michael Jackson', 'Thriller', 294, 'C:/music/billie_jean.mp3'),
('Hey Jude', 'The Beatles', 'Hey Jude', 431, 'C:/music/hey_jude.mp3'),
('Wonderwall', 'Oasis', 'Whats the Story Morning Glory', 258, 'C:/music/wonderwall.mp3'),
('November Rain', 'Guns N Roses', 'Use Your Illusion I', 537, 'C:/music/november_rain.mp3');
```

### **Resultado:**

| id | titulo | artista | album | duracion | ruta_archivo |
|----|--------|---------|-------|----------|--------------|
| 1 | Bohemian Rhapsody | Queen | A Night at the Opera | 354 | C:/music/bohemian_rhapsody.mp3 |
| 2 | Stairway to Heaven | Led Zeppelin | Led Zeppelin IV | 482 | C:/music/stairway_to_heaven.mp3 |
| 3 | Hotel California | Eagles | Hotel California | 391 | C:/music/hotel_california.mp3 |
| ... | ... | ... | ... | ... | ... |

---

## 🔍 Consultas Útiles

### **Contar canciones:**
```sql
SELECT COUNT(*) FROM canciones;
```

### **Canciones por artista:**
```sql
SELECT COUNT(*) as total, artista 
FROM canciones 
GROUP BY artista 
ORDER BY total DESC;
```

### **Canción más larga:**
```sql
SELECT titulo, artista, duracion 
FROM canciones 
ORDER BY duracion DESC 
LIMIT 1;
```

### **Canciones agregadas hoy:**
```sql
SELECT * FROM canciones 
WHERE DATE(fecha_agregada) = DATE('now');
```

### **Duración total de la biblioteca:**
```sql
SELECT SUM(duracion) as total_segundos FROM canciones;
```

---

## 🛡️ Seguridad

### **✅ Buenas Prácticas:**

#### **1. Usar PreparedStatement (NO String concatenation):**

❌ **MAL:**
```java
String sql = "SELECT * FROM canciones WHERE titulo = '" + titulo + "'";
Statement stmt = conexion.createStatement();
stmt.executeQuery(sql);
// VULNERABLE A SQL INJECTION
```

✅ **BIEN:**
```java
String sql = "SELECT * FROM canciones WHERE titulo = ?";
PreparedStatement pstmt = conexion.prepareStatement(sql);
pstmt.setString(1, titulo);
pstmt.executeQuery();
// PROTEGIDO CONTRA SQL INJECTION
```

#### **2. Cerrar recursos con try-with-resources:**

✅ **BIEN:**
```java
try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
    ResultSet rs = pstmt.executeQuery();
    // Usar rs...
} // Se cierra automáticamente
```

#### **3. Validar datos antes de insertar:**

```java
if (titulo == null || titulo.trim().isEmpty()) {
    throw new IllegalArgumentException("Título no puede estar vacío");
}

if (duracion <= 0) {
    throw new IllegalArgumentException("Duración debe ser mayor a 0");
}
```

---

## 🔧 Mantenimiento

### **Backup de la Base de Datos:**

**Windows (PowerShell):**
```powershell
Copy-Item canciones.db canciones_backup_$(Get-Date -Format "yyyyMMdd_HHmmss").db
```

**Linux/Mac:**
```bash
cp canciones.db canciones_backup_$(date +%Y%m%d_%H%M%S).db
```

### **Restaurar Backup:**
```powershell
Copy-Item canciones_backup_20251108_203000.db canciones.db
```

### **Limpiar Base de Datos:**
```sql
DELETE FROM canciones;
DELETE FROM sqlite_sequence WHERE name='canciones'; -- Reinicia autoincrement
```

### **Optimizar Base de Datos:**
```sql
VACUUM;
```

---

## 📊 Herramientas de Gestión

### **1. DB Browser for SQLite**
- Descarga: https://sqlitebrowser.org/
- GUI para visualizar y editar la BD
- Ejecutar consultas SQL

### **2. SQLite Command Line:**
```bash
sqlite3 canciones.db
```

```sql
sqlite> .tables
canciones

sqlite> .schema canciones
CREATE TABLE canciones (...);

sqlite> SELECT * FROM canciones LIMIT 5;
```

### **3. DBeaver**
- IDE universal para bases de datos
- Soporte para SQLite

---

## 🎯 Ventajas de SQLite

✅ **Sin servidor** - Archivo local, no necesita MySQL/PostgreSQL  
✅ **Portable** - Un solo archivo .db  
✅ **Ligero** - Solo 600 KB de tamaño  
✅ **Rápido** - Ideal para aplicaciones de escritorio  
✅ **Sin configuración** - No requiere instalación adicional  
✅ **ACID** - Transacciones seguras  
✅ **Standard SQL** - Sintaxis SQL estándar

---

## 🚀 Inicialización Automática

La base de datos se crea automáticamente al iniciar la aplicación:

```java
// En MiConexion.java
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
        e.printStackTrace();
    }
}
```

**Flujo:**
1. Aplicación inicia
2. MiConexion.getInstancia()
3. Constructor de MiConexion
4. Conecta a `canciones.db` (crea archivo si no existe)
5. Ejecuta `CREATE TABLE IF NOT EXISTS`
6. Tabla lista para usar

---

## 📝 Convenciones

### **Nombres de Columnas:**
- snake_case: `fecha_agregada`, `ruta_archivo`
- Descriptivos en español
- Sin caracteres especiales

### **Nombres de Tablas:**
- Plural: `canciones` (no `cancion`)
- Minúsculas
- Sin espacios

### **IDs:**
- Siempre `id` como PRIMARY KEY
- INTEGER AUTOINCREMENT
- Comienza en 1

---

## 🔄 Migración de Datos

### **Exportar a CSV:**
```sql
.headers on
.mode csv
.output canciones.csv
SELECT * FROM canciones;
.output stdout
```

### **Importar desde CSV:**
```sql
.mode csv
.import canciones.csv canciones
```

---

## 📚 Referencias

- **SQLite Documentation:** https://www.sqlite.org/docs.html
- **JDBC SQLite Driver:** https://github.com/xerial/sqlite-jdbc
- **SQL Tutorial:** https://www.w3schools.com/sql/
