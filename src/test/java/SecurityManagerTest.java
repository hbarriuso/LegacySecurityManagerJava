import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class SecurityManagerTest {
    private BufferedReader in;
    private PrintStream out;
    private SecurityManagerStub securityManagerStub;

    @BeforeMethod
    public void setUp() {
        in = mock(BufferedReader.class);
        out = mock(PrintStream.class);
        securityManagerStub = new SecurityManagerStub(in, out);
    }

    @Test
    public void testEverythingIsOk() throws IOException {
        when(in.readLine()).thenReturn("Pepe", "Pepe Sanchez", "12345678");
        securityManagerStub.innerCreateUser();

        verify(out).println("Saving Details for User (Pepe, Pepe Sanchez, 87654321)\n");
    }

    @Test
    public void testPasswordsDontMatch() throws IOException {
        when(in.readLine()).thenReturn("Pepe", "Pepe Sanchez", "12345678", "12345679");
        securityManagerStub.innerCreateUser();

        verify(out).println("The passwords don't match");
    }

    @Test
    public void testPasswordMustBeAtLeastEightCharacters() throws IOException {
        when(in.readLine()).thenReturn("Pepe", "Pepe Sanchez", "123456", "123456");
        securityManagerStub.innerCreateUser();

        verify(out).println("Password must be at least 8 characters in length");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testNullPointerExceptionIsThrownWhenAnIOExceptionIsThrownBeforePasswordIsGiven() throws IOException {
        when(in.readLine()).thenThrow(new IOException("Fail!"));
        securityManagerStub.innerCreateUser();
    }

    @Test
    public void testIOExceptionIsCatched() throws IOException {
        IOException expected = new IOException("Fail!");
        when(in.readLine()).thenReturn("Pepe", "Pepe Sanchez", "12345678").thenThrow(expected);
        securityManagerStub.innerCreateUser();
        assertEquals(securityManagerStub.catchedException, expected);
    }

    private class SecurityManagerStub extends SecurityManager {

        private final BufferedReader in;
        private final PrintStream out;
        private IOException catchedException;

        public SecurityManagerStub(BufferedReader in, PrintStream out) {
            super();
            this.in = in;
            this.out = out;
        }

        @Override
        protected String readLine() throws IOException {
            return in.readLine();
        }

        @Override
        protected void writeToConsole(String line) {
            out.println(line);
        }

        @Override
        protected void printIOException(IOException e) {
            catchedException = e;
        }
    }
}
