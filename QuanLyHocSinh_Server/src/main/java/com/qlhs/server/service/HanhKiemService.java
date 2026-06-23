package com.qlhs.server.service;

import com.qlhs.server.entity.HanhKiem;
import com.qlhs.server.repository.HanhKiemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class HanhKiemService {
    @Autowired
    private HanhKiemRepository hanhKiemRepository;

    public List<Map<String, Object>> getHanhKiemByFilter(String maLop, String namHoc, int hocKy) {
        return hanhKiemRepository.getHanhKiemByFilter(maLop, namHoc, hocKy);
    }

    public List<Map<String, Object>> searchHanhKiem(String keyword) {
        return hanhKiemRepository.searchHanhKiem(keyword);
    }

    public List<Map<String, Object>> getHanhKiemByMaHS(String maHS) {
        return hanhKiemRepository.getHanhKiemByMaHS(maHS);
    }

    public List<String> getDistinctNamHoc() {
        return hanhKiemRepository.getDistinctNamHoc();
    }

    public HanhKiem save(HanhKiem hk) {
        return hanhKiemRepository.save(hk);
    }

    public void delete(String maHS, String namHoc, int hocKy) {
        hanhKiemRepository.deleteById(new HanhKiem.HanhKiemId(maHS, namHoc, hocKy));
    }

    public boolean exists(String maHS, String namHoc, int hocKy) {
        return hanhKiemRepository.existsById(new HanhKiem.HanhKiemId(maHS, namHoc, hocKy));
    }
}
