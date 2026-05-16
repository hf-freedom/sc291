package com.visitor.access.controller;

import com.visitor.access.dto.request.VisitorDTO;
import com.visitor.access.dto.response.ApiResponse;
import com.visitor.access.model.Visitor;
import com.visitor.access.service.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visitors")
@CrossOrigin(origins = "*")
public class VisitorController {

    @Autowired
    private VisitorService visitorService;

    @PostMapping
    public ApiResponse<Visitor> createVisitor(@RequestBody VisitorDTO dto) {
        try {
            Visitor visitor = visitorService.createVisitor(dto);
            return ApiResponse.success("访客创建成功", visitor);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<Visitor> getVisitor(@PathVariable String id) {
        return visitorService.findById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("访客不存在"));
    }

    @GetMapping
    public ApiResponse<List<Visitor>> getAllVisitors() {
        return ApiResponse.success(visitorService.findAll());
    }
}
