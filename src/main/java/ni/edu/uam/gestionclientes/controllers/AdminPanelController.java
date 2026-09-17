package ni.edu.uam.gestionclientes.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ni.edu.uam.gestionclientes.dao.UserDaoImpl;
import ni.edu.uam.gestionclientes.models.User;
import ni.edu.uam.gestionclientes.util.AlertHelper;
import ni.edu.uam.gestionclientes.util.SceneManager;
import ni.edu.uam.gestionclientes.util.Session;

public class AdminPanelController {

    @FXML
    private TableView<User> tableUsers;

    @FXML
    private TableColumn<User, String> colUsername;

    @FXML
    private TableColumn<User, String> colRole;

    @FXML
    private TableColumn<User, String> colActive;

    @FXML
    public void initialize() {
        colUsername.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getUsername()));
        colRole.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getRole().toString()));
        colActive.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().isActive() ? "Sí" : "No"));

        tableUsers.setItems(UserDaoImpl.getInstance().listing());
    }

    @FXML
    public void onNewAccountAction(ActionEvent event) {
        SceneManager.sceneChange("account-create-view.fxml");
    }

    @FXML
    public void onDeleteAccountAction(ActionEvent event) {
        User selected = tableUsers.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showWarning("Ninguna selección", null, "Seleccione una cuenta para eliminar.");
            return;
        }

        User current = Session.getCurrentUser();
        if (current != null && current.getId() == selected.getId()) {
            AlertHelper.showWarning("Operación no permitida", null,
                    "No puede eliminar la cuenta con la que inició sesión.");
            return;
        }

        boolean confirmar = AlertHelper.showConfirmation(
                "Eliminar Cuenta",
                "Confirmación de Eliminación",
                "¿Está seguro que desea eliminar la cuenta \"" + selected.getUsername() + "\"?"
        );
        if (confirmar) {
            UserDaoImpl.getInstance().delete(selected.getId());
        }
    }

    @FXML
    public void onBackAction(ActionEvent event) {
        SceneManager.sceneChange("main-menu-view.fxml");
    }
}
