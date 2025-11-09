# 📊 ESTRUCTURAS DE DATOS - Implementación Manual

## ⚠️ REGLA FUNDAMENTAL

**NO SE UTILIZAN COLECCIONES DE JAVA** (`ArrayList`, `LinkedList`, `Stack`, `Queue`, etc.)

Todas las estructuras fueron implementadas **manualmente desde cero**.

---

## 🧱 1. NODO GENÉRICO (Nodo.java)

### **Ubicación:** `org.upemor.reproductor.estructuras.Nodo`

### **Descripción:**
Componente básico de todas las estructuras enlazadas. Es un **nodo doblemente enlazado genérico**.

### **Código:**
```java
package org.upemor.reproductor.estructuras;

public class Nodo<T> {
    private T dato;
    private Nodo<T> siguiente;
    private Nodo<T> anterior;
    
    public Nodo(T dato) {
        this.dato = dato;
        this.siguiente = null;
        this.anterior = null;
    }
    
    // Getters y Setters
    public T getDato() { return dato; }
    public void setDato(T dato) { this.dato = dato; }
    
    public Nodo<T> getSiguiente() { return siguiente; }
    public void setSiguiente(Nodo<T> siguiente) { this.siguiente = siguiente; }
    
    public Nodo<T> getAnterior() { return anterior; }
    public void setAnterior(Nodo<T> anterior) { this.anterior = anterior; }
}
```

### **Características:**
- ✅ **Genérico:** `<T>` permite almacenar cualquier tipo
- ✅ **Doblemente enlazado:** Tiene `siguiente` y `anterior`
- ✅ **Simple:** Solo almacena datos y referencias

### **Diagrama:**
```
┌─────────────────────┐
│      Nodo<T>        │
├─────────────────────┤
│ - dato: T           │
│ - siguiente: Nodo   │
│ - anterior: Nodo    │
└─────────────────────┘
```

---

## 🔗 2. LISTA CIRCULAR DOBLEMENTE ENLAZADA (MiLista.java)

### **Ubicación:** `org.upemor.reproductor.estructuras.MiLista`

### **Descripción:**
Lista **circular** y **doblemente enlazada** que permite navegación bidireccional eficiente.

### **Características:**
- ✅ **Circular:** El último nodo apunta al primero
- ✅ **Doblemente enlazada:** Se puede recorrer en ambas direcciones
- ✅ **Genérica:** `MiLista<T>`
- ✅ **Tamaño dinámico:** Crece según se agregan elementos

### **Estructura:**
```java
public class MiLista<T> {
    private Nodo<T> cabeza;  // Primer nodo
    private Nodo<T> cola;    // Último nodo
    private int tamanio;     // Contador de elementos
}
```

### **Métodos Principales:**

#### **agregar(T dato)**
Agrega un elemento al final de la lista.

```java
public void agregar(T dato) {
    Nodo<T> nuevoNodo = new Nodo<>(dato);
    
    if (cabeza == null) {
        // Lista vacía - primer elemento
        cabeza = nuevoNodo;
        cola = nuevoNodo;
        cabeza.setSiguiente(cabeza);
        cabeza.setAnterior(cabeza);
    } else {
        // Agregar al final y mantener circularidad
        nuevoNodo.setAnterior(cola);
        nuevoNodo.setSiguiente(cabeza);
        cola.setSiguiente(nuevoNodo);
        cabeza.setAnterior(nuevoNodo);
        cola = nuevoNodo;
    }
    tamanio++;
}
```

#### **obtener(int indice)**
Obtiene el elemento en la posición indicada.

```java
public T obtener(int indice) {
    if (indice < 0 || indice >= tamanio) {
        throw new IndexOutOfBoundsException("Índice fuera de rango");
    }
    
    Nodo<T> actual = cabeza;
    for (int i = 0; i < indice; i++) {
        actual = actual.getSiguiente();
    }
    return actual.getDato();
}
```

#### **eliminar(int indice)**
Elimina el elemento en la posición indicada.

