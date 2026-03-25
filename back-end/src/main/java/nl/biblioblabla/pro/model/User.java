package nl.biblioblabla.pro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class User {

    private int id;
    private String naam;
    private String email;

    @JsonIgnore
    private String wachtwoordHash;

    public User() {
        // Een lege constructor voor frameworks
    }

    public User(int id, String naam, String email, String wachtwoordHash) {
        this.id = id;
        this.naam = naam;
        this.email = email;
        this.wachtwoordHash = wachtwoordHash;
    }

    // -- Getters --

    public int getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public String getEmail() {
        return email;
    }

    public String getWachtwoordHash() {
        return wachtwoordHash;
    }

    // -- Setters --

    public void setId(int id) {
        this.id = id;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setWachtwoordHash(String wachtwoordHash) {
        this.wachtwoordHash = wachtwoordHash;
    }
}
