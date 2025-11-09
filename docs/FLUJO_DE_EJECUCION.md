# 🚀 FLUJO DE EJECUCIÓN DEL PROGRAMA

## 📍 Punto de Entrada: Main.java

**Ubicación:** `org.upemor.reproductor.Main`

### **Código Completo:**

```java
package org.upemor.reproductor;

import org.upemor.reproductor.view.PrincipalDlg;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Banner de inicio
        imprimirBanner();
        
        // Inicializar aplicación en Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("Inicializando JavaFX...");
                System.out.println("Inicializando base de datos...");
                System.out.println("Configurando interfaz gráfica...");
                
                PrincipalDlg ventana = new PrincipalDlg();
                ventana.setVisible(true);
                
                System.out.println("✅ Aplicación iniciada correctamente");
                System.out.println("Iniciando aplicación...");
                
            } catch (Exception e) {
                System.err.println("❌ Error al iniciar aplicación: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
    
    private static void imprimirBanner() {
        System.out.println("==============================================");
        System.out.println("🎵 REPRODUCTOR DE CANCIONES");
        System.out.println("    Sistema con Estructuras de Datos");
        System.out.println("==============================================");
        System.out.println();
        System.out.println("📊 Estructuras implementadas:");
        System.out.println("   ✓ MiLista  - Lista Circular Doblemente Enlazada");
        System.out.println("   ✓ MiPila   - Pila (LIFO)");
        System.out.println("   ✓ MiCola   - Cola (FIFO)");
        System.out.println();
        System.out.println("🗄️  Base de Datos: SQLite");
        System.out.println("🎵 Audio: JavaFX MediaPlayer (con pause/resume)");
        System.out.println("==============================================");
        System.out.println();
    }
}
```

---

## 🔄 Flujo de Inicio Detallado

### **1. JVM Inicia**
```
java -jar reproductor.jar
    ↓
JVM carga clases
    ↓
Busca método main()
    ↓
Main.main(String[] args)
```

---

### **2. Main.java Ejecuta**

```java
public static void main(String[] args) {
    imprimirBanner();  // ← Paso 1: Muestra información
    SwingUtilities.invokeLater(() -> {  // ← Paso 2: Inicia UI
        PrincipalDlg ventana = new PrincipalDlg();
        ventana.setVisible(true);
    });
}
```

**Consola:**
```
==============================================
🎵 REPRODUCTOR DE CANCIONES
    Sistema con Estructuras de Datos
==============================================

📊 Estructuras implementadas:
   ✓ MiLista  - Lista Circular Doblemente Enlazada
   ✓ MiPila   - Pila (LIFO)
   ✓ MiCola   - Cola (FIFO)

🗄️  Base de Datos: SQLite
🎵 Audio: JavaFX MediaPlayer (con pause/resume)
==============================================

Inicializando JavaFX...
Inicializando base de datos...
Configurando interfaz gráfica...
```

---

### **3. SwingUtilities.invokeLater()**

**¿Por qué?**
- Swing NO es thread-safe
- UI debe ejecutarse en Event Dispatch Thread (EDT)
- `invokeLater()` asegura ejecución en EDT

```java
SwingUtilities.invokeLater(() -> {
    // Todo este código se ejecuta en EDT
    PrincipalDlg ventana = new PrincipalDlg();
    ventana.setVisible(true);
});
```

---

### **4. new PrincipalDlg()**

```java
public PrincipalDlg() {
    FlatLightLaf.setup();  // ← Paso 1: Aplicar tema
    
    setTitle("Reproductor de Canciones...");  // ← Paso 2: Configurar ventana
    setSize(1200, 700);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    
    reproductor = new Reproductor();  // ← Paso 3: Crear reproductor
    
    initComponents();  // ← Paso 4: Crear pestañas
}
```

#### **Paso 4.1: new Reproductor()**

