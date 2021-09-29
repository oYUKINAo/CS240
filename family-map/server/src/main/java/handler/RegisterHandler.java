package handler;

import com.sun.net.httpserver.HttpExchange;
import service.RegisterService;
import request.RegisterRequest;
import response.RegisterResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class RegisterHandler extends POSTHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                InputStream requestBody = exchange.getRequestBody();
                String inJson = readString(requestBody);

                RegisterRequest request = JsonSerializer.deserialize(inJson, RegisterRequest.class);
                RegisterResponse response = RegisterService.register(request);

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
                sendResponse(new RegisterResponse("Error: Http Bad Request"), exchange.getResponseBody());
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
