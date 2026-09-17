package ni.edu.uam.gestionclientes.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ni.edu.uam.gestionclientes.models.Client;

import java.io.IOException;

public class SceneManager {

    private static Stage stagePrincipal;

    public static void setStageMain(Stage stage) {
        stagePrincipal = stage;
    }

    public static void sceneChange(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneManager.class.getResource("/ni/edu/uam/gestionclientes/views/" + fxmlFile));
            Parent root = loader.load();
            stagePrincipal.setScene(new Scene(root));
            stagePrincipal.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void changeSceneWithData(String fxmlFile, Client datos) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneManager.class.getResource("/ni/edu/uam/gestionclientes/views/" + fxmlFile));
            Parent root = loader.load();

            Object controller = loader.getController();

              /**
             *  * Interfaz que debe implementar cualquier controlador que necesite recibir
             *  * un objeto Client al navegar desde otra ventana (por ejemplo, al abrir
             *  * el detalle de un cliente desde la tabla de consulta).
             *  *
             *  * SceneManager la usa dentro de cambiarEscenaConDatos(): si el controlador
             *  * de la nueva ventana implementa esta interfaz, le pasa el Client
             *  * automáticamente llamando a recibirDatos(). Así SceneManager no necesita
             *  * saber de qué pantalla específica se trata, solo si "puede recibir datos" o no.
             *  */

            if (controller instanceof RecClientData) {
                ((RecClientData) controller).recibirDatos(datos);
            }

            stagePrincipal.setScene(new Scene(root));
            stagePrincipal.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}