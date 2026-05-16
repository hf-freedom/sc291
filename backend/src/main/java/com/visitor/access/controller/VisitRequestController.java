package com.visitor.access.controller;

import com.visitor.access.dto.request.ApprovalDTO;
import com.visitor.access.dto.request.VisitRequestDTO;
import com.visitor.access.dto.response.ApiResponse;
import com.visitor.access.dto.response.VisitRequestDetailDTO;
import com.visitor.access.model.Area;
import com.visitor.access.model.Host;
import com.visitor.access.model.VisitRequest;
import com.visitor.access.model.Visitor;
import com.visitor.access.model.enums.RequestStatus;
import com.visitor.access.repository.InMemoryRepository;
import com.visitor.access.service.VisitRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/visit-requests")
@CrossOrigin(origins = "*")
public class VisitRequestController {

    @Autowired
    private VisitRequestService visitRequestService;

    @Autowired
    private InMemoryRepository repository;

    @PostMapping
    public ApiResponse<VisitRequest> createRequest(@RequestBody VisitRequestDTO dto) {
        try {
            VisitRequest request = visitRequestService.createRequest(dto);
            return ApiResponse.success("申请已提交", request);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<VisitRequestDetailDTO> getRequest(@PathVariable String id) {
        return visitRequestService.findById(id)
                .map(request -> ApiResponse.success(enrichRequest(request)))
                .orElse(ApiResponse.error("预约申请不存在"));
    }

    @GetMapping
    public ApiResponse<List<VisitRequestDetailDTO>> getRequests(
            @RequestParam(required = false) String visitorId,
            @RequestParam(required = false) String hostId,
            @RequestParam(required = false) String status) {
        
        List<VisitRequest> requests;
        if (visitorId != null) {
            requests = visitRequestService.findByVisitorId(visitorId);
        } else if (hostId != null) {
            requests = visitRequestService.findByHostId(hostId);
        } else if (status != null) {
            requests = visitRequestService.findByStatus(RequestStatus.valueOf(status));
        } else {
            requests = visitRequestService.findAll();
        }
        
        List<VisitRequestDetailDTO> enrichedRequests = requests.stream()
                .map(this::enrichRequest)
                .collect(Collectors.toList());
        
        return ApiResponse.success(enrichedRequests);
    }

    @PostMapping("/{id}/approve")
    public ApiResponse<VisitRequest> approveByHost(
            @PathVariable String id,
            @RequestBody ApprovalDTO dto) {
        try {
            VisitRequest request = visitRequestService.approveByHost(id, dto.isApproved(), dto.getComment());
            return ApiResponse.success("审批成功", request);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/security-approve")
    public ApiResponse<VisitRequest> approveBySecurity(
            @PathVariable String id,
            @RequestBody ApprovalDTO dto) {
        try {
            VisitRequest request = visitRequestService.approveBySecurity(id, dto.isApproved(), dto.getComment());
            return ApiResponse.success("保安审批成功", request);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/cancel")
    public ApiResponse<VisitRequest> cancelRequest(@PathVariable String id) {
        try {
            VisitRequest request = visitRequestService.cancelRequest(id);
            return ApiResponse.success("预约已取消", request);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/pending-security")
    public ApiResponse<List<VisitRequestDetailDTO>> getPendingSecurityApprovals() {
        List<VisitRequest> requests = visitRequestService.findPendingSecurityApprovals();
        List<VisitRequestDetailDTO> enrichedRequests = requests.stream()
                .map(this::enrichRequest)
                .collect(Collectors.toList());
        return ApiResponse.success(enrichedRequests);
    }

    private VisitRequestDetailDTO enrichRequest(VisitRequest request) {
        String visitorName = repository.findVisitorById(request.getVisitorId())
                .map(Visitor::getName)
                .orElse(request.getVisitorId());
        
        String hostName = repository.findHostById(request.getHostId())
                .map(Host::getName)
                .orElse(request.getHostId());
        
        String areaName = null;
        String areaLevel = null;
        Area area = repository.findAreaById(request.getAreaId()).orElse(null);
        if (area != null) {
            areaName = area.getName();
            areaLevel = area.getRequiredPermissionLevel() != null ? 
                    area.getRequiredPermissionLevel().name() : null;
        }
        
        return VisitRequestDetailDTO.fromVisitRequest(request, visitorName, hostName, areaName, areaLevel);
    }
}
