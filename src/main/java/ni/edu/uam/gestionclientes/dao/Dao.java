package ni.edu.uam.gestionclientes.dao;

import javafx.collections.ObservableList;
import java.util.Optional;

public interface Dao<T, ID> {

    ObservableList<T> listing();

    Optional<T> searchByID(ID id);

    void save(T entity);

    boolean refresh(ID id, T entity);

    boolean delete(ID id);
}
