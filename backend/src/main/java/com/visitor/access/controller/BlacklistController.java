package com.visitor.access.controller;

import com.visitor.access.dto.request.BlacklistDTO;
import com.visitor.access.dto.response.ApiResponse;
import com.visitor.access.model.BlacklistEntry;
import com.visitor.access.service.BlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blacklist")
@CrossOrigin(origins = "*")
public class BlacklistController {

    @Autowired
    private BlacklistService blacklistService;

    @PostMapping
    public ApiResponse<BlacklistEntry> addToBlacklist(@RequestBody BlacklistDTO dto) {
        try {
            BlacklistEntry entry = blacklistService.addToBlacklist(dto);
            return ApiResponse.success("已添加到黑名单", entry);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/{visitorId}")
    public ApiResponse<Void> removeFromBlacklist(@PathVariable String visitorId) {
        try {
            blacklistService.removeFromBlacklist(visitorId);
            return ApiResponse.success("已从黑名单移除", null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping
    public ApiResponse<List<BlacklistEntry>> getAllBlacklist() {
        return ApiResponse.success(blacklistService.findAll());
    }

    @GetMapping("/{visitorId}")
    public ApiResponse<BlacklistEntry> getBlacklistEntry(@PathVariable String visitorId) {
        return blacklistService.findByVisitorId(visitorId)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("访客不在黑名单中"));
    }
}
