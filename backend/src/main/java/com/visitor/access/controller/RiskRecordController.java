package com.visitor.access.controller;

import com.visitor.access.dto.response.ApiResponse;
import com.visitor.access.model.RiskRecord;
import com.visitor.access.model.enums.RiskType;
import com.visitor.access.service.RiskRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/risk-records")
@CrossOrigin(origins = "*")
public class RiskRecordController {

    @Autowired
    private RiskRecordService riskRecordService;

    @GetMapping
    public ApiResponse<List<RiskRecord>> getRiskRecords(
            @RequestParam(required = false) String visitorId,
            @RequestParam(required = false) String riskType) {
        
        List<RiskRecord> records;
        if (visitorId != null) {
            records = riskRecordService.findByVisitorId(visitorId);
        } else if (riskType != null) {
            records = riskRecordService.findByType(RiskType.valueOf(riskType));
        } else {
            records = riskRecordService.findAll();
        }
        return ApiResponse.success(records);
    }

    @GetMapping("/{id}")
    public ApiResponse<RiskRecord> getRiskRecord(@PathVariable String id) {
        return riskRecordService.findById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("风险记录不存在"));
    }
}
