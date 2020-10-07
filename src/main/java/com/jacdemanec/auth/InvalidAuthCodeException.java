package com.jacdemanec.auth;

public class InvalidAuthCodeException extends Exception {
    public InvalidAuthCodeException(String message) {
        super(message);
    }
}
