package ni.edu.uam.gestionclientes.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Request{

    private int id;
    private Client client;
    private LocalDate requestDate;
    private String status;       // e.g. "Pendiente", "Atendida", "Rechazada"
    private String notes;        // optional free-text field
}