package com.qlhs.server.controller;

import com.qlhs.server.entity.TaiKhoan;
import com.qlhs.server.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/taikhoan")
public class TaiKhoanController {

    @Autowired
    private TaiKhoanService taiKhoanService;

    // Nhận yêu cầu đăng nhập từ Client
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("tenDangNhap");
        String password = loginData.get("matKhau");

        TaiKhoan tk = taiKhoanService.checkLogin(username, password);
        if (tk != null) {
            return ResponseEntity.ok(tk); // Trả về JSON thông tin tài khoản (có Quyen, MaNguoiDung)
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai tài khoản hoặc mật khẩu");
        }
    }
}
