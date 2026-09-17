package ni.edu.uam.gestionclientes.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ni.edu.uam.gestionclientes.models.Client;

import java.io.IOException;
import java.net.URL;

public class SceneManager {

    private static final String STYLESHEET = "/ni/edu/uam/gestionclientes/css/app.css";

    private static Stage stagePrincipal;

    public static void setStageMain(Stage stage) {
        stagePrincipal = stage;
    }

    public static void sceneChange(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneManager.class.getResource("/ni/edu/uam/gestionclientes/views/" + fxmlFile));
            Parent root = loader.load();
            applyScene(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void changeSceneWithData(String fxmlFile, Client client) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneManager.class.getResource("/ni/edu/uam/gestionclientes/views/" + fxmlFile));
            Parent root = loader.load();

            Object controller = loader.getController();
            if (controller instanceof ReceivesClientData) {
                ((ReceivesClientData) controller).receiveClientData(client);
            }

            applyScene(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void applyScene(Parent root) {
        Scene scene = new Scene(root);
        URL css = SceneManager.class.getResource(STYLESHEET);
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }
        stagePrincipal.setScene(scene);
        stagePrincipal.show();
    }
}
