package com.devbank.DevBank.exeptions;

public class VerifyCodeHasSendingException extends RuntimeException {
    public VerifyCodeHasSendingException(String message) {
        super(message);
    }
}
