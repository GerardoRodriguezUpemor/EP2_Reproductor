# 🎵 LÓGICA DEL REPRODUCTOR - Reproducción de MP3

## 📦 Estructura del Paquete `logica`

```
org.upemor.reproductor.logica/
├── Reproductor.java          → Gestor de reproducción (cola, historial)
└── ReproductorAudio.java     → Reproducción real de MP3 con JavaFX
```

---

## 🎮 Reproductor.java - Gestor de Reproducción

**Ubicación:** `org.upemor.reproductor.logica.Reproductor`

### **Responsabilidades:**
- Gestionar cola de reproducción (MiCola)
- Mantener historial de reproducción (MiPila)
- Controlar canción actual
- Coordinar con ReproductorAudio
- Notificar cambios a la UI

### **Estructura de Datos:**
```java
public class Reproductor implements ReproductorAudio.ReproductorListener {
    private MiCola<Cancion> colaReproduccion;  // Cola FIFO
    private MiPila<Cancion> historial;         // Pila LIFO
    private Cancion cancionActual;             // Canción reproduciéndose
    private boolean reproduciendo;             // Estado de reproducción
    private ReproductorAudio reproductorAudio; // Motor de audio
    private ReproductorListener listener;      // Observer para UI
}
```

### **Diagrama de Estados:**
```
   Vacío
     ↓
  Agregar a Cola
     ↓
  Reproducir Siguiente
     ↓
┌──────────────┐
│ Reproduciendo│ ←→ Pausa/Resume
└──────────────┘
     ↓
  Siguiente/Termina
     ↓
  Apilar en Historial
     ↓
  Reproducir Siguiente
```

---

### **Métodos Principales:**

#### **1. agregarACola(Cancion cancion)**
Agrega una canción al final de la cola.

```java
public void agregarACola(Cancion cancion) {
    colaReproduccion.encolar(cancion);
    System.out.println("✓ Canción agregada a la cola: " + cancion.getTitulo());
}
```

**Uso:**
```java
reproductor.agregarACola(cancion1); // Bohemian Rhapsody
reproductor.agregarACola(cancion2); // Stairway to Heaven
reproductor.agregarACola(cancion3); // Hotel California

// Cola: [Bohemian → Stairway → Hotel]
```

---

#### **2. reproducirSiguiente()**
Reproduce la siguiente canción de la cola.

```java
public Cancion reproducirSiguiente() {
    // Si hay canción actual, agregarla al historial
    if (cancionActual != null) {
        historial.apilar(cancionActual);
    }
    
    // Obtener siguiente canción de la cola
    cancionActual = colaReproduccion.desencolar();
    
    if (cancionActual != null) {
        reproduciendo = true;
        reproductorAudio.reproducir(cancionActual);
        System.out.println("▶️ Reproduciendo: " + cancionActual.getTitulo());
        notificarCancionCambiada(cancionActual);
    } else {
        reproduciendo = false;
        reproductorAudio.detener();
        System.out.println("⏹️ No hay más canciones en la cola");
    }
    
    return cancionActual;
}
```

**Flujo:**
```
Cola: [A, B, C]
Historial: []

reproducirSiguiente()
  → Desencola A
  → Reproduce A
  → cancionActual = A

Cola: [B, C]
Historial: []

reproducirSiguiente()
  → Apila A en historial
  → Desencola B
  → Reproduce B
  → cancionActual = B

Cola: [C]
Historial: [A]
```

---

#### **3. reproducirAnterior()**
Reproduce la canción anterior del historial.

```java
public Cancion reproducirAnterior() {
    Cancion anterior = historial.desapilar();
    
    if (anterior != null) {
        // Si hay canción actual, regresarla a la cola
        if (cancionActual != null) {
            MiCola<Cancion> nuevaCola = new MiCola<>();
            nuevaCola.encolar(cancionActual);
            
            while (!colaReproduccion.estaVacia()) {
                nuevaCola.encolar(colaReproduccion.desencolar());
            }
            
            colaReproduccion = nuevaCola;
        }
        
        cancionActual = anterior;
        reproduciendo = true;
        reproductorAudio.reproducir(cancionActual);
        
        System.out.println("⏮️ Reproduciendo anterior: " + cancionActual.getTitulo());
        notificarCancionCambiada(cancionActual);
    } else {
        System.out.println("⏹️ No hay canciones en el historial");
    }
    
    return cancionActual;
}
```

