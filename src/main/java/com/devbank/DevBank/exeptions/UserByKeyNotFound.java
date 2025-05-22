package com.devbank.DevBank.exeptions;

public class UserByKeyNotFound extends RuntimeException {
    public UserByKeyNotFound(String message) {
        super(message);
    }
}
