module ni.edu.uam.gestionclientes {
    requires javafx.controls;
    requires javafx.fxml;


    opens ni.edu.uam.gestionclientes to javafx.fxml;
    exports ni.edu.uam.gestionclientes;
}