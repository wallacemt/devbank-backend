package com.devbank.DevBank.exeptions;

public class BonusHasReclaimException extends RuntimeException {
    public BonusHasReclaimException(String message) {
        super(message);
    }
}
