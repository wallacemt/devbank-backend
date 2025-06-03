package com.devbank.DevBank.exeptions;

public class TransactionNotFountExeception extends RuntimeException {
    public TransactionNotFountExeception(String message) {
        super(message);
    }
}
