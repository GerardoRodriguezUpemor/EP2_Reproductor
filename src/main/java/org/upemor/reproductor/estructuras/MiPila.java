package org.upemor.reproductor.estructuras;

/**
 * Implementación manual de Pila (Stack) - LIFO (Last In First Out)
 * NO usa Stack de Java
 * @param <T> Tipo de dato que almacena la pila
 * @author Sistema de Estructuras de Datos
 */
public class MiPila<T> {
    private Nodo<T> tope;
    private int tamanio;
    
    public MiPila() {
        this.tope = null;
        this.tamanio = 0;
    }
    
    /**
     * Apila un elemento (push)
     */
    public void apilar(T dato) {
        Nodo<T> nuevoNodo = new Nodo<>(dato);
        nuevoNodo.setSiguiente(tope);
        tope = nuevoNodo;
        tamanio++;
    }
    
    /**
     * Desapila un elemento (pop)
     */
    public T desapilar() {
        if (estaVacia()) {
            return null;
        }
        
        T dato = tope.getDato();
        tope = tope.getSiguiente();
        tamanio--;
        return dato;
    }
    
    /**
     * Ve el elemento en el tope sin sacarlo (peek)
     */
    public T verTope() {
        if (estaVacia()) {
            return null;
        }
        return tope.getDato();
    }
    
    /**
     * Verifica si la pila está vacía
     */
    public boolean estaVacia() {
        return tope == null;
    }
    
    /**
     * Obtiene el tamaño de la pila
     */
    public int tamanio() {
        return tamanio;
    }
    
    /**
     * Limpia toda la pila
     */
    public void limpiar() {
        tope = null;
        tamanio = 0;
    }
    
    /**
     * Convierte la pila a una lista para poder visualizarla
     * (sin modificar la pila original)
     */
    public MiLista<T> aLista() {
        MiLista<T> lista = new MiLista<>();
        Nodo<T> actual = tope;
        
        while (actual != null) {
            lista.agregar(actual.getDato());
            actual = actual.getSiguiente();
        }
        
        return lista;
    }
}
