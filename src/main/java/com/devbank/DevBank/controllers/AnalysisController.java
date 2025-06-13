package com.devbank.DevBank.controllers;


import com.devbank.DevBank.dtos.response.FinancialAnalysisDTO;
import com.devbank.DevBank.entities.User.User;
import com.devbank.DevBank.services.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/analysis")
public class AnalysisController {
    @Autowired
    private AnalysisService analysisService;

    @GetMapping("/overview")
    public ResponseEntity<List<FinancialAnalysisDTO>> getOverviewAnalysis(
            @AuthenticationPrincipal User user
    ) {
        List<FinancialAnalysisDTO> overview = analysisService.getLastSixMonthsAnalysis(user.getId());
        return ResponseEntity.ok(overview);
    }
}
