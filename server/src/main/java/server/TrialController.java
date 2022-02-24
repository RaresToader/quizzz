package server;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TrialController {

    private List<String> names;

    public TrialController(List<String> names) {
        this.names = names;
    }


    @GetMapping("/trial")
    public String doSomething() {
        return "You tried something!";
    }

    @PostMapping("/trial")
    public ResponseEntity<String> addName(@RequestBody String name) {
        names.add(name);
        return ResponseEntity.ok(name);
    }

    @GetMapping("/trial/names")
    public List<String> getNames() {
        return this.names;
    }

    @DeleteMapping("/trial")
    public ResponseEntity<String> deleteName(@RequestBody String name) {
        this.names.remove(name);
        return ResponseEntity.ok(name);
    }

}
