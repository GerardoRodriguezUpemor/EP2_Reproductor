package org.upemor.reproductor.estructuras;

/**
 * Implementación manual de Cola (Queue) - FIFO (First In First Out)
 * NO usa Queue de Java
 * @param <T> Tipo de dato que almacena la cola
 * @author Sistema de Estructuras de Datos
 */
public class MiCola<T> {
    private Nodo<T> frente;
    private Nodo<T> fin;
    private int tamanio;
    
    public MiCola() {
        this.frente = null;
        this.fin = null;
        this.tamanio = 0;
    }
    
    /**
     * Encola un elemento (enqueue)
     */
    public void encolar(T dato) {
        Nodo<T> nuevoNodo = new Nodo<>(dato);
        
        if (estaVacia()) {
            frente = nuevoNodo;
            fin = nuevoNodo;
        } else {
            fin.setSiguiente(nuevoNodo);
            fin = nuevoNodo;
        }
        tamanio++;
    }
    
    /**
     * Desencola un elemento (dequeue)
     */
    public T desencolar() {
        if (estaVacia()) {
            return null;
        }
        
        T dato = frente.getDato();
        frente = frente.getSiguiente();
        
        if (frente == null) {
            fin = null;
        }
        
        tamanio--;
        return dato;
    }
    
    /**
     * Ve el elemento al frente sin sacarlo (peek)
     */
    public T verFrente() {
        if (estaVacia()) {
            return null;
        }
        return frente.getDato();
    }
    
    /**
     * Verifica si la cola está vacía
     */
    public boolean estaVacia() {
        return frente == null;
    }
    
    /**
     * Obtiene el tamaño de la cola
     */
    public int tamanio() {
        return tamanio;
    }
    
    /**
     * Limpia toda la cola
     */
    public void limpiar() {
        frente = null;
        fin = null;
        tamanio = 0;
    }
    
    /**
     * Convierte la cola a una lista para poder visualizarla
     * (sin modificar la cola original)
     */
    public MiLista<T> aLista() {
        MiLista<T> lista = new MiLista<>();
        Nodo<T> actual = frente;
        
        while (actual != null) {
            lista.agregar(actual.getDato());
            actual = actual.getSiguiente();
        }
        
        return lista;
    }
}