```java
public Reproductor() {
    this.colaReproduccion = new MiCola<>();  // ← Cola FIFO
    this.historial = new MiPila<>();         // ← Pila LIFO
    this.cancionActual = null;
    this.reproduciendo = false;
    this.reproductorAudio = new ReproductorAudio();  // ← Motor MP3
    this.reproductorAudio.setListener(this);
}
```

**Consola:**
```
✅ JavaFX inicializado
```

#### **Paso 4.2: initComponents()**

```java
private void initComponents() {
    add(crearHeader(), BorderLayout.NORTH);  // ← Header
    
    tabbedPane = new JTabbedPane();
    
    // Pestaña 0: Reproductor
    reproductorDlg = new ReproductorDlg(reproductor);
    tabbedPane.addTab("🎵 Reproductor", reproductorDlg);
    
    // Pestaña 1: Biblioteca
    bibliotecaDlg = new BibliotecaDlg(reproductor);
    tabbedPane.addTab("📚 Biblioteca", bibliotecaDlg);
    
    // Pestaña 2: Historial
    historialDlg = new HistorialDlg(reproductor);
    tabbedPane.addTab("📜 Historial", historialDlg);
    
    add(tabbedPane, BorderLayout.CENTER);
    add(crearFooter(), BorderLayout.SOUTH);  // ← Footer
}
```

---

### **5. new BibliotecaDlg()**

```java
public BibliotecaDlg(Reproductor reproductor) {
    super();  // ← Llama a BaseDlg constructor
    this.reproductor = reproductor;
    this.cancionesActuales = new MiLista<>();
    inicializar();
}

private void inicializar() {
    controller = new CancionController();  // ← Crea controlador
    
    // Configurar columnas de tabla
    modeloTabla.addColumn("ID");
    modeloTabla.addColumn("Título");
    modeloTabla.addColumn("Artista");
    modeloTabla.addColumn("Álbum");
    modeloTabla.addColumn("Duración");
    
    // Personalizar botones
    btnAgregar.setText("+Canción");
    btnEditar.setText("✏️ Editar");
    btnEliminar.setText("-Canción");
    
    // Agregar botones adicionales
    agregarBotonCola();
    
    // Cargar datos
    eventoBotonBuscar();  // ← Busca todas las canciones
}
```

#### **Paso 5.1: new CancionController()**

```java
public CancionController() {
    this.repository = new CancionRepository();  // ← Crea repositorio
    System.out.println("✅ CancionController inicializado");
}
```

#### **Paso 5.2: new CancionRepository()**

```java
public CancionRepository() {
    this.conexion = MiConexion.getInstancia().getConexion();  // ← Conexión BD
}
```

#### **Paso 5.3: MiConexion.getInstancia()**

```java
public static synchronized MiConexion getInstancia() {
    if (instancia == null) {
        instancia = new MiConexion();  // ← Primera vez: crea instancia
    }
    return instancia;
}

private MiConexion() {
    Class.forName("org.sqlite.JDBC");  // ← Carga driver
    conexion = DriverManager.getConnection("jdbc:sqlite:canciones.db");  // ← Conecta
    crearTablas();  // ← Crea tabla si no existe
}
```

**Consola:**
```
✅ Conexión a base de datos establecida
✅ Tabla 'canciones' verificada/creada
✅ CancionController inicializado
```

#### **Paso 5.4: eventoBotonBuscar()**

```java
protected void eventoBotonBuscar() {
    limpiarTabla();
    
    String textoBusqueda = tfBuscar.getText().trim();  // ← Vacío = todas
    MiLista<Cancion> canciones = controller.buscar(textoBusqueda);
    
    // Llenar tabla
    for (int i = 0; i < canciones.tamanio(); i++) {
        Cancion cancion = canciones.obtener(i);
        Object[] fila = {
            cancion.getId(),
            cancion.getTitulo(),
            cancion.getArtista(),
            cancion.getAlbum(),
            cancion.getDuracionFormateada()
        };
        modeloTabla.addRow(fila);
    }
}
```

