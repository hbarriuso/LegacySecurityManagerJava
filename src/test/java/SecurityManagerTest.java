import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class SecurityManagerTest {
    private BufferedReader buffer;
    private PrintStream out;
    private PrintStream err;
    private SecurityManager securityManager;

    @BeforeMethod
    public void setUp() {
        buffer = mock(BufferedReader.class);
        out = mock(PrintStream.class);
        err = mock(PrintStream.class);
        securityManager = new SecurityManager(buffer, out, err);
    }

    @Test
    public void testEverythingIsOk() throws IOException {
        when(buffer.readLine()).thenReturn("Pepe", "Pepe Sanchez", "12345678");

        securityManager.innerCreateUser();

        verify(out).println("Saving Details for User (Pepe, Pepe Sanchez, 87654321)\n");
    }

    @Test
    public void testPasswordsDontMatch() throws IOException {
        when(buffer.readLine()).thenReturn("Pepe", "Pepe Sanchez", "12345678", "12345679");

        securityManager.innerCreateUser();

        verify(out).println("The passwords don't match");
    }

    @Test
    public void testPasswordMustBeAtLeastEightCharacters() throws IOException {
        when(buffer.readLine()).thenReturn("Pepe", "Pepe Sanchez", "123456", "123456");

        securityManager.innerCreateUser();

        verify(out).println("Password must be at least 8 characters in length");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testNullPointerExceptionIsThrownWhenAnIOExceptionIsThrownBeforePasswordIsGiven() throws IOException {
        when(buffer.readLine()).thenThrow(new IOException("Fail!"));
        securityManager.innerCreateUser();
    }

    @Test
    public void testIOExceptionIsCatched() throws IOException {
        IOException expected = new IOException("Fail!");
        when(buffer.readLine()).thenReturn("Pepe", "Pepe Sanchez", "12345678").thenThrow(expected);
        securityManager.innerCreateUser();
        verify(err).println(expected);
    }
}