**Flujo:**
```
cancionActual = B
Cola: [C, D]
Historial: [A]

reproducirAnterior()
  → Desapila A del historial
  → Encola B de vuelta a la cola
  → Reproduce A
  → cancionActual = A

cancionActual = A
Cola: [B, C, D]
Historial: []
```

---

#### **4. togglePausa()**
Alterna entre pausar y reanudar la reproducción.

```java
public void togglePausa() {
    if (cancionActual != null) {
        if (reproductorAudio.isReproduciendo()) {
            reproductorAudio.pausar();
            reproduciendo = false;
            System.out.println("⏸️ Pausado");
        } else {
            reproductorAudio.reanudar();
            reproduciendo = true;
            System.out.println("▶️ Reproduciendo");
        }
        notificarEstadoCambiado(reproduciendo);
    }
}
```

**Estados:**
```
Reproduciendo → togglePausa() → Pausado
Pausado → togglePausa() → Reproduciendo
```

---

#### **5. detener()**
Detiene la reproducción y agrega la canción al historial.

```java
public void detener() {
    // Si hay canción actual, agregarla al historial antes de detener
    if (cancionActual != null) {
        historial.apilar(cancionActual);
        System.out.println("📜 Canción agregada al historial: " + cancionActual.getTitulo());
    }
    
    reproduciendo = false;
    reproductorAudio.detener();
    cancionActual = null;
    System.out.println("⏹️ Reproducción detenida");
    notificarEstadoCambiado(false);
}
```

**Nota Importante:** 
- ✅ **Canción detenida SE agrega al historial**
- ✅ **No importa si terminó o no**
- ✅ **Todas las canciones reproducidas quedan registradas**

---

#### **6. limpiarCola() / limpiarHistorial()**
```java
public void limpiarCola() {
    colaReproduccion.limpiar();
    System.out.println("🗑️ Cola de reproducción limpiada");
}

public void limpiarHistorial() {
    historial.limpiar();
    System.out.println("🗑️ Historial limpiado");
}
```

---

### **Interfaz ReproductorListener:**

```java
public interface ReproductorListener {
    void onCancionCambiada(Cancion cancion);
    void onEstadoCambiado(boolean reproduciendo);
}
```

**Uso en la UI:**
```java
reproductor.setListener(new Reproductor.ReproductorListener() {
    @Override
    public void onCancionCambiada(Cancion cancion) {
        SwingUtilities.invokeLater(() -> {
            lblTitulo.setText(cancion.getTitulo());
            lblArtista.setText(cancion.getArtista());
            actualizarCola();
        });
    }
    
    @Override
    public void onEstadoCambiado(boolean reproduciendo) {
        SwingUtilities.invokeLater(() -> {
            btnPlayPause.setText(reproduciendo ? "⏸️ Pausar" : "▶️ Reproducir");
        });
    }
});
```

---

## 🔊 ReproductorAudio.java - Motor de Reproducción MP3

**Ubicación:** `org.upemor.reproductor.logica.ReproductorAudio`

### **Responsabilidades:**
- Reproducir archivos MP3 reales
- Controlar MediaPlayer de JavaFX
- Pause/Resume funcional
- Notificar al terminar canción

### **Tecnología:**
- ✅ **JavaFX MediaPlayer** - Reproductor nativo
- ✅ **Soporte MP3** - Formato principal
- ✅ **Thread-safe** - Usa Platform.runLater()

### **Estructura:**
```java
public class ReproductorAudio {
    private MediaPlayer mediaPlayer;
    private ReproductorListener listener;
    private boolean inicializado = false;
    
    public interface ReproductorListener {
        void onCancionTerminada();
        void onError(String mensaje);
        void onEstadoCambiado(boolean reproduciendo);
    }
}
```

---

### **Métodos Principales:**

#### **1. inicializarJavaFX()**
Inicializa el toolkit de JavaFX (necesario para MediaPlayer).

```java
private void inicializarJavaFX() {
    if (!inicializado) {
        try {
            Platform.startup(() -> {
                System.out.println("✅ JavaFX inicializado");
            });
            inicializado = true;
        } catch (IllegalStateException e) {
            inicializado = true;
        }
    }
}
```

**Nota:** JavaFX debe inicializarse una sola vez al inicio.

---

#### **2. reproducir(Cancion cancion)**
Reproduce un archivo MP3.

```java
public void reproducir(Cancion cancion) {
    Platform.runLater(() -> {
        try {
            detener(); // Detener canción actual si existe
            
            File archivoMP3 = new File(cancion.getRutaArchivo());
            if (!archivoMP3.exists()) {
                notificarError("Archivo no encontrado: " + cancion.getRutaArchivo());
                return;
            }
            
            Media media = new Media(archivoMP3.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            
            // Listener al terminar
            mediaPlayer.setOnEndOfMedia(() -> {
                System.out.println("✅ Canción terminada");
                if (listener != null) {
                    listener.onCancionTerminada();
                }
            });
            
            // Listener de errores
            mediaPlayer.setOnError(() -> {
                MediaException error = mediaPlayer.getError();
                notificarError("Error: " + error.getMessage());
            });
            
            // Reproducir
            mediaPlayer.play();
            System.out.println("▶️ Reproduciendo: " + cancion.getTitulo());
            
            if (listener != null) {
                listener.onEstadoCambiado(true);
            }
            
        } catch (Exception e) {
            notificarError("Error al reproducir: " + e.getMessage());
        }
    });
}
```

**Flujo:**
```
[Usuario] → Reproduce canción
     ↓
[ReproductorAudio] → Crea Media(archivo.mp3)
     ↓
[MediaPlayer] → Carga archivo
     ↓
[MediaPlayer.play()] → Inicia reproducción
     ↓
[onEndOfMedia] → Callback al terminar
     ↓
[listener.onCancionTerminada()] → Notifica
     ↓
[Reproductor] → reproducirSiguiente()
```

---

#### **3. pausar() / reanudar()**
Pausa y reanuda la reproducción.

```java
public void pausar() {
    if (mediaPlayer != null) {
        Platform.runLater(() -> {
            mediaPlayer.pause();
            System.out.println("⏸️ Pausado");
            if (listener != null) {
                listener.onEstadoCambiado(false);
            }
        });
    }
}

public void reanudar() {
    if (mediaPlayer != null) {
        Platform.runLater(() -> {
            mediaPlayer.play();
            System.out.println("▶️ Reproduciendo");
            if (listener != null) {
                listener.onEstadoCambiado(true);
            }
        });
    }
}
```

**Ventaja sobre JLayer:**
- ✅ JLayer no soporta pause/resume nativo
- ✅ MediaPlayer sí tiene pause/resume funcional
- ✅ No necesita reiniciar la canción

---

#### **4. detener()**
Detiene y libera recursos del MediaPlayer.

```java
public void detener() {
    if (mediaPlayer != null) {
        Platform.runLater(() -> {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
            System.out.println("⏹️ Reproducción detenida");
            if (listener != null) {
                listener.onEstadoCambiado(false);
            }
        });
    }
}
```

---

#### **5. isReproduciendo()**
Verifica si hay reproducción activa.

```java
public boolean isReproduciendo() {
    if (mediaPlayer != null) {
        return mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }
    return false;
}
```

---

## 🔄 Flujo Completo de Reproducción

### **Escenario 1: Reproducir Canción**

```
1. Usuario hace clic en "▶ Reproducir Todo" en Biblioteca
      ↓
2. BibliotecaDlg.reproducirTodasLasCanciones()
      ↓
3. reproductor.limpiarCola()
      ↓
4. for (cada canción): reproductor.agregarACola(cancion)
      ↓
   Cola: [A, B, C, D, E]
      ↓
5. reproductor.reproducirSiguiente()
      ↓
6. Desencola A → cancionActual = A
      ↓
7. reproductorAudio.reproducir(A)
      ↓
8. JavaFX MediaPlayer carga y reproduce A.mp3
      ↓
9. notificarCancionCambiada(A)
      ↓
10. ReproductorDlg actualiza UI:
    - Título: Bohemian Rhapsody
    - Artista: Queen
    - Tabla de cola: [B, C, D, E]
      ↓
11. Usuario escucha la canción...
      ↓
12. Canción termina → onEndOfMedia()
      ↓
13. listener.onCancionTerminada()
      ↓
14. reproductor.reproducirSiguiente()
      ↓
15. Apila A en historial
      ↓
16. Desencola B → cancionActual = B
      ↓
17. REPITE el proceso...
```

