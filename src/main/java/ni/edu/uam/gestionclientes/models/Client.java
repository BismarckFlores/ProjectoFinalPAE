package ni.edu.uam.gestionclientes.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Client {

    private String id;
    private String firstName;
    private String lastName;
    private String customerType;
    private String city;
    private LocalDate birthDate;
    private String requestType;
    private List<String> servicesOfInterest;
    private String imagePath;

}