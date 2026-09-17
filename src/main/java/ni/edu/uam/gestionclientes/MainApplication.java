package ni.edu.uam.gestionclientes;

import javafx.application.Application;
import javafx.stage.Stage;
import ni.edu.uam.gestionclientes.util.SceneManager;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Sistema de Gestión de Clientes");
        SceneManager.setStageMain(stage);
        SceneManager.sceneChange("login-view.fxml");
    }
}
