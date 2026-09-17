module ni.edu.uam.gestionclientes {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;


    opens ni.edu.uam.gestionclientes to javafx.fxml;
    opens ni.edu.uam.gestionclientes.models to javafx.fxml, javafx.base;
    opens ni.edu.uam.gestionclientes.controllers to javafx.fxml;
    opens ni.edu.uam.gestionclientes.util to javafx.fxml;

    exports ni.edu.uam.gestionclientes;
    exports ni.edu.uam.gestionclientes.models;
    exports ni.edu.uam.gestionclientes.controllers;
    exports ni.edu.uam.gestionclientes.util;
}