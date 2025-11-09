# 🎨 CAPA DE VISTA - Interfaz Gráfica

## 📦 Estructura del Paquete `view`

```
org.upemor.reproductor.view/
├── PrincipalDlg.java           → Ventana principal con pestañas
├── tools/
│   ├── BaseDlg.java           → Panel base reutilizable con tabla
│   └── BaseModelDlg.java      → Diálogo modal base
└── administracion/
    ├── BibliotecaDlg.java     → Gestión de biblioteca de canciones
    ├── ReproductorDlg.java    → Panel del reproductor
    ├── HistorialDlg.java      → Historial de reproducción
    └── CancionModalDlg.java   → Modal para agregar/editar canciones
```

---

## 🖼️ PrincipalDlg.java - Ventana Principal

**Ubicación:** `org.upemor.reproductor.view.PrincipalDlg`

### **Responsabilidades:**
- Crear la ventana principal de la aplicación
- Gestionar las 3 pestañas principales
- Coordinar la comunicación entre pestañas
- Aplicar tema FlatLaf

### **Estructura:**
```
┌────────────────────────────────────────────────────┐
│  REPRODUCTOR DE CANCIONES                          │
│  Sistema con Estructuras de Datos: Lista, Pila y Cola │
├────────────────────────────────────────────────────┤
│  [Reproductor] [Biblioteca] [Historial]            │
├────────────────────────────────────────────────────┤
│                                                    │
│         CONTENIDO DE LA PESTAÑA ACTIVA             │
│                                                    │
│                                                    │
└────────────────────────────────────────────────────┘
```

### **Código Principal:**
```java
public class PrincipalDlg extends JFrame {
    private JTabbedPane tabbedPane;
    private BibliotecaDlg bibliotecaDlg;
    private ReproductorDlg reproductorDlg;
    private HistorialDlg historialDlg;
    private Reproductor reproductor;
    
    public PrincipalDlg() {
        // Aplicar tema FlatLaf
        FlatLightLaf.setup();
        
        // Configurar ventana
        setTitle("Reproductor de Canciones - Sistema de Estructuras de Datos");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Crear reproductor
        reproductor = new Reproductor();
        
        // Inicializar componentes
        initComponents();
    }
    
    private void initComponents() {
        // Header
        add(crearHeader(), BorderLayout.NORTH);
        
        // Tabs
        tabbedPane = new JTabbedPane();
        
        // Pestaña 1: Reproductor
        reproductorDlg = new ReproductorDlg(reproductor);
        tabbedPane.addTab("🎵 Reproductor", reproductorDlg);
        
        // Pestaña 2: Biblioteca
        bibliotecaDlg = new BibliotecaDlg(reproductor);
        tabbedPane.addTab("📚 Biblioteca", bibliotecaDlg);
        
        // Pestaña 3: Historial
        historialDlg = new HistorialDlg(reproductor);
        tabbedPane.addTab("📜 Historial", historialDlg);
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Footer
        add(crearFooter(), BorderLayout.SOUTH);
    }
}
```

### **Características:**
- ✅ **FlatLaf Theme** - Look and feel moderno
- ✅ **3 Pestañas** - Reproductor, Biblioteca, Historial
- ✅ **Tamaño:** 1200x700 píxeles
- ✅ **Centrada** en pantalla
- ✅ **Instancia única** de Reproductor compartida

---

## 🛠️ BaseDlg.java - Panel Base Reutilizable

**Ubicación:** `org.upemor.reproductor.view.tools.BaseDlg`

### **Propósito:**
Componente reutilizable que proporciona estructura común para paneles con tabla y botones CRUD.

### **Estructura Visual:**
```
┌─────────────────────────────────────────────────┐
│ Buscar: [____________] [🔍 Buscar]              │
├─────────────────────────────────────────────────┤
│                                                 │
│             TABLA DE DATOS                      │
│                                                 │
├─────────────────────────────────────────────────┤
│ [+Canción] [✏️ Editar] [-Canción]               │
└─────────────────────────────────────────────────┘
```

### **Componentes:**
```java
protected JTable tabla;
protected DefaultTableModel modeloTabla;
protected JTextField tfBuscar;
protected JButton btnBuscar;
protected JButton btnAgregar;
protected JButton btnEditar;
protected JButton btnEliminar;
protected JLabel lbBuscar;
```

