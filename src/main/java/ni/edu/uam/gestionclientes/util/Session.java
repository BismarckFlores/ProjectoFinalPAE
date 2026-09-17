package ni.edu.uam.gestionclientes.util;

import ni.edu.uam.gestionclientes.models.Role;
import ni.edu.uam.gestionclientes.models.User;

public class Session {

    private static User currentUser;

    private Session() {
    }

    public static void login(User user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean isAdmin() {
        return currentUser != null && currentUser.getRole() == Role.ADMIN;
    }
}
