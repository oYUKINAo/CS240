package handler;

import com.sun.net.httpserver.HttpExchange;
import service.LoginService;
import request.LoginRequest;
import response.LoginResponse;

import java.io.*;
import java.net.HttpURLConnection;

public class LoginHandler extends POSTHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                InputStream requestBody = exchange.getRequestBody();
                String inJson = readString(requestBody);

                LoginRequest request = JsonSerializer.deserialize(inJson, LoginRequest.class);
                LoginResponse response = LoginService.login(request);

                if (response.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                }
                else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
                sendResponse(response, exchange.getResponseBody());
            }
            else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                sendResponse(new LoginResponse("Error: Http Bad Request"), exchange.getResponseBody());
            }
        }
        catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            e.printStackTrace();
        }
        finally {
            exchange.getResponseBody().close();
        }
    }
}