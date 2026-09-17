package ni.edu.uam.gestionclientes.util;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;

/**
 * Punto de conexión entre el shell persistente (MenuBar/ToolBar fijos) y el
 * contenido que SceneManager va cargando dentro de él, sin reemplazar la
 * ventana completa cada vez que se navega a otra pantalla.
 */
public class AppShell {

    private static StackPane contentArea;
    private static Node homePane;

    private AppShell() {
    }

    public static void init(StackPane area, Node home) {
        contentArea = area;
        homePane = home;
    }

    public static void show(Node content) {
        if (contentArea != null) {
            contentArea.getChildren().setAll(content);
        }
    }

    public static void showHome() {
        if (contentArea != null && homePane != null) {
            contentArea.getChildren().setAll(homePane);
        }
    }
}
