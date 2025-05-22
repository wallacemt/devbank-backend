package com.devbank.DevBank.exeptions;

public class InvalidSenderTransactionsException extends RuntimeException {
    public InvalidSenderTransactionsException(String message) {
        super(message);
    }
}
