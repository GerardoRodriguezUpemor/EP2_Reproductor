package org.upemor.reproductor.estructuras;

/**
 * Implementación manual de Lista Circular Doblemente Enlazada
 * NO usa LinkedList de Java
 * @param <T> Tipo de dato que almacena la lista
 * @author Sistema de Estructuras de Datos
 */
public class MiLista<T> {
    private Nodo<T> cabeza;
    private Nodo<T> cola;
    private int tamanio;
    
    public MiLista() {
        this.cabeza = null;
        this.cola = null;
        this.tamanio = 0;
    }
    
    /**
     * Agrega un elemento al final de la lista circular doblemente enlazada
     */
    public void agregar(T dato) {
        Nodo<T> nuevoNodo = new Nodo<>(dato);
        
        if (cabeza == null) {
            // Primera inserción
            cabeza = nuevoNodo;
            cola = nuevoNodo;
            // Hacer circular
            nuevoNodo.setSiguiente(nuevoNodo);
            nuevoNodo.setAnterior(nuevoNodo);
        } else {
            // Insertar al final
            nuevoNodo.setAnterior(cola);
            nuevoNodo.setSiguiente(cabeza);
            cola.setSiguiente(nuevoNodo);
            cabeza.setAnterior(nuevoNodo);
            cola = nuevoNodo;
        }
        tamanio++;
    }
    
    /**
     * Elimina un elemento en la posición especificada
     */
    public boolean eliminar(int indice) {
        if (indice < 0 || indice >= tamanio || cabeza == null) {
            return false;
        }
        
        if (tamanio == 1) {
            // Único elemento
            cabeza = null;
            cola = null;
        } else if (indice == 0) {
            // Eliminar la cabeza
            Nodo<T> nuevaCabeza = cabeza.getSiguiente();
            nuevaCabeza.setAnterior(cola);
            cola.setSiguiente(nuevaCabeza);
            cabeza = nuevaCabeza;
        } else {
            // Buscar el nodo a eliminar
            Nodo<T> actual = cabeza;
            for (int i = 0; i < indice; i++) {
                actual = actual.getSiguiente();
            }
            
            Nodo<T> anterior = actual.getAnterior();
            Nodo<T> siguiente = actual.getSiguiente();
            
            anterior.setSiguiente(siguiente);
            siguiente.setAnterior(anterior);
            
            if (actual == cola) {
                cola = anterior;
            }
        }
        tamanio--;
        return true;
    }
    
    /**
     * Obtiene un elemento por su índice
     */
    public T obtener(int indice) {
        if (indice < 0 || indice >= tamanio || cabeza == null) {
            return null;
        }
        
        Nodo<T> actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.getSiguiente();
        }
        return actual.getDato();
    }
    
    /**
     * Busca un elemento en la lista
     */
    public int buscar(T dato) {
        if (cabeza == null) {
            return -1;
        }
        
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
    
    /**
     * Verifica si la lista está vacía
     */
    public boolean estaVacia() {
        return cabeza == null;
    }
    
    /**
     * Obtiene el tamaño de la lista
     */
    public int tamanio() {
        return tamanio;
    }
    
    /**
     * Limpia toda la lista
     */
    public void limpiar() {
        cabeza = null;
        cola = null;
        tamanio = 0;
    }
    
    /**
     * Recorre la lista y ejecuta una acción para cada elemento
     */
    public void recorrer(AccionLista<T> accion) {
        if (cabeza == null) {
            return;
        }
        
        Nodo<T> actual = cabeza;
        int indice = 0;
        
        do {
            accion.ejecutar(actual.getDato(), indice);
            actual = actual.getSiguiente();
            indice++;
        } while (actual != cabeza && indice < tamanio);
    }
    
    /**
     * Obtiene la cabeza de la lista
     */
    public Nodo<T> getCabeza() {
        return cabeza;
    }
    
    /**
     * Obtiene la cola de la lista
     */
    public Nodo<T> getCola() {
        return cola;
    }
    
    /**
     * Ordena la lista usando Bubble Sort (Ordenamiento de burbuja)
     * Complejidad: O(n²)
     * @param comparador Comparador para determinar el orden
     */
    public void ordenarBubbleSort(Comparador<T> comparador) {
        if (tamanio <= 1) {
            return;
        }
        
        boolean huboIntercambio;
        do {
            huboIntercambio = false;
            Nodo<T> actual = cabeza;
            
            for (int i = 0; i < tamanio - 1; i++) {
                Nodo<T> siguiente = actual.getSiguiente();
                
                // Si el actual es mayor que el siguiente, intercambiar
                if (comparador.comparar(actual.getDato(), siguiente.getDato()) > 0) {
                    // Intercambiar los datos
                    T temp = actual.getDato();
                    actual.setDato(siguiente.getDato());
                    siguiente.setDato(temp);
                    huboIntercambio = true;
                }
                
                actual = siguiente;
            }
        } while (huboIntercambio);
    }
    
    /**
     * Ordena la lista usando Insertion Sort (Ordenamiento por inserción)
     * Complejidad: O(n²) en promedio, O(n) en el mejor caso (lista ya ordenada)
     * @param comparador Comparador para determinar el orden
     */
    public void ordenarInsertionSort(Comparador<T> comparador) {
        if (tamanio <= 1) {
            return;
        }
        
        // Crear array temporal para facilitar el ordenamiento
        @SuppressWarnings("unchecked")
        T[] elementos = (T[]) new Object[tamanio];
        
        // Copiar elementos al array
        Nodo<T> actual = cabeza;
        for (int i = 0; i < tamanio; i++) {
            elementos[i] = actual.getDato();
            actual = actual.getSiguiente();
        }
        
        // Insertion Sort en el array
        for (int i = 1; i < tamanio; i++) {
            T clave = elementos[i];
            int j = i - 1;
            
            while (j >= 0 && comparador.comparar(elementos[j], clave) > 0) {
                elementos[j + 1] = elementos[j];
                j--;
            }
            elementos[j + 1] = clave;
        }
        
        // Copiar elementos ordenados de vuelta a la lista
        actual = cabeza;
        for (int i = 0; i < tamanio; i++) {
            actual.setDato(elementos[i]);
            actual = actual.getSiguiente();
        }
    }
    
    /**
     * Interfaz funcional para recorrer la lista
     */
    @FunctionalInterface
    public interface AccionLista<T> {
        void ejecutar(T dato, int indice);
    }
    
    /**
     * Interfaz funcional para comparar elementos
     */
    @FunctionalInterface
    public interface Comparador<T> {
        /**
         * Compara dos elementos
         * @return negativo si a < b, 0 si a == b, positivo si a > b
         */
        int comparar(T a, T b);
    }
}