### **Métodos Abstractos (Template Method):**
```java
protected abstract void eventoBotonBuscar();
protected abstract void eventoBotonAgregar();
protected abstract void eventoBotonEditar();
protected abstract void eventoBotonEliminar();
```

### **Uso (Herencia):**
```java
public class BibliotecaDlg extends BaseDlg {
    @Override
    protected void eventoBotonBuscar() {
        // Implementación específica de búsqueda
    }
    
    @Override
    protected void eventoBotonAgregar() {
        // Abrir modal para agregar canción
    }
}
```

---

## 📚 BibliotecaDlg.java - Gestión de Biblioteca

**Ubicación:** `org.upemor.reproductor.view.administracion.BibliotecaDlg`

### **Responsabilidades:**
- Mostrar tabla de canciones
- Búsqueda por título/artista
- CRUD de canciones
- Agregar canciones a cola de reproducción
- Reproducir todas las canciones

### **Interfaz:**
```
┌──────────────────────────────────────────────────────────────┐
│ Buscar canción: [___________] [🔍 Buscar]                    │
├──────────────────────────────────────────────────────────────┤
│ ID │ Título            │ Artista        │ Álbum  │ Duración  │
│  1 │ Bohemian Rhapsody │ Queen          │ Opera  │ 05:54     │
│  2 │ Stairway to Heaven│ Led Zeppelin   │ IV     │ 08:02     │
│  3 │ Hotel California  │ Eagles         │ Hotel  │ 06:31     │
├──────────────────────────────────────────────────────────────┤
│ [+Canción] [✏️ Editar] [-Canción]      [+Cola] [▶]          │
└──────────────────────────────────────────────────────────────┘
```

### **Botones:**
- **+Canción** (verde) - Abre modal para agregar nueva canción
- **✏️ Editar** (azul) - Edita canción seleccionada
- **-Canción** (rojo) - Elimina canción seleccionada
- **+Cola** (morado) - Agrega canción a cola de reproducción
- **▶** (verde) - Reproduce todas las canciones filtradas

### **Funcionalidades Especiales:**

#### **1. Reproducir Todo:**
```java
private void reproducirTodasLasCanciones() {
    // Limpiar cola actual
    reproductor.limpiarCola();
    
    // Agregar todas las canciones a la cola
    for (int i = 0; i < cancionesActuales.tamanio(); i++) {
        Cancion cancion = cancionesActuales.obtener(i);
        reproductor.agregarACola(cancion);
    }
    
    // Iniciar reproducción
    reproductor.reproducirSiguiente();
}
```

#### **2. Agregar a Cola:**
```java
private void agregarAColaReproduccion() {
    Long id = seleccionarID();
    Cancion cancion = controller.obtenerPorId(id);
    reproductor.agregarACola(cancion);
}
```

---

## 🎵 ReproductorDlg.java - Panel del Reproductor

**Ubicación:** `org.upemor.reproductor.view.administracion.ReproductorDlg`

### **Responsabilidades:**
- Mostrar canción actualmente en reproducción
- Controles de reproducción
- Mostrar cola de reproducción
- Actualizar UI en tiempo real

### **Interfaz:**
```
┌────────────────────────────────────────────────────┐
│ 🎵 CANCIÓN ACTUAL                                  │
│                                                    │
│ Título: Bohemian Rhapsody                          │
│ Artista: Queen                                     │
│ Álbum: A Night at the Opera                        │
│ Duración: 05:54                                    │
├────────────────────────────────────────────────────┤
│                                                    │
│  [⏮️ Anterior] [⏯️ Play/Pause] [⏭️ Siguiente] [⏹️ Detener]  │
│                                                    │
├────────────────────────────────────────────────────┤
│ 📋 COLA DE REPRODUCCIÓN (3 canciones)              │
│                                                    │
│ # │ Título          │ Artista      │ Duración     │
│ 1 │ Stairway...     │ Led Zeppelin │ 08:02        │
│ 2 │ Hotel...        │ Eagles       │ 06:31        │
│ 3 │ Imagine         │ John Lennon  │ 03:03        │
└────────────────────────────────────────────────────┘
```

