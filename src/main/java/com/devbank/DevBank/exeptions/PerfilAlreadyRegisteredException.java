package com.devbank.DevBank.exeptions;

public class PerfilAlreadyRegisteredException extends RuntimeException {
    public PerfilAlreadyRegisteredException(String message) {
        super(message);
    }
}
