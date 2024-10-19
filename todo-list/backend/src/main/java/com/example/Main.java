package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Main {

    // TODO - hardcode
    public static void main(String[] args) throws Exception {
        // Connect to database
        Connection conn = connectDB("jdbc:postgresql://localhost:5432/postgres");

        // Set up HTTP server
        int port = 8000;
        HttpServer server = setupServer(port);
        server.start();
        System.out.printf("Server on port %d\n", port);
    }

    // TODO - hardcode, logging
    public static Connection connectDB(String connURL) {
        Connection conn = null;
        Properties props = new Properties();

        props.put("user", "postgres");
        props.put("password", "");

        try {
            conn = DriverManager.getConnection(connURL, props);
        } catch (SQLException e) {
            System.out.println(e.toString());
            System.exit(1);
        }

        return conn;
    }

    // TODO - logging
    public static HttpServer setupServer(int port) {
        HttpServer s = null;

        try {
            s = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            System.out.println(e.toString());
            System.exit(1);
        }

        s.createContext("/", new MyHandler());

        return s;
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            StringBuilder request = new StringBuilder(); // part of response
            StringBuilder response = new StringBuilder(); // sent as full response
            InputStream ios = t.getRequestBody();

            int i;
            while ((i = ios.read()) != -1) {
                String s = String.format("%d is %s\n", i, (char) i);
                request.append((char) i);
                response.append(s);
            }

            response.append("\nYour request was: ").append(request.toString());

            OutputStream os = t.getResponseBody();
            t.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            t.sendResponseHeaders(200, response.length());

            os.write(response.toString().getBytes());
            os.close();
        }
    }
}
