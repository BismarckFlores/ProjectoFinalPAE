package ni.edu.uam.gestionclientes.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ni.edu.uam.gestionclientes.models.Client;

import java.util.Optional;

public class ClientDaoImpl implements Dao<Client, String> {

    // Lista observable para almacenar los clientes en memoria.
    // Al ser ObservableList, el TableView (UI) se actualizará automáticamente cuando agreguemos datos.
    private final ObservableList<Client> clientList;

    public ClientDaoImpl() {
        this.clientList = FXCollections.observableArrayList();
    }

    @Override
    public ObservableList<Client> listing() {
        return clientList;
    }

    @Override
    public Optional<Client> searchByID(String id) {
        return clientList.stream()
                .filter(client -> client.getId() != null && client.getId().equalsIgnoreCase(id))
                .findFirst();
    }

    @Override
    public void save(Client entity) {
        clientList.add(entity);
    }

    @Override
    public boolean refresh(String id, Client entidad) {
        Optional<Client> existingClient = searchByID(id);
        if (existingClient.isPresent()) {
            int index = clientList.indexOf(existingClient.get());
            clientList.set(index, entidad);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        Optional<Client> existingClient = searchByID(id);
        if (existingClient.isPresent()) {
            clientList.remove(existingClient.get());
            return true;
        }
        return false;
    }
}
