package ni.edu.uam.gestionclientes.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import ni.edu.uam.gestionclientes.dao.ClientDaoImpl;
import ni.edu.uam.gestionclientes.models.Client;
import ni.edu.uam.gestionclientes.util.AlertHelper;
import ni.edu.uam.gestionclientes.util.FileChooserHelper;
import ni.edu.uam.gestionclientes.util.SceneManager;

import java.io.File;

public class ClientListController {

    @FXML
    private TableView<Client> tableClients;

    @FXML
    private TableColumn<Client, String> colFullName;

    @FXML
    private TableColumn<Client, String> colCustomerType;

    @FXML
    private TableColumn<Client, String> colCity;

    @FXML
    private TableColumn<Client, String> colBirthDate;

    @FXML
    private TableColumn<Client, String> colRequestType;

    @FXML
    private Button btnExport;

    @FXML
    private Button btnBack;

    @FXML
    public void initialize() {
        colFullName.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getFirstName() + " " + data.getValue().getLastName()));
        colCustomerType.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getCustomerType()));
        colCity.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getCity()));
        colBirthDate.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getBirthDate() == null ? "" : data.getValue().getBirthDate().toString()));
        colRequestType.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getRequestType()));

        tableClients.setItems(ClientDaoImpl.getInstance().listing());
    }

    @FXML
    public void onTableRowClicked(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            Client selected = tableClients.getSelectionModel().getSelectedItem();
            if (selected != null) {
                SceneManager.changeSceneWithData("client-detail-view.fxml", selected);
            }
        }
    }

    @FXML
    public void onExportAction(ActionEvent event) {
        File directory = FileChooserHelper.selectDirectory(tableClients.getScene().getWindow());
        if (directory != null) {
            AlertHelper.showInfo("Exportación", null,
                    "Los datos se exportarían a: " + directory.getAbsolutePath());
        }
    }

    @FXML
    public void onBackAction(ActionEvent event) {
        SceneManager.sceneChange("main-menu-view.fxml");
    }
}
