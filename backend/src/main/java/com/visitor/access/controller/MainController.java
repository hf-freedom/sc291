package com.visitor.access.controller;

import com.visitor.access.dto.response.ApiResponse;
import com.visitor.access.dto.response.StatisticsResponse;
import com.visitor.access.model.Host;
import com.visitor.access.model.SecurityGuard;
import com.visitor.access.service.HostService;
import com.visitor.access.service.ScheduledTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class MainController {

    @Autowired
    private ScheduledTaskService scheduledTaskService;

    @Autowired
    private HostService hostService;

    @GetMapping("/statistics")
    public ApiResponse<StatisticsResponse> getStatistics() {
        return ApiResponse.success(scheduledTaskService.getStatistics());
    }

    @GetMapping("/hosts")
    public ApiResponse<List<Host>> getAllHosts() {
        return ApiResponse.success(hostService.findAll());
    }

    @GetMapping("/hosts/{id}")
    public ApiResponse<Host> getHost(@PathVariable String id) {
        return hostService.findById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("被访人不存在"));
    }

    @GetMapping("/security-guards")
    public ApiResponse<List<SecurityGuard>> getAllSecurityGuards() {
        return ApiResponse.success(hostService.findAllSecurityGuards());
    }

    @GetMapping("/security-guards/{id}")
    public ApiResponse<SecurityGuard> getSecurityGuard(@PathVariable String id) {
        return hostService.findSecurityGuardById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("保安不存在"));
    }
}
