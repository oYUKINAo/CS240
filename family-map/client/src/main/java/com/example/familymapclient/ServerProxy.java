package com.example.familymapclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import model.AuthToken;
import request.LoginRequest;
import request.MultipleEventsRequest;
import request.MultiplePeopleRequest;
import request.RegisterRequest;
import response.ClearResponse;
import response.LoginResponse;
import response.MultipleEventsResponse;
import response.MultiplePeopleResponse;
import response.RegisterResponse;
import response.SinglePersonResponse;

public class ServerProxy {
    private String prefix = "http://";
    private String host;
    private String port;

    private static <T> T deserialize(String value, Class<T> object) {
        return (new Gson()).fromJson(value, object);
    }

    private static <T> String serialize(T javaObject) {
        return new Gson().toJson(javaObject);
    }

    private void sendRequest(String JSon, OutputStream requestBody) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(requestBody);
        writer.write(JSon);
        writer.flush();
    }

    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    public ServerProxy(String host, String port) {
        this.host = host;
        this.port = port;
    }

    public void clear() throws IOException {
        URL url = new URL(prefix + host + ":" + port + "/clear");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(false);
        connection.connect();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream responseBody = connection.getInputStream();
            String outJson = readString(responseBody);
        }
    }

    public LoginResponse login(LoginRequest request) throws IOException {
        LoginResponse response;

        URL url = new URL(prefix + host + ":" + port + "/user/login");    // create URL instance: http://10.0.2.2:8080/user/login
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();    // open connection
        connection.setRequestMethod("POST");    // set request method to POST
        connection.setDoOutput(true);   // allow request body
        connection.connect();   // connect

        try (OutputStream requestBody = connection.getOutputStream()) {
            // write request body to OutputStream
            String inJson = serialize(request);
            sendRequest(inJson, requestBody);
        }

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            // read response body from InputStream
            InputStream responseBody = connection.getInputStream();
            String outJson = readString(responseBody);
            response = deserialize(outJson, LoginResponse.class);
        }
        else if (connection.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
            response = new LoginResponse("Error: Http Bad Request");
        }
        else {
            response = new LoginResponse("Error: Http Internal Error");
        }

        return response;
    }

    public RegisterResponse register(RegisterRequest request) throws IOException {
        RegisterResponse response;

        URL url = new URL(prefix + host + ":" + port + "/user/register");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.connect();

        try (OutputStream requestBody = connection.getOutputStream()) {
            String inJson = serialize(request);
            sendRequest(inJson, requestBody);
        }

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream responseBody = connection.getInputStream();
            String outJson = readString(responseBody);
            response = deserialize(outJson, RegisterResponse.class);
        }
        else if (connection.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
            response = new RegisterResponse("Error: Http Bad Request");
        }
        else {
            response = new RegisterResponse("Error: Http Internal Error");
        }
        return response;
    }

    public SinglePersonResponse getPerson(String personID, String tokenStr) throws IOException {
        URL url = new URL(prefix + host + ":" + port + "/person/" + personID);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.addRequestProperty("Authorization", tokenStr);
        connection.connect();

        SinglePersonResponse response;
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream responseBody = connection.getInputStream();
            String outJson = readString(responseBody);
            response = deserialize(outJson, SinglePersonResponse.class);
        }
        else if (connection.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
            response = new SinglePersonResponse("Error: Http Bad Request");
        }
        else {
            response = new SinglePersonResponse("Error: Http Internal Error");
        }

        return response;
    }

    public MultiplePeopleResponse getPeople(String tokenStr) throws IOException {
        URL url = new URL(prefix + host + ":" + port + "/person");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // set HTTP request header
        connection.addRequestProperty("Authorization", tokenStr);

        connection.connect();

        MultiplePeopleResponse response;
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream responseBody = connection.getInputStream();
            String outJson = readString(responseBody);
            response = deserialize(outJson, MultiplePeopleResponse.class);
        }
        else if (connection.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
            response = new MultiplePeopleResponse("Error: Http Bad Request");
        }
        else {
            response = new MultiplePeopleResponse("Error: Http Internal Error");
        }
        return response;
    }

    public MultipleEventsResponse getEvents(String tokenStr) throws IOException {
        URL url = new URL(prefix + host + ":" + port + "/event");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.addRequestProperty("Authorization", tokenStr);
        connection.connect();

        MultipleEventsResponse response;
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream responseBody = connection.getInputStream();
            String outJson = readString(responseBody);
            response = deserialize(outJson, MultipleEventsResponse.class);
        }
        else if (connection.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
            response = new MultipleEventsResponse("Error: Http Bad Request");
        }
        else {
            response = new MultipleEventsResponse("Error: Http Internal Error");
        }
        return response;
    }
}
