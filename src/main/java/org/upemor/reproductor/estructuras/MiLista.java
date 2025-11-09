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
     * Interfaz funcional para recorrer la lista
     */
    @FunctionalInterface
    public interface AccionLista<T> {
        void ejecutar(T dato, int indice);
    }
}
