package com.devbank.DevBank.exeptions;

public class SendEmailError extends RuntimeException {
    public SendEmailError(String message) {
        super(message);
    }
}
