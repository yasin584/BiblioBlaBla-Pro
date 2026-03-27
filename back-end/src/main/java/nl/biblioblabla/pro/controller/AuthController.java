package nl.biblioblabla.pro.controller;

import nl.biblioblabla.pro.dto.LoginRequest;
import nl.biblioblabla.pro.dto.LoginResponse;
import nl.biblioblabla.pro.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
    
}
