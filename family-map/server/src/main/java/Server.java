import java.io.IOException;

import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;
import handler.*;

public class Server {

    // The maximum number of waiting incoming connections to queue.
    private static final int MAX_WAITING_CONNECTIONS = 12;

    // Java provides an HttpServer class that can be used to embed
    // an HTTP server in any Java program.
    // Using the HttpServer class, you can easily make a Java
    // program that can receive incoming HTTP requests, and respond
    // with appropriate HTTP responses.
    // HttpServer is the class that actually implements the HTTP network
    // protocol (be glad you don't have to).
    private HttpServer server;

    // This method initializes and runs the server.
    // The "portNumber" parameter specifies the port number on which the
    // server should accept incoming client connections.
    private void run(String portNumber) {

        System.out.println("Initializing HTTP Server...");

        try {
            server = HttpServer.create(new InetSocketAddress(Integer.parseInt(portNumber)), MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Indicate that we are using the default "executor".
        server.setExecutor(null);

        // Log message indicating that the server is creating and installing its HTTP handlers.
        System.out.println("Creating contexts...");

        // The HttpServer class listens for incoming HTTP requests: when one is received, it looks at the URL path inside the HTTP request, and forwards the request to the handler for that URL path.
        server.createContext("/", new FileHandler());

        server.createContext("/clear", new ClearHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/user/login", new LoginHandler());

        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/fill/", new FillHandler());
        server.createContext("/person/", new SinglePersonHandler());
        server.createContext("/person", new MultiplePeopleHandler());
        server.createContext("/event/", new SingleEventHandler());
        server.createContext("/event", new MultipleEventsHandler());

        // Log message indicating that the HttpServer is about the start accepting incoming client connections.
        System.out.println("Starting server...");

        // Tells the HttpServer to start accepting incoming client connections.
        // This method call will return immediately, and the "main" method for the program will also complete.
        // Even though the "main" method has completed, the program will continue running because the HttpServer object we created is still running in the background.
        server.start();

        // Log message indicating that the server has successfully started.
        System.out.println("Server started: Family Map Server listening on port " + portNumber + ".");
    }

    // "main" method for the server program
    // "args" should contain one command-line argument, which is the port number on which the server should accept incoming client connections.
    public static void main(String[] args) {
        String portNumber = args[0];    // has to be Port 8080
        new Server().run(portNumber);
    }
}
