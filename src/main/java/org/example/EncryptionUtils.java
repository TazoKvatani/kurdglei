package org.example;

public class EncryptionUtils {

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

        for (char c : message.toCharArray()) {
            if (Character.isLetter(c)) {
                char lowerCaseChar = Character.toLowerCase(c);
                char encryptedChar = (char) ((lowerCaseChar - 'a' + coefficient) % 26 + 'a');
                encryptedMessage.append(encryptedChar);
            } else {
                encryptedMessage.append(c);
            }
        }

        return encryptedMessage.toString();
    }

    /**
     * Decrypts a message by shifting letters back based on a computed coefficient.
     *
     * @param encryptedMessage The encrypted message to be decrypted.
     * @param timeIndicator    The time when the message was sent (1 for AM, 2 for PM).
     * @param dayOfWeek        The day of the week the message was sent (1 for Monday, 2 for Tuesday, etc.).
     * @return                 The decrypted message.
     */
    public static String decrypt(String encryptedMessage, int timeIndicator, int dayOfWeek) {
        int messageLength = encryptedMessage.length();
        int coefficient = (messageLength + timeIndicator + dayOfWeek) % 26;
        int reverseCoefficient = (26 - coefficient) % 26;
        StringBuilder decryptedMessage = new StringBuilder();


        for (char c : encryptedMessage.toCharArray()) {
            if (Character.isLetter(c)) {
                char lowerCaseChar = Character.toLowerCase(c);
                char decryptedChar = (char) ((lowerCaseChar - 'a' + reverseCoefficient) % 26 + 'a');
                decryptedMessage.append(decryptedChar);
            } else {
                decryptedMessage.append(c);
            }
        }

        return decryptedMessage.toString();
    }

    public static void main(String[] args) {
        String message = "Hello, World!";
        int timeIndicator = 2;
        int dayOfWeek = 2;

        String encryptedMessage = encrypt(message, timeIndicator, dayOfWeek);
        System.out.println("Encrypted Message: " + encryptedMessage);

        String decryptedMessage = decrypt(encryptedMessage, timeIndicator, dayOfWeek);
        System.out.println("Decrypted Message: " + decryptedMessage);
    }
}
