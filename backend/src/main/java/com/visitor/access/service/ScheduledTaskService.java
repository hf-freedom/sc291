package com.visitor.access.service;

import com.visitor.access.dto.response.StatisticsResponse;
import com.visitor.access.model.AccessPermission;
import com.visitor.access.model.RiskRecord;
import com.visitor.access.model.VisitRequest;
import com.visitor.access.model.enums.PermissionStatus;
import com.visitor.access.model.enums.RequestStatus;
import com.visitor.access.model.enums.RiskType;
import com.visitor.access.repository.InMemoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduledTaskService {

    @Autowired
    private InMemoryRepository repository;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RiskRecordService riskRecordService;

    @Autowired
    private VisitorService visitorService;

    @Scheduled(fixedRate = 300000)
    public void checkExpiredPermissions() {
        LocalDateTime now = LocalDateTime.now();
        List<AccessPermission> activePermissions = repository.findPermissionsByStatus(PermissionStatus.ACTIVE);
        
        for (AccessPermission permission : activePermissions) {
            if (now.isAfter(permission.getValidUntil())) {
                permissionService.expirePermission(permission.getId());
            }
        }

        List<AccessPermission> pendingPermissions = repository.findPermissionsByStatus(PermissionStatus.PENDING);
        for (AccessPermission permission : pendingPermissions) {
            if (now.isAfter(permission.getValidFrom().plusHours(2))) {
                repository.findVisitRequestById(permission.getRequestId()).ifPresent(request -> {
                    request.setStatus(RequestStatus.EXPIRED);
                    repository.saveVisitRequest(request);
                });
                permissionService.expirePermission(permission.getId());
            }
        }
    }

    @Scheduled(fixedRate = 3600000)
    public void checkNotExitedVisitors() {
        LocalDateTime now = LocalDateTime.now();
        List<AccessPermission> activePermissions = repository.findPermissionsByStatus(PermissionStatus.ACTIVE);
        
        for (AccessPermission permission : activePermissions) {
            if (now.isAfter(permission.getValidUntil().plusHours(2))) {
                riskRecordService.createRiskRecord(
                        permission.getVisitorId(),
                        permission.getRequestId(),
                        RiskType.NOT_EXITED,
                        com.visitor.access.model.enums.RiskLevel.HIGH,
                        "访客未在规定时间内离场"
                );
            }
        }
    }

    @Scheduled(cron = "0 0 2 * * ?")
    public void generateDailyRiskReport() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        List<RiskRecord> recentRisks = repository.findAllRiskRecords().stream()
                .filter(r -> r.getRecordedAt().isAfter(yesterday))
                .toList();

        for (RiskRecord record : recentRisks) {
            updateVisitorRiskLevel(record.getVisitorId());
        }
    }

    private void updateVisitorRiskLevel(String visitorId) {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        List<RiskRecord> recentRecords = repository.findRiskRecordsByVisitorId(visitorId).stream()
                .filter(r -> r.getRecordedAt().isAfter(thirtyDaysAgo))
                .toList();

        int score = 0;
        for (RiskRecord record : recentRecords) {
            switch (record.getRiskType()) {
                case LATE: score += 1; break;
                case OVERSTAY: score += 2; break;
                case NOT_EXITED: score += 3; break;
                case NO_SHOW: score += 5; break;
            }
        }

        com.visitor.access.model.enums.RiskLevel level;
        if (score >= 10) {
            level = com.visitor.access.model.enums.RiskLevel.HIGH;
        } else if (score >= 5) {
            level = com.visitor.access.model.enums.RiskLevel.MEDIUM;
        } else {
            level = com.visitor.access.model.enums.RiskLevel.LOW;
        }

        visitorService.updateVisitorRiskLevel(visitorId, level);
    }

    public StatisticsResponse getStatistics() {
        StatisticsResponse stats = new StatisticsResponse();
        LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();
        
        stats.setTodayVisitors(repository.findAllVisitRequests().stream()
                .filter(r -> r.getCreatedAt().isAfter(today))
                .count());
        
        stats.setPendingApprovals(repository.findVisitRequestsByStatus(RequestStatus.PENDING).stream()
                .count());
        
        stats.setActivePermissions(repository.findPermissionsByStatus(PermissionStatus.ACTIVE).stream()
                .count());
        
        stats.setRiskRecords(repository.findAllRiskRecords().stream()
                .filter(r -> r.getRecordedAt().isAfter(today))
                .count());
        
        stats.setNotExitedVisitors(repository.findPermissionsByStatus(PermissionStatus.ACTIVE).stream()
                .filter(p -> LocalDateTime.now().isAfter(p.getValidUntil().plusHours(2)))
                .count());
        
        return stats;
    }
}
