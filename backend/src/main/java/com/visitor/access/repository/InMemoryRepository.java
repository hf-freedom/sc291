package com.visitor.access.repository;

import com.visitor.access.model.*;
import com.visitor.access.model.enums.*;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Repository
public class InMemoryRepository {

    private final Map<String, Visitor> visitors = new ConcurrentHashMap<>();
    private final Map<String, VisitRequest> visitRequests = new ConcurrentHashMap<>();
    private final Map<String, AccessPermission> permissions = new ConcurrentHashMap<>();
    private final Map<String, Area> areas = new ConcurrentHashMap<>();
    private final Map<String, RiskRecord> riskRecords = new ConcurrentHashMap<>();
    private final List<BlacklistEntry> blacklist = new CopyOnWriteArrayList<>();
    private final Map<String, Host> hosts = new ConcurrentHashMap<>();
    private final Map<String, SecurityGuard> securityGuards = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        initVisitors();
        initHosts();
        initSecurityGuards();
        initAreas();
    }

    private void initVisitors() {
        Visitor v1 = new Visitor();
        v1.setId("visitor-001");
        v1.setName("张三");
        v1.setPhone("13800138001");
        v1.setIdCard("110101199001011001");
        v1.setEmail("zhangsan@example.com");
        v1.setRiskLevel(RiskLevel.LOW);
        v1.setCreatedAt(LocalDateTime.now());
        v1.setUpdatedAt(LocalDateTime.now());
        visitors.put(v1.getId(), v1);

        Visitor v2 = new Visitor();
        v2.setId("visitor-002");
        v2.setName("李四");
        v2.setPhone("13800138002");
        v2.setIdCard("110101199002022002");
        v2.setEmail("lisi@example.com");
        v2.setRiskLevel(RiskLevel.MEDIUM);
        v2.setCreatedAt(LocalDateTime.now());
        v2.setUpdatedAt(LocalDateTime.now());
        visitors.put(v2.getId(), v2);

        Visitor v3 = new Visitor();
        v3.setId("visitor-003");
        v3.setName("王五");
        v3.setPhone("13800138003");
        v3.setIdCard("110101199003033003");
        v3.setEmail("wangwu@example.com");
        v3.setRiskLevel(RiskLevel.HIGH);
        v3.setCreatedAt(LocalDateTime.now());
        v3.setUpdatedAt(LocalDateTime.now());
        visitors.put(v3.getId(), v3);
    }

    private void initHosts() {
        Host h1 = new Host();
        h1.setId("host-001");
        h1.setName("赵经理");
        h1.setDepartment("总经理室");
        h1.setPhone("13900139001");
        h1.setEmail("zhaojingli@example.com");
        hosts.put(h1.getId(), h1);

        Host h2 = new Host();
        h2.setId("host-002");
        h2.setName("钱主管");
        h2.setDepartment("研发部");
        h2.setPhone("13900139002");
        h2.setEmail("qianzhuguan@example.com");
        hosts.put(h2.getId(), h2);

        Host h3 = new Host();
        h3.setId("host-003");
        h3.setName("孙工程师");
        h3.setDepartment("技术部");
        h3.setPhone("13900139003");
        h3.setEmail("sungongchengshi@example.com");
        hosts.put(h3.getId(), h3);
    }

    private void initSecurityGuards() {
        SecurityGuard s1 = new SecurityGuard();
        s1.setId("security-001");
        s1.setName("周保安");
        s1.setBadgeNumber("B001");
        s1.setPhone("13700137001");
        securityGuards.put(s1.getId(), s1);

        SecurityGuard s2 = new SecurityGuard();
        s2.setId("security-002");
        s2.setName("吴保安");
        s2.setBadgeNumber("B002");
        s2.setPhone("13700137002");
        securityGuards.put(s2.getId(), s2);
    }

    private void initAreas() {
        Area a1 = new Area();
        a1.setId("area-001");
        a1.setName("一号门");
        a1.setDescription("园区主入口");
        a1.setRequiredPermissionLevel(PermissionLevel.BASIC);
        a1.setStatus("ACTIVE");
        a1.setCreatedAt(LocalDateTime.now());
        areas.put(a1.getId(), a1);

        Area a2 = new Area();
        a2.setId("area-002");
        a2.setName("办公区A");
        a2.setDescription("普通办公区域");
        a2.setRequiredPermissionLevel(PermissionLevel.BASIC);
        a2.setStatus("ACTIVE");
        a2.setCreatedAt(LocalDateTime.now());
        areas.put(a2.getId(), a2);

        Area a3 = new Area();
        a3.setId("area-003");
        a3.setName("研发中心");
        a3.setDescription("研发部门区域");
        a3.setRequiredPermissionLevel(PermissionLevel.INTERMEDIATE);
        a3.setStatus("ACTIVE");
        a3.setCreatedAt(LocalDateTime.now());
        areas.put(a3.getId(), a3);

        Area a4 = new Area();
        a4.setId("area-004");
        a4.setName("数据中心");
        a4.setDescription("核心机密区域");
        a4.setRequiredPermissionLevel(PermissionLevel.ADVANCED);
        a4.setStatus("ACTIVE");
        a4.setCreatedAt(LocalDateTime.now());
        areas.put(a4.getId(), a4);
    }

    public void saveVisitor(Visitor visitor) {
        visitors.put(visitor.getId(), visitor);
    }

    public Optional<Visitor> findVisitorById(String id) {
        return Optional.ofNullable(visitors.get(id));
    }

    public List<Visitor> findAllVisitors() {
        return new ArrayList<>(visitors.values());
    }

    public void saveVisitRequest(VisitRequest request) {
        visitRequests.put(request.getId(), request);
    }

    public Optional<VisitRequest> findVisitRequestById(String id) {
        return Optional.ofNullable(visitRequests.get(id));
    }

    public List<VisitRequest> findAllVisitRequests() {
        return new ArrayList<>(visitRequests.values());
    }

    public List<VisitRequest> findVisitRequestsByVisitorId(String visitorId) {
        return visitRequests.values().stream()
                .filter(r -> r.getVisitorId().equals(visitorId))
                .collect(Collectors.toList());
    }

    public List<VisitRequest> findVisitRequestsByHostId(String hostId) {
        return visitRequests.values().stream()
                .filter(r -> r.getHostId().equals(hostId))
                .collect(Collectors.toList());
    }

    public List<VisitRequest> findVisitRequestsByStatus(RequestStatus status) {
        return visitRequests.values().stream()
                .filter(r -> r.getStatus() == status)
                .collect(Collectors.toList());
    }

    public void savePermission(AccessPermission permission) {
        permissions.put(permission.getId(), permission);
    }

    public Optional<AccessPermission> findPermissionById(String id) {
        return Optional.ofNullable(permissions.get(id));
    }

    public Optional<AccessPermission> findPermissionByCode(String code) {
        return permissions.values().stream()
                .filter(p -> p.getPermissionCode().equals(code))
                .findFirst();
    }

    public List<AccessPermission> findAllPermissions() {
        return new ArrayList<>(permissions.values());
    }

    public List<AccessPermission> findPermissionsByStatus(PermissionStatus status) {
        return permissions.values().stream()
                .filter(p -> p.getStatus() == status)
                .collect(Collectors.toList());
    }

    public void saveArea(Area area) {
        areas.put(area.getId(), area);
    }

    public Optional<Area> findAreaById(String id) {
        return Optional.ofNullable(areas.get(id));
    }

    public List<Area> findAllAreas() {
        return new ArrayList<>(areas.values());
    }

    public void saveRiskRecord(RiskRecord record) {
        riskRecords.put(record.getId(), record);
    }

    public List<RiskRecord> findAllRiskRecords() {
        return new ArrayList<>(riskRecords.values());
    }

    public List<RiskRecord> findRiskRecordsByVisitorId(String visitorId) {
        return riskRecords.values().stream()
                .filter(r -> r.getVisitorId().equals(visitorId))
                .collect(Collectors.toList());
    }

    public List<RiskRecord> findRiskRecordsByType(RiskType type) {
        return riskRecords.values().stream()
                .filter(r -> r.getRiskType() == type)
                .collect(Collectors.toList());
    }

    public void addToBlacklist(BlacklistEntry entry) {
        blacklist.add(entry);
    }

    public void removeFromBlacklist(String visitorId) {
        blacklist.removeIf(e -> e.getVisitorId().equals(visitorId));
    }

    public List<BlacklistEntry> findAllBlacklist() {
        return new ArrayList<>(blacklist);
    }

    public Optional<BlacklistEntry> findBlacklistByVisitorId(String visitorId) {
        return blacklist.stream()
                .filter(e -> e.getVisitorId().equals(visitorId))
                .findFirst();
    }

    public boolean isInBlacklist(String visitorId) {
        Optional<BlacklistEntry> entry = findBlacklistByVisitorId(visitorId);
        if (!entry.isPresent()) {
            return false;
        }
        BlacklistEntry e = entry.get();
        if (e.getExpireAt() == null) {
            return true;
        }
        return e.getExpireAt().isAfter(LocalDateTime.now());
    }

    public List<Host> findAllHosts() {
        return new ArrayList<>(hosts.values());
    }

    public Optional<Host> findHostById(String id) {
        return Optional.ofNullable(hosts.get(id));
    }

    public List<SecurityGuard> findAllSecurityGuards() {
        return new ArrayList<>(securityGuards.values());
    }

    public Optional<SecurityGuard> findSecurityGuardById(String id) {
        return Optional.ofNullable(securityGuards.get(id));
    }

    public String generateId(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8);
    }

    public String generatePermissionCode() {
        return "PC" + System.currentTimeMillis();
    }
}
