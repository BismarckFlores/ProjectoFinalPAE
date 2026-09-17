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
import javafx.scene.layout.VBox;
import ni.edu.uam.gestionclientes.models.User;
import ni.edu.uam.gestionclientes.util.AlertHelper;
import ni.edu.uam.gestionclientes.util.SceneManager;
import ni.edu.uam.gestionclientes.util.Session;

/**
 * Controlador de la Ventana Principal / Menú de Navegación del Sistema.
 * Administra las interacciones con MenuBar, ToolBar, ContextMenu y botones de acceso directo.
 */
public class MainMenuController {

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

    // --- Componentes de la Barra de Herramientas (ToolBar) ---
    @FXML
    private ToolBar toolBar;

    @FXML
    private Button btnToolBarRegistro;

    @FXML
    private Button btnToolBarConsulta;

    @FXML
    private Button btnToolBarCerrarSesion;

    // --- Botones Principales de Acceso Directo ---
    @FXML
    private Button btnRegistrarCliente;

    @FXML
    private Button btnConsultarClientes;

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private Button btnSalir;

    // --- Panel de Administración (solo visible para el rol Admin) ---
    @FXML
    private MenuItem menuAdminPanel;

    @FXML
    private Button btnToolBarAdminPanel;

    @FXML
    private Button btnAdminPanel;

    // --- Área de trabajo donde se activa el Menú Contextual ---
    @FXML
    private VBox workspacePane;

    // --- Menú Contextual (ContextMenu) ---
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

    /**
     * Inicialización del controlador.
     */
    @FXML
    public void initialize() {
        System.out.println("[MainMenuController] Inicializado correctamente.");

        User currentUser = Session.getCurrentUser();
        boolean isAdmin = Session.isAdmin();

        if (lblEstado != null) {
            lblEstado.setText(currentUser == null
                    ? "Sistema activo - Menú Principal"
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

        // VBox no es un Control: no tiene soporte nativo de ContextMenu, así que
        // se muestra manualmente al detectar el evento de clic derecho.
        if (workspacePane != null && contextMenu != null) {
            workspacePane.setOnContextMenuRequested(event ->
                    contextMenu.show(workspacePane, event.getScreenX(), event.getScreenY()));
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

    /**
     * Acción para navegar a la ventana de Registro de Cliente.
     */
    @FXML
    public void onMenuRegistroClick(ActionEvent event) {
        if (!Session.isAdmin()) {
            AlertHelper.showWarning("Acceso restringido", null,
                    "Su cuenta tiene acceso de solo lectura y no puede registrar clientes.");
            return;
        }
        SceneManager.sceneChange("client-registration-view.fxml");
    }

    /**
     * Acción para navegar al Panel de Administración (solo rol Admin).
     */
    @FXML
    public void onAdminPanelClick(ActionEvent event) {
        if (!Session.isAdmin()) {
            AlertHelper.showWarning("Acceso restringido", null,
                    "Solo un administrador puede acceder al panel de administración.");
            return;
        }
        SceneManager.sceneChange("admin-panel-view.fxml");
    }

    /**
     * Acción para navegar a la ventana de Consulta de Clientes.
     */
    @FXML
    public void onMenuConsultaClick(ActionEvent event) {
        SceneManager.sceneChange("client-list-view.fxml");
    }

    /**
     * Acción para cerrar sesión y retornar a la pantalla de Login.
     */
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

    /**
     * Acción para salir y cerrar la aplicación.
     */
    @FXML
    public void onSalirClick(ActionEvent event) {
        boolean confirmar = AlertHelper.showConfirmation(
                "Salir de la Aplicación",
                "Confirmación de Cierre",
                "¿Está seguro que desea salir del sistema de gestión de clientes?"
        );
        if (confirmar) {
            System.out.println("[MainMenuController] Cerrando aplicación.");
            Platform.exit();
            System.exit(0);
        }
    }

    /**
     * Acción para mostrar el diálogo Informativo "Acerca De".
     */
    @FXML
    public void onAcercaDeClick(ActionEvent event) {
        AlertHelper.showInfo(
                "Acerca del Sistema",
                "Sistema de Registro y Consulta de Clientes v1.0",
                "Desarrollado en JavaFX con arquitectura MVC.\nAsignatura: Programación de Aplicaciones de Escritorio."
        );
    }

    /**
     * Acción del Menú Contextual para mostrar información contextual del elemento seleccionado.
     */
    @FXML
    public void onContextInfoClick(ActionEvent event) {
        AlertHelper.showInfo(
                "Información del Sistema",
                "Opciones Contextuales",
                "Desde este menú principal puedes acceder rápidamente al registro de solicitudes o consultar la lista existente."
        );
    }
}
