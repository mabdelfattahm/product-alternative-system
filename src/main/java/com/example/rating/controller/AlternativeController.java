package com.example.rating.controller;

import com.example.rating.entity.Alternative;
import com.example.rating.service.AlternativeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/alternatives")
public class AlternativeController {

    private final AlternativeService alternativeService;

    public AlternativeController(AlternativeService alternativeService) {
        this.alternativeService = alternativeService;
    }

    @GetMapping("/{productId}")
    public List<Alternative> getAlternatives(@PathVariable Long productId) {
        return alternativeService.findAllApprovedAlternatives(productId);
    }

    @PostMapping
    @PreAuthorize("hasRole('NORMAL_USER')")
    public Alternative addAlternative(@RequestBody Alternative alternative) {
        return alternativeService.saveAlternative(alternative);
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('MODERATOR')")
    public void approveAlternative(@PathVariable Long id) {
        alternativeService.approveAlternative(id);
    }
}