### **Controles:**
- **⏮️ Anterior** - Reproduce la canción anterior del historial
- **⏯️ Play/Pause** - Alterna entre reproducir y pausar
- **⏭️ Siguiente** - Salta a la siguiente canción de la cola
- **⏹️ Detener** - Detiene la reproducción

### **Actualización Automática:**
```java
reproductor.setListener(new Reproductor.ReproductorListener() {
    @Override
    public void onCancionCambiada(Cancion cancion) {
        actualizarInfoCancion(cancion);
        actualizarCola();
    }
    
    @Override
    public void onEstadoCambiado(boolean reproduciendo) {
        btnPlayPause.setText(reproduciendo ? "⏸️ Pausar" : "▶️ Reproducir");
    }
});
```

---

## 📜 HistorialDlg.java - Historial de Reproducción

**Ubicación:** `org.upemor.reproductor.view.administracion.HistorialDlg`

### **Responsabilidades:**
- Mostrar historial de canciones reproducidas
- Reproducir canciones desde el historial
- Limpiar historial

### **Interfaz:**
```
┌──────────────────────────────────────────────────────┐
│ 📜 Historial de Reproducción                         │
│ 5 canción(es) reproducida(s)                         │
├──────────────────────────────────────────────────────┤
│ # │ Título            │ Artista      │ Álbum │ Dur. │
│ 5 │ Imagine           │ John Lennon  │ Imag. │ 03:03│
│ 4 │ Hotel California  │ Eagles       │ Hotel │ 06:31│
│ 3 │ Stairway to Heaven│ Led Zeppelin │ IV    │ 08:02│
│ 2 │ Bohemian Rhapsody │ Queen        │ Opera │ 05:54│
│ 1 │ Sweet Child       │ Guns N Roses │ Appe. │ 05:56│
├──────────────────────────────────────────────────────┤
│    [▶️ Reproducir] [🔄 Actualizar] [🗑️ Limpiar]      │
└──────────────────────────────────────────────────────┘
```

### **Orden de Visualización:**
Las canciones se muestran en **orden inverso** (la más reciente primero):
```java
for (int i = historial.tamanio() - 1; i >= 0; i--) {
    Cancion cancion = historial.obtener(i);
    // Agregar a tabla
}
```

### **Botones:**
- **▶️ Reproducir** (verde) - Reproduce canción seleccionada
- **🔄 Actualizar** (azul) - Refresca la tabla
- **🗑️ Limpiar Historial** (rojo) - Limpia todo el historial

---

## 📝 CancionModalDlg.java - Modal Agregar/Editar

**Ubicación:** `org.upemor.reproductor.view.administracion.CancionModalDlg`

### **Responsabilidades:**
- Modal para agregar nueva canción
- Modal para editar canción existente
- Validar formulario
- Selector de archivos MP3

### **Interfaz (Modo Agregar):**
```
┌─────────────────────────────────────────┐
│ ➕ Agregar Canción                      │
├─────────────────────────────────────────┤
│                                         │
│ Título: *                               │
│ [_____________________________]         │
│                                         │
│ Artista: *                              │
│ [_____________________________]         │
│                                         │
│ Álbum:                                  │
│ [_____________________________]         │
│                                         │
│ Duración (segundos): *                  │
│ [_____________________________]         │
│                                         │
│ Archivo MP3: *                          │
│ [_____________________] [📁 Buscar]     │
│                                         │
├─────────────────────────────────────────┤
│              [💾 Guardar] [❌ Cancelar]  │
└─────────────────────────────────────────┘
```

### **Validaciones del Formulario:**
```java
private boolean validarFormulario() {
    // Título no vacío
    if (tfTitulo.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "El título es obligatorio");
        return false;
    }
    
    // Artista no vacío
    if (tfArtista.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "El artista es obligatorio");
        return false;
    }
    
    // Duración válida
    try {
        int duracion = Integer.parseInt(tfDuracion.getText());
        if (duracion <= 0) {
            JOptionPane.showMessageDialog(this, "Duración debe ser mayor a 0");
            return false;
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Duración inválida");
        return false;
    }
    
    // Archivo seleccionado
    if (tfRutaArchivo.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Debe seleccionar un archivo MP3");
        return false;
    }
    
    return true;
}
```

