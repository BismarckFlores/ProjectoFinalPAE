package ni.edu.uam.gestionclientes.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ni.edu.uam.gestionclientes.models.Client;

import java.net.URL;

public class SceneManager {

    private static final String STYLESHEET = "/ni/edu/uam/gestionclientes/css/app.css";

    // Tamaño estándar de la ventana: todas las pantallas lo comparten para que
    // la app no cambie de tamaño al navegar entre ellas.
    public static final double WINDOW_WIDTH = 960.0;
    public static final double WINDOW_HEIGHT = 640.0;

    private static Stage stagePrincipal;

    public static void setStageMain(Stage stage) {
        stagePrincipal = stage;
    }

    /**
     * Reemplaza toda la ventana (usado únicamente para entrar/salir del shell:
     * Login <-> pantalla principal). El resto de la navegación ocurre dentro
     * del shell vía setContent()/setContentWithData().
     */
    public static void sceneChange(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneManager.class.getResource("/ni/edu/uam/gestionclientes/views/" + fxmlFile));
            Parent root = loader.load();
            applyScene(root);
        } catch (Exception e) {
            reportNavigationError(fxmlFile, e);
        }
    }

    /**
     * Carga una vista dentro del área de contenido del shell, manteniendo
     * MenuBar/ToolBar y el tamaño de la ventana sin cambios.
     */
    public static void setContent(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneManager.class.getResource("/ni/edu/uam/gestionclientes/views/" + fxmlFile));
            Parent content = loader.load();
            AppShell.show(content);
        } catch (Exception e) {
            reportNavigationError(fxmlFile, e);
        }
    }

    /**
     * Igual que setContent(), pero además entrega un Client al controller
     * destino si este implementa ReceivesClientData.
     */
    public static void setContentWithData(String fxmlFile, Client client) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneManager.class.getResource("/ni/edu/uam/gestionclientes/views/" + fxmlFile));
            Parent content = loader.load();

            Object controller = loader.getController();
            if (controller instanceof ReceivesClientData) {
                ((ReceivesClientData) controller).receiveClientData(client);
            }

            AppShell.show(content);
        } catch (Exception e) {
            reportNavigationError(fxmlFile, e);
        }
    }

    /** Vuelve al panel de bienvenida del shell (equivalente a "menú principal"). */
    public static void showHome() {
        AppShell.showHome();
    }

    private static void reportNavigationError(String fxmlFile, Exception e) {
        AlertHelper.showError("Error de Navegación", null,
                "No se pudo cargar la pantalla \"" + fxmlFile + "\": " + e);
    }

    private static void applyScene(Parent root) {
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        URL css = SceneManager.class.getResource(STYLESHEET);
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }
        stagePrincipal.setScene(scene);
        stagePrincipal.show();
    }
}
