package ni.edu.uam.gestionclientes.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ni.edu.uam.gestionclientes.models.Role;
import ni.edu.uam.gestionclientes.models.User;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class UserDaoImpl implements Dao<User, Integer> {

    private static final UserDaoImpl INSTANCE = new UserDaoImpl();

    private final ObservableList<User> userList;
    private final AtomicInteger idSequence = new AtomicInteger(1);

    private UserDaoImpl() {
        this.userList = FXCollections.observableArrayList();
        userList.add(User.builder()
                .id(idSequence.get())
                .username("admin")
                .password("admin123")
                .active(true)
                .role(Role.ADMIN)
                .build());
    }

    public static UserDaoImpl getInstance() {
        return INSTANCE;
    }

    public Optional<User> validateCredentials(String username, String password) {
        return userList.stream()
                .filter(user -> user.isActive()
                        && user.getUsername().equalsIgnoreCase(username)
                        && user.getPassword().equals(password))
                .findFirst();
    }

    public boolean usernameExists(String username) {
        return userList.stream().anyMatch(user -> user.getUsername().equalsIgnoreCase(username));
    }

    public int nextId() {
        return idSequence.incrementAndGet();
    }

    @Override
    public ObservableList<User> listing() {
        return userList;
    }

    @Override
    public Optional<User> searchByID(Integer id) {
        return userList.stream().filter(user -> user.getId() == id).findFirst();
    }

    @Override
    public void save(User entity) {
        userList.add(entity);
    }

    @Override
    public boolean refresh(Integer id, User entity) {
        Optional<User> existingUser = searchByID(id);
        if (existingUser.isPresent()) {
            int index = userList.indexOf(existingUser.get());
            userList.set(index, entity);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        Optional<User> existingUser = searchByID(id);
        if (existingUser.isPresent()) {
            userList.remove(existingUser.get());
            return true;
        }
        return false;
    }
}
