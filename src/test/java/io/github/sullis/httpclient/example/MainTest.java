package io.github.sullis.httpclient.example;

import io.atlassian.fugue.Pair;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest
    extends AbstractTest {

    @Test
    public void happyPath() {
         Main main = new Main();
         Pair<ExitCode, Optional<String>> result = main.execute(new String[] {
            "--verbose",
            "--input",
            TestUtil.twelveUrls.toPath().toString()
         });
         assertEquals(Optional.empty(), result.right());
         assertEquals(ExitCode.OK, result.left());
    }

}
