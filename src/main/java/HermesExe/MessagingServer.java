package HermesExe;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class MessagingServer {

    private static final int PORT = 12345;
    private static final String USER_FILE = "users.txt";
    private static final Map<String, String> users = new HashMap<>();
    private static final Map<String, PrintWriter> clientWriters = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        loadUsers();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private static void loadUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    users.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }

    private static void broadcastMessage(String message) {
        for (PrintWriter writer : clientWriters.values()) {
            writer.println(message);
        }
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                String request = in.readLine();
                if (request != null) {
                    handleRequest(request);
                }
            } catch (IOException e) {
                System.err.println("Error handling client: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Error closing socket: " + e.getMessage());
                }
                if (username != null) {
                    clientWriters.remove(username);
                }
            }
        }

        private void handleRequest(String request) {
            String[] parts = request.split(" ", 2);
            if (parts.length == 2) {
                String command = parts[0];
                String data = parts[1];

                switch (command) {
                    case "REGISTER":
                        handleRegister(data);
                        break;
                    case "LOGIN":
                        handleLogin(data);
                        break;
                    case "MESSAGE":
                        handleMessage(data);
                        break;
                    default:
                        out.println("Unknown command");
                        break;
                }
            }
        }

        private void handleRegister(String data) {
            String[] parts = data.split(" ", 2);
            if (parts.length == 2) {
                String username = parts[0];
                String password = parts[1];
                if (!users.containsKey(username)) {
                    users.put(username, password);
                    saveUser(username, password);
                    out.println("Registration successful");
                } else {
                    out.println("Username already exists");
                }
            } else {
                out.println("Invalid registration format");
            }
        }

        private void handleLogin(String data) {
            String[] parts = data.split(" ", 2);
            if (parts.length == 2) {
                String username = parts[0];
                String password = parts[1];
                if (users.get(username) != null && users.get(username).equals(password)) {
                    this.username = username;
                    clientWriters.put(username, out);
                    out.println("Login successful");
                } else {
                    out.println("Invalid username or password");
                }
            } else {
                out.println("Invalid login format");
            }
        }

        private void handleMessage(String data) {
            String[] parts = data.split(" ", 2);
            if (parts.length == 2) {
                String recipient = parts[0];
                String message = parts[1];
                if (clientWriters.containsKey(recipient)) {
                    clientWriters.get(recipient).println("Message from " + username + ": " + message);
                } else {
                    out.println("Recipient not found");
                }
            } else {
                out.println("Invalid message format");
            }
        }

        private void saveUser(String username, String password) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_FILE, true))) {
                bw.write(username + ":" + password);
                bw.newLine();
            } catch (IOException e) {
                System.err.println("Error saving user: " + e.getMessage());
            }
        }
    }
}
