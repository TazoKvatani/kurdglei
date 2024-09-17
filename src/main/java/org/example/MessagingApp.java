package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

public class MessagingApp {

    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("Messaging App");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Create and set up components
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
                String message = messageField.getText();
                if (!message.isEmpty()) {
                    // Encrypt the message
                    int timeIndicator = getTimeIndicator();
                    int dayOfWeek = getDayOfWeek();
                    String encryptedMessage = encrypt(message, timeIndicator, dayOfWeek);

                    // Display the encrypted message in the chat area
                    chatArea.append("You: " + encryptedMessage + "\n");

                    // Clear the input field
                    messageField.setText("");
                }
            }
        });

        // Display the frame
        frame.setVisible(true);
    }

    /**
     * Encrypts a message by shifting letters based on a computed coefficient.
     *
     * @param message        The message to be encrypted.
     * @param timeIndicator  The time when the message was sent (1 for AM, 2 for PM).
     * @param dayOfWeek      The day of the week the message was sent (1 for Monday, 2 for Tuesday, etc.).
     * @return               The encrypted message.
     */
    public static String encrypt(String message, int timeIndicator, int dayOfWeek) {
        int messageLength = message.length();
        int coefficient = (messageLength + timeIndicator + dayOfWeek) % 26;
        StringBuilder encryptedMessage = new StringBuilder();

        // Encrypt each character in the message
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

    /**
     * Gets the time indicator based on the current time.
     *
     * @return 1 for AM, 2 for PM
     */
    public static int getTimeIndicator() {
        LocalDateTime now = LocalDateTime.now();
        return now.getHour() < 12 ? 1 : 2;
    }

    /**
     * Gets the day of the week as an integer (1 for Monday, 2 for Tuesday, etc.).
     *
     * @return Day of the week as an integer
     */
    public static int getDayOfWeek() {
        LocalDateTime now = LocalDateTime.now();
        return now.getDayOfWeek().getValue();
    }
}
