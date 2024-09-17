package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EncryptionGUI {

    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("Message Encryptor");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Create and set up components
        JLabel messageLabel = new JLabel("Message:");
        messageLabel.setBounds(20, 20, 100, 25);
        frame.add(messageLabel);

        JTextField messageField = new JTextField();
        messageField.setBounds(130, 20, 230, 25);
        frame.add(messageField);

        JButton encryptButton = new JButton("Encrypt");
        encryptButton.setBounds(150, 60, 100, 30);
        frame.add(encryptButton);

        JLabel resultLabel = new JLabel("Encrypted Message:");
        resultLabel.setBounds(20, 100, 200, 25);
        frame.add(resultLabel);

        JTextArea resultArea = new JTextArea();
        resultArea.setBounds(20, 130, 350, 100);
        resultArea.setEditable(false);
        frame.add(resultArea);

        // Add action listener to the button
        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String message = messageField.getText();
                    
                    // Automatically determine time indicator and day of the week
                    int timeIndicator = getTimeIndicator();
                    int dayOfWeek = getDayOfWeek();
                    
                    // Encrypt the message
                    String encryptedMessage = encrypt(message, timeIndicator, dayOfWeek);

                    // Display the encrypted message
                    resultArea.setText(encryptedMessage);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "An error occurred during encryption.", "Error", JOptionPane.ERROR_MESSAGE);
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
