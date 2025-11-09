# 📊 Complejidad Algorítmica (Big O)

Este documento analiza la **complejidad temporal** de las operaciones implementadas en el reproductor de música.

---

## 📐 Notación Big O

La notación **Big O** describe el comportamiento del tiempo de ejecución de un algoritmo en función del tamaño de los datos de entrada (`n`).

| Notación | Nombre | Descripción |
|----------|--------|-------------|
| **O(1)** | Constante | El tiempo es independiente del tamaño de entrada |
| **O(n)** | Lineal | El tiempo crece linealmente con el tamaño |
| **O(n²)** | Cuadrática | El tiempo crece cuadráticamente (bucles anidados) |
| **O(log n)** | Logarítmica | El tiempo crece logarítmicamente |

---

## 🎯 Operaciones del Reproductor

| Operación | Estructura | Complejidad | Justificación |
|-----------|-----------|-------------|---------------|
| **Insertar al final** | Lista simple circular | **O(n)** | Se recorre toda la lista para encontrar el final |
| **Insertar al final (optimizado)** | Lista con referencia a cola | **O(1)** | Se mantiene referencia directa a la cola |
| **Eliminar por índice** | Lista circular | **O(n)** | Se recorre hasta el índice especificado |
| **Buscar canción** | Lista simple | **O(n)** | Se revisa elemento por elemento (búsqueda lineal) |
| **Reproducir canción** | Pila (Stack) | **O(1)** | Solo se usa el tope de la pila |
| **Apilar (push)** | Pila | **O(1)** | Inserción al inicio de la lista |
| **Desapilar (pop)** | Pila | **O(1)** | Eliminación del inicio de la lista |
| **Encolar (enqueue)** | Cola (Queue) | **O(1)** | Inserción al final con referencia directa |
| **Desencolar (dequeue)** | Cola | **O(1)** | Eliminación del inicio |
| **Ver tope (peek)** | Pila | **O(1)** | Solo lectura del primer elemento |
| **Ver frente (peek)** | Cola | **O(1)** | Solo lectura del primer elemento |
| **Obtener por índice** | Lista | **O(n)** | Se recorre hasta el índice |
| **Recorrer lista** | Lista | **O(n)** | Se visita cada elemento una vez |
| **Ordenar (Bubble Sort)** | Lista | **O(n²)** | Comparaciones e intercambios anidados |
| **Ordenar (Insertion Sort)** | Lista | **O(n²)** | En promedio; **O(n)** en mejor caso |

---

## 🔍 Algoritmos de Búsqueda

### Búsqueda Lineal
```java
public int buscar(T dato) {
    // Recorre elemento por elemento
    // Complejidad: O(n)
}
```

**Análisis:**
- **Mejor caso:** O(1) - El elemento está en la primera posición
- **Peor caso:** O(n) - El elemento está al final o no existe
- **Caso promedio:** O(n/2) ≈ O(n)

---

## 📊 Algoritmos de Ordenamiento

### 1. Bubble Sort (Ordenamiento de Burbuja)

```java
public void ordenarBubbleSort(Comparador<T> comparador) {
    // Compara pares adyacentes y los intercambia si están en orden incorrecto
    // Complejidad: O(n²)
}
```

**Análisis:**
- **Mejor caso:** O(n) - Lista ya ordenada (con optimización)
- **Peor caso:** O(n²) - Lista en orden inverso
- **Caso promedio:** O(n²)
- **Espacio:** O(1) - Ordenamiento in-place

**Ventajas:**
- ✅ Simple de implementar
- ✅ Ordenamiento estable (mantiene orden relativo)
- ✅ No requiere memoria adicional

**Desventajas:**
- ❌ Muy lento para listas grandes
- ❌ Muchas comparaciones innecesarias

---

### 2. Insertion Sort (Ordenamiento por Inserción)

```java
public void ordenarInsertionSort(Comparador<T> comparador) {
    // Construye una lista ordenada insertando elementos uno por uno
    // Complejidad: O(n²) en promedio, O(n) en mejor caso
}
```

**Análisis:**
- **Mejor caso:** O(n) - Lista ya ordenada
- **Peor caso:** O(n²) - Lista en orden inverso
- **Caso promedio:** O(n²)
- **Espacio:** O(n) - Usa array temporal

**Ventajas:**
- ✅ Eficiente para listas pequeñas
- ✅ Eficiente para listas casi ordenadas
- ✅ Ordenamiento estable
- ✅ Adaptativo (aprovecha orden existente)

**Desventajas:**
- ❌ Lento para listas grandes
- ❌ Usa memoria adicional en nuestra implementación

---

## 📈 Comparación de Rendimiento

Para una lista de **n = 100 canciones**:

| Algoritmo | Comparaciones (peor caso) | Tiempo relativo |
|-----------|---------------------------|-----------------|
| **Bubble Sort** | ~10,000 | 100% |
| **Insertion Sort** | ~5,000 | 50% |
| **Búsqueda Lineal** | ~100 | 1% |

---

## 🎵 Aplicación en el Reproductor

### Ordenar Biblioteca por Título
```java
MiLista<Cancion> biblioteca = repositorio.obtenerTodas();
biblioteca.ordenarBubbleSort((c1, c2) -> 
    c1.getTitulo().compareToIgnoreCase(c2.getTitulo())
);
```

### Ordenar Biblioteca por Artista
```java
biblioteca.ordenarInsertionSort((c1, c2) -> 
    c1.getArtista().compareToIgnoreCase(c2.getArtista())
);
```

---

## 💡 Optimizaciones Implementadas

### 1. Lista Circular Doblemente Enlazada
- ✅ **Referencia directa a cola**: Inserción O(1) al final
- ✅ **Navegación bidireccional**: Recorrido en ambas direcciones
- ✅ **Estructura circular**: Facilita reproducción en loop

### 2. Pila con Lista Enlazada
- ✅ **Push/Pop en O(1)**: Operaciones instantáneas
- ✅ **Sin límite de tamaño**: Crece dinámicamente
- ✅ **Historial ilimitado**: Todas las canciones reproducidas

### 3. Cola con Lista Enlazada
- ✅ **Enqueue/Dequeue en O(1)**: Sin recorridos
- ✅ **FIFO garantizado**: Primera canción agregada, primera en reproducir
- ✅ **Playlist dinámica**: Agregar canciones mientras se reproduce

---

## 🎓 Conclusiones

1. **Estructuras de Datos Manuales**: Implementamos todas las estructuras sin usar clases de Java (`LinkedList`, `Stack`, `Queue`)

2. **Complejidad Aceptable**: Para bibliotecas musicales típicas (100-1000 canciones), O(n²) es aceptable

3. **Trade-offs**: Sacrificamos velocidad de ordenamiento por simplicidad de implementación

4. **Futuras Mejoras**: Se podría implementar **QuickSort** O(n log n) o **MergeSort** O(n log n) para listas muy grandes

---

## 📚 Referencias

- **Bubble Sort**: [Algoritmo de ordenamiento](https://es.wikipedia.org/wiki/Ordenamiento_de_burbuja)
- **Insertion Sort**: [Algoritmo de inserción](https://es.wikipedia.org/wiki/Ordenamiento_por_inserci%C3%B3n)
- **Big O Notation**: [Análisis de algoritmos](https://es.wikipedia.org/wiki/Cota_superior_asint%C3%B3tica)
