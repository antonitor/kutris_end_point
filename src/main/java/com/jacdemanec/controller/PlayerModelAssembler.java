package com.jacdemanec.controller;

import com.jacdemanec.model.Player;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

@Component
public class PlayerModelAssembler implements RepresentationModelAssembler<Player, EntityModel<Player>> {
    @Override
    public EntityModel<Player> toModel(Player entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(PlayerController.class).one(entity.getId())).withSelfRel(),
                linkTo(methodOn(PlayerController.class).all()).withRel("players"));
    }

}
