# 🎵 REPRODUCTOR DE CANCIONES - Sistema con Estructuras de Datos

## 📋 Índice de Documentación

Este proyecto es un **Reproductor de Música MP3** implementado en **Java 21** con **Swing** y **JavaFX**, utilizando estructuras de datos manuales (sin usar colecciones de Java).

### 📚 Documentación Completa

1. [**ARQUITECTURA.md**](./ARQUITECTURA.md) - Arquitectura general del sistema
2. [**ESTRUCTURAS_DE_DATOS.md**](./ESTRUCTURAS_DE_DATOS.md) - Implementación de estructuras de datos manuales
3. [**MODELO.md**](./MODELO.md) - Capa de modelo y entidades
4. [**CONTROLADOR.md**](./CONTROLADOR.md) - Capa de controladores
5. [**VISTA.md**](./VISTA.md) - Capa de vista (interfaz gráfica)
6. [**LOGICA_REPRODUCTOR.md**](./LOGICA_REPRODUCTOR.md) - Lógica del reproductor de audio
7. [**BASE_DE_DATOS.md**](./BASE_DE_DATOS.md) - Configuración de la base de datos SQLite
8. [**FLUJO_DE_EJECUCION.md**](./FLUJO_DE_EJECUCION.md) - Flujo de ejecución del programa
9. [**COMPLEJIDAD_ALGORITMICA.md**](./COMPLEJIDAD_ALGORITMICA.md) - ⭐ Análisis Big O y algoritmos de ordenamiento

---

## 🎯 Descripción General

Este proyecto es un **reproductor de música MP3** desarrollado como práctica educativa para la materia de **Estructuras de Datos**. El sistema permite:

- ✅ **Gestionar una biblioteca de canciones** (agregar, editar, eliminar, buscar)
- ✅ **Reproducir archivos MP3** reales con controles de play, pause, stop
- ✅ **Cola de reproducción** automática
- ✅ **Historial de reproducción**
- ✅ **Navegación** entre canciones (siguiente, anterior)
- ✅ **Ordenamiento de canciones** por título (Bubble Sort) o artista (Insertion Sort)
- ✅ **Interfaz gráfica moderna** con Swing y FlatLaf

---

## 🏗️ Arquitectura

El proyecto sigue el patrón **MVC (Modelo-Vista-Controlador)** con una capa adicional de lógica de negocio:

```
org.upemor.reproductor/
├── 📦 estructuras/          → Estructuras de datos manuales
│   ├── Nodo.java           → Nodo genérico (doblemente enlazado)
│   ├── MiLista.java        → Lista circular doblemente enlazada
│   ├── MiPila.java         → Pila (LIFO)
│   └── MiCola.java         → Cola (FIFO)
│
├── 📦 model/               → Capa de Modelo (MVC)
│   ├── entity/             → Entidades del dominio
│   │   ├── Entity.java     → Clase base abstracta
│   │   └── Cancion.java    → Entidad Canción
│   └── repository/         → Repositorios (acceso a datos)
│       ├── MiConexion.java → Conexión a SQLite
│       ├── Repository.java → Interfaz genérica de repositorio
│       └── CancionRepository.java → Repositorio de canciones
│
├── 📦 controller/          → Capa de Controlador (MVC)
│   ├── Controller.java     → Controlador genérico abstracto
│   └── CancionController.java → Controlador de canciones
│
├── 📦 view/                → Capa de Vista (MVC)
│   ├── PrincipalDlg.java   → Ventana principal con pestañas
│   ├── tools/              → Componentes reutilizables
│   │   ├── BaseDlg.java    → Panel base con tabla
│   │   └── BaseModelDlg.java → Diálogo modal base
│   └── administracion/     → Vistas específicas
│       ├── BibliotecaDlg.java → Gestión de biblioteca
│       ├── ReproductorDlg.java → Panel del reproductor
│       ├── HistorialDlg.java → Historial de reproducción
│       └── CancionModalDlg.java → Modal para agregar/editar
│
├── 📦 logica/              → Lógica de Negocio
│   ├── Reproductor.java    → Gestión de reproducción
│   └── ReproductorAudio.java → Reproducción real de MP3
│
└── 📄 Main.java            → Punto de entrada del programa
```

---

## 🛠️ Tecnologías Utilizadas

### **Backend**
- **Java 21** - Lenguaje de programación
- **SQLite 3.42.0.0** - Base de datos embebida
- **Lombok 1.18.38** - Reducción de código boilerplate

### **Frontend**
- **Swing** - Framework GUI nativo de Java
- **FlatLaf 3.3** - Look and Feel moderno
- **JavaFX 21.0.1** - MediaPlayer para reproducción de audio

### **Build Tool**
- **Maven** - Gestión de dependencias y compilación

