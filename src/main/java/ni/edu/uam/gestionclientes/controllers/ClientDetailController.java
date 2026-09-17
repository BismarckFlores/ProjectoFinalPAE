package ni.edu.uam.gestionclientes.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ni.edu.uam.gestionclientes.models.Client;
import ni.edu.uam.gestionclientes.util.ReceivesClientData;
import ni.edu.uam.gestionclientes.util.SceneManager;

public class ClientDetailController implements ReceivesClientData {

    @FXML
    private Label lblFullName;

    @FXML
    private Label lblCustomerType;

    @FXML
    private Label lblCity;

    @FXML
    private Label lblBirthDate;

    @FXML
    private Label lblRequestType;

    @FXML
    private Label lblServices;

    @FXML
    private ImageView imgPhoto;

    @FXML
    private Button btnBack;

    @Override
    public void receiveClientData(Client client) {
        lblFullName.setText(client.getFirstName() + " " + client.getLastName());
        lblCustomerType.setText(client.getCustomerType());
        lblCity.setText(client.getCity());
        lblBirthDate.setText(client.getBirthDate() == null ? "" : client.getBirthDate().toString());
        lblRequestType.setText(client.getRequestType());
        lblServices.setText(client.getServicesOfInterest() == null || client.getServicesOfInterest().isEmpty()
                ? "Ninguno"
                : String.join(", ", client.getServicesOfInterest()));

        if (client.getImagePath() != null && !client.getImagePath().isBlank()) {
            imgPhoto.setImage(new Image(client.getImagePath()));
        }
    }

    @FXML
    public void onBackAction(ActionEvent event) {
        SceneManager.showHome();
    }
}