**Consola:**
```
🔍 Buscando canciones con filtro: (todos)
✅ Encontradas 10 canciones
```

---

### **6. ventana.setVisible(true)**

```java
ventana.setVisible(true);  // ← Muestra la ventana
```

**Resultado:**
- Ventana aparece en pantalla
- Todas las pestañas cargadas
- Tabla de biblioteca llena con canciones
- Aplicación lista para usar

**Consola:**
```
✅ Aplicación iniciada correctamente
Iniciando aplicación...
```

---

## 🔄 Flujo Completo en Diagrama

```
┌────────────────────────────────────────────────────┐
│ 1. JVM inicia → Main.main()                        │
└───────────────────┬────────────────────────────────┘
                    ↓
┌────────────────────────────────────────────────────┐
│ 2. imprimirBanner()                                │
│    → Muestra información en consola                │
└───────────────────┬────────────────────────────────┘
                    ↓
┌────────────────────────────────────────────────────┐
│ 3. SwingUtilities.invokeLater()                    │
│    → Ejecuta en Event Dispatch Thread              │
└───────────────────┬────────────────────────────────┘
                    ↓
┌────────────────────────────────────────────────────┐
│ 4. new PrincipalDlg()                              │
│    ├─ FlatLightLaf.setup()                         │
│    ├─ Configurar ventana (1200x700)                │
│    ├─ new Reproductor()                            │
│    │  ├─ new MiCola<>() → Cola reproducción        │
│    │  ├─ new MiPila<>() → Historial                │
│    │  └─ new ReproductorAudio() → Motor MP3        │
│    └─ initComponents()                             │
│       ├─ new ReproductorDlg(reproductor)           │
│       ├─ new BibliotecaDlg(reproductor)            │
│       │  └─ new CancionController()                │
│       │     └─ new CancionRepository()             │
│       │        └─ MiConexion.getInstancia()        │
│       │           ├─ Conecta a canciones.db        │
│       │           └─ CREATE TABLE IF NOT EXISTS    │
│       └─ new HistorialDlg(reproductor)             │
└───────────────────┬────────────────────────────────┘
                    ↓
┌────────────────────────────────────────────────────┐
│ 5. BibliotecaDlg.inicializar()                     │
│    └─ eventoBotonBuscar()                          │
│       └─ controller.buscar(null)                   │
│          └─ repository.buscar(null)                │
│             └─ SELECT * FROM canciones             │
│                └─ MiLista<Cancion> con 10 canciones│
└───────────────────┬────────────────────────────────┘
                    ↓
┌────────────────────────────────────────────────────┐
│ 6. ventana.setVisible(true)                        │
│    → Ventana visible en pantalla                   │
│    → Usuario puede interactuar                     │
└────────────────────────────────────────────────────┘
```

---

## 🎯 Orden de Inicialización de Componentes

| # | Componente | Acción | Consola |
|---|-----------|--------|---------|
| 1 | Main | Inicia aplicación | Banner con logo |
| 2 | PrincipalDlg | Crea ventana | "Inicializando JavaFX..." |
| 3 | Reproductor | Crea estructuras | - |
| 4 | ReproductorAudio | Inicia JavaFX | "✅ JavaFX inicializado" |
| 5 | BibliotecaDlg | Crea vista | - |
| 6 | CancionController | Crea controlador | "✅ CancionController inicializado" |
| 7 | CancionRepository | Crea repositorio | - |
| 8 | MiConexion | Conecta BD | "✅ Conexión establecida" |
| 9 | MiConexion | Crea tabla | "✅ Tabla verificada/creada" |
| 10 | BibliotecaDlg | Busca canciones | "✅ Encontradas 10 canciones" |
| 11 | PrincipalDlg | Muestra ventana | "✅ Aplicación iniciada" |

---

## 🎬 Secuencia de Eventos Post-Inicio

### **Usuario hace clic en "▶ Reproducir Todo":**

