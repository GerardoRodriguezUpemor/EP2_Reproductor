# 🎵 Reproductor de Canciones MP3

Sistema de reproductor de música desarrollado en **Java 21** con **Swing** y **JavaFX**, implementando estructuras de datos manuales (sin usar colecciones de Java).

![Java](https://img.shields.io/badge/Java-21-orange)
![Maven](https://img.shields.io/badge/Maven-3.9-blue)
![SQLite](https://img.shields.io/badge/SQLite-3.42-green)
![JavaFX](https://img.shields.io/badge/JavaFX-21-red)

## 📋 Descripción

Proyecto educativo para la materia de **Estructuras de Datos** que implementa un reproductor de música MP3 completamente funcional utilizando:

- ✅ **Estructuras de datos manuales** (sin Java Collections)
- ✅ **Arquitectura MVC** (Modelo-Vista-Controlador)
- ✅ **Base de datos SQLite** embebida
- ✅ **Reproducción real de MP3** con JavaFX MediaPlayer
- ✅ **Interfaz gráfica moderna** con Swing + FlatLaf

## 🎯 Características

### Estructuras de Datos Implementadas

- **MiLista** - Lista Circular Doblemente Enlazada
- **MiPila** - Pila (LIFO) para historial de reproducción
- **MiCola** - Cola (FIFO) para cola de reproducción

### Funcionalidades

- 🎵 Reproducción de archivos MP3
- ⏯️ Controles: Play, Pause, Stop, Siguiente, Anterior
- 📚 Gestión de biblioteca de canciones (CRUD)
- 🔍 Búsqueda por título o artista
- 📜 Historial de reproducción
- 🎼 Cola de reproducción automática
- ▶️ Reproducir todas las canciones
- ➕ Agregar canciones a la cola

## 🛠️ Tecnologías

| Categoría | Tecnología | Versión |
|-----------|-----------|---------|
| **Lenguaje** | Java | 21 |
| **Build Tool** | Maven | 3.9+ |
| **Base de Datos** | SQLite | 3.42.0.0 |
| **Audio** | JavaFX MediaPlayer | 21.0.1 |
| **GUI Framework** | Swing | Java 21 |
| **Look & Feel** | FlatLaf | 3.3 |
| **Annotations** | Lombok | 1.18.38 |

## 📦 Instalación

### Prerrequisitos

- **JDK 21** o superior
- **Maven 3.9+**
- Sistema operativo: Windows, Linux o macOS

### Clonar el Repositorio

```bash
git clone https://github.com/TU_USUARIO/EP2_Reproductor.git
cd EP2_Reproductor
```

### Compilar el Proyecto

```bash
mvn clean compile
```

### Ejecutar la Aplicación

```bash
mvn exec:java
```

O desde tu IDE favorito ejecutando la clase `Main.java`.

## 🎮 Uso

### 1️⃣ Agregar Canciones

1. Ve a la pestaña **📚 Biblioteca**
2. Haz clic en **+Canción**
3. Llena el formulario:
   - Título
   - Artista
   - Álbum (opcional)
   - Duración (segundos)
   - Selecciona archivo MP3
4. Guarda la canción

### 2️⃣ Reproducir Música

**Opción A: Reproducir una canción**
1. Selecciona una canción en la biblioteca
2. Haz clic en **+Cola** para agregarla a la cola
3. Ve a la pestaña **🎵 Reproductor**
4. Usa los controles de reproducción

**Opción B: Reproducir todo**
1. En la pestaña **📚 Biblioteca**
2. Haz clic en **▶** para reproducir todas las canciones

### 3️⃣ Ver Historial

1. Ve a la pestaña **📜 Historial**
2. Verás todas las canciones que has reproducido
3. Puedes seleccionar y reproducir canciones desde ahí

## 📁 Estructura del Proyecto

```
EP2_Reproductor/
├── src/main/java/org/upemor/reproductor/
│   ├── Main.java                      # Punto de entrada
│   ├── estructuras/                   # Estructuras de datos manuales
│   │   ├── Nodo.java                  # Nodo genérico doblemente enlazado
│   │   ├── MiLista.java               # Lista circular doblemente enlazada
│   │   ├── MiPila.java                # Pila (LIFO)
│   │   └── MiCola.java                # Cola (FIFO)
│   ├── model/                         # Capa de Modelo (MVC)
│   │   ├── entity/
│   │   │   ├── Entity.java            # Entidad base
│   │   │   └── Cancion.java           # Entidad Canción
│   │   └── repository/
│   │       ├── MiConexion.java        # Singleton de conexión SQLite
│   │       ├── Repository.java         # Interfaz genérica
│   │       └── CancionRepository.java # Repositorio de canciones
│   ├── controller/                    # Capa de Controlador (MVC)
│   │   ├── Controller.java            # Controlador genérico
│   │   └── CancionController.java     # Controlador de canciones
│   ├── view/                          # Capa de Vista (MVC)
│   │   ├── PrincipalDlg.java          # Ventana principal
│   │   ├── tools/
│   │   │   ├── BaseDlg.java           # Panel base
│   │   │   └── BaseModelDlg.java      # Modal base
│   │   └── administracion/
│   │       ├── BibliotecaDlg.java     # Gestión de biblioteca
│   │       ├── ReproductorDlg.java    # Panel reproductor
│   │       ├── HistorialDlg.java      # Historial
│   │       └── CancionModalDlg.java   # Modal agregar/editar
│   └── logica/                        # Lógica de negocio
│       ├── Reproductor.java           # Gestor de reproducción
│       └── ReproductorAudio.java      # Motor MP3 (JavaFX)
├── docs/                              # Documentación completa
│   ├── README.md
│   ├── ARQUITECTURA.md
│   ├── ESTRUCTURAS_DE_DATOS.md
│   ├── MODELO.md
│   ├── CONTROLADOR.md
│   ├── VISTA.md
│   ├── LOGICA_REPRODUCTOR.md
│   ├── BASE_DE_DATOS.md
│   └── FLUJO_DE_EJECUCION.md
├── pom.xml                            # Configuración Maven
├── canciones.db                       # Base de datos SQLite
└── README.md                          # Este archivo
```

## 📚 Documentación Completa

Consulta la carpeta [`docs/`](./docs/) para documentación detallada:

- [**Arquitectura del Sistema**](./docs/ARQUITECTURA.md)
- [**Estructuras de Datos**](./docs/ESTRUCTURAS_DE_DATOS.md)
- [**Capa de Modelo**](./docs/MODELO.md)
- [**Capa de Controlador**](./docs/CONTROLADOR.md)
- [**Capa de Vista**](./docs/VISTA.md)
- [**Lógica del Reproductor**](./docs/LOGICA_REPRODUCTOR.md)
- [**Base de Datos**](./docs/BASE_DE_DATOS.md)
- [**Flujo de Ejecución**](./docs/FLUJO_DE_EJECUCION.md)

## 🎓 Conceptos de Estructuras de Datos

Este proyecto implementa y demuestra:

- ✅ **Listas Enlazadas** - Circular y doblemente enlazada
- ✅ **Pilas (Stack)** - LIFO para historial
- ✅ **Colas (Queue)** - FIFO para reproducción
- ✅ **Nodos genéricos** - Con tipos parametrizados
- ✅ **Iteración personalizada** - Sin usar `Iterator` de Java
- ✅ **Complejidad algorítmica** - Análisis Big O

## 🏗️ Patrones de Diseño

- **MVC (Model-View-Controller)** - Arquitectura principal
- **Singleton** - Conexión única a base de datos
- **Repository** - Abstracción del acceso a datos
- **Observer/Listener** - Notificación de eventos
- **Template Method** - Componentes base reutilizables

## 🧪 Testing

```bash
# Compilar y verificar
mvn clean compile

# Ejecutar
mvn exec:java
```

## 🤝 Contribuciones

Este es un proyecto educativo. Si deseas contribuir:

1. Fork el proyecto
2. Crea una rama (`git checkout -b feature/NuevaCaracteristica`)
3. Commit tus cambios (`git commit -m 'Agregar nueva característica'`)
4. Push a la rama (`git push origin feature/NuevaCaracteristica`)
5. Abre un Pull Request

## 👨‍💻 Autor

**Gerardo**  
Universidad Politécnica del Estado de Morelos (UPEMOR)  
Materia: Estructuras de Datos

## 📄 Licencia

Este proyecto es de código abierto con fines educativos.

## 🙏 Agradecimientos

- Universidad Politécnica del Estado de Morelos (UPEMOR)
- Profesor de Estructuras de Datos
- Comunidad de Java y desarrolladores Open Source

---

**⭐ Si este proyecto te fue útil, dale una estrella en GitHub! ⭐**
