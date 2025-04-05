package com.devbank.DevBank.exeptions;

public class CpfAlreadyRegisteredException extends RuntimeException {
    public CpfAlreadyRegisteredException(String message) {
        super(message);
    }
}
