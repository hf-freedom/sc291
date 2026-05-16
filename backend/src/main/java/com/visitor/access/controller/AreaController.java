package com.visitor.access.controller;

import com.visitor.access.dto.request.AreaDTO;
import com.visitor.access.dto.response.ApiResponse;
import com.visitor.access.model.Area;
import com.visitor.access.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/areas")
@CrossOrigin(origins = "*")
public class AreaController {

    @Autowired
    private AreaService areaService;

    @PostMapping
    public ApiResponse<Area> createArea(@RequestBody AreaDTO dto) {
        try {
            Area area = areaService.createArea(dto);
            return ApiResponse.success("区域创建成功", area);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<Area> getArea(@PathVariable String id) {
        return areaService.findById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("区域不存在"));
    }

    @GetMapping
    public ApiResponse<List<Area>> getAllAreas() {
        return ApiResponse.success(areaService.findAll());
    }

    @PutMapping("/{id}")
    public ApiResponse<Area> updateArea(@PathVariable String id, @RequestBody AreaDTO dto) {
        try {
            Area area = areaService.updateArea(id, dto);
            return ApiResponse.success("区域更新成功", area);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteArea(@PathVariable String id) {
        try {
            areaService.deleteArea(id);
            return ApiResponse.success("区域删除成功", null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
