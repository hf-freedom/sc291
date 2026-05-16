package com.visitor.access.service;

import com.visitor.access.dto.request.VisitorDTO;
import com.visitor.access.model.Visitor;
import com.visitor.access.model.enums.RiskLevel;
import com.visitor.access.repository.InMemoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VisitorService {

    @Autowired
    private InMemoryRepository repository;

    public Visitor createVisitor(VisitorDTO dto) {
        Visitor visitor = new Visitor();
        visitor.setId(repository.generateId("visitor"));
        visitor.setName(dto.getName());
        visitor.setPhone(dto.getPhone());
        visitor.setIdCard(dto.getIdCard());
        visitor.setEmail(dto.getEmail());
        visitor.setRiskLevel(RiskLevel.LOW);
        visitor.setCreatedAt(LocalDateTime.now());
        visitor.setUpdatedAt(LocalDateTime.now());
        repository.saveVisitor(visitor);
        return visitor;
    }

    public Optional<Visitor> findById(String id) {
        return repository.findVisitorById(id);
    }

    public List<Visitor> findAll() {
        return repository.findAllVisitors();
    }

    public void updateVisitorRiskLevel(String visitorId, RiskLevel level) {
        repository.findVisitorById(visitorId).ifPresent(visitor -> {
            visitor.setRiskLevel(level);
            visitor.setUpdatedAt(LocalDateTime.now());
            repository.saveVisitor(visitor);
        });
    }

    public boolean isInBlacklist(String visitorId) {
        return repository.isInBlacklist(visitorId);
    }
}