---

### **Escenario 2: Botón Anterior**

```
Estado actual:
  cancionActual = C
  Cola: [D, E]
  Historial: [A, B]

1. Usuario hace clic en "⏮️ Anterior"
      ↓
2. reproductor.reproducirAnterior()
      ↓
3. Desapila B del historial
      ↓
4. Encola C de vuelta: nuevaCola = [C, D, E]
      ↓
5. reproductorAudio.reproducir(B)
      ↓
6. cancionActual = B
      ↓
7. UI actualiza:
    - Título: Stairway to Heaven
    - Cola: [C, D, E]
    - Historial: [A]
```

---

### **Escenario 3: Botón Detener**

```
Estado actual:
  cancionActual = B (reproduciendo)
  Cola: [C, D, E]
  Historial: [A]

1. Usuario hace clic en "⏹️ Detener"
      ↓
2. reproductor.detener()
      ↓
3. Apila B en historial
      ↓
4. reproductorAudio.detener()
      ↓
5. mediaPlayer.stop() y dispose()
      ↓
6. cancionActual = null
      ↓
7. reproduciendo = false
      ↓
Estado final:
  cancionActual = null
  Cola: [C, D, E]
  Historial: [A, B]  ← B agregada aunque no terminó
```

---

## 🎯 Callbacks y Eventos

### **Eventos de ReproductorAudio:**

```java
mediaPlayer.setOnEndOfMedia(() -> {
    // Canción terminó normalmente
    listener.onCancionTerminada();
});

mediaPlayer.setOnError(() -> {
    // Error en reproducción
    MediaException error = mediaPlayer.getError();
    listener.onError(error.getMessage());
});
```

### **Eventos de Reproductor:**

```java
@Override
public void onCancionTerminada() {
    System.out.println("✅ Canción terminada, reproduciendo siguiente...");
    reproducirSiguiente();
}

@Override
public void onError(String mensaje) {
    System.err.println("❌ Error en reproductor de audio: " + mensaje);
}

@Override
public void onEstadoCambiado(boolean reproduciendo) {
    this.reproduciendo = reproduciendo;
    notificarEstadoCambiado(reproduciendo);
}
```

---

## 🧪 Testing del Reproductor

### **Pruebas de Cola:**
```java
Reproductor reproductor = new Reproductor();

// Agregar 3 canciones
reproductor.agregarACola(cancion1);
reproductor.agregarACola(cancion2);
reproductor.agregarACola(cancion3);

// Verificar tamaño
assert reproductor.getTamanoCola() == 3;

// Reproducir siguiente
Cancion actual = reproductor.reproducirSiguiente();
assert actual == cancion1;
assert reproductor.getTamanoCola() == 2;
assert reproductor.getTamanoHistorial() == 0;

// Reproducir siguiente de nuevo
actual = reproductor.reproducirSiguiente();
assert actual == cancion2;
assert reproductor.getTamanoCola() == 1;
assert reproductor.getTamanoHistorial() == 1; // cancion1 en historial
```

### **Pruebas de Historial:**
```java
// Reproducir anterior
Cancion anterior = reproductor.reproducirAnterior();
assert anterior == cancion1;
assert reproductor.getTamanoCola() == 2; // cancion2 regresó a cola
assert reproductor.getTamanoHistorial() == 0;
```

---

## 💡 Ventajas de esta Arquitectura

✅ **Separación de Concerns:**
- `Reproductor` maneja lógica de cola/historial
- `ReproductorAudio` maneja solo reproducción MP3

✅ **Event-Driven:**
- Callbacks para actualización automática de UI
- No polling, eventos en tiempo real

✅ **Thread-Safe:**
- JavaFX Platform.runLater() para operaciones de audio
- SwingUtilities.invokeLater() para operaciones de UI

✅ **Estructuras Manuales:**
- MiCola y MiPila implementadas desde cero
- No dependencia de Java Collections

✅ **Auto-Reproducción:**
- Canción siguiente se reproduce automáticamente
- Experiencia de usuario fluida

✅ **Historial Completo:**
- Incluye canciones detenidas (no solo completadas)
- Historial real de lo que se escuchó
