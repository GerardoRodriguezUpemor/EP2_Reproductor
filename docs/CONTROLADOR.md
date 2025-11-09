# 🎮 CAPA DE CONTROLADOR

## 📦 Estructura del Paquete `controller`

```
org.upemor.reproductor.controller/
├── Controller.java          → Controlador abstracto genérico
└── CancionController.java   → Controlador específico de canciones
```

---

## 🎯 Responsabilidades del Controlador

El controlador actúa como **intermediario** entre la Vista (UI) y el Modelo (datos):

```
┌─────────┐      ┌──────────────┐      ┌──────────┐
│  VISTA  │ ←──→ │ CONTROLADOR  │ ←──→ │  MODELO  │
│  (UI)   │      │ (Lógica)     │      │   (BD)   │
└─────────┘      └──────────────┘      └──────────┘
```

### **Funciones:**
1. ✅ **Validar datos** antes de enviarlos al modelo
2. ✅ **Coordinar operaciones** entre vista y modelo
3. ✅ **Manejar errores** y excepciones
4. ✅ **Transformar datos** según sea necesario
5. ✅ **Logging** de operaciones

---

## 🏗️ Controller.java - Controlador Genérico Abstracto

**Ubicación:** `org.upemor.reproductor.controller.Controller`

### **Código Completo:**

```java
package org.upemor.reproductor.controller;

import org.upemor.reproductor.model.repository.Repository;
import org.upemor.reproductor.estructuras.MiLista;

/**
 * Controlador genérico abstracto
 * @param <T> Tipo de entidad que maneja el controlador
 * @author Sistema Reproductor
 */
public abstract class Controller<T> {
    protected Repository<T> repository;
    
    /**
     * Busca entidades según un filtro
     * @param filtro Texto de búsqueda
     * @return Lista de entidades encontradas
     */
    public abstract MiLista<T> buscar(String filtro);
    
    /**
     * Obtiene una entidad por su ID
     * @param id Identificador único
     * @return Entidad encontrada o null
     */
    public abstract T obtenerPorId(Long id);
    
    /**
     * Guarda una nueva entidad
     * @param entidad Entidad a guardar
     * @return true si se guardó correctamente
     */
    public abstract boolean guardar(T entidad);
    
    /**
     * Actualiza una entidad existente
     * @param entidad Entidad con datos actualizados
     * @return true si se actualizó correctamente
     */
    public abstract boolean actualizar(T entidad);
    
    /**
     * Elimina una entidad por su ID
     * @param id Identificador de la entidad
     * @return true si se eliminó correctamente
     */
    public abstract boolean eliminar(Long id);
}
```

### **Características:**

#### **Genérico `<T>`:**
```java
public abstract class Controller<T>
```
- Permite reutilizar la misma estructura para cualquier entidad
- `T` puede ser `Cancion`, `Artista`, `Album`, etc.

#### **Métodos Abstractos:**
Definen el contrato que deben implementar las clases hijas:
- `buscar(String filtro)`
- `obtenerPorId(Long id)`
- `guardar(T entidad)`
- `actualizar(T entidad)`
- `eliminar(Long id)`

#### **Campo Protegido:**
```java
protected Repository<T> repository;
```
- Accesible por clases hijas
- Permite comunicación con la capa de datos

---

## 🎵 CancionController.java - Controlador de Canciones

**Ubicación:** `org.upemor.reproductor.controller.CancionController`

### **Código Completo:**

