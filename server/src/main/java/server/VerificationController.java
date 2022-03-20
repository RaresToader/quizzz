package server;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ThreadLocalRandom;

@RestController
public class VerificationController {

    /**
     * Empty constructor
     */
    public VerificationController() {
        //Object mapping purposes
    }

    /**
     * Provides number for QuizzServer validation
     * @return number from range <100,110>
     */
    @GetMapping("/verification")
    public int verifyServer() {
        return ThreadLocalRandom.current().nextInt(100, 110 + 1);
    }
}
