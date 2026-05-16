package com.visitor.access.service;

import com.visitor.access.model.RiskRecord;
import com.visitor.access.model.enums.RiskType;
import com.visitor.access.model.enums.RiskLevel;
import com.visitor.access.repository.InMemoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RiskRecordService {

    @Autowired
    private InMemoryRepository repository;

    public RiskRecord createRiskRecord(String visitorId, String requestId, 
                                       RiskType riskType, RiskLevel severity, String description) {
        RiskRecord record = new RiskRecord();
        record.setId(repository.generateId("risk"));
        record.setVisitorId(visitorId);
        record.setRequestId(requestId);
        record.setRiskType(riskType);
        record.setSeverity(severity.name());
        record.setDescription(description);
        record.setRecordedAt(LocalDateTime.now());
        repository.saveRiskRecord(record);
        return record;
    }

    public List<RiskRecord> findAll() {
        return repository.findAllRiskRecords();
    }

    public List<RiskRecord> findByVisitorId(String visitorId) {
        return repository.findRiskRecordsByVisitorId(visitorId);
    }

    public List<RiskRecord> findByType(RiskType type) {
        return repository.findRiskRecordsByType(type);
    }

    public Optional<RiskRecord> findById(String id) {
        return repository.findAllRiskRecords().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst();
    }
}
