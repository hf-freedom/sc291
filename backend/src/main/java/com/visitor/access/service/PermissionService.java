package com.visitor.access.service;

import com.visitor.access.dto.response.CheckInResponse;
import com.visitor.access.dto.response.CheckOutResponse;
import com.visitor.access.model.AccessPermission;
import com.visitor.access.model.Area;
import com.visitor.access.model.RiskRecord;
import com.visitor.access.model.VisitRequest;
import com.visitor.access.model.enums.PermissionStatus;
import com.visitor.access.model.enums.RiskLevel;
import com.visitor.access.model.enums.RiskType;
import com.visitor.access.repository.InMemoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {

    @Autowired
    private InMemoryRepository repository;

    @Autowired
    private RiskRecordService riskRecordService;

    public AccessPermission generatePermission(VisitRequest request) {
        Area area = repository.findAreaById(request.getAreaId())
                .orElseThrow(() -> new RuntimeException("区域不存在"));

        AccessPermission permission = new AccessPermission();
        permission.setId(repository.generateId("perm"));
        permission.setRequestId(request.getId());
        permission.setVisitorId(request.getVisitorId());
        permission.setPermissionCode(repository.generatePermissionCode());
        permission.setValidFrom(request.getExpectedEntryTime());
        permission.setValidUntil(request.getExpectedExitTime());
        permission.setAccessibleAreas(new ArrayList<>());
        permission.getAccessibleAreas().add(area.getId());
        permission.setPermissionLevel(area.getRequiredPermissionLevel());
        permission.setStatus(PermissionStatus.PENDING);
        permission.setCreatedAt(LocalDateTime.now());
        permission.setUpdatedAt(LocalDateTime.now());
        repository.savePermission(permission);
        return permission;
    }

    public Optional<AccessPermission> findById(String id) {
        return repository.findPermissionById(id);
    }

    public Optional<AccessPermission> findByCode(String code) {
        return repository.findPermissionByCode(code);
    }

    public List<AccessPermission> findAll() {
        return repository.findAllPermissions();
    }

    public List<AccessPermission> findByStatus(PermissionStatus status) {
        return repository.findPermissionsByStatus(status);
    }

    public CheckInResponse checkIn(String permissionId) {
        AccessPermission permission = repository.findPermissionById(permissionId)
                .orElseThrow(() -> new RuntimeException("权限不存在"));

        LocalDateTime now = LocalDateTime.now();

        if (permission.getStatus() != PermissionStatus.PENDING) {
            throw new RuntimeException("权限状态无效，无法入园");
        }

        if (now.isBefore(permission.getValidFrom().minusMinutes(30))) {
            throw new RuntimeException("尚未到达入园时间");
        }

        if (now.isAfter(permission.getValidFrom().plusHours(2))) {
            throw new RuntimeException("已超过入园时间2小时，权限失效");
        }

        boolean isLate = now.isAfter(permission.getValidFrom().plusMinutes(30));

        if (isLate) {
            riskRecordService.createRiskRecord(
                    permission.getVisitorId(),
                    permission.getRequestId(),
                    RiskType.LATE,
                    RiskLevel.MEDIUM,
                    "访客迟到超过30分钟"
            );
        }

        permission.setActualEntryTime(now);
        permission.setStatus(PermissionStatus.ACTIVE);
        permission.setUpdatedAt(LocalDateTime.now());
        repository.savePermission(permission);

        CheckInResponse response = new CheckInResponse();
        response.setPermissionId(permissionId);
        response.setActualEntryTime(now.toString());
        response.setStatus(PermissionStatus.ACTIVE.name());
        response.setLate(isLate);
        response.setMessage(isLate ? "入园成功，但您已迟到" : "入园成功");
        return response;
    }

    public CheckOutResponse checkOut(String permissionId) {
        AccessPermission permission = repository.findPermissionById(permissionId)
                .orElseThrow(() -> new RuntimeException("权限不存在"));

        LocalDateTime now = LocalDateTime.now();

        if (permission.getStatus() != PermissionStatus.ACTIVE) {
            throw new RuntimeException("权限状态无效，无法离园");
        }

        boolean isOverstay = now.isAfter(permission.getValidUntil().plusMinutes(15));

        if (isOverstay) {
            riskRecordService.createRiskRecord(
                    permission.getVisitorId(),
                    permission.getRequestId(),
                    RiskType.OVERSTAY,
                    RiskLevel.MEDIUM,
                    "访客超时离场超过15分钟"
            );
        }

        permission.setActualExitTime(now);
        permission.setStatus(PermissionStatus.EXPIRED);
        permission.setUpdatedAt(LocalDateTime.now());
        repository.savePermission(permission);

        CheckOutResponse response = new CheckOutResponse();
        response.setPermissionId(permissionId);
        response.setActualExitTime(now.toString());
        response.setStatus(PermissionStatus.EXPIRED.name());
        response.setOverstay(isOverstay);
        response.setMessage(isOverstay ? "离园成功，但您已超时" : "离园成功");
        return response;
    }

    public void revokePermissionByRequestId(String requestId) {
        repository.findAllPermissions().stream()
                .filter(p -> p.getRequestId().equals(requestId))
                .findFirst()
                .ifPresent(permission -> {
                    permission.setStatus(PermissionStatus.REVOKED);
                    permission.setUpdatedAt(LocalDateTime.now());
                    repository.savePermission(permission);
                });
    }

    public void expirePermission(String permissionId) {
        repository.findPermissionById(permissionId).ifPresent(permission -> {
            permission.setStatus(PermissionStatus.EXPIRED);
            permission.setUpdatedAt(LocalDateTime.now());
            repository.savePermission(permission);
        });
    }
}
