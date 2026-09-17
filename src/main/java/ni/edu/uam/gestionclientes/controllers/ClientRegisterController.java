package ni.edu.uam.gestionclientes.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ni.edu.uam.gestionclientes.dao.ClientDaoImpl;
import ni.edu.uam.gestionclientes.dao.Dao;
import ni.edu.uam.gestionclientes.models.Client;
import ni.edu.uam.gestionclientes.util.AlertHelper;
import ni.edu.uam.gestionclientes.util.FileChooserHelper;
import ni.edu.uam.gestionclientes.util.SceneManager;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClientRegisterController {

    @FXML
    private TextField txtFirstName;

    @FXML
    private TextField txtLastName;

    @FXML
    private ComboBox<String> cmbCustomerType;

    @FXML
    private ComboBox<String> cmbCity;

    @FXML
    private DatePicker dpBirthDate;

    // ToggleGroup se asigna en scene, pero accedemos  a los radio buttons aqui
    @FXML
    private ToggleGroup tgRequestType;
    @FXML
    private RadioButton rbConsulta;
    @FXML
    private RadioButton rbReclamo;
    @FXML
    private RadioButton rbSoporte;

    // Checkboxes for services
    @FXML
    private CheckBox chkInternet;
    @FXML
    private CheckBox chkTelefonica;
    @FXML
    private CheckBox chkTelevision;

    @FXML
    private ImageView imgPhoto;

    // Ruta de la imagen seleccionada
    private String selectedImagePath;

    // Repositorio compartido: el mismo singleton que alimenta la tabla de Búsqueda.
    private final Dao<Client, String> clientDao = ClientDaoImpl.getInstance();

    @FXML
    public void initialize() {
        // Inicializar datos para los ComboBoxes (para que el Dev 4 no tenga que hacerlo en la UI)
        cmbCustomerType.getItems().addAll("Regular", "VIP", "Corporativo");
        cmbCity.getItems().addAll("Managua", "León", "Granada", "Masaya");
    }

    @FXML
    public void onSelectPhotoAction(ActionEvent event) {
        // Abre el explorador de archivos
        File file = FileChooserHelper.selectImageFile(null);
        if (file != null) {
            selectedImagePath = file.toURI().toString();
            // Mostrar la imagen en el ImageView
            imgPhoto.setImage(new Image(selectedImagePath));
        }
    }

    @FXML
    public void onSaveAction(ActionEvent event) {
        // Validar datos básicos
        if (txtFirstName.getText() == null || txtFirstName.getText().isBlank()
                || txtLastName.getText() == null || txtLastName.getText().isBlank()) {
            AlertHelper.showWarning("Campos incompletos", null, "Por favor llene los nombres y apellidos.");
            return;
        }
        if (cmbCustomerType.getValue() == null) {
            AlertHelper.showWarning("Campos incompletos", null, "Seleccione el tipo de cliente.");
            return;
        }
        if (cmbCity.getValue() == null) {
            AlertHelper.showWarning("Campos incompletos", null, "Seleccione la ciudad.");
            return;
        }
        if (dpBirthDate.getValue() == null) {
            AlertHelper.showWarning("Campos incompletos", null, "Seleccione la fecha de nacimiento.");
            return;
        }
        if (dpBirthDate.getValue().isAfter(LocalDate.now())) {
            AlertHelper.showWarning("Fecha inválida", null, "La fecha de nacimiento no puede ser futura.");
            return;
        }
        RadioButton selectedRadio = (RadioButton) tgRequestType.getSelectedToggle();
        if (selectedRadio == null) {
            AlertHelper.showWarning("Campos incompletos", null, "Seleccione el tipo de solicitud.");
            return;
        }
        String requestType = selectedRadio.getText();

        // Obtener servicios de los checkboxes
        List<String> services = new ArrayList<>();
        if (chkInternet.isSelected()) services.add(chkInternet.getText());
        if (chkTelefonica.isSelected()) services.add(chkTelefonica.getText());
        if (chkTelevision.isSelected()) services.add(chkTelevision.getText());

        // Crear objeto Cliente
        Client newClient = new Client(
                java.util.UUID.randomUUID().toString(),
                txtFirstName.getText(),
                txtLastName.getText(),
                cmbCustomerType.getValue(),
                cmbCity.getValue(),
                dpBirthDate.getValue(),
                requestType,
                services,
                selectedImagePath
        );

        // Guardar cliente
        clientDao.save(newClient);

        // Confirmar guardado y limpiar
        AlertHelper.showInfo("Éxito", null, "Cliente guardado correctamente.");
        onClearAction(event);
    }

    @FXML
    public void onClearAction(ActionEvent event) {
        txtFirstName.clear();
        txtLastName.clear();
        cmbCustomerType.getSelectionModel().clearSelection();
        cmbCity.getSelectionModel().clearSelection();
        dpBirthDate.setValue(null);
        tgRequestType.selectToggle(null);
        chkInternet.setSelected(false);
        chkTelefonica.setSelected(false);
        chkTelevision.setSelected(false);
        imgPhoto.setImage(null);
        selectedImagePath = null;
    }

    @FXML
    public void onCancelAction(ActionEvent event) {
        SceneManager.showHome();
    }
}