```java
public boolean eliminar(int indice) {
    if (indice < 0 || indice >= tamanio) {
        return false;
    }
    
    if (tamanio == 1) {
        // Único elemento
        cabeza = null;
        cola = null;
    } else if (indice == 0) {
        // Eliminar cabeza
        cabeza = cabeza.getSiguiente();
        cabeza.setAnterior(cola);
        cola.setSiguiente(cabeza);
    } else if (indice == tamanio - 1) {
        // Eliminar cola
        cola = cola.getAnterior();
        cola.setSiguiente(cabeza);
        cabeza.setAnterior(cola);
    } else {
        // Eliminar en medio
        Nodo<T> actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.getSiguiente();
        }
        actual.getAnterior().setSiguiente(actual.getSiguiente());
        actual.getSiguiente().setAnterior(actual.getAnterior());
    }
    
    tamanio--;
    return true;
}
```

#### **buscar(T dato)**
Busca un elemento en la lista.

```java
public int buscar(T dato) {
    if (cabeza == null) return -1;
    
    Nodo<T> actual = cabeza;
    int indice = 0;
    
    do {
        if (actual.getDato().equals(dato)) {
            return indice;
        }
        actual = actual.getSiguiente();
        indice++;
    } while (actual != cabeza && indice < tamanio);
    
    return -1;
}
```

#### **recorrer(AccionNodo<T> accion)**
Ejecuta una acción sobre cada elemento.

```java
public void recorrer(AccionNodo<T> accion) {
    if (cabeza == null) return;
    
    Nodo<T> actual = cabeza;
    int indice = 0;
    
    do {
        accion.ejecutar(actual.getDato(), indice);
        actual = actual.getSiguiente();
        indice++;
    } while (actual != cabeza && indice < tamanio);
}
```

### **Interfaz Funcional:**
```java
@FunctionalInterface
public interface AccionNodo<T> {
    void ejecutar(T dato, int indice);
}
```

### **Diagrama Circular:**
```
        cabeza
          ↓
    ┌─→ [A] ─→ [B] ─→ [C] ─┐
    │    ↑              ↓    │
    │    └──────← ←─────┘    │
    └────────────────────────┘
                cola
```

### **Uso en el Sistema:**
- ✅ Almacenar resultados de búsqueda de canciones
- ✅ Base para convertir cola/pila a lista
- ✅ Iteración sobre elementos

---

## 📚 3. PILA (LIFO) - MiPila.java

### **Ubicación:** `org.upemor.reproductor.estructuras.MiPila`

### **Descripción:**
Estructura **LIFO (Last In, First Out)** - El último en entrar es el primero en salir.

### **Uso en el Sistema:**
✅ **Historial de reproducción** - Las canciones más recientes se acceden primero

### **Estructura:**
```java
public class MiPila<T> {
    private MiLista<T> elementos;
    
    public MiPila() {
        this.elementos = new MiLista<>();
    }
}
```

### **Métodos Principales:**

#### **apilar(T elemento)**
Agrega un elemento al tope de la pila.

```java
public void apilar(T elemento) {
    elementos.agregar(elemento);
    System.out.println("📚 Apilado: " + elemento);
}
```

#### **desapilar()**
Remueve y retorna el elemento del tope.

```java
public T desapilar() {
    if (estaVacia()) {
        System.out.println("⚠️ Pila vacía, no se puede desapilar");
        return null;
    }
    
    T elemento = elementos.obtener(elementos.tamanio() - 1);
    elementos.eliminar(elementos.tamanio() - 1);
    System.out.println("📤 Desapilado: " + elemento);
    return elemento;
}
```

#### **verTope()**
Retorna el elemento del tope sin removerlo.

```java
public T verTope() {
    if (estaVacia()) {
        return null;
    }
    return elementos.obtener(elementos.tamanio() - 1);
}
```

#### **estaVacia()**
Verifica si la pila está vacía.

```java
public boolean estaVacia() {
    return elementos.tamanio() == 0;
}
```

#### **limpiar()**
Elimina todos los elementos.

```java
public void limpiar() {
    elementos = new MiLista<>();
    System.out.println("🗑️ Pila limpiada");
}
```

#### **aLista()**
Convierte la pila a lista para visualización.

