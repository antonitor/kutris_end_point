package com.jacdemanec.controller;


import com.jacdemanec.auth.RestFilter;
import com.jacdemanec.model.AppPlayer;
import com.jacdemanec.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;



@RestController
public class PlayerController {

    private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);

    private final PlayerRepository repository;
    private final PlayerModelAssembler assembler;

    PlayerController(PlayerRepository repository, PlayerModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    //Aggregate root
    @PostMapping("/login")
    EntityModel<AppPlayer> authenticate(HttpServletRequest request) {
        String userId = (String)request.getAttribute("userID");
        AppPlayer appPlayer = repository.findByGpgsId(userId);
        AtomicInteger position = new AtomicInteger(1);
        for (AppPlayer p : repository.findAll()) {
            if (p.getScore() >= appPlayer.getScore() && !p.getId().equals(appPlayer.getId())) position.getAndIncrement();
        }
        appPlayer.setClassification(position.get());
        logger.info("POST /login " + userId);
        return assembler.toModel(appPlayer);
    }

    @GetMapping("/players/{id}")
    EntityModel<AppPlayer> one(@PathVariable Long id) throws PlayerNotFoundException {
        AppPlayer appPlayer = repository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id));
        AtomicInteger position = new AtomicInteger(1);
        for (AppPlayer p : repository.findAll()) {
            if (p.getScore() >= appPlayer.getScore() && !p.getId().equals(appPlayer.getId())) position.getAndIncrement();
        }
        appPlayer.setClassification(position.get());
        logger.info("GET /players/" + id + " : " + appPlayer.toString());
        return assembler.toModel(appPlayer);
    }

    @GetMapping("/leaderboard")
    CollectionModel<EntityModel<AppPlayer>> leaderboard() {
        List<EntityModel<AppPlayer>> players = repository.findAll(Sort.by(Sort.Direction.DESC, "score")).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        logger.info("GET /leaderboard : " + players.toString());
        return CollectionModel.of(players,
                linkTo(methodOn(PlayerController.class).leaderboard()).withSelfRel());
    }

    @GetMapping("/leaderboard/{maxSize}")
    CollectionModel<EntityModel<AppPlayer>> leaderboard(@PathVariable long maxSize) {
        List<EntityModel<AppPlayer>> players = repository.findAll(Sort.by(Sort.Direction.DESC, "score")).stream().limit(maxSize)
                .map(assembler::toModel)
                .collect(Collectors.toList());
        logger.info("GET /leaderboard/(MAXSIZE)" + maxSize + " : " + players.toString());
        return CollectionModel.of(players,
                linkTo(methodOn(PlayerController.class).leaderboard()).withSelfRel());
    }


    @GetMapping("/players")
    CollectionModel<EntityModel<AppPlayer>> all() {
        List<EntityModel<AppPlayer>> players = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        logger.info("GET /players : " + players.toString());
        return CollectionModel.of(players,
                linkTo(methodOn(PlayerController.class).all()).withSelfRel());
    }



    @PostMapping("/players")
    ResponseEntity<?> newPlayer(@RequestBody AppPlayer newAppPlayer) {
        EntityModel<AppPlayer> entityModel = assembler.toModel(repository.save(newAppPlayer));
        logger.info("POST /players : " + entityModel.toString());
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    // Single item



    @PutMapping("/players/alias/{id}")
    ResponseEntity<?> aliasUpdate(HttpServletResponse response, @RequestBody AppPlayer newAppPlayer, @PathVariable Long id) throws IOException  {
        for (AppPlayer p : repository.findAll()) {
            if (newAppPlayer.getAliasString().equalsIgnoreCase(p.getAliasString())) {
                response.sendError(HttpStatus.UNAUTHORIZED.value());
                return null;
            }
        }
        AppPlayer updatedAppPlayer = repository.findById(id)
                .map(appPlayer -> {
                    appPlayer.setAliasString(newAppPlayer.getAliasString());
                    return repository.save(appPlayer);
                })
                .orElseThrow(() -> new PlayerNotFoundException(id));
        logger.info("PUT /players/alias : " + newAppPlayer.getAliasString());
        EntityModel<AppPlayer> entityModel = assembler.toModel(updatedAppPlayer);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }


    @PutMapping("/players/score/{id}")
    ResponseEntity<?> updateScore(@RequestBody AppPlayer newAppPlayer, @PathVariable Long id) throws PlayerNotFoundException {
        AppPlayer updatedAppPlayer =  repository.findById(id)
                .map(appPlayer -> {
                    appPlayer.setScore(newAppPlayer.getScore());
                    appPlayer.setLines_score(newAppPlayer.getLines_score());
                    appPlayer.setLevel_score(newAppPlayer.getLevel_score());
                    return repository.save(appPlayer);
                })
                .orElseThrow(() -> new PlayerNotFoundException(id));

        logger.info("UPDATE SCORE " + newAppPlayer.getScore() + " FOR PLAYER " + newAppPlayer.getAliasString());

        EntityModel<AppPlayer> entityModel = assembler.toModel(updatedAppPlayer);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/players/{id}")
    ResponseEntity<?> deletePlayer(@PathVariable Long id) {
        repository.deleteById(id);
        logger.info("DELETE /players/" + id);
        return ResponseEntity.noContent().build();
    }


}
