package service;

import DAO.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.MultiplePeopleRequest;
import request.RegisterRequest;
import response.MultiplePeopleResponse;

import static org.junit.jupiter.api.Assertions.*;

public class MultiplePeopleServiceTest {

    private String authtoken;

    private MultiplePeopleRequest request;
    private MultiplePeopleResponse response;

    @BeforeEach
    public void setUp() throws DataAccessException {
        ClearService.clear();

        authtoken = RegisterService.register(new RegisterRequest(
                "shilongcui",
                "130551",
                "seryu.lucky@gmail.com",
                "jay",
                "cui",
                "m"
        )).getTokenStr();
    }

    @AfterEach
    public void tearDown() {}

    @Test
    public void getPeoplePass() {
        request = new MultiplePeopleRequest(authtoken);

        response = MultiplePeopleService.getPeople(request);

        assertNotNull(response.getData());
        assertTrue(response.isSuccess());
    }

    @Test
    public void getEventsFail() {
        request = new MultiplePeopleRequest("authtoken");

        response = MultiplePeopleService.getPeople(request);

        assertEquals("Error: invalid auth token", response.getMessage());
        assertFalse(response.isSuccess());
    }
}
