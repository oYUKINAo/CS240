package service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import response.RegisterResponse;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterServiceTest {

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;

    private RegisterRequest request;
    private RegisterResponse response;

    @BeforeEach
    public void setUp() {
        ClearService.clear();

        username = "shilongcui";
        password = "130551";
        email = "seryu.lucky@gmail.com";
        firstName = "jay";
        lastName = "cui";
        gender = "m";
    }

    @AfterEach
    public void tearDown() {}

    @Test
    public void registerPass() {
        request = new RegisterRequest(username, password, email, firstName, lastName, gender);

        response = RegisterService.register(request);

        assertEquals(username, response.getUsername());
        assertTrue(response.isSuccess());
    }

    @Test
    public void registerFail() {
        RegisterService.register(new RegisterRequest(username, password, email, firstName, lastName, gender));

        request = new RegisterRequest(username, password, email, firstName, lastName, gender);

        response = RegisterService.register(request);

        assertEquals("Error: username already taken", response.getMessage());
        assertFalse(response.isSuccess());
    }
}