---

## 📊 Estructuras de Datos Implementadas

### ✨ **SIN USAR JAVA COLLECTIONS**

Todas las estructuras fueron implementadas manualmente:

| Estructura | Tipo | Uso en el Sistema |
|-----------|------|-------------------|
| **MiLista** | Lista Circular Doblemente Enlazada | Almacenar resultados de búsqueda, elementos de cola/pila |
| **MiPila** | Pila (LIFO) | Historial de reproducción |
| **MiCola** | Cola (FIFO) | Cola de reproducción |
| **Nodo** | Nodo Genérico | Componente base de todas las estructuras |

---

## 🗄️ Base de Datos

### **Tabla: canciones**

```sql
CREATE TABLE canciones (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    titulo VARCHAR(200) NOT NULL,
    artista VARCHAR(100) NOT NULL,
    album VARCHAR(100),
    duracion INTEGER NOT NULL,
    ruta_archivo VARCHAR(500) NOT NULL,
    fecha_agregada DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

---

## 🎨 Interfaz Gráfica

### **Ventana Principal con 3 Pestañas:**

#### 1️⃣ **Reproductor** (Pestaña principal)
- Muestra la canción actual
- Controles: Play/Pause, Siguiente, Anterior, Detener
- Tabla con la cola de reproducción

#### 2️⃣ **Biblioteca**
- Tabla con todas las canciones
- Búsqueda por título/artista
- Botones: +Canción, Editar, -Canción, +Cola, ▶
- **🔤 Ordenar por Título** (usa Bubble Sort)
- **🎤 Ordenar por Artista** (usa Insertion Sort)
- Funcionalidad "Reproducir Todo"

#### 3️⃣ **Historial**
- Tabla con canciones reproducidas
- Botones: Reproducir, Actualizar, Limpiar Historial

---

## 🚀 Cómo Ejecutar

### **Compilar:**
```bash
mvn clean compile
```

### **Ejecutar:**
```bash
mvn exec:java
```

O desde tu IDE favorito ejecutando la clase `Main.java`

---

## 📖 Funcionalidades Principales

### **Gestión de Biblioteca**
- ✅ Agregar canciones (título, artista, álbum, duración, archivo MP3)
- ✅ Editar información de canciones
- ✅ Eliminar canciones
- ✅ Buscar por título o artista
- ✅ **Ordenar por título** usando **Bubble Sort** (O(n²))
- ✅ **Ordenar por artista** usando **Insertion Sort** (O(n²))

### **Reproducción**
- ✅ Reproducir archivos MP3 reales
- ✅ Pause/Resume funcional
- ✅ Navegación: Siguiente/Anterior
- ✅ Detener reproducción
- ✅ Cola automática de reproducción
- ✅ Auto-reproducción cuando termina una canción

### **Historial**
- ✅ Registro de todas las canciones reproducidas
- ✅ Incluye canciones detenidas (no solo las completadas)
- ✅ Volver a reproducir desde el historial
- ✅ Limpiar historial

---

## 🎓 Conceptos de Estructuras de Datos Aplicados

1. **Lista Circular Doblemente Enlazada** - Navegación bidireccional eficiente
2. **Pila (LIFO)** - Historial con acceso al último elemento
3. **Cola (FIFO)** - Reproducción ordenada de canciones
4. **Nodos Genéricos** - Reutilización con tipos parametrizados
5. **Búsqueda Lineal** - O(n) para buscar canciones por texto
6. **Bubble Sort** - O(n²) para ordenar por título
7. **Insertion Sort** - O(n²) para ordenar por artista
8. **Patrón Repository** - Abstracción del acceso a datos
9. **Patrón MVC** - Separación de responsabilidades

---

## 📦 Dependencias (pom.xml)

```xml
<dependencies>
    <!-- SQLite JDBC -->
    <dependency>
        <groupId>org.xerial</groupId>
        <artifactId>sqlite-jdbc</artifactId>
        <version>3.42.0.0</version>
    </dependency>
    
    <!-- FlatLaf (Look and Feel moderno) -->
    <dependency>
        <groupId>com.formdev</groupId>
        <artifactId>flatlaf</artifactId>
        <version>3.3</version>
    </dependency>
    
    <!-- JavaFX para reproducción de audio -->
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-media</artifactId>
        <version>21</version>
    </dependency>
    
    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.38</version>
    </dependency>
</dependencies>
```

---

## 👨‍💻 Autor

**Sistema Reproductor de Canciones**  
Universidad Politécnica del Estado de Morelos (UPEMOR)  
Materia: Estructuras de Datos  
Versión: 1.0 - 2025

---

## 📄 Licencia

Proyecto educativo - Todos los derechos reservados