```java
public MiLista<T> aLista() {
    return elementos;
}
```

### **Diagrama LIFO:**
```
    ┌─────────┐
    │   [C]   │ ← Tope (último en entrar, primero en salir)
    ├─────────┤
    │   [B]   │
    ├─────────┤
    │   [A]   │ ← Base (primero en entrar)
    └─────────┘
    
apilar(D) → [D] se pone arriba de [C]
desapilar() → retorna [C] y lo elimina
```

### **Ejemplo de Uso - Historial:**
```java
MiPila<Cancion> historial = new MiPila<>();

historial.apilar(cancion1); // "Bohemian Rhapsody"
historial.apilar(cancion2); // "Stairway to Heaven"
historial.apilar(cancion3); // "Hotel California"

// Reproducir anterior (desapilar):
Cancion anterior = historial.desapilar(); 
// Retorna "Hotel California" (la más reciente)
```

---

## 🔄 4. COLA (FIFO) - MiCola.java

### **Ubicación:** `org.upemor.reproductor.estructuras.MiCola`

### **Descripción:**
Estructura **FIFO (First In, First Out)** - El primero en entrar es el primero en salir.

### **Uso en el Sistema:**
✅ **Cola de reproducción** - Las canciones se reproducen en el orden que fueron agregadas

### **Estructura:**
```java
public class MiCola<T> {
    private MiLista<T> elementos;
    
    public MiCola() {
        this.elementos = new MiLista<>();
    }
}
```

### **Métodos Principales:**

#### **encolar(T elemento)**
Agrega un elemento al final de la cola.

```java
public void encolar(T elemento) {
    elementos.agregar(elemento);
    System.out.println("➕ Encolado: " + elemento);
}
```

#### **desencolar()**
Remueve y retorna el primer elemento de la cola.

```java
public T desencolar() {
    if (estaVacia()) {
        System.out.println("⚠️ Cola vacía, no se puede desencolar");
        return null;
    }
    
    T elemento = elementos.obtener(0);
    elementos.eliminar(0);
    System.out.println("➡️ Desencolado: " + elemento);
    return elemento;
}
```

#### **verFrente()**
Retorna el primer elemento sin removerlo.

```java
public T verFrente() {
    if (estaVacia()) {
        return null;
    }
    return elementos.obtener(0);
}
```

#### **estaVacia()**
Verifica si la cola está vacía.

```java
public boolean estaVacia() {
    return elementos.tamanio() == 0;
}
```

#### **limpiar()**
Elimina todos los elementos.

```java
public void limpiar() {
    elementos = new MiLista<>();
    System.out.println("🗑️ Cola limpiada");
}
```

#### **aLista()**
Convierte la cola a lista para visualización.

```java
public MiLista<T> aLista() {
    return elementos;
}
```

### **Diagrama FIFO:**
```
Frente                              Final
  ↓                                   ↓
[A] → [B] → [C] → [D] → [E]

encolar(F) → se agrega al final: [A] → [B] → [C] → [D] → [E] → [F]
desencolar() → retorna [A] y queda: [B] → [C] → [D] → [E] → [F]
```

### **Ejemplo de Uso - Cola de Reproducción:**
```java
MiCola<Cancion> cola = new MiCola<>();

cola.encolar(cancion1); // "Bohemian Rhapsody"
cola.encolar(cancion2); // "Stairway to Heaven"
cola.encolar(cancion3); // "Hotel California"

// Reproducir siguiente (desencolar):
Cancion siguiente = cola.desencolar(); 
// Retorna "Bohemian Rhapsody" (la primera agregada)
```

---

## 📊 Comparación de Estructuras

| Característica | MiLista | MiPila | MiCola |
|----------------|---------|--------|--------|
| **Tipo** | Circular Doblemente Enlazada | LIFO | FIFO |
| **Acceso** | Por índice | Solo tope | Solo frente |
| **Inserción** | Cualquier posición | Solo tope | Solo final |
| **Eliminación** | Cualquier posición | Solo tope | Solo frente |
| **Complejidad Insertar** | O(1) al final | O(1) | O(1) |
| **Complejidad Eliminar** | O(n) | O(1) | O(1) |
| **Complejidad Buscar** | O(n) | O(n) | O(n) |
| **Navegación** | Bidireccional | Unidireccional | Unidireccional |
| **Uso en Sistema** | Resultados búsqueda | Historial | Cola reproducción |

