package nl.biblioblabla.pro.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String token;
    private int gebruikerId;

    public LoginResponse() {
    }

    public LoginResponse(String token, int gebruikerId) {
        this.token = token;
        this.gebruikerId = gebruikerId;
    }
}