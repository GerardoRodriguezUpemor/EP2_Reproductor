# 🏗️ ARQUITECTURA DEL SISTEMA

## 📐 Patrón de Diseño: MVC (Modelo-Vista-Controlador)

El proyecto sigue una arquitectura en capas basada en el patrón **MVC** con una capa adicional de lógica de negocio.

---

## 🎯 Capas del Sistema

### **1. CAPA DE MODELO (Model)**

**Ubicación:** `org.upemor.reproductor.model`

#### **Responsabilidades:**
- Representar las entidades del dominio
- Gestionar el acceso a la base de datos
- Proporcionar abstracción sobre los datos

#### **Componentes:**

##### **📦 `model.entity`** - Entidades del Dominio

**Entity.java** - Clase base abstracta
```java
public abstract class Entity {
    protected Long id;
    // Métodos comunes a todas las entidades
}
```

**Cancion.java** - Entidad principal
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cancion extends Entity {
    private String titulo;
    private String artista;
    private String album;
    private Integer duracion; // en segundos
    private String rutaArchivo;
    private LocalDateTime fechaAgregada;
}
```

##### **📦 `model.repository`** - Repositorios (Patrón Repository)

**Repository.java** - Interfaz genérica
```java
public interface Repository<T> {
    MiLista<T> buscar(String filtro);
    T obtenerPorId(Long id);
    boolean guardar(T entidad);
    boolean actualizar(T entidad);
    boolean eliminar(Long id);
}
```

**MiConexion.java** - Gestión de conexión a SQLite
- Singleton para conexión única
- Inicialización de base de datos
- Creación de tablas si no existen

**CancionRepository.java** - Implementación específica
- CRUD completo de canciones
- Búsqueda por título/artista
- Conversión ResultSet → Cancion

---

### **2. CAPA DE CONTROLADOR (Controller)**

**Ubicación:** `org.upemor.reproductor.controller`

#### **Responsabilidades:**
- Intermediario entre Vista y Modelo
- Validación de datos
- Manejo de errores
- Coordinación de operaciones

#### **Componentes:**

**Controller.java** - Controlador abstracto genérico
```java
public abstract class Controller<T> {
    protected Repository<T> repository;
    
    public abstract MiLista<T> buscar(String filtro);
    public abstract T obtenerPorId(Long id);
    public abstract boolean guardar(T entidad);
    public abstract boolean actualizar(T entidad);
    public abstract boolean eliminar(Long id);
}
```

**CancionController.java** - Controlador de canciones
```java
public class CancionController extends Controller<Cancion> {
    private CancionRepository repository;
    
    // Implementación de métodos CRUD
    // Validaciones específicas de canciones
}
```

---

### **3. CAPA DE VISTA (View)**

**Ubicación:** `org.upemor.reproductor.view`

#### **Responsabilidades:**
- Presentar la interfaz gráfica al usuario
- Capturar eventos de usuario
- Actualizar la UI según los datos

#### **Componentes:**

##### **📦 `view`** - Ventana Principal

**PrincipalDlg.java** - Ventana principal con pestañas
```java
public class PrincipalDlg extends JFrame {
    private JTabbedPane tabbedPane;
    private BibliotecaDlg bibliotecaDlg;
    private ReproductorDlg reproductorDlg;
    private HistorialDlg historialDlg;
    private Reproductor reproductor;
}
```

**Pestañas:**
1. **Tab 0:** 🎵 Reproductor (principal)
2. **Tab 1:** 📚 Biblioteca
3. **Tab 2:** 📜 Historial

##### **📦 `view.tools`** - Componentes Reutilizables

**BaseDlg.java** - Panel base con tabla
- Tabla genérica con modelo
- Barra de búsqueda
- Botones estándar (Agregar, Editar, Eliminar)
- Métodos abstractos para eventos

**BaseModelDlg.java** - Diálogo modal base
- Ventana modal reutilizable
- Gestión de componentes
- Validación de formularios

##### **📦 `view.administracion`** - Vistas Específicas

**BibliotecaDlg.java** - Gestión de biblioteca
- Extiende `BaseDlg`
- Tabla de canciones
- Búsqueda por título/artista
- Botones: +Canción, Editar, -Canción, +Cola, ▶
- Funcionalidad "Reproducir Todo"

**ReproductorDlg.java** - Panel del reproductor
- Información de canción actual
- Controles: Play/Pause, Siguiente, Anterior, Detener
- Tabla de cola de reproducción
- Actualización automática

**HistorialDlg.java** - Historial de reproducción
- Tabla de canciones reproducidas
- Botones: Reproducir, Actualizar, Limpiar
- Contador de canciones

**CancionModalDlg.java** - Modal para agregar/editar
- Extiende `BaseModelDlg`
- Formulario completo
- Selector de archivos MP3
- Validaciones

---

### **4. CAPA DE LÓGICA DE NEGOCIO**

**Ubicación:** `org.upemor.reproductor.logica`

#### **Responsabilidades:**
- Gestionar la lógica del reproductor
- Controlar la cola de reproducción
- Mantener el historial
- Reproducir archivos MP3

#### **Componentes:**

**Reproductor.java** - Gestor de reproducción
```java
public class Reproductor implements ReproductorAudio.ReproductorListener {
    private MiCola<Cancion> colaReproduccion;
    private MiPila<Cancion> historial;
    private Cancion cancionActual;
    private boolean reproduciendo;
    private ReproductorAudio reproductorAudio;
    
