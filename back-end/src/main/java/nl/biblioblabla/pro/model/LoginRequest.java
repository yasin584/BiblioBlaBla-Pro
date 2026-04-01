package nl.biblioblabla.pro.model;

import lombok.Getter;
import lombok.Setter;

@Setter
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
}