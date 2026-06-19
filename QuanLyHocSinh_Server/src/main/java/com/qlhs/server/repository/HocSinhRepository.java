package com.qlhs.server.repository;

import com.qlhs.server.entity.HocSinh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HocSinhRepository extends JpaRepository<HocSinh, String> {
    // Chỉ cần kế thừa JpaRepository, Spring Boot đã tự tạo sẵn các lệnh:
    // findAll() -> Lấy tất cả
    // findById() -> Tìm theo mã
    // save() -> Thêm/Sửa
    // deleteById() -> Xóa
}
