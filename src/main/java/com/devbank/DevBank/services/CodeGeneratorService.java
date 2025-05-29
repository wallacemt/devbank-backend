package com.devbank.DevBank.services;

import org.springframework.stereotype.Service;


@Service
public class CodeGeneratorService {
    public String generateCode() {
        return String.format("%06d", (int) (Math.random() * 1000000));
    }
}

