package org.gamescore;

import org.gamescore.controller.GameScoreController;
import org.gamescore.util.DatabaseConnection;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        properties.load(DatabaseConnection.class.getClassLoader().getResourceAsStream("application.properties"));

        int port = Integer.parseInt(properties.getProperty("server.port"));
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {

    private final Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String requestLine = in.readLine();
            System.out.println("Received request: " + requestLine);

            String headerLine;
            int contentLength = 0;
            while ((headerLine = in.readLine()) != null && !headerLine.isEmpty()) {
                if (headerLine.startsWith("Content-Length:")) {
                    contentLength = Integer.parseInt(headerLine.split(":")[1].trim());
                }
            }

            if (requestLine != null && requestLine.startsWith("POST") && requestLine.contains("/api/scores")) {
                handleSaveScoreRequest(in, out, contentLength);
            }
            else if (requestLine != null && requestLine.startsWith("GET") && requestLine.contains("/api/scores/user/")) {
                String[] parts = requestLine.split(" ");
                String[] pathParts = parts[1].split("/");
                int userId = Integer.parseInt(pathParts[4]);

                String response = new GameScoreController().getHighestScoresForUser(userId);
                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: text/html");
                out.println();
                out.println(response);
            }
            else if (requestLine != null && requestLine.startsWith("GET") && requestLine.contains("/api/scores/top10/")) {
                String[] parts = requestLine.split(" ");
                String[] pathParts = parts[1].split("/");
                int gameId = Integer.parseInt(pathParts[4]);

                String response = new GameScoreController().getTop10ScoresForGame(gameId);
                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: text/html");
                out.println();
                out.println(response);
            }
            else {
                out.println("HTTP/1.1 404 Not Found");
                out.println("Content-Type: text/html");
                out.println();
                out.println("<html><body><h1>Invalid Endpoint</h1></body></html>");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleSaveScoreRequest(BufferedReader in, PrintWriter out, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        int bytesRead = in.read(body, 0, contentLength);

        if (bytesRead == -1) {
            out.println("HTTP/1.1 400 Bad Request");
            out.println("Content-Type: text/html");
            out.println();
            out.println("<html><body><h1>Request body is empty!</h1></body></html>");
            return;
        }

        String requestBody = new String(body);

        if (requestBody.isEmpty()) {
            out.println("HTTP/1.1 400 Bad Request");
            out.println("Content-Type: text/html");
            out.println();
            out.println("<html><body><h1>Request body is empty!</h1></body></html>");
            return;
        }

        try {
            JSONObject json = new JSONObject(requestBody);

            String userIdStr = json.optString("userId");
            String gameIdStr = json.optString("gameId");
            String scoreStr = json.optString("score");

            if (userIdStr.isEmpty() || gameIdStr.isEmpty() || scoreStr.isEmpty()) {
                out.println("HTTP/1.1 400 Bad Request");
                out.println("Content-Type: text/html");
                out.println();
                out.println("<html><body><h1>Missing parameters: userId, gameId, or score</h1></body></html>");
                return;
            }

            try {
                int userId = Integer.parseInt(userIdStr);
                int gameId = Integer.parseInt(gameIdStr);
                int score = Integer.parseInt(scoreStr);

                GameScoreController controller = new GameScoreController();
                String result = controller.saveScore(userId, gameId, score);

                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: text/html");
                out.println();
                out.println("<html><body><h1>" + result + "</h1></body></html>");
            } catch (NumberFormatException e) {
                out.println("HTTP/1.1 400 Bad Request");
                out.println("Content-Type: text/html");
                out.println();
                out.println("<html><body><h1>Invalid number format for userId, gameId, or score</h1></body></html>");
            }

        } catch (org.json.JSONException e) {
            out.println("HTTP/1.1 400 Bad Request");
            out.println("Content-Type: text/html");
            out.println();
            out.println("<html><body><h1>Invalid JSON format</h1></body></html>");
        }
    }
}