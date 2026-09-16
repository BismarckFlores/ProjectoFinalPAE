package ni.edu.uam.gestionclientes.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private int id;
    private String username;
    private String password;
    private boolean active;
    private Role role;
}
