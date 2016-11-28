
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class SecurityManager {
    private final BufferedReader in;
    private final PrintStream out;
    private final PrintStream err;

    public SecurityManager(BufferedReader in, PrintStream out, PrintStream err) {
        this.in = in;
        this.out = out;
        this.err = err;
    }

    public static void createUser() {
        final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        final PrintStream out = System.out;
        final PrintStream err = System.err;
        new SecurityManager(in, out, err).innerCreateUser();
    }

    public void innerCreateUser() {

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

    private String readLine() throws IOException {
        return in.readLine();
    }

    private void writeToConsole(String line) {
        out.println(line);
    }

    private void printIOException(IOException e) {
        e.printStackTrace(err);
    }
}
