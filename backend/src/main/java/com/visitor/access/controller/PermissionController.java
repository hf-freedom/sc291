package com.visitor.access.controller;

import com.visitor.access.dto.response.ApiResponse;
import com.visitor.access.dto.response.CheckInResponse;
import com.visitor.access.dto.response.CheckOutResponse;
import com.visitor.access.model.AccessPermission;
import com.visitor.access.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@CrossOrigin(origins = "*")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/{id}")
    public ApiResponse<AccessPermission> getPermission(@PathVariable String id) {
        return permissionService.findById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("权限不存在"));
    }

    @GetMapping("/code/{code}")
    public ApiResponse<AccessPermission> getPermissionByCode(@PathVariable String code) {
        return permissionService.findByCode(code)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("权限不存在"));
    }

    @GetMapping
    public ApiResponse<List<AccessPermission>> getAllPermissions() {
        return ApiResponse.success(permissionService.findAll());
    }

    @PostMapping("/{id}/check-in")
    public ApiResponse<CheckInResponse> checkIn(@PathVariable String id) {
        try {
            CheckInResponse response = permissionService.checkIn(id);
            return ApiResponse.success(response.getMessage(), response);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/check-out")
    public ApiResponse<CheckOutResponse> checkOut(@PathVariable String id) {
        try {
            CheckOutResponse response = permissionService.checkOut(id);
            return ApiResponse.success(response.getMessage(), response);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
