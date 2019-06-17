package io.github.sullis.httpclient.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;


public class MainTest
    extends AbstractTest {

    @Test
    public void happyPath() {
         Main main = new Main();
         MainResult result = main.execute(new String[] {
            "--verbose",
            "--input",
            TestUtil.twelveUrls.toPath().toString()
         });
         assertEquals(Optional.empty(), result.message());
         assertEquals(ExitCode.OK, result.exitCode());
    }

}
