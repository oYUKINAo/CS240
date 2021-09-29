package com.example.familymapclient;

import org.junit.After;
import org.junit.Test;
import org.junit.Before;

import java.io.IOException;

import request.LoginRequest;
import request.RegisterRequest;
import response.LoginResponse;
import response.MultipleEventsResponse;
import response.MultiplePeopleResponse;
import response.RegisterResponse;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ServerProxyTest {
    private String host = "localhost";
    private String port = "8080";
    private ServerProxy proxy;

    private String username = "jay";
    private String password = "cui";

    private String email = "shilong@byu.edu";
    private String firstName = "Jay";
    private String lastName = "Cui";
    private String gender = "m";

    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;

    @Before
    public void set_up() {
        proxy = new ServerProxy(host, port);
    }

    @After
    public void tear_down() throws IOException {
        proxy.clear();
    }

    // Login method
    @Test
    public void login_PASS() throws IOException {
        registerRequest = new RegisterRequest(username, password, email, firstName, lastName, gender);
        RegisterResponse registerResponse = proxy.register(registerRequest);

        loginRequest = new LoginRequest(username, password);
        LoginResponse loginResponse = proxy.login(loginRequest);

        assertTrue(loginResponse.isSuccess());
    }
    @Test
    public void login_FAIL() throws IOException {
        registerRequest = new RegisterRequest(username, password, email, firstName, lastName, gender);
        proxy.register(registerRequest);

        loginRequest = new LoginRequest("shiba", "inu");
        LoginResponse response = proxy.login(loginRequest);

        assertFalse(response.isSuccess());
    }

    // Registering a new user
    @Test
    public void register_PASS() throws IOException {
        registerRequest = new RegisterRequest(username, password, email, firstName, lastName, gender);
        RegisterResponse response = proxy.register(registerRequest);

        assertTrue(response.isSuccess());
    }
    @Test
    public void register_FAIL() throws IOException {
        registerRequest = new RegisterRequest(username, password, email, firstName, lastName, gender);
        RegisterResponse first_response = proxy.register(registerRequest);
        assertTrue(first_response.isSuccess());

        RegisterResponse second_response = proxy.register(registerRequest);
        assertFalse(second_response.isSuccess());
    }

    // Retrieving people related to a logged in/registered user
    @Test
    public void getPeople_PASS() throws IOException {
        registerRequest = new RegisterRequest(username, password, email, firstName, lastName, gender);
        RegisterResponse registerResponse = proxy.register(registerRequest);
        assertTrue(registerResponse.isSuccess());

        String tokenStr = registerResponse.getAuthorizationToken();
        MultiplePeopleResponse multiplePeopleResponse = proxy.getPeople(tokenStr);
        assertTrue(multiplePeopleResponse.isSuccess());
    }
    @Test
    public void getPeople_FAIL() throws IOException {
        registerRequest = new RegisterRequest(username, password, email, firstName, lastName, gender);
        RegisterResponse registerResponse = proxy.register(registerRequest);
        assertTrue(registerResponse.isSuccess());

        String tokenStr = "";
        MultiplePeopleResponse multiplePeopleResponse = proxy.getPeople(tokenStr);
        assertFalse(multiplePeopleResponse.isSuccess());
    }

    // Retrieving events related to a logged in/registered user
    @Test
    public void getEvents_PASS() throws IOException {
        registerRequest = new RegisterRequest(username, password, email, firstName, lastName, gender);
        RegisterResponse registerResponse = proxy.register(registerRequest);
        assertTrue(registerResponse.isSuccess());

        String tokenStr = registerResponse.getAuthorizationToken();
        MultipleEventsResponse multipleEventsResponse = proxy.getEvents(tokenStr);
        assertTrue(multipleEventsResponse.isSuccess());
    }
    @Test
    public void getEvents_FAIL() throws IOException {
        registerRequest = new RegisterRequest(username, password, email, firstName, lastName, gender);
        RegisterResponse registerResponse = proxy.register(registerRequest);
        assertTrue(registerResponse.isSuccess());

        String tokenStr = "";
        MultipleEventsResponse multipleEventsResponse = proxy.getEvents(tokenStr);
        assertFalse(multipleEventsResponse.isSuccess());
    }
}