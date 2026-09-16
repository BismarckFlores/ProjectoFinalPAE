module ni.edu.uam.gestionclientes {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;


    opens ni.edu.uam.gestionclientes to javafx.fxml;
    exports ni.edu.uam.gestionclientes;
}