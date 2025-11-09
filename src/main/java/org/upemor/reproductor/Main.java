package org.upemor.reproductor;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import javafx.application.Platform;
import javax.swing.UIManager;
import org.upemor.reproductor.model.repository.MiConexion;
import org.upemor.reproductor.view.PrincipalDlg;

/**
 * Clase principal del Reproductor de Canciones
 * @author Sistema Reproductor
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("🎵 REPRODUCTOR DE CANCIONES");
        System.out.println("    Sistema con Estructuras de Datos");
        System.out.println("==============================================");
        System.out.println();
        System.out.println("📊 Estructuras implementadas:");
        System.out.println("   ✓ MiLista  - Lista Circular Doblemente Enlazada");
        System.out.println("   ✓ MiPila   - Pila (LIFO)");
        System.out.println("   ✓ MiCola   - Cola (FIFO)");
        System.out.println();
        System.out.println("🗄️  Base de Datos: SQLite");
        System.out.println("🎵 Audio: JavaFX MediaPlayer (con pause/resume)");
        System.out.println("==============================================");
        System.out.println();
        
        try {
            // Inicializar JavaFX toolkit (necesario para MediaPlayer)
            System.out.println("Inicializando JavaFX...");
            Platform.startup(() -> {
                // JavaFX inicializado
            });
            
            // Inicializar conexión a la base de datos
            System.out.println("Inicializando base de datos...");
            MiConexion.getInstancia();
            
            // Configurar Look and Feel
            System.out.println("Configurando interfaz gráfica...");
            FlatMacLightLaf.setup();
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);
            UIManager.put("ProgressBar.arc", 10);
            UIManager.put("TextComponent.arc", 10);
            UIManager.setLookAndFeel(new FlatMacLightLaf());
            
            // Crear ventana principal
            System.out.println("Iniciando aplicación...");
            System.out.println();
            new PrincipalDlg();
            
        } catch (Exception e) {
            System.err.println("❌ Error al iniciar la aplicación: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
