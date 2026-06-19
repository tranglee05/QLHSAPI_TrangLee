package com.qlhs.server.repository;

import com.qlhs.server.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, String> {
    
    // Spring Data JPA tự động dịch tên hàm thành câu query:
    // SELECT * FROM TaiKhoan WHERE TenDangNhap = ? AND MatKhau = ?
    Optional<TaiKhoan> findByTenDangNhapAndMatKhau(String tenDangNhap, String matKhau);
}
