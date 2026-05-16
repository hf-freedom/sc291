package com.visitor.access.service;

import com.visitor.access.dto.request.BlacklistDTO;
import com.visitor.access.model.BlacklistEntry;
import com.visitor.access.repository.InMemoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class BlacklistService {

    @Autowired
    private InMemoryRepository repository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public BlacklistEntry addToBlacklist(BlacklistDTO dto) {
        if (repository.isInBlacklist(dto.getVisitorId())) {
            throw new RuntimeException("访客已在黑名单中");
        }

        BlacklistEntry entry = new BlacklistEntry();
        entry.setId(repository.generateId("bl"));
        entry.setVisitorId(dto.getVisitorId());
        entry.setReason(dto.getReason());
        
        if (dto.getExpireAt() != null && !dto.getExpireAt().isEmpty()) {
            LocalDate date = LocalDate.parse(dto.getExpireAt(), DATE_FORMATTER);
            entry.setExpireAt(date.atStartOfDay());
        }
        
        entry.setCreatedAt(LocalDateTime.now());
        repository.addToBlacklist(entry);
        return entry;
    }

    public void removeFromBlacklist(String visitorId) {
        if (!repository.isInBlacklist(visitorId)) {
            throw new RuntimeException("访客不在黑名单中");
        }
        repository.removeFromBlacklist(visitorId);
    }

    public List<BlacklistEntry> findAll() {
        return repository.findAllBlacklist();
    }

    public Optional<BlacklistEntry> findByVisitorId(String visitorId) {
        return repository.findBlacklistByVisitorId(visitorId);
    }

    public boolean isInBlacklist(String visitorId) {
        return repository.isInBlacklist(visitorId);
    }
}