    // Métodos principales:
    - agregarACola(Cancion)
    - reproducirSiguiente()
    - reproducirAnterior()
    - togglePausa()
    - detener()
    - limpiarCola()
    - limpiarHistorial()
}
```

**Flujo de reproducción:**
1. Usuario agrega canciones a la cola
2. `reproducirSiguiente()` desencola y reproduce
3. Canción actual se apila en historial
4. Al terminar, auto-reproduce siguiente
5. `reproducirAnterior()` desapila del historial

**ReproductorAudio.java** - Reproductor MP3 real
```java
public class ReproductorAudio {
    private MediaPlayer mediaPlayer;
    private ReproductorListener listener;
    
    // Métodos:
    - reproducir(Cancion)
    - pausar()
    - reanudar()
    - detener()
    - isReproduciendo()
}
```

**Características:**
- Usa JavaFX MediaPlayer
- Soporte para MP3
- Pause/Resume funcional
- Callbacks al terminar canción

---

### **5. CAPA DE ESTRUCTURAS DE DATOS**

**Ubicación:** `org.upemor.reproductor.estructuras`

#### **Responsabilidades:**
- Implementar estructuras de datos manuales
- NO usar Java Collections
- Proporcionar tipos genéricos reutilizables

Ver documento: [ESTRUCTURAS_DE_DATOS.md](./ESTRUCTURAS_DE_DATOS.md)

---

## 🔄 Flujo de Datos

### **Operación: Buscar Canciones**

```
[Usuario] 
    ↓ (escribe en búsqueda)
[BibliotecaDlg]
    ↓ (captura evento)
[CancionController.buscar()]
    ↓ (llama repositorio)
[CancionRepository.buscar()]
    ↓ (consulta SQL)
[SQLite Database]
    ↓ (ResultSet)
[CancionRepository] 
    ↓ (convierte a MiLista<Cancion>)
[CancionController]
    ↓ (retorna lista)
[BibliotecaDlg]
    ↓ (actualiza tabla)
[Usuario ve resultados]
```

### **Operación: Reproducir Canción**

```
[Usuario] 
    ↓ (selecciona canción)
[BibliotecaDlg]
    ↓ (agrega a cola)
[Reproductor.agregarACola()]
    ↓ (encola en MiCola)
[Reproductor.reproducirSiguiente()]
    ↓ (desencola canción)
    ↓ (apila en historial)
[ReproductorAudio.reproducir()]
    ↓ (carga archivo MP3)
[JavaFX MediaPlayer]
    ↓ (reproduce audio)
    ↓ (notifica al terminar)
[Reproductor.onCancionTerminada()]
    ↓ (reproduce siguiente)
```

---

## 📊 Diagrama de Componentes

```
┌─────────────────────────────────────────────────────────┐
│                    MAIN.java                             │
│                 (Punto de Entrada)                       │
└────────────────────┬────────────────────────────────────┘
                     ↓
┌─────────────────────────────────────────────────────────┐
│                 PrincipalDlg                             │
│          (Ventana con 3 pestañas)                        │
└──────┬──────────────┬──────────────┬────────────────────┘
       ↓              ↓              ↓
┌──────────┐  ┌──────────┐  ┌──────────┐
│Reproductor│  │Biblioteca│  │Historial │
│   Dlg    │  │   Dlg    │  │   Dlg    │
└─────┬────┘  └─────┬────┘  └─────┬────┘
      │             │              │
      └─────────────┼──────────────┘
                    ↓
        ┌───────────────────────┐
        │   CancionController    │
        └───────────┬────────────┘
                    ↓
        ┌───────────────────────┐
        │  CancionRepository     │
        └───────────┬────────────┘
                    ↓
        ┌───────────────────────┐
        │    MiConexion          │
        │   (SQLite DB)          │
        └────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│                  Reproductor (Lógica)                    │
│  ┌──────────┐  ┌──────────┐  ┌──────────────────┐      │
│  │ MiCola   │  │ MiPila   │  │ReproductorAudio  │      │
│  │ (Cola)   │  │(Historial)│  │  (JavaFX Media)  │      │
│  └──────────┘  └──────────┘  └──────────────────┘      │
└─────────────────────────────────────────────────────────┘
```

---

## 🎯 Patrones de Diseño Utilizados

### 1. **MVC (Model-View-Controller)**
- Separación de responsabilidades
- Facilita mantenimiento y testing

### 2. **Repository Pattern**
- Abstracción del acceso a datos
- Facilita cambio de base de datos

### 3. **Singleton**
- `MiConexion` - Una sola conexión a BD

### 4. **Observer/Listener**
- `ReproductorListener` - Notificación de eventos
- `ReproductorAudio.ReproductorListener` - Callbacks de audio

### 5. **Template Method**
- `BaseDlg` - Estructura común de paneles
- `BaseModelDlg` - Estructura de modales

### 6. **Factory Method** (implícito)
- Creación de componentes Swing

---

## 🔐 Principios SOLID Aplicados

### **S - Single Responsibility**
- Cada clase tiene una única responsabilidad
- `Cancion` solo representa datos
- `CancionRepository` solo accede a BD
- `CancionController` solo coordina

### **O - Open/Closed**
- `Repository<T>` permite extensión sin modificación
- `Controller<T>` permite nuevos tipos

### **L - Liskov Substitution**
- `CancionController` puede usarse como `Controller<Cancion>`
- `MiLista<Cancion>` puede usarse como `MiLista<T>`

### **I - Interface Segregation**
- `Repository<T>` solo métodos necesarios
- `ReproductorListener` solo callbacks relevantes

### **D - Dependency Inversion**
- Vista depende de abstracción (Controller)
- Controller depende de abstracción (Repository)
- No dependen de implementaciones concretas

---

## 📝 Convenciones de Código

### **Nomenclatura:**
- **Clases:** PascalCase (`CancionRepository`)
- **Métodos:** camelCase (`obtenerPorId`)
- **Constantes:** UPPER_SNAKE_CASE (`MAX_DURACION`)
- **Paquetes:** lowercase (`org.upemor.reproductor`)

### **Organización:**
- **Una clase por archivo**
- **Imports organizados**
- **Documentación JavaDoc**

### **Comentarios:**
```java
/**
 * Descripción de la clase/método
 * @param parametro Descripción
 * @return Descripción del retorno
 * @author Sistema Reproductor
 */
```

---

## 🚀 Ventajas de esta Arquitectura

✅ **Mantenibilidad** - Fácil de modificar y extender  
✅ **Testabilidad** - Capas pueden probarse independientemente  
✅ **Reutilización** - Componentes genéricos reutilizables  
✅ **Escalabilidad** - Fácil agregar nuevas funcionalidades  
✅ **Claridad** - Responsabilidades bien definidas  
✅ **Separación de Concerns** - UI, lógica y datos separados
