package com.qlhs.server.controller;

import com.qlhs.server.entity.Diem;
import com.qlhs.server.service.DiemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diem")
public class DiemController {

    @Autowired
    private DiemService diemService;

    @GetMapping
    public List<Diem> getAllDiem() {
        return diemService.getAllDiem();
    }

    @GetMapping("/hocsinh/{maHS}")
    public List<Diem> getDiemByMaHS(@PathVariable String maHS) {
        return diemService.getDiemByMaHS(maHS);
    }

    @PostMapping
    public ResponseEntity<Diem> createOrUpdateDiem(@RequestBody Diem diem) {
        return ResponseEntity.ok(diemService.saveDiem(diem));
    }
}
