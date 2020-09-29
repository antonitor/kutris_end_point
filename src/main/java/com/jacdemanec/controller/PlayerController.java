package com.jacdemanec.controller;

import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.jacdemanec.model.Player;
import com.jacdemanec.repository.PlayerRepository;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;



@RestController
public class PlayerController {

    private final PlayerRepository repository;
    private final PlayerModelAssembler assembler;

    PlayerController(PlayerRepository repository, PlayerModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    //Aggregate root

    @GetMapping("/players")
    CollectionModel<EntityModel<Player>> all() {
        List<EntityModel<Player>> players = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(players,
                linkTo(methodOn(PlayerController.class).all()).withSelfRel());
    }

    @PostMapping("/players")
    ResponseEntity<?> newPlayer(@RequestBody Player newPlayer) {
        EntityModel<Player> entityModel = assembler.toModel(repository.save(newPlayer));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    // Single item

    @GetMapping("/players/{id}")
    EntityModel<Player> one(@PathVariable Long id) throws PlayerNotFoundException {
        Player player = repository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id));
        AtomicInteger position = new AtomicInteger(1);
        for (Player p : repository.findAll()) {
            if (p.getScore() >= player.getScore() && !p.getId().equals(player.getId())) position.getAndIncrement();
        }
        player.setClassification(position.get());
        return assembler.toModel(player);
    }

    @PutMapping("/players/alias/{id}")
    ResponseEntity<?> aliasUpdate(@RequestBody Player newPlayer, @PathVariable Long id) {
        Player updatedPlayer = repository.findById(id)
                .map(player -> {
                    player.setAliasString(newPlayer.getAliasString());
                    return repository.save(player);
                })
                .orElseThrow(() -> new PlayerNotFoundException(id));

        EntityModel<Player> entityModel = assembler.toModel(updatedPlayer);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/players/email/{id}")
    ResponseEntity<?> emailUpdate(@RequestBody Player newPlayer, @PathVariable Long id) {
        Player updatedPlayer =  repository.findById(id)
                .map(player -> {
                    player.setEmailString(newPlayer.getEmailString());
                    return repository.save(player);
                })
                .orElseThrow(() -> new PlayerNotFoundException(id));

        EntityModel<Player> entityModel = assembler.toModel(updatedPlayer);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/players/score/{id}")
    ResponseEntity<?> updateScore(@RequestBody Player newPlayer, @PathVariable Long id) throws PlayerNotFoundException {
        Player updatedPlayer =  repository.findById(id)
                .map(player -> {
                    player.setScore(newPlayer.getScore());
                    player.setLines_score(newPlayer.getLines_score());
                    player.setLevel_score(newPlayer.getLevel_score());
                    return repository.save(player);
                })
                .orElseThrow(() -> new PlayerNotFoundException(id));
        EntityModel<Player> entityModel = assembler.toModel(updatedPlayer);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/players/{id}")
    ResponseEntity<?> deletePlayer(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/leaderboard")
    CollectionModel<EntityModel<Player>> leaderboard() {
        List<EntityModel<Player>> players = repository.findAll(Sort.by(Sort.Direction.DESC, "score")).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(players,
                linkTo(methodOn(PlayerController.class).leaderboard()).withSelfRel());
    }

    @GetMapping("/leaderboard/{maxSize}")
    CollectionModel<EntityModel<Player>> leaderboard(@PathVariable long maxSize) {
        List<EntityModel<Player>> players = repository.findAll(Sort.by(Sort.Direction.DESC, "score")).stream().limit(maxSize)
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(players,
                linkTo(methodOn(PlayerController.class).leaderboard()).withSelfRel());
    }

    /*
    @PostMapping("/authcode")
    EntityModel<Player> authWithAuthCode(@RequestBody String authCode) {

        String CLIENT_SECRET_FILE = "client_secret.json";

        try {
            GoogleClientSecrets clientSecrets =
                    GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(), new FileReader(CLIENT_SECRET_FILE));

            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance(),
                    "https://oauth2.googleapis.com/token",
                    clientSecrets.getDetails().getClientId(),
                    clientSecrets.getDetails().getClientSecret(),
                    authCode,
                    "")  // Specify the same redirect URI that you use with your web
                    // app. If you don't have a web version of your app, you can
                    // specify an empty string.
                    .execute();
            String accessToken = tokenResponse.getAccessToken();

            GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
            // Get profile info from ID token
            GoogleIdToken idToken = tokenResponse.parseIdToken();
            GoogleIdToken.Payload payload = idToken.getPayload();
            String userId = payload.getSubject();  // Use this value as a key to identify a user.
            String email = payload.getEmail();
            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");



        } catch (IOException e) {
            e.printStackTrace();
        }

        //O_O
        Player player = repository.findById(1L)
                .orElseThrow(() -> new PlayerNotFoundException(1L));

        return assembler.toModel(player);
    }

     */




}
