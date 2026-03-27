package nl.biblioblabla.pro.dto;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String email;
    private String wachtwoord;

    public LoginRequest() {
    }

    public LoginRequest(String email, String wachtwoord) {
        this.email = email;
        this.wachtwoord = wachtwoord;
    }

    // -- Getters --

    public String getEmail() {
        return email;
    }

    public String getWachtwoord() {
        return wachtwoord;
    }

    // -- Setters --

    public void setEmail(String email) {
        this.email = email;
    }

    public void setWachtwoord(String wachtwoord) {
        this.wachtwoord = wachtwoord;
    }
}
