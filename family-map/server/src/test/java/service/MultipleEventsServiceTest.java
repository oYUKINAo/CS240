package service;

import DAO.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.MultipleEventsRequest;
import request.RegisterRequest;
import response.MultipleEventsResponse;

import static org.junit.jupiter.api.Assertions.*;

public class MultipleEventsServiceTest {

    private String authtoken;

    private MultipleEventsRequest request;
    private MultipleEventsResponse response;

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
    public void getEventsPass() {
        request = new MultipleEventsRequest(authtoken);

        response = MultipleEventsService.getEvents(request);

        assertNotNull(response.getData());
        assertTrue(response.isSuccess());
    }

    @Test
    public void getEventsFail() {
        request = new MultipleEventsRequest("authtoken");

        response = MultipleEventsService.getEvents(request);

        assertEquals("Error: invalid auth token", response.getMessage());
        assertFalse(response.isSuccess());
    }
}
