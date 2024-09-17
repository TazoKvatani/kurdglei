package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;

public class MessagingClient {

    private static final String SERVER_ADDRESS = "localhost"; // Change this to the server's IP address
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("Messaging Client");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JTextArea chatArea = new JTextArea();
        chatArea.setBounds(20, 20, 450, 250);
        chatArea.setEditable(false);
        frame.add(chatArea);

        JTextField messageField = new JTextField();
        messageField.setBounds(20, 280, 350, 25);
        frame.add(messageField);

        JButton sendButton = new JButton("Send");
        sendButton.setBounds(380, 280, 90, 25);
        frame.add(sendButton);

        // Add action listener to the send button
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String message = messageField.getText();
                    if (!message.isEmpty()) {
                        int timeIndicator = getTimeIndicator();
                        int dayOfWeek = getDayOfWeek();
                        String encryptedMessage = encrypt(message, timeIndicator, dayOfWeek);

                        // Send the encrypted message
                        sendMessageToServer(encryptedMessage);

                        // Display the encrypted message in the chat area
                        chatArea.append("You: " + encryptedMessage + "\n");

                        // Clear the input field
                        messageField.setText("");
                    }
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(frame, "Error sending message.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Display the frame
        frame.setVisible(true);
    }

    public static void sendMessageToServer(String message) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            out.println(message);
        }
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
}