```java
package org.upemor.reproductor.controller;

import org.upemor.reproductor.model.entity.Cancion;
import org.upemor.reproductor.model.repository.CancionRepository;
import org.upemor.reproductor.estructuras.MiLista;

/**
 * Controlador para gestionar canciones
 * Implementa validaciones y lógica de negocio
 * @author Sistema Reproductor
 */
public class CancionController extends Controller<Cancion> {
    
    public CancionController() {
        this.repository = new CancionRepository();
        System.out.println("✅ CancionController inicializado");
    }
    
    @Override
    public MiLista<Cancion> buscar(String filtro) {
        try {
            System.out.println("🔍 Buscando canciones con filtro: " + 
                (filtro == null || filtro.isEmpty() ? "(todos)" : filtro));
            
            MiLista<Cancion> resultado = repository.buscar(filtro);
            
            System.out.println("✅ Encontradas " + resultado.tamanio() + " canciones");
            return resultado;
            
        } catch (Exception e) {
            System.err.println("❌ Error en búsqueda: " + e.getMessage());
            e.printStackTrace();
            return new MiLista<>(); // Retorna lista vacía en caso de error
        }
    }
    
    @Override
    public Cancion obtenerPorId(Long id) {
        try {
            if (id == null || id <= 0) {
                System.err.println("⚠️ ID inválido: " + id);
                return null;
            }
            
            System.out.println("🔍 Obteniendo canción con ID: " + id);
            Cancion cancion = repository.obtenerPorId(id);
            
            if (cancion != null) {
                System.out.println("✅ Canción encontrada: " + cancion.getTitulo());
            } else {
                System.out.println("⚠️ Canción no encontrada");
            }
            
            return cancion;
            
        } catch (Exception e) {
            System.err.println("❌ Error al obtener canción: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public boolean guardar(Cancion cancion) {
        try {
            // Validaciones
            if (cancion == null) {
                System.err.println("❌ Canción nula, no se puede guardar");
                return false;
            }
            
            if (!validarCancion(cancion)) {
                return false;
            }
            
            System.out.println("💾 Guardando canción: " + cancion.getTitulo());
            boolean guardada = repository.guardar(cancion);
            
            if (guardada) {
                System.out.println("✅ Canción guardada exitosamente (ID: " + cancion.getId() + ")");
            } else {
                System.err.println("❌ No se pudo guardar la canción");
            }
            
            return guardada;
            
        } catch (Exception e) {
            System.err.println("❌ Error al guardar canción: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean actualizar(Cancion cancion) {
        try {
            // Validaciones
            if (cancion == null) {
                System.err.println("❌ Canción nula, no se puede actualizar");
                return false;
            }
            
            if (cancion.getId() == null || cancion.getId() <= 0) {
                System.err.println("❌ ID inválido, no se puede actualizar");
                return false;
            }
            
            if (!validarCancion(cancion)) {
                return false;
            }
            
            System.out.println("✏️ Actualizando canción: " + cancion.getTitulo());
            boolean actualizada = repository.actualizar(cancion);
            
            if (actualizada) {
                System.out.println("✅ Canción actualizada exitosamente");
            } else {
                System.err.println("❌ No se pudo actualizar la canción");
            }
            
            return actualizada;
            
        } catch (Exception e) {
            System.err.println("❌ Error al actualizar canción: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean eliminar(Long id) {
        try {
            if (id == null || id <= 0) {
                System.err.println("❌ ID inválido: " + id);
                return false;
            }
            
            System.out.println("🗑️ Eliminando canción con ID: " + id);
            boolean eliminada = repository.eliminar(id);
            
            if (eliminada) {
                System.out.println("✅ Canción eliminada exitosamente");
            } else {
                System.err.println("❌ No se pudo eliminar la canción");
            }
            
            return eliminada;
            
        } catch (Exception e) {
            System.err.println("❌ Error al eliminar canción: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Valida que una canción tenga todos los campos requeridos
     * @param cancion Canción a validar
     * @return true si es válida
     */
    private boolean validarCancion(Cancion cancion) {
        // Validar título
        if (cancion.getTitulo() == null || cancion.getTitulo().trim().isEmpty()) {
            System.err.println("❌ Validación fallida: Título vacío");
            return false;
        }
        
        if (cancion.getTitulo().length() > 200) {
            System.err.println("❌ Validación fallida: Título muy largo (máx. 200 caracteres)");
            return false;
        }
        
        // Validar artista
        if (cancion.getArtista() == null || cancion.getArtista().trim().isEmpty()) {
            System.err.println("❌ Validación fallida: Artista vacío");
            return false;
        }
        
        if (cancion.getArtista().length() > 100) {
            System.err.println("❌ Validación fallida: Artista muy largo (máx. 100 caracteres)");
            return false;
        }
        
        // Validar duración
        if (cancion.getDuracion() == null || cancion.getDuracion() <= 0) {
            System.err.println("❌ Validación fallida: Duración inválida");
            return false;
        }
        
        if (cancion.getDuracion() > 7200) { // Máximo 2 horas
            System.err.println("❌ Validación fallida: Duración muy larga (máx. 2 horas)");
            return false;
        }
        
        // Validar ruta de archivo
        if (cancion.getRutaArchivo() == null || cancion.getRutaArchivo().trim().isEmpty()) {
            System.err.println("❌ Validación fallida: Ruta de archivo vacía");
            return false;
        }
        
        // Validar extensión .mp3
        if (!cancion.getRutaArchivo().toLowerCase().endsWith(".mp3")) {
            System.err.println("❌ Validación fallida: El archivo debe ser .mp3");
            return false;
        }
        
        // Validar que el archivo exista
        java.io.File archivo = new java.io.File(cancion.getRutaArchivo());
        if (!archivo.exists()) {
            System.err.println("❌ Validación fallida: El archivo no existe: " + cancion.getRutaArchivo());
            return false;
        }
        
        System.out.println("✅ Canción validada correctamente");
        return true;
    }
}
```

