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
import java.util.Locale;
import java.util.Optional;

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
    private Button btnSearch;

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
    public void onSearchAction(ActionEvent event) {
        Optional<String> input = AlertHelper.showInputDialog(
                "Buscar Cliente", null, "Ingrese el nombre o apellido a buscar:");

        input.map(String::trim).filter(text -> !text.isEmpty()).ifPresent(query -> {
            String needle = query.toLowerCase(Locale.ROOT);
            Client match = tableClients.getItems().stream()
                    .filter(client -> (client.getFirstName() + " " + client.getLastName())
                            .toLowerCase(Locale.ROOT).contains(needle))
                    .findFirst()
                    .orElse(null);

            if (match == null) {
                AlertHelper.showInfo("Sin resultados", null,
                        "No se encontró ningún cliente que coincida con \"" + query + "\".");
                return;
            }

            tableClients.getSelectionModel().select(match);
            tableClients.scrollTo(match);
        });
    }

    @FXML
    public void onBackAction(ActionEvent event) {
        SceneManager.sceneChange("main-menu-view.fxml");
    }
}
