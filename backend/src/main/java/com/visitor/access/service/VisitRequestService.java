package com.visitor.access.service;

import com.visitor.access.dto.request.VisitRequestDTO;
import com.visitor.access.model.AccessPermission;
import com.visitor.access.model.VisitRequest;
import com.visitor.access.model.enums.*;
import com.visitor.access.repository.InMemoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class VisitRequestService {

    @Autowired
    private InMemoryRepository repository;

    @Autowired
    private VisitorService visitorService;

    @Autowired
    private PermissionService permissionService;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public VisitRequest createRequest(VisitRequestDTO dto) {
        if (visitorService.isInBlacklist(dto.getVisitorId())) {
            throw new RuntimeException("访客在黑名单中，无法提交申请");
        }

        VisitRequest request = new VisitRequest();
        request.setId(repository.generateId("req"));
        request.setVisitorId(dto.getVisitorId());
        request.setHostId(dto.getHostId());
        request.setAreaId(dto.getAreaId());
        request.setExpectedEntryTime(LocalDateTime.parse(dto.getExpectedEntryTime().replace(" ", "T"), FORMATTER));
        request.setExpectedExitTime(LocalDateTime.parse(dto.getExpectedExitTime().replace(" ", "T"), FORMATTER));
        request.setReason(dto.getReason());
        request.setStatus(RequestStatus.PENDING);
        
        Optional.ofNullable(visitorService.findById(dto.getVisitorId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .ifPresent(visitor -> {
                    request.setRequireSecurityApproval(visitor.getRiskLevel() == RiskLevel.HIGH);
                    request.setSecurityApprovalStatus(
                            request.isRequireSecurityApproval() ? ApprovalStatus.PENDING : null
                    );
                });

        request.setCreatedAt(LocalDateTime.now());
        request.setUpdatedAt(LocalDateTime.now());
        repository.saveVisitRequest(request);
        return request;
    }

    public Optional<VisitRequest> findById(String id) {
        return repository.findVisitRequestById(id);
    }

    public List<VisitRequest> findAll() {
        return repository.findAllVisitRequests();
    }

    public List<VisitRequest> findByVisitorId(String visitorId) {
        return repository.findVisitRequestsByVisitorId(visitorId);
    }

    public List<VisitRequest> findByHostId(String hostId) {
        return repository.findVisitRequestsByHostId(hostId);
    }

    public List<VisitRequest> findByStatus(RequestStatus status) {
        return repository.findVisitRequestsByStatus(status);
    }

    public VisitRequest approveByHost(String requestId, boolean approved, String comment) {
        VisitRequest request = repository.findVisitRequestById(requestId)
                .orElseThrow(() -> new RuntimeException("预约申请不存在"));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("当前状态无法审批");
        }

        request.setHostComment(comment);
        request.setUpdatedAt(LocalDateTime.now());

        if (approved) {
            if (request.isRequireSecurityApproval()) {
                request.setStatus(RequestStatus.APPROVED);
            } else {
                permissionService.generatePermission(request);
                request.setStatus(RequestStatus.APPROVED);
            }
        } else {
            request.setStatus(RequestStatus.REJECTED);
        }

        repository.saveVisitRequest(request);
        return request;
    }

    public VisitRequest approveBySecurity(String requestId, boolean approved, String comment) {
        VisitRequest request = repository.findVisitRequestById(requestId)
                .orElseThrow(() -> new RuntimeException("预约申请不存在"));

        if (request.getStatus() != RequestStatus.APPROVED || 
            request.getSecurityApprovalStatus() != ApprovalStatus.PENDING) {
            throw new RuntimeException("当前状态无需保安审批");
        }

        request.setSecurityComment(comment);
        request.setUpdatedAt(LocalDateTime.now());

        if (approved) {
            permissionService.generatePermission(request);
        } else {
            request.setStatus(RequestStatus.REJECTED);
        }

        request.setSecurityApprovalStatus(approved ? ApprovalStatus.APPROVED : ApprovalStatus.REJECTED);
        repository.saveVisitRequest(request);
        return request;
    }

    public VisitRequest cancelRequest(String requestId) {
        VisitRequest request = repository.findVisitRequestById(requestId)
                .orElseThrow(() -> new RuntimeException("预约申请不存在"));

        if (request.getStatus() == RequestStatus.CANCELLED || 
            request.getStatus() == RequestStatus.EXPIRED) {
            throw new RuntimeException("当前状态无法取消");
        }

        if (request.getStatus() == RequestStatus.APPROVED) {
            permissionService.revokePermissionByRequestId(requestId);
        }

        request.setStatus(RequestStatus.CANCELLED);
        request.setUpdatedAt(LocalDateTime.now());
        repository.saveVisitRequest(request);
        return request;
    }

    public List<VisitRequest> findPendingSecurityApprovals() {
        return repository.findAllVisitRequests().stream()
                .filter(r -> r.getStatus() == RequestStatus.APPROVED)
                .filter(r -> r.getSecurityApprovalStatus() == ApprovalStatus.PENDING)
                .collect(java.util.stream.Collectors.toList());
    }
}