```
1. BibliotecaDlg.reproducirTodasLasCanciones()
      ↓
2. reproductor.limpiarCola()
      ↓
3. for (cada canción): reproductor.agregarACola(cancion)
      ↓
4. reproductor.reproducirSiguiente()
      ↓
5. colaReproduccion.desencolar() → Cancion A
      ↓
6. reproductorAudio.reproducir(A)
      ↓
7. JavaFX MediaPlayer carga A.mp3
      ↓
8. mediaPlayer.play() → Audio suena
      ↓
9. notificarCancionCambiada(A)
      ↓
10. ReproductorDlg actualiza UI
      ↓
11. Usuario escucha música ♪
```

---

## 🛑 Cierre de la Aplicación

### **Usuario cierra ventana (X):**

```
1. Usuario hace clic en X
      ↓
2. WindowListener detecta evento
      ↓
3. setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
      ↓
4. reproductorAudio.detener()
      ↓
5. mediaPlayer.dispose()
      ↓
6. MiConexion.cerrar()
      ↓
7. conexion.close()
      ↓
8. System.exit(0)
      ↓
9. JVM termina
```

**Consola:**
```
✅ Conexión cerrada
```

---

## ⚙️ Configuración de Ejecución

### **Maven:**

```bash
mvn clean compile
mvn exec:java
```

**pom.xml:**
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>exec-maven-plugin</artifactId>
            <version>3.0.0</version>
            <configuration>
                <mainClass>org.upemor.reproductor.Main</mainClass>
            </configuration>
        </plugin>
    </plugins>
</build>
```

### **IDE (IntelliJ IDEA / Eclipse):**

1. Click derecho en `Main.java`
2. "Run 'Main.main()'"
3. Aplicación inicia

### **JAR Ejecutable:**

```bash
mvn clean package
java -jar target/EP2_Reproductor-1.0.jar
```

---

## 🧪 Verificación de Inicio Correcto

### **Checklist:**

✅ Banner se muestra en consola  
✅ "✅ JavaFX inicializado"  
✅ "✅ Conexión a base de datos establecida"  
✅ "✅ Tabla 'canciones' verificada/creada"  
✅ "✅ CancionController inicializado"  
✅ "✅ Encontradas X canciones"  
✅ "✅ Aplicación iniciada correctamente"  
✅ Ventana visible con 3 pestañas  
✅ Tabla de biblioteca llena  
✅ Sin errores en consola

---

## 🐛 Problemas Comunes al Iniciar

### **Error: "Driver SQLite no encontrado"**
```
❌ Solución: Verificar dependencia en pom.xml
<dependency>
    <groupId>org.xerial</groupId>
    <artifactId>sqlite-jdbc</artifactId>
    <version>3.42.0.0</version>
</dependency>
```

### **Error: "JavaFX not found"**
```
❌ Solución: Agregar JavaFX a pom.xml
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-media</artifactId>
    <version>21</version>
</dependency>
```

### **Error: "FlatLaf not found"**
```
❌ Solución: Agregar FlatLaf a pom.xml
<dependency>
    <groupId>com.formdev</groupId>
    <artifactId>flatlaf</artifactId>
    <version>3.3</version>
</dependency>
```

---

## 📊 Tiempo de Inicio

**Componente** → **Tiempo Aproximado**

- Cargar clases: ~500ms
- Conectar BD: ~100ms
- Crear tablas: ~50ms
- Cargar canciones: ~200ms
- Renderizar UI: ~300ms

**Total: ~1.2 segundos** ⚡

---

## 🎓 Conceptos Aplicados

✅ **Event Dispatch Thread (EDT)** - Swing UI thread  
✅ **Singleton Pattern** - MiConexion única instancia  
✅ **Lazy Initialization** - Conexión al primer uso  
✅ **Observer Pattern** - Listeners para eventos  
✅ **Dependency Injection** - Reproductor inyectado en vistas  
✅ **Template Method** - BaseDlg estructura común
