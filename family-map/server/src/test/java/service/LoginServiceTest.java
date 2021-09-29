package service;

import DAO.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import request.RegisterRequest;
import response.LoginResponse;

import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest {

    private String username;
    private String password;

    private LoginRequest request;
    private LoginResponse response;

    @BeforeEach
    public void setUp() throws DataAccessException {
        username = "shilongcui";
        password = "130551";

        RegisterService.register(new RegisterRequest(
                "shilongcui",
                "130551",
                "seryu.lucky@gmail.com",
                "jay",
                "cui",
                "m")
        );
    }

    @AfterEach
    public void tearDown() {}

    @Test
    public void loginPass() {
        request = new LoginRequest(username, password);

        response = LoginService.login(request);

        assertEquals(username, response.getUsername());
        assertTrue(response.isSuccess());
    }

    @Test
    public void loginUsernameFail() {
        request = new LoginRequest("shilongjaycui", password);

        response = LoginService.login(request);

        assertEquals("Error: user not found in the database", response.getMessage());
        assertFalse(response.isSuccess());
    }

    @Test
    public void loginPasswordFail() {
        request = new LoginRequest(username, "130501");

        response = LoginService.login(request);

        assertEquals("Error: wrong password", response.getMessage());
        assertFalse(response.isSuccess());
    }
}
