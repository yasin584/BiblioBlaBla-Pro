package nl.biblioblabla.pro.controller;


import nl.biblioblabla.pro.model.LoginRequest;
import nl.biblioblabla.pro.model.LoginResponse;
import nl.biblioblabla.pro.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;


//    private LoginRequest testLoginRequest;

    @Test
    void tryLogin_ReturnsLoginResponseOK() {

        //arrange
        LoginRequest testLoginRequest = new LoginRequest("testMail", "testPassword");

        String testToken = "test.token.test";
        LoginResponse expectedResponse =  new LoginResponse(testToken,1);

        when(authService.tryLogin(testLoginRequest)).thenReturn(expectedResponse);


        //act
        ResponseEntity result = authController.tryLogin(testLoginRequest);


        //assert
        System.out.println("HttpStatus should be OK (200), result: " + result.getStatusCode());
        assertEquals(HttpStatus.OK, result.getStatusCode());

        assertEquals(expectedResponse, result.getBody());
    }

}