package handler;

import com.sun.net.httpserver.HttpHandler;

import response.Response;

import java.io.*;

abstract class Handler implements HttpHandler {
    protected void sendResponse(Response response, OutputStream responseBody) throws IOException {
        String JSon = JsonSerializer.serialize(response);
        try (OutputStreamWriter writer = new OutputStreamWriter(responseBody)) {
            writer.write(JSon);
            writer.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    protected String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }
}