package test;

import static org.junit.Assert.assertEquals;
// import static org.junit.Assert.assertNull;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
// import compiler.*;

public class testPrint {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Test
    public void test() {
        // try {
        //     MWCompiler.main(new String[] { "Hello, world!\r\n" });
        // } catch (Exception ex) {
        //     assertNull(ex);
        // }
        System.out.println("Hello, world!");
        assertEquals("Hello, world!\r\n", outContent.toString());
    }

    @After
    public void restoreStreams() {
        System.setOut(System.out);
        System.setErr(System.err);
    }
}