package com.qlhs.server.service;

import com.qlhs.server.entity.Diem;
import com.qlhs.server.repository.DiemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiemService {

    @Autowired
    private DiemRepository diemRepository;

    public List<Diem> getAllDiem() {
        return diemRepository.findAll();
    }

    public List<Diem> getDiemByMaHS(String maHS) {
        return diemRepository.findByMaHS(maHS);
    }

    public Diem saveDiem(Diem diem) {
        return diemRepository.save(diem);
    }
}
