package com.devbank.DevBank.exeptions;

public class InsuficientAmountException extends RuntimeException {
    public InsuficientAmountException(String message) {
        super(message);
    }
}
