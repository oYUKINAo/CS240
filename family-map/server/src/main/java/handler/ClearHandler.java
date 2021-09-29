package handler;

import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.HttpURLConnection;

import service.ClearService;
import response.ClearResponse;


public class ClearHandler extends POSTHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            // Determine the HTTP request type (GET, POST, etc.)
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                ClearResponse response = ClearService.clear();

                // Start sending the HTTP response to the client
                if (response.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                }
                else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }

                // Send response back to the client
                sendResponse(response, exchange.getResponseBody());
            }
            else {
                // We expected a POST but got something else, so we return a "bad request" status code to the client.
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

                // Send response back to the client
                sendResponse(new ClearResponse("Error: Http Bad Request"), exchange.getResponseBody());
            }
        }
        catch (IOException e) {
            // Some kind of internal error has occurred inside the server (not the client's fault), so we return an "internal server error" status code to the client.
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);

            // Display/log the stack trace
            e.printStackTrace();
        }
        finally {
            exchange.getResponseBody().close();
        }
    }
}
