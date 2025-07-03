package com.devbank.DevBank.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatusResponseDTO {

    private Map<String, Object> database;
    private Map<String, Object> server;
    private Map<String, Object> git;


}
