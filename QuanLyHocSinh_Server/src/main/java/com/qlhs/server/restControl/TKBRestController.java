package com.qlhs.server.restControl;

import com.qlhs.server.entity.TKB;
import com.qlhs.server.repository.GiaoVienRepository;
import com.qlhs.server.repository.LopRepository;
import com.qlhs.server.repository.MonHocRepository;
import com.qlhs.server.repository.PhongHocRepository;
import com.qlhs.server.service.TKBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tkb")
public class TKBRestController {
    @Autowired
    private TKBService tkbService;
    @Autowired
    private GiaoVienRepository giaoVienRepository;

    @Autowired
    private LopRepository lopRepository;

    @Autowired
    private PhongHocRepository phongHocRepository;

    @Autowired
    private MonHocRepository monHocRepository;

    @GetMapping
    public List<TKB> getAllTKB() {
        return tkbService.getAllTKB();
    }

    @GetMapping("/{maTKB}")  // fix: đổi {maKB} → {maTKB}
    public ResponseEntity<TKB> getByIdTKB(@PathVariable Integer maTKB) {
        return tkbService.getByIdTKB(maTKB)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/danhsachlop")
    public List<String> getDanhSachLop() {
        return tkbService.getDistinctMaLop();
    }

    @GetMapping("/danhsachlop/tatca")
    public List<Map<String, String>> getDanhSachLopTatCa() {
        return lopRepository.findAll().stream()
                .map(l -> Map.of("ma", l.getMaLop(), "ten", l.getTenLop()))
                .collect(Collectors.toList());
    }

    @GetMapping("/danhsachmon")
    public List<Map<String, String>> getDanhSachMon() {
        return monHocRepository.findAll().stream()
                .map(m -> Map.of("ma", m.getMaMH(), "ten", m.getTenMH()))
                .collect(Collectors.toList());
    }


    @GetMapping("/danhsachphong")
    public List<Map<String, String>> getDanhSachPhong() {
        return phongHocRepository.findAll().stream()
                .map(p -> Map.of("ma", p.getMaPhong(), "ten", p.getTenPhong()))
                .collect(Collectors.toList());
    }

    @GetMapping("/danhsachgv")
    public List<Map<String, String>> getDanhSachGV() {
        return giaoVienRepository.findAll().stream()
                .map(g -> Map.of("ma", g.getMaGV(), "ten", g.getHoTen()))
                .collect(Collectors.toList());
    }

    @GetMapping("/filter")
    public List<TKB> filter(
            @RequestParam(defaultValue = "") String maLop,
            @RequestParam(defaultValue = "") String maMH,
            @RequestParam(defaultValue = "0") int thu) {
        return tkbService.filter(maLop, maMH, thu);
    }

    @PostMapping
    public ResponseEntity<TKB> create(@RequestBody TKB tkb) {
        if (tkbService.isTrungTiet(tkb)) {
            return ResponseEntity.status(409).body(null);
        }
        return ResponseEntity.ok(tkbService.save(tkb));
    }

    @PutMapping("/{maTKB}")
    public ResponseEntity<TKB> update(@PathVariable Integer maTKB, @RequestBody TKB tkb) {
        if (!tkbService.existsByIdTKB(maTKB)) {
            return ResponseEntity.notFound().build();
        }
        tkb.setMaTKB(maTKB);
        return ResponseEntity.ok(tkbService.save(tkb));
    }

    @DeleteMapping("/{maTKB}")
    public ResponseEntity<Void> delete(@PathVariable Integer maTKB) {
        if (!tkbService.existsByIdTKB(maTKB)) {
            return ResponseEntity.notFound().build();
        }
        tkbService.delete(maTKB);
        return ResponseEntity.ok().build();
    }
}