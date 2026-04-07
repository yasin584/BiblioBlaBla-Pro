package nl.biblioblabla.pro.controller;

import nl.biblioblabla.pro.model.LoginRequest;
import nl.biblioblabla.pro.model.LoginResponse;
import nl.biblioblabla.pro.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> tryLogin(@RequestBody LoginRequest loginRequest) {

            LoginResponse loginResponse = authService.tryLogin(loginRequest);

            return ResponseEntity.ok(loginResponse);
        }
    }
