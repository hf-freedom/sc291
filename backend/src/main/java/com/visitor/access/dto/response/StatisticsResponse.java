package com.visitor.access.dto.response;

public class StatisticsResponse {
    private long todayVisitors;
    private long pendingApprovals;
    private long activePermissions;
    private long riskRecords;
    private long notExitedVisitors;

    public long getTodayVisitors() { return todayVisitors; }
    public void setTodayVisitors(long todayVisitors) { this.todayVisitors = todayVisitors; }
    public long getPendingApprovals() { return pendingApprovals; }
    public void setPendingApprovals(long pendingApprovals) { this.pendingApprovals = pendingApprovals; }
    public long getActivePermissions() { return activePermissions; }
    public void setActivePermissions(long activePermissions) { this.activePermissions = activePermissions; }
    public long getRiskRecords() { return riskRecords; }
    public void setRiskRecords(long riskRecords) { this.riskRecords = riskRecords; }
    public long getNotExitedVisitors() { return notExitedVisitors; }
    public void setNotExitedVisitors(long notExitedVisitors) { this.notExitedVisitors = notExitedVisitors; }
}
