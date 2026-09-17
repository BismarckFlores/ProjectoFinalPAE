package ni.edu.uam.gestionclientes.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ni.edu.uam.gestionclientes.dao.UserDaoImpl;
import ni.edu.uam.gestionclientes.models.User;
import ni.edu.uam.gestionclientes.util.AlertHelper;
import ni.edu.uam.gestionclientes.util.SceneManager;

import java.util.Optional;

public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField pfPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnExit;

    @FXML
    public void onLoginAction(ActionEvent event) {
        attemptLogin();
    }

    @FXML
    public void onPasswordKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            attemptLogin();
        }
    }

    private void attemptLogin() {
        String username = txtUsername.getText();
        String password = pfPassword.getText();

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            AlertHelper.showWarning("Campos incompletos", null,
                    "Por favor ingrese usuario y contraseña.");
            return;
        }

        Optional<User> user = UserDaoImpl.getInstance().validateCredentials(username, password);
        if (user.isEmpty()) {
            AlertHelper.showError("Acceso denegado", null,
                    "Usuario o contraseña incorrectos.");
            return;
        }

        SceneManager.sceneChange("main-menu-view.fxml");
    }

    @FXML
    public void onExitAction(ActionEvent event) {
        boolean confirmar = AlertHelper.showConfirmation(
                "Salir de la Aplicación",
                "Confirmación de Cierre",
                "¿Está seguro que desea salir del sistema de gestión de clientes?"
        );
        if (confirmar) {
            Platform.exit();
            System.exit(0);
        }
    }
}
