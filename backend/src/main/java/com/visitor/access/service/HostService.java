package com.visitor.access.service;

import com.visitor.access.model.Host;
import com.visitor.access.model.SecurityGuard;
import com.visitor.access.repository.InMemoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HostService {

    @Autowired
    private InMemoryRepository repository;

    public List<Host> findAll() {
        return repository.findAllHosts();
    }

    public Optional<Host> findById(String id) {
        return repository.findHostById(id);
    }

    public List<SecurityGuard> findAllSecurityGuards() {
        return repository.findAllSecurityGuards();
    }

    public Optional<SecurityGuard> findSecurityGuardById(String id) {
        return repository.findSecurityGuardById(id);
    }
}
