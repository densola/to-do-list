package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
        HttpServer server = setupServer(port, conn);
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
    public static HttpServer setupServer(int port, Connection c) {
        HttpServer s = null;

        try {
            s = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            System.out.println(e.toString());
            System.exit(1);
        }

        s.createContext("/getTasks", new HandleGetTasks(c));
        s.createContext("/createTask", new HandleCreateTask(c));

        return s;
    }

    static class HandleGetTasks implements HttpHandler {
        private final Connection db;

        HandleGetTasks(Connection db) {
            this.db = db;
        }

        @Override
        public void handle(HttpExchange t) throws IOException {
            if (!t.getRequestMethod().equals("GET")) {
                return;
            }

            String response = "";

            try {
                PreparedStatement stmt = db.prepareStatement("SELECT name, info FROM tasks");
                ResultSet results = stmt.executeQuery();
                response = getTasksJSON(results);
            } catch (SQLException e) {
                System.out.println(e.toString());
            }

            t.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            t.sendResponseHeaders(200, response.length());

            try (OutputStream oStream = t.getResponseBody()) {
                oStream.write(response.getBytes());
            }
        }

        private String getTasksJSON(ResultSet r) throws SQLException {
            Tasks ts = new Tasks();

            while (r.next()) {
                ts.addTask(r.getString(1), r.getString(2));
            }

            return ts.toJSON();
        }
    }

    static class HandleCreateTask implements HttpHandler {
        private final Connection db;

        HandleCreateTask(Connection db) {
            this.db = db;
        }

        @Override
        public void handle(HttpExchange t) throws IOException {
            Task task = parseRequest(t.getRequestBody());

            try {
                PreparedStatement stmt = db.prepareStatement("INSERT INTO tasks (name, info) VALUES (?, ?)");
                stmt.setString(1, task.getName());
                stmt.setString(2, task.getInfo());
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }

        /**
         * parseRequest parses the request body returning
         * a Task.
         * 
         * @param inStream
         * @return Task with name and info from submission data.
         */
        private Task parseRequest(InputStream inStream) {
            StringBuilder builder = new StringBuilder();
            Task task = new Task();

            try {
                int i;
                while ((i = inStream.read()) != -1) {
                    if (isValidStreamVal(i)) {
                        if (i == 43) {
                            builder.append((char) 43);
                        } else {
                            builder.append((char) i);
                        }
                    } else {
                        System.out.println("Error: Passing in invalid data.");
                        throw new RuntimeException();
                    }
                }
            } catch (IOException e) {
                System.out.println(e.toString());
            }

            String request = builder.toString();
            String[] tsk = request.split("&");

            tsk[0] = tsk[0].replaceAll("name=", "");
            tsk[0] = tsk[0].replaceAll("[+]", " ");

            tsk[1] = tsk[1].replaceAll("info=", "");
            tsk[1] = tsk[1].replaceAll("[+]", " ");

            task.setName(tsk[0]);
            task.setInfo(tsk[1]);

            return task;
        }

        /**
         * isValidStream returns true if i is the ASCII int
         * value for characters 0-9, A-Z, a-z, =, &, and space.
         * 
         * @param i
         * @return
         */
        private boolean isValidStreamVal(int i) {
            return ((47 < i && i < 58) || (64 < i && i < 91) || (96 < i && i < 123) || i == 61 || i == 38 || i == 43);
        }
    }
}
