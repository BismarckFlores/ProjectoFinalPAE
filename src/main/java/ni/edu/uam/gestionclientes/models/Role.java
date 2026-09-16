package ni.edu.uam.gestionclientes.models;

/**
 * Enum para los roles de usuario en la aplicación.
 */
public enum Role {
    ADMIN("Administrador"),
    COMUN("Usuario Común");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}
