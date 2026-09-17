package ni.edu.uam.gestionclientes.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import ni.edu.uam.gestionclientes.models.User;
import ni.edu.uam.gestionclientes.util.AlertHelper;
import ni.edu.uam.gestionclientes.util.AppShell;
import ni.edu.uam.gestionclientes.util.SceneManager;
import ni.edu.uam.gestionclientes.util.Session;

/**
 * Controlador del shell principal de la aplicación: MenuBar, ToolBar y
 * ContextMenu permanecen fijos mientras el contenido central (contentArea)
 * cambia según la pantalla a la que se navega.
 */
public class AppShellController {

    // --- Componentes de Navegación del Menú (MenuBar) ---
    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem menuRegistro;

    @FXML
    private MenuItem menuConsulta;

    @FXML
    private MenuItem menuCerrarSesion;

    @FXML
    private MenuItem menuSalir;

    @FXML
    private MenuItem menuAcercaDe;

    @FXML
    private MenuItem menuAdminPanel;

    // --- Componentes de la Barra de Herramientas (ToolBar) ---
    @FXML
    private ToolBar toolBar;

    @FXML
    private Button btnToolBarRegistro;

    @FXML
    private Button btnToolBarConsulta;

    @FXML
    private Button btnToolBarAdminPanel;

    @FXML
    private Button btnToolBarCerrarSesion;

    // --- Área de contenido persistente ---
    @FXML
    private StackPane contentArea;

    @FXML
    private VBox homePane;

    // --- Botones de acceso directo en el panel de bienvenida ---
    @FXML
    private Button btnRegistrarCliente;

    @FXML
    private Button btnConsultarClientes;

    @FXML
    private Button btnAdminPanel;

    // --- Menú Contextual (ContextMenu), disponible en toda el área de trabajo ---
    @FXML
    private ContextMenu contextMenu;

    @FXML
    private MenuItem contextItemRegistro;

    @FXML
    private MenuItem contextItemConsulta;

    @FXML
    private MenuItem contextItemInfo;

    // --- Etiquetas Informativas ---
    @FXML
    private Label lblBienvenida;

    @FXML
    private Label lblEstado;

    @FXML
    public void initialize() {
        AppShell.init(contentArea, homePane);

        User currentUser = Session.getCurrentUser();
        boolean isAdmin = Session.isAdmin();

        if (lblEstado != null) {
            lblEstado.setText(currentUser == null
                    ? "Sistema activo"
                    : "Sesión: " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
        }

        // Rol "Usuario Común": acceso de solo lectura, sin registro ni panel de administración.
        setNodeAvailable(menuRegistro, isAdmin);
        setNodeAvailable(contextItemRegistro, isAdmin);
        setButtonAvailable(btnToolBarRegistro, isAdmin);
        setButtonAvailable(btnRegistrarCliente, isAdmin);

        setNodeAvailable(menuAdminPanel, isAdmin);
        setButtonAvailable(btnToolBarAdminPanel, isAdmin);
        setButtonAvailable(btnAdminPanel, isAdmin);

        // StackPane no es un Control: no tiene soporte nativo de ContextMenu, así
        // que se muestra manualmente al detectar el evento de clic derecho.
        if (contentArea != null && contextMenu != null) {
            contentArea.setOnContextMenuRequested(event ->
                    contextMenu.show(contentArea, event.getScreenX(), event.getScreenY()));
        }
    }

    private void setNodeAvailable(MenuItem item, boolean available) {
        if (item != null) {
            item.setVisible(available);
            item.setDisable(!available);
        }
    }

    private void setButtonAvailable(Button button, boolean available) {
        if (button != null) {
            button.setVisible(available);
            button.setManaged(available);
        }
    }

    @FXML
    public void onMenuRegistroClick(ActionEvent event) {
        if (!Session.isAdmin()) {
            AlertHelper.showWarning("Acceso restringido", null,
                    "Su cuenta tiene acceso de solo lectura y no puede registrar clientes.");
            return;
        }
        SceneManager.setContent("client-registration-view.fxml");
    }

    @FXML
    public void onAdminPanelClick(ActionEvent event) {
        if (!Session.isAdmin()) {
            AlertHelper.showWarning("Acceso restringido", null,
                    "Solo un administrador puede acceder al panel de administración.");
            return;
        }
        SceneManager.setContent("admin-panel-view.fxml");
    }

    @FXML
    public void onMenuConsultaClick(ActionEvent event) {
        SceneManager.setContent("client-list-view.fxml");
    }

    @FXML
    public void onCerrarSesionClick(ActionEvent event) {
        boolean confirmar = AlertHelper.showConfirmation(
                "Cerrar Sesión",
                "Confirmación de Salida de Usuario",
                "¿Está seguro que desea cerrar la sesión actual?"
        );
        if (confirmar) {
            Session.logout();
            SceneManager.sceneChange("login-view.fxml");
        }
    }

    @FXML
    public void onSalirClick(ActionEvent event) {
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

    @FXML
    public void onAcercaDeClick(ActionEvent event) {
        AlertHelper.showInfo(
                "Acerca del Sistema",
                "Sistema de Registro y Consulta de Clientes v1.0",
                "Desarrollado en JavaFX con arquitectura MVC.\nAsignatura: Programación de Aplicaciones de Escritorio."
        );
    }

    @FXML
    public void onContextInfoClick(ActionEvent event) {
        AlertHelper.showInfo(
                "Información del Sistema",
                "Opciones Contextuales",
                "Desde aquí puedes acceder rápidamente al registro de solicitudes o consultar la lista existente."
        );
    }
}
