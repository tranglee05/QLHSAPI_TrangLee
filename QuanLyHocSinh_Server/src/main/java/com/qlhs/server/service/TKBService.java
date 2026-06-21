package com.qlhs.server.service;

import com.qlhs.server.entity.TKB;
import com.qlhs.server.repository.MonHocRepository;
import com.qlhs.server.repository.TKBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TKBService {
    @Autowired
    private TKBRepository tkbRepository;

    @Autowired
    private MonHocRepository monHocRepository;

    private void fillTenMH(List<TKB> list) {
        list.forEach(t -> monHocRepository.findById(t.getMaMH())
                .ifPresent(m -> t.setTenMH(m.getTenMH())));
    }

    public List<TKB> getAllTKB() {
        List<TKB> list = tkbRepository.findAll();
        fillTenMH(list);
        return list;
    }

    public Optional<TKB> getByIdTKB(String maTKB) {
        return tkbRepository.findById(maTKB);
    }

    public List<TKB> getByMaLop(String maLop) {
        List<TKB> list = tkbRepository.findByMaLop(maLop);
        fillTenMH(list);
        return list;
    }

    public List<TKB> getByMaMH(String maMH) {
        List<TKB> list = tkbRepository.findByMaMH(maMH);
        fillTenMH(list);
        return list;
    }

    public List<TKB> getByMaPhong(String maPhong) {
        List<TKB> list = tkbRepository.findByMaPhong(maPhong);
        fillTenMH(list);
        return list;
    }

    public List<TKB> getByMaGV(String maGV) {
        List<TKB> list = tkbRepository.findByMaGV(maGV);
        fillTenMH(list);
        return list;
    }

    public List<TKB> filter(String maLop, String maMH, int thu) {
        List<TKB> list = tkbRepository.findAll().stream()
                .filter(t -> maLop.isEmpty() || maLop.equals("Tất cả") || t.getMaLop().equals(maLop))
                .filter(t -> maMH.isEmpty() || t.getMaMH().contains(maMH))
                .filter(t -> thu == 0 || t.getThu() == thu)
                .collect(Collectors.toList());
        fillTenMH(list);
        return list;
    }

    public List<String> getDistinctMaLop() {
        return tkbRepository.findAll().stream()
                .map(TKB::getMaLop)
                .distinct()
                .collect(Collectors.toList());
    }

    public TKB save(TKB tkb) {
        return tkbRepository.save(tkb);
    }

    public void delete(String maTKB) {
        tkbRepository.deleteById(maTKB);
    }

    public boolean existsByIdTKB(String maTKB) {
        return tkbRepository.existsById(maTKB);
    }
}