package nl.biblioblabla.pro.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor // Vervangt jouw handgeschreven constructor
public class User {

    private int id;
    private String naam;
    private String email;
    private String wachtwoordHash;
}
