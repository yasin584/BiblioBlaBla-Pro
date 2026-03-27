package nl.biblioblabla.pro.dto;

public class LoginResponse {
    private String token;
    private int gebruikerId;

    public LoginResponse() {
    }

    public LoginResponse(String token, int gebruikerId) {
        this.token = token;
        this.gebruikerId = gebruikerId;
    }

    // -- Getters --

    public String getToken() {  
        return token; 
    }

    public int getGebruikerId() {
        return gebruikerId;
    }

    // -- Setters --

    public void setToken(String token) {
        this.token = token;
    }

    public void setGebruikerId(int gebruikerId) {
        this.gebruikerId = gebruikerId;
    }
}