---

## 🎯 Ventajas de Implementación Manual

### ✅ **Ventajas Educativas:**
1. **Comprensión profunda** de cómo funcionan las estructuras
2. **Control total** sobre comportamiento y memoria
3. **Personalización** según necesidades del proyecto
4. **Aprendizaje** de punteros y referencias

### ✅ **Ventajas Técnicas:**
1. **Sin dependencias** de librerías externas
2. **Tamaño reducido** del programa
3. **Debugging más simple** (código propio)
4. **Optimización específica** para el dominio

---

## 🔍 Complejidad Temporal (Big O)

### **MiLista:**
- `agregar(dato)` → **O(1)** (al final)
- `obtener(indice)` → **O(n)** (recorrido lineal)
- `eliminar(indice)` → **O(n)** (buscar + reorganizar)
- `buscar(dato)` → **O(n)** (recorrido completo)

### **MiPila:**
- `apilar(elemento)` → **O(1)**
- `desapilar()` → **O(1)**
- `verTope()` → **O(1)**

### **MiCola:**
- `encolar(elemento)` → **O(1)**
- `desencolar()` → **O(1)** (eliminación en cabeza)
- `verFrente()` → **O(1)**

---

## 💡 Casos de Uso en el Sistema

### **Escenario 1: Buscar Canciones**
```java
// CancionRepository devuelve MiLista<Cancion>
MiLista<Cancion> resultados = repository.buscar("Queen");

// Recorrer resultados
resultados.recorrer((cancion, indice) -> {
    System.out.println(indice + ": " + cancion.getTitulo());
});
```

### **Escenario 2: Cola de Reproducción**
```java
MiCola<Cancion> cola = new MiCola<>();

// Agregar 5 canciones
cola.encolar(cancion1);
cola.encolar(cancion2);
cola.encolar(cancion3);

// Reproducir en orden
while (!cola.estaVacia()) {
    Cancion actual = cola.desencolar();
    reproducir(actual);
}
```

### **Escenario 3: Historial (Botón Anterior)**
```java
MiPila<Cancion> historial = new MiPila<>();

// Cada canción reproducida se apila
historial.apilar(cancionReproducida);

// Botón "Anterior"
Cancion anterior = historial.desapilar();
if (anterior != null) {
    reproducir(anterior);
}
```

---

## 🧪 Pruebas de las Estructuras

### **Test Manual de MiLista:**
```java
MiLista<String> lista = new MiLista<>();
lista.agregar("A");
lista.agregar("B");
lista.agregar("C");

System.out.println(lista.obtener(0)); // "A"
System.out.println(lista.obtener(1)); // "B"
System.out.println(lista.tamanio());  // 3

lista.eliminar(1);
System.out.println(lista.tamanio());  // 2
```

### **Test Manual de Circularidad:**
```java
MiLista<Integer> circular = new MiLista<>();
circular.agregar(1);
circular.agregar(2);
circular.agregar(3);

// Verificar que cola.siguiente == cabeza
Nodo<Integer> cola = circular.getCola();
Nodo<Integer> cabeza = circular.getCabeza();
System.out.println(cola.getSiguiente() == cabeza); // true
```

---

## 📚 Referencias y Conceptos

### **Conceptos Aplicados:**
- ✅ **Tipos Genéricos** (`<T>`)
- ✅ **Referencias** (punteros en Java)
- ✅ **Enlazamiento doble** (anterior/siguiente)
- ✅ **Circularidad** (último → primero)
- ✅ **Interfaz Funcional** (`AccionNodo<T>`)
- ✅ **Encapsulamiento** (getters/setters)

### **Patrones de Diseño:**
- ✅ **Iterator** (mediante `recorrer()`)
- ✅ **Wrapper** (MiPila y MiCola envuelven MiLista)
- ✅ **Template Method** (métodos abstractos en MiLista)
