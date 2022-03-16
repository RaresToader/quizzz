import org.junit.jupiter.api.Test;
import server.VerificationController;

import static org.junit.jupiter.api.Assertions.*;

public class VerificationControllerTest {
    @Test
    public void verificationControllerInitTest() {
        VerificationController x = new VerificationController();
        assertNotNull(x);
    }

    @Test
    public void verificationControllerRandomNumTest() {
        VerificationController x = new VerificationController();
        int genNum = x.verifyServer();
        assertTrue(genNum >= 100 && genNum <= 110);
    }
}
