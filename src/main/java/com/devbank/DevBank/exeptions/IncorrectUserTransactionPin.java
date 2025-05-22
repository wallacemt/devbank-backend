package com.devbank.DevBank.exeptions;

public class IncorrectUserTransactionPin extends RuntimeException {
    public IncorrectUserTransactionPin(String message) {
        super(message);
    }
}
