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

//    public User(int id, String naam, String email, String wachtwoordHash) {
//        this.id = id;
//        this.naam = naam;
//        this.email = email;
//        this.wachtwoordHash = wachtwoordHash;
//    }
}
