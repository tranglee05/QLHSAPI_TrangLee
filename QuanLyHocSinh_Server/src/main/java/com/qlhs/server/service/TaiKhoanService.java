package com.qlhs.server.service;

import com.qlhs.server.entity.TaiKhoan;
import com.qlhs.server.repository.TaiKhoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaiKhoanService {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    public TaiKhoan checkLogin(String username, String password) {
        Optional<TaiKhoan> tk = taiKhoanRepository.findByTenDangNhapAndMatKhau(username, password);
        return tk.orElse(null); // Trả về null nếu không tìm thấy
    }
}
