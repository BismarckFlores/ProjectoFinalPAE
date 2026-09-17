package ni.edu.uam.gestionclientes.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ni.edu.uam.gestionclientes.models.Client;

import java.net.URL;

public class SceneManager {

    private static final String STYLESHEET = "/ni/edu/uam/gestionclientes/css/app.css";

    // Tamaño con el que abre la ventana la primera vez. A partir de ahí, si el
    // usuario la redimensiona o maximiza, ese tamaño se respeta durante el
    // resto de la ejecución: nunca se vuelve a crear la Scene desde cero.
    public static final double WINDOW_WIDTH = 960.0;
    public static final double WINDOW_HEIGHT = 640.0;

    private static Stage stagePrincipal;
    private static Scene mainScene;

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
            applyRoot(root);
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

    /**
     * Solo la primera vez crea la Scene (con el tamaño inicial). De ahí en
     * adelante reutiliza esa misma Scene y solo le cambia el root, así que el
     * tamaño/maximizado que haya elegido el usuario nunca se pierde al navegar
     * entre Login y el shell.
     */
    private static void applyRoot(Parent root) {
        if (mainScene == null) {
            mainScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            URL css = SceneManager.class.getResource(STYLESHEET);
            if (css != null) {
                mainScene.getStylesheets().add(css.toExternalForm());
            }
            stagePrincipal.setScene(mainScene);
            stagePrincipal.show();
        } else {
            mainScene.setRoot(root);
        }
    }
}
