package handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import service.MultiplePeopleService;
import request.MultiplePeopleRequest;
import response.MultiplePeopleResponse;

import java.io.IOException;
import java.net.HttpURLConnection;

public class MultiplePeopleHandler extends GETHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
                Headers headers = exchange.getRequestHeaders();
                if (headers.containsKey("Authorization")) {
                    String authToken = headers.getFirst("Authorization");

                    MultiplePeopleRequest request = new MultiplePeopleRequest(authToken);
                    MultiplePeopleResponse response = MultiplePeopleService.getPeople(request);

                    if (response.isSuccess()) {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    }
                    else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    }
                    sendResponse(response, exchange.getResponseBody());
                }
                else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
                    sendResponse(new MultiplePeopleResponse("Error: Unauthorized"), exchange.getResponseBody());
                }
            }
            else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                sendResponse(new MultiplePeopleResponse("Error: Http Bad Request"), exchange.getResponseBody());
            }
        }
        catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            e.printStackTrace();
        }
        finally {
            exchange.getResponseBody().close();
        }
    }
}
