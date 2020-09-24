package com.jacdemanec;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PlayerController {

    private final PlayerRepository repository;

    PlayerController(PlayerRepository repository) {
        this.repository = repository;
    }

    //Aggregate root

    @GetMapping("/players")
    List<Player> all(){
        return repository.findAll();
    }

    @PostMapping("/players")
    Player newPlayer(@RequestBody Player newPlayer){
        return repository.save(newPlayer);
    }

    // Single item

    @GetMapping("/players/{id}")
    Player one(@PathVariable Long id) throws PlayerNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id));
    }

    @PutMapping("/players/alias/{id}")
    Player aliasUpdate(@RequestBody Player newPlayer, @PathVariable Long id) {
        return repository.findById(id)
                .map(player -> {
                    player.setAliasString(newPlayer.getAliasString());
                    return repository.save(player);
                })
                .orElseThrow(() -> new PlayerNotFoundException(id));
    }

    @PutMapping("/players/email/{id}")
    Player emailUpdate(@RequestBody Player newPlayer, @PathVariable Long id) {
        return repository.findById(id)
                .map(player -> {
                    player.setEmailString(newPlayer.getEmailString());
                    return repository.save(player);
                })
                .orElseThrow(() -> new PlayerNotFoundException(id));
    }

    @PutMapping("/players/score/{id}")
    Player updateScore(@RequestBody Player newPlayer, @PathVariable Long id) throws PlayerNotFoundException {
        return repository.findById(id)
                .map(player -> {
                    player.setScore(newPlayer.getScore());
                    player.setLines_score(newPlayer.getLines_score());
                    player.setLevel_score(newPlayer.getLevel_score());
                    return repository.save(player);
                })
                .orElseThrow(() -> new PlayerNotFoundException(id));
    }

    @DeleteMapping("/players/{id}")
    void deletePlayer(@PathVariable Long id) {
        repository.deleteById(id);
    }


}