---

## 🔍 Validaciones Implementadas

### **1. Validación de Título**
```java
✅ No vacío
✅ Máximo 200 caracteres
❌ null o string vacío rechazado
```

### **2. Validación de Artista**
```java
✅ No vacío
✅ Máximo 100 caracteres
❌ null o string vacío rechazado
```

### **3. Validación de Duración**
```java
✅ Mayor a 0
✅ Máximo 7200 segundos (2 horas)
❌ null o valor <= 0 rechazado
```

### **4. Validación de Ruta de Archivo**
```java
✅ No vacío
✅ Extensión .mp3
✅ Archivo debe existir en el sistema
❌ Ruta inválida rechazada
```

### **5. Validación de ID (para actualizar/eliminar)**
```java
✅ No null
✅ Mayor a 0
❌ ID inválido rechazado
```

---

## 📊 Flujo de Operaciones

### **Operación: Guardar Canción**

```
[Vista] Usuario llena formulario
          ↓
[Vista] Crea objeto Cancion
          ↓
[Vista] Llama controller.guardar(cancion)
          ↓
[Controller] Validación: cancion != null
          ↓
[Controller] validarCancion(cancion)
          ├→ Valida título
          ├→ Valida artista
          ├→ Valida duración
          └→ Valida archivo
          ↓
[Controller] ✅ Todas las validaciones OK
          ↓
[Controller] repository.guardar(cancion)
          ↓
[Repository] Ejecuta INSERT SQL
          ↓
[Repository] Obtiene ID generado
          ↓
[Repository] Retorna true
          ↓
[Controller] Log: "✅ Canción guardada"
          ↓
[Controller] Retorna true
          ↓
[Vista] Muestra mensaje de éxito
```

### **Operación: Buscar Canciones**

```
[Vista] Usuario escribe "Queen"
          ↓
[Vista] Llama controller.buscar("Queen")
          ↓
[Controller] Log: "🔍 Buscando: Queen"
          ↓
[Controller] repository.buscar("Queen")
          ↓
[Repository] Ejecuta SELECT con LIKE
          ↓
[Repository] Retorna MiLista<Cancion>
          ↓
[Controller] Log: "✅ Encontradas 5 canciones"
          ↓
[Controller] Retorna lista
          ↓
[Vista] Actualiza tabla con resultados
```

---

## 🎯 Manejo de Errores

### **Estrategia de Error Handling:**

