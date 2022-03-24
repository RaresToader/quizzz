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

    /**
     * Player Controller constructor
     *
     * @param playerRepo a player repository
     */
    public PlayerController(PlayerRepository playerRepo) {
        this.playerRepo = playerRepo;
    }

    /**
     * Gets all players from the repository
     *
     * @return the list of all players
     */
    @GetMapping({"/", ""})
    public List<Player> getAllPlayers() {
        return playerRepo.findAll();
    }

    /**
     * Adds a player to the repository
     *
     * @param player a Player object to be added
     * @return whether the player was added successfully
     */
    @PostMapping("/add")
    public ResponseEntity<Player> addPlayer(@RequestBody Player player) {
        if (player == null || player.getUsername() == null)
            return ResponseEntity.badRequest().build();
        if (playerRepo.findById(player.getUsername()).isPresent())
            return ResponseEntity.ok(player);
        Player saved = playerRepo.save(player);
        return ResponseEntity.ok(saved);
    }

    /**
     * Retrieve a player by his id
     *
     * @param id the id of the player
     * @return the actual player, if he exists, or a not found response
     */
    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable String id) {
        if (playerRepo.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(playerRepo.findById(id).get());
    }

    /**
     * Modifies a player
     *
     * @param modified a Player object
     * @return the modified player, if he already existed before in the repository
     */
    @PutMapping({"/", ""})
    public ResponseEntity<Player> modifyPlayer(@RequestBody Player modified) {
        if (modified == null || modified.getUsername() == null || playerRepo.findById(modified.getUsername()).isEmpty())
            return ResponseEntity.notFound().build();
        Player toBeModified = playerRepo.getById(modified.getUsername());
        toBeModified.setScore(modified.getScore());
        return ResponseEntity.ok(playerRepo.save(toBeModified));
    }

    /**
     * Modify player in the repository
     *
     * @param id     the id of the player
     * @param player the actual player that is to be stored
     * @return the modified player
     */
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

    /**
     * Updated a player's score if he exists in the database, and only if the score is higher than
     * his current stored score
     *
     * @param player the player to be stored in the database
     * @return the modified player
     */
    @PutMapping("/update/")
    public ResponseEntity<Player> updatePlayerScore(@RequestBody Player player) {
        if (player == null || player.getUsername() == null || playerRepo.findById(player.getUsername()).isEmpty())
            return ResponseEntity.notFound().build();

        Player toBeModified = playerRepo.findById(player.getUsername()).get();
        long currentScore = toBeModified.getScore();
        if (currentScore >= player.getScore())
            return ResponseEntity.ok(toBeModified);
        playerRepo.deleteById(player.getUsername());
        return ResponseEntity.ok(playerRepo.save(player));
    }

    /**
     * Deletes all players from the repository
     *
     * @return a response entity
     */
    @DeleteMapping("/deleteAll")
    public ResponseEntity<Player> deleteAllPlayers() {
        this.playerRepo.deleteAll();
        return ResponseEntity.ok().build();
    }

    /**
     * Deletes a player by his id
     *
     * @param id the id of the player
     * @return the player that was deleted
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Player> deleteById(@PathVariable String id) {
        if (id == null || playerRepo.findById(id).isEmpty())
            return ResponseEntity.notFound().build();
        playerRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Delete a player
     *
     * @param player the player to be deleted
     * @return the deleted player
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Player> deleteByObject(@RequestBody Player player) {
        if (player == null || player.getUsername() == null || playerRepo.findById(player.getUsername()).isEmpty())
            return ResponseEntity.notFound().build();
        playerRepo.deleteById(player.getUsername());
        return ResponseEntity.ok().build();
    }

    /**
     * Retrieves the top 20 players
     *
     * @return a list of 20 players
     */
    @GetMapping("/leaderboard")
    public ResponseEntity<List<Player>> getLeaderboardPlayers() {
        List<Player> allPlayers = this.playerRepo.findAll();
        allPlayers.sort(new PlayerComparator());
        if (allPlayers.size() < 20)
            return ResponseEntity.ok(allPlayers);
        else
            return ResponseEntity.ok(allPlayers.subList(0, 19));
    }
}
