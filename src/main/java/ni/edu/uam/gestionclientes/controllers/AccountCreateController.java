package ni.edu.uam.gestionclientes.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ni.edu.uam.gestionclientes.dao.UserDaoImpl;
import ni.edu.uam.gestionclientes.models.Role;
import ni.edu.uam.gestionclientes.models.User;
import ni.edu.uam.gestionclientes.util.AlertHelper;
import ni.edu.uam.gestionclientes.util.SceneManager;

public class AccountCreateController {

    @FXML
    private TextField txtNewUsername;

    @FXML
    private PasswordField pfNewPassword;

    @FXML
    private ComboBox<Role> cmbRole;

    @FXML
    public void initialize() {
        cmbRole.getItems().addAll(Role.values());
        cmbRole.getSelectionModel().select(Role.COMUN);
    }

    @FXML
    public void onSaveAction(ActionEvent event) {
        String username = txtNewUsername.getText();
        String password = pfNewPassword.getText();
        Role role = cmbRole.getValue();

        if (username == null || username.isBlank() || password == null || password.isBlank() || role == null) {
            AlertHelper.showWarning("Campos incompletos", null,
                    "Por favor complete usuario, contraseña y rol.");
            return;
        }

        if (password.length() < 4) {
            AlertHelper.showWarning("Contraseña muy corta", null,
                    "La contraseña debe tener al menos 4 caracteres.");
            return;
        }

        if (UserDaoImpl.getInstance().usernameExists(username)) {
            AlertHelper.showWarning("Usuario existente", null,
                    "Ya existe una cuenta con ese nombre de usuario.");
            return;
        }

        User newUser = User.builder()
                .id(UserDaoImpl.getInstance().nextId())
                .username(username)
                .password(password)
                .active(true)
                .role(role)
                .build();

        UserDaoImpl.getInstance().save(newUser);
        AlertHelper.showInfo("Cuenta creada", null,
                "La cuenta \"" + username + "\" se creó correctamente con rol " + role + ".");
        SceneManager.setContent("admin-panel-view.fxml");
    }

    @FXML
    public void onCancelAction(ActionEvent event) {
        SceneManager.setContent("admin-panel-view.fxml");
    }
}
