package com.qlhs.server.service;

import com.qlhs.server.repository.LopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LopService {

    @Autowired
    private LopRepository lopRepository;

    public List<Map<String, Object>> getAllLop() {
        return lopRepository.findAllLopWithGVCN();
    }
}
