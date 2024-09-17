package HermesExe;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;

public class MessagingClient {

    private static final String SERVER_ADDRESS = "localhost"; // Change this to the server's IP address
    private static final int SERVER_PORT = 12345;

    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;

    public static void main(String[] args) {
        showLoginScreen();
    }

    public static void showLoginScreen() {
        JFrame frame = new JFrame("Login");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(50, 30, 200, 25);
        frame.add(usernameField);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(50, 70, 200, 25);
        frame.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(50, 110, 90, 25);
        frame.add(loginButton);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(160, 110, 90, 25);
        frame.add(registerButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String username = usernameField.getText();
                    String password = new String(passwordField.getPassword());
                    sendRequest("LOGIN " + username + " " + password);
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(frame, "Error logging in.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String username = usernameField.getText();
                    String password = new String(passwordField.getPassword());
                    sendRequest("REGISTER " + username + " " + password);
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(frame, "Error registering.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.setVisible(true);
    }

    public static void sendRequest(String request) throws IOException {
        if (socket == null || socket.isClosed()) {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        out.println(request);
        String response = in.readLine();
        JOptionPane.showMessageDialog(null, response);

        if (request.startsWith("LOGIN") && response.equals("Login successful")) {
            showChatScreen();
        }
    }

    public static void showChatScreen() {
        JFrame frame = new JFrame("Messaging Client");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JTextArea chatArea = new JTextArea();
        chatArea.setBounds(20, 20, 450, 250);
        chatArea.setEditable(false);
        frame.add(chatArea);

        JTextField recipientField = new JTextField();
        recipientField.setBounds(20, 280, 150, 25);
        frame.add(recipientField);

        JTextField messageField = new JTextField();
        messageField.setBounds(20, 310, 350, 25);
        frame.add(messageField);

        JButton sendButton = new JButton("Send");
        sendButton.setBounds(380, 310, 90, 25);
        frame.add(sendButton);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String recipient = recipientField.getText();
                    String message = messageField.getText();
                    if (!recipient.isEmpty() && !message.isEmpty()) {
                        int timeIndicator = getTimeIndicator();
                        int dayOfWeek = getDayOfWeek();
                        String encryptedMessage = encrypt(message, timeIndicator, dayOfWeek);

                        // Send the encrypted message to the specified recipient
                        sendRequest("MESSAGE " + recipient + " " + encryptedMessage);

                        // Display the encrypted message in the chat area
                        chatArea.append("You: " + encryptedMessage + "\n");

                        // Clear the input fields
                        recipientField.setText("");
                        messageField.setText("");
                    }
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(frame, "Error sending message.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.setVisible(true);
    }

    public static int getTimeIndicator() {
        LocalDateTime now = LocalDateTime.now();
        return now.getHour() < 12 ? 1 : 2;
    }

    public static int getDayOfWeek() {
        LocalDateTime now = LocalDateTime.now();
        return now.getDayOfWeek().getValue();
    }

    public static String encrypt(String message, int timeIndicator, int dayOfWeek) {
        int messageLength = message.length();
        int coefficient = (messageLength + timeIndicator + dayOfWeek) % 26;
        StringBuilder encryptedMessage = new StringBuilder();

        for (char c : message.toCharArray()) {
            if (Character.isLetter(c)) {
                char lowerCaseChar = Character.toLowerCase(c);
                char encryptedChar = (char) ((lowerCaseChar - 'a' + coefficient) % 26 + 'a');
                encryptedMessage.append(encryptedChar);
            } else {
                encryptedMessage.append(c); // Preserve non-letter characters
            }
        }

        return encryptedMessage.toString();
    }
    public static String encrypt2(String message, int timeIndicator, int dayOfWeek) {
        int messageLength = message.length();
        int coefficient = (messageLength + timeIndicator + dayOfWeek) % 26;
        StringBuilder encryptedMessage = new StringBuilder();

        for (char c : message.toCharArray()) {
            if (Character.isLetter(c)) {
                char lowerCaseChar = Character.toLowerCase(c);
                char encryptedChar = (char) ((lowerCaseChar - 'a' + coefficient) % 26 + 'a');
                encryptedMessage.append(encryptedChar);
            } else {
                encryptedMessage.append(c); // Preserve non-letter characters
            }
        }

        return encryptedMessage.toString();
    }
}
