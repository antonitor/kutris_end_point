package com.jacdemanec.controller;

public class PlayerNotFoundException extends RuntimeException{

    public PlayerNotFoundException(Long id) {
        super("Could not find player with id " + id);
    }

}