#### **1. Try-Catch en todos los métodos**
```java
try {
    // Operación
    return resultado;
} catch (Exception e) {
    System.err.println("❌ Error: " + e.getMessage());
    e.printStackTrace();
    return valorPorDefecto; // null, false, lista vacía
}
```

#### **2. Validaciones Previas**
```java
if (cancion == null) {
    System.err.println("❌ Canción nula");
    return false; // Retorno inmediato
}
```

#### **3. Logging Detallado**
```java
System.out.println("🔍 Buscando...");  // Info
System.err.println("❌ Error...");     // Error
System.out.println("✅ Éxito...");     // Éxito
```

#### **4. Retornos Seguros**
```java
// En caso de error, retornar:
- boolean → false
- Objeto → null
- Lista → new MiLista<>() (vacía, no null)
```

---

## 💡 Ejemplo de Uso Completo

### **Desde la Vista:**

```java
// En BibliotecaDlg.java o CancionModalDlg.java

// 1. Crear controlador
CancionController controller = new CancionController();

// 2. Buscar todas las canciones
MiLista<Cancion> todas = controller.buscar(null);

// 3. Buscar con filtro
MiLista<Cancion> resultados = controller.buscar("Queen");

// 4. Obtener canción específica
Cancion cancion = controller.obtenerPorId(5L);

// 5. Crear nueva canción
Cancion nueva = new Cancion(
    "Imagine",
    "John Lennon",
    "Imagine",
    183,
    "C:/music/imagine.mp3"
);

boolean guardada = controller.guardar(nueva);
if (guardada) {
    JOptionPane.showMessageDialog(null, "Canción guardada");
} else {
    JOptionPane.showMessageDialog(null, "Error al guardar");
}

// 6. Actualizar canción
cancion.setTitulo("Imagine (Remastered)");
boolean actualizada = controller.actualizar(cancion);

// 7. Eliminar canción
boolean eliminada = controller.eliminar(5L);
```

---

## 🔐 Principios SOLID en el Controlador

### **S - Single Responsibility**
- Controlador solo coordina entre Vista y Modelo
- No maneja UI directamente
- No maneja SQL directamente

### **O - Open/Closed**
- `Controller<T>` es extensible sin modificar código base
- Puedo crear `ArtistController`, `AlbumController`, etc.

### **L - Liskov Substitution**
```java
Controller<Cancion> controller = new CancionController();
// Funciona correctamente como Controller<Cancion>
```

### **D - Dependency Inversion**
```java
protected Repository<T> repository; // Depende de abstracción
// No depende de CancionRepository directamente
```

---

## 🎓 Ventajas de esta Arquitectura de Controlador

✅ **Validación Centralizada** - Todas las validaciones en un solo lugar  
✅ **Reutilización** - Controlador genérico para nuevas entidades  
✅ **Manejo Consistente de Errores** - Misma estrategia en todos los métodos  
✅ **Logging Uniforme** - Fácil debug y seguimiento  
✅ **Separación de Concerns** - Vista no valida, Modelo no valida  
✅ **Testeable** - Fácil crear tests unitarios  
✅ **Mantenible** - Cambios en validaciones solo afectan al controlador

---

## 📝 Convenciones de Logging

```java
🔍 = Búsqueda/Consulta
💾 = Guardar
✏️ = Actualizar
🗑️ = Eliminar
✅ = Éxito
❌ = Error
⚠️ = Advertencia
```

---

## 🚀 Extensibilidad

Para agregar un nuevo tipo de entidad (ej: `Artista`):

```java
// 1. Crear entidad
public class Artista extends Entity {
    private String nombre;
    private String pais;
}

// 2. Crear repositorio
public class ArtistaRepository implements Repository<Artista> {
    // Implementar métodos CRUD
}

// 3. Crear controlador
public class ArtistaController extends Controller<Artista> {
    public ArtistaController() {
        this.repository = new ArtistaRepository();
    }
    
    // Implementar métodos abstractos
    // Agregar validaciones específicas
}

// ¡Listo! Sin modificar código existente
```
