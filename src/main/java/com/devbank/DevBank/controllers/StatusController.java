package com.devbank.DevBank.controllers;

import com.devbank.DevBank.dtos.response.StatusResponseDTO;
import com.devbank.DevBank.services.StatusService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/status")
public class StatusController {
    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping
    public StatusResponseDTO getStatus() {
        return statusService.getStatus();
    }
}
