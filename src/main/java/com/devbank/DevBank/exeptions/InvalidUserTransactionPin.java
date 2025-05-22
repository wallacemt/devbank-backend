package com.devbank.DevBank.exeptions;

public class InvalidUserTransactionPin extends RuntimeException {
    public InvalidUserTransactionPin(String message) {
        super(message);
    }
}
