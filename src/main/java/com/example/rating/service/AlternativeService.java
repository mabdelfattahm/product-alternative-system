
package com.example.rating.service;

import com.example.rating.entity.Alternative;
import com.example.rating.repository.AlternativeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlternativeService {

    private final AlternativeRepository alternativeRepository;

    public AlternativeService(AlternativeRepository alternativeRepository) {
        this.alternativeRepository = alternativeRepository;
    }

    public List<Alternative> findAllApprovedAlternatives(Long productId) {
        return alternativeRepository.findAll()
            .stream()
            .filter(alt -> alt.getProduct().getId().equals(productId) && alt.isApproved())
            .collect(Collectors.toList());
    }

    public Alternative saveAlternative(Alternative alternative) {
        return alternativeRepository.save(alternative);
    }

    public void approveAlternative(Long id) {
        Alternative alternative = alternativeRepository.findById(id).orElseThrow();
        alternative.setApproved(true);
        alternativeRepository.save(alternative);
    }
}
