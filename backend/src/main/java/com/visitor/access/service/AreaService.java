package com.visitor.access.service;

import com.visitor.access.dto.request.AreaDTO;
import com.visitor.access.model.Area;
import com.visitor.access.repository.InMemoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AreaService {

    @Autowired
    private InMemoryRepository repository;

    public Area createArea(AreaDTO dto) {
        Area area = new Area();
        area.setId(repository.generateId("area"));
        area.setName(dto.getName());
        area.setDescription(dto.getDescription());
        area.setRequiredPermissionLevel(dto.getRequiredPermissionLevel());
        area.setStatus("ACTIVE");
        area.setCreatedAt(LocalDateTime.now());
        repository.saveArea(area);
        return area;
    }

    public Optional<Area> findById(String id) {
        return repository.findAreaById(id);
    }

    public List<Area> findAll() {
        return repository.findAllAreas();
    }

    public Area updateArea(String id, AreaDTO dto) {
        Area area = repository.findAreaById(id)
                .orElseThrow(() -> new RuntimeException("区域不存在"));
        area.setName(dto.getName());
        area.setDescription(dto.getDescription());
        area.setRequiredPermissionLevel(dto.getRequiredPermissionLevel());
        repository.saveArea(area);
        return area;
    }

    public void deleteArea(String id) {
        Area area = repository.findAreaById(id)
                .orElseThrow(() -> new RuntimeException("区域不存在"));
        area.setStatus("INACTIVE");
        repository.saveArea(area);
    }
}
