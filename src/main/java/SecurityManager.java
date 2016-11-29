
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SecurityManager {
    private BufferedReader buffer;

    public static void createUser() {
        new SecurityManager().innerCreateUser();
    }

    public void innerCreateUser() {
        buffer = new BufferedReader(new InputStreamReader(System.in));

        String username = null;
        String fullName = null;
        String password = null;
        String confirmPassword = null;
        try {
            writeToConsole("Enter a username");
            username = readLine();
            writeToConsole("Enter your full name");
            fullName = readLine();
            writeToConsole("Enter your password");
            password = readLine();
            writeToConsole("Re-enter your password");
            confirmPassword = readLine();
        } catch (IOException e) {
            printIOException(e);
        }

        if (!password.equals(confirmPassword)) {
            writeToConsole("The passwords don't match");
            return;
        }

        if (password.length() < 8) {
            writeToConsole("Password must be at least 8 characters in length");
            return;
        }

        // Encrypt the password (just reverse it, should be secure)
        String encryptedPassword = new StringBuilder(password).reverse().toString();

        writeToConsole(String.format(
                "Saving Details for User (%s, %s, %s)\n",
                username,
                fullName,
                encryptedPassword));
    }

    protected void printIOException(IOException e) {
        e.printStackTrace();
    }

    protected void writeToConsole(String line) {
        System.out.println(line);
    }

    protected String readLine() throws IOException {
        return buffer.readLine();
    }
}
