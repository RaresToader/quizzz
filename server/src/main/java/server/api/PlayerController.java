package server.api;

import commons.Player;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.PlayerRepository;

import java.util.List;


@RestController
@RequestMapping("/player")
public class PlayerController {
    private final PlayerRepository playerRepo;

    public PlayerController(PlayerRepository playerRepo) {
        this.playerRepo = playerRepo;
    }

    @GetMapping({"/", ""})
    public List<Player> getAllPlayers() {
        return playerRepo.findAll();
    }

    @PostMapping("/add")
    public ResponseEntity<Player> addPlayer(@RequestBody Player player) {
        if (player == null || player.getUsername() == null)
            return ResponseEntity.badRequest().build();
        if(playerRepo.findById(player.getUsername()).isPresent())
            return ResponseEntity.ok(player);
        Player saved = playerRepo.save(player);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable String id) {
        if (playerRepo.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(playerRepo.findById(id).get());
    }

    @PutMapping({"/", ""})
    public ResponseEntity<Player> modifyPlayer(@RequestBody Player modified) {
        if (modified == null || modified.getUsername() == null || playerRepo.findById(modified.getUsername()).isEmpty())
            return ResponseEntity.notFound().build();
        Player toBeModified = playerRepo.getById(modified.getUsername());
        toBeModified.setScore(modified.getScore());
        return ResponseEntity.ok(playerRepo.save(toBeModified));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Player> modifyPlayerByPath(@PathVariable String id, @RequestBody Player player) {
        if (id == null || playerRepo.findById(id).isEmpty() || player == null || player.getUsername() == null) {
            return ResponseEntity.notFound().build();
        }
        Player toBeModified = playerRepo.getById(id);
        playerRepo.deleteById(id);
        toBeModified.setScore(player.getScore());
        toBeModified.setUsername(player.getUsername());
        return ResponseEntity.ok(playerRepo.save(toBeModified));
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<Player> deleteAllPlayers() {
        this.playerRepo.deleteAll();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Player> deleteById(@PathVariable String id) {
        if (id == null || playerRepo.findById(id).isEmpty())
            return ResponseEntity.notFound().build();
        playerRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Player> deleteByObject(@RequestBody Player player) {
        if (player == null || player.getUsername() == null || playerRepo.findById(player.getUsername()).isEmpty())
            return ResponseEntity.notFound().build();
        playerRepo.deleteById(player.getUsername());
        return ResponseEntity.ok().build();
    }

    /**
     * Retrieves the top 50 players
     * @return a list of 50 players
     */
    @GetMapping("/leaderboard")
    public ResponseEntity<List<Player>> getLeaderboardPlayers() {
        List<Player> allPlayers = this.playerRepo.findAll();
        allPlayers.sort(new PlayerComparator());
        if (allPlayers.size() < 50)
            return ResponseEntity.ok(allPlayers);
        else
            return ResponseEntity.ok(allPlayers.subList(0, 49));
    }
}
