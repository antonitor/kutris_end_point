package com.jacdemanec.controller;

import com.jacdemanec.model.AppPlayer;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

@Component
public class PlayerModelAssembler implements RepresentationModelAssembler<AppPlayer, EntityModel<AppPlayer>> {
    @Override
    public EntityModel<AppPlayer> toModel(AppPlayer entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(PlayerController.class).one(entity.getId())).withSelfRel(),
                linkTo(methodOn(PlayerController.class).all()).withRel("players"));
    }

}
