package handler;

import com.sun.net.httpserver.HttpExchange;
import service.FillService;
import request.FillRequest;
import response.FillResponse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;

public class FillHandler extends POSTHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {

                URI uri = exchange.getRequestURI();
                String path = uri.getPath();
                String[] parts = path.split("/");
                String username = parts[2]; // empty string before slash

                int numGenerations;
                if (parts.length == 3) {
                    numGenerations = 4;
                }
                else {
                    numGenerations = Integer.parseInt(parts[3]);
                }

                FillRequest request = new FillRequest(username, numGenerations);
                FillResponse response = FillService.fill(request);

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
                sendResponse(new FillResponse("Error: Http Bad Request"), exchange.getResponseBody());
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