### **Selector de Archivos:**
```java
private void seleccionarArchivo() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos MP3", "mp3"));
    
    int resultado = fileChooser.showOpenDialog(this);
    if (resultado == JFileChooser.APPROVE_OPTION) {
        File archivo = fileChooser.getSelectedFile();
        tfRutaArchivo.setText(archivo.getAbsolutePath());
    }
}
```

---

## 🎨 Estilos y Colores

### **Paleta de Colores:**
```java
// Botones de acción
Verde:   new Color(76, 175, 80)   // Agregar, Guardar, Reproducir
Azul:    new Color(33, 150, 243)  // Editar, Actualizar
Rojo:    new Color(244, 67, 54)   // Eliminar, Cancelar
Morado:  new Color(156, 39, 176)  // Agregar a Cola

// Backgrounds
Gris claro: new Color(245, 245, 247)
Gris borde: new Color(100, 100, 100)
```

### **Fuentes:**
```java
Header:  Segoe UI, Bold, 24pt
Título:  Segoe UI, Bold, 20pt
Texto:   Segoe UI, Plain, 14pt
Botones: Segoe UI, Bold, 13pt
Tabla:   Segoe UI, Plain, 13pt
```

---

## 🔄 Comunicación entre Componentes

### **Patrón Observer/Listener:**
```
Reproductor (Subject)
         ↓
    notifica cambios
         ↓
ReproductorDlg (Observer)
         ↓
  actualiza interfaz
```

### **Ejemplo de Listener:**
```java
// En Reproductor.java
public interface ReproductorListener {
    void onCancionCambiada(Cancion cancion);
    void onEstadoCambiado(boolean reproduciendo);
}

// En ReproductorDlg.java
reproductor.setListener(new Reproductor.ReproductorListener() {
    @Override
    public void onCancionCambiada(Cancion cancion) {
        SwingUtilities.invokeLater(() -> {
            lblTitulo.setText(cancion.getTitulo());
            lblArtista.setText(cancion.getArtista());
            actualizarCola();
        });
    }
});
```

---

## 🧪 Testing de la Vista

### **Pruebas Manuales:**

1. ✅ **Iniciar aplicación** - Ventana se abre centrada
2. ✅ **Cambiar pestañas** - Todas cargan correctamente
3. ✅ **Búsqueda** - Filtra canciones en tiempo real
4. ✅ **Agregar canción** - Modal se abre, validaciones funcionan
5. ✅ **Reproducir** - Controles responden correctamente
6. ✅ **Historial** - Se actualiza al reproducir

### **Pruebas de UI:**
- ✅ Botones con efectos hover
- ✅ Tablas con selección
- ✅ Modales centrados
- ✅ Mensajes de confirmación
- ✅ Redimensionamiento de ventana

---

## 📱 Responsive Design

### **Anchos de Columnas:**
```java
// Tabla de Biblioteca
tabla.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
tabla.getColumnModel().getColumn(1).setPreferredWidth(250);  // Título
tabla.getColumnModel().getColumn(2).setPreferredWidth(200);  // Artista
tabla.getColumnModel().getColumn(3).setPreferredWidth(200);  // Álbum
tabla.getColumnModel().getColumn(4).setPreferredWidth(80);   // Duración
```

### **Tamaños de Botones:**
```java
btn.setPreferredSize(new Dimension(140, 40));  // Botones estándar
btn.setPreferredSize(new Dimension(180, 40));  // Botones historial
```

---

## 🎯 Mejores Prácticas Implementadas

✅ **SwingUtilities.invokeLater()** - Actualiza UI en Event Dispatch Thread  
✅ **Try-with-resources** - Cierra recursos automáticamente  
✅ **Validaciones en UI** - Feedback inmediato al usuario  
✅ **Mensajes descriptivos** - JOptionPane con contexto claro  
✅ **Iconos/Emojis** - Interfaz más visual e intuitiva  
✅ **Colores semánticos** - Verde=ok, Rojo=peligro, Azul=info  
✅ **Separation of Concerns** - Vista no maneja lógica de negocio
