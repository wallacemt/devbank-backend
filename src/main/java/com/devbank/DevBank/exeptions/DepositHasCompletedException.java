package com.devbank.DevBank.exeptions;

public class DepositHasCompletedException extends RuntimeException {
    public DepositHasCompletedException(String message) {
        super(message);
    }
}
