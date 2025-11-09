package org.upemor.reproductor.test;

import org.upemor.reproductor.estructuras.MiLista;
import org.upemor.reproductor.estructuras.MiPila;
import org.upemor.reproductor.estructuras.MiCola;

/**
 * Clase de prueba para demostrar el funcionamiento de las estructuras de datos
 * @author Sistema Reproductor
 */
public class TestEstructuras {
    
    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("🧪 PRUEBAS DE ESTRUCTURAS DE DATOS");
        System.out.println("==============================================\n");
        
        probarMiLista();
        System.out.println("\n----------------------------------------------\n");
        
        probarMiPila();
        System.out.println("\n----------------------------------------------\n");
        
        probarMiCola();
        System.out.println("\n==============================================");
    }
    
    /**
     * Prueba de MiLista (Lista Enlazada)
     */
    private static void probarMiLista() {
        System.out.println("📋 PRUEBA DE MiLista (Lista Enlazada)");
        System.out.println("----------------------------------------------");
        
        MiLista<String> lista = new MiLista<>();
        
        // Agregar elementos
        System.out.println("➕ Agregando elementos...");
        lista.agregar("Bohemian Rhapsody");
        lista.agregar("Stairway to Heaven");
        lista.agregar("Hotel California");
        lista.agregar("Imagine");
        
        System.out.println("   Tamaño de la lista: " + lista.tamanio());
        
        // Recorrer lista
        System.out.println("\n🔄 Recorriendo la lista:");
        lista.recorrer((dato, indice) -> {
            System.out.println("   [" + indice + "] " + dato);
        });
        
        // Buscar elemento
        System.out.println("\n🔍 Buscando 'Imagine':");
        int indice = lista.buscar("Imagine");
        System.out.println("   Encontrado en índice: " + indice);
        
        // Obtener elemento
        System.out.println("\n📌 Obteniendo elemento en índice 1:");
        String elemento = lista.obtener(1);
        System.out.println("   Elemento: " + elemento);
        
        // Eliminar elemento
        System.out.println("\n🗑️  Eliminando elemento en índice 2:");
        lista.eliminar(2);
        System.out.println("   Nuevo tamaño: " + lista.tamanio());
        
        System.out.println("\n🔄 Lista después de eliminar:");
        lista.recorrer((dato, i) -> {
            System.out.println("   [" + i + "] " + dato);
        });
    }
    
    /**
     * Prueba de MiPila (Stack - LIFO)
     */
    private static void probarMiPila() {
        System.out.println("📚 PRUEBA DE MiPila (Stack - LIFO)");
        System.out.println("----------------------------------------------");
        
        MiPila<String> pila = new MiPila<>();
        
        // Apilar elementos
        System.out.println("⬆️  Apilando elementos (push)...");
        pila.apilar("Canción 1");
        pila.apilar("Canción 2");
        pila.apilar("Canción 3");
        pila.apilar("Canción 4");
        
        System.out.println("   Tamaño de la pila: " + pila.tamanio());
        
        // Ver tope
        System.out.println("\n👀 Viendo el tope (peek):");
        System.out.println("   Tope: " + pila.verTope());
        
        // Desapilar elementos (LIFO - Last In First Out)
        System.out.println("\n⬇️  Desapilando elementos (pop) - LIFO:");
        while (!pila.estaVacia()) {
            String elemento = pila.desapilar();
            System.out.println("   Desapilado: " + elemento + " | Quedan: " + pila.tamanio());
        }
        
        System.out.println("\n✅ Pila vacía: " + pila.estaVacia());
    }
    
    /**
     * Prueba de MiCola (Queue - FIFO)
     */
    private static void probarMiCola() {
        System.out.println("🎯 PRUEBA DE MiCola (Queue - FIFO)");
        System.out.println("----------------------------------------------");
        
        MiCola<String> cola = new MiCola<>();
        
        // Encolar elementos
        System.out.println("➡️  Encolando elementos (enqueue)...");
        cola.encolar("Primera canción");
        cola.encolar("Segunda canción");
        cola.encolar("Tercera canción");
        cola.encolar("Cuarta canción");
        
        System.out.println("   Tamaño de la cola: " + cola.tamanio());
        
        // Ver frente
        System.out.println("\n👀 Viendo el frente (peek):");
        System.out.println("   Frente: " + cola.verFrente());
        
        // Desencolar elementos (FIFO - First In First Out)
        System.out.println("\n⬅️  Desencolando elementos (dequeue) - FIFO:");
        while (!cola.estaVacia()) {
            String elemento = cola.desencolar();
            System.out.println("   Desencolado: " + elemento + " | Quedan: " + cola.tamanio());
        }
        
        System.out.println("\n✅ Cola vacía: " + cola.estaVacia());
    }
}
