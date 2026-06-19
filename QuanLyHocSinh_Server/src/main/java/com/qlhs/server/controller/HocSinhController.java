package com.qlhs.server.controller;

import com.qlhs.server.entity.HocSinh;
import com.qlhs.server.service.HocSinhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Cho biết đây là lớp Controller trả về dữ liệu (JSON)
@RequestMapping("/api/hocsinh") // Đường dẫn gốc cho tất cả API học sinh
public class HocSinhController {

    @Autowired
    private HocSinhService hocSinhService;

    // API 1: Lấy danh sách học sinh
    // Khi gọi GET http://localhost:8080/api/hocsinh
    @GetMapping
    public List<HocSinh> getAllHocSinh() {
        return hocSinhService.getAllHocSinh();
    }

    // API 2: Lấy 1 học sinh theo Mã
    // Khi gọi GET http://localhost:8080/api/hocsinh/{maHS}
    @GetMapping("/{maHS}")
    public ResponseEntity<HocSinh> getHocSinhById(@PathVariable String maHS) {
        return hocSinhService.getHocSinhById(maHS)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // API 3: Thêm mới hoặc Cập nhật học sinh
    // Khi gọi POST http://localhost:8080/api/hocsinh
    @PostMapping
    public HocSinh createOrUpdateHocSinh(@RequestBody HocSinh hs) {
        return hocSinhService.saveHocSinh(hs);
    }

    // API 4: Xóa học sinh
    // Khi gọi DELETE http://localhost:8080/api/hocsinh/{maHS}
    @DeleteMapping("/{maHS}")
    public ResponseEntity<Void> deleteHocSinh(@PathVariable String maHS) {
        hocSinhService.deleteHocSinh(maHS);
        return ResponseEntity.ok().build();
    }
}
