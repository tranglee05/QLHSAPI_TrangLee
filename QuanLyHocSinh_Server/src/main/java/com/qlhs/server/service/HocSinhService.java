package com.qlhs.server.service;

import com.qlhs.server.entity.HocSinh;
import com.qlhs.server.repository.HocSinhRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Đánh dấu đây là lớp xử lý Logic Nghiệp Vụ
public class HocSinhService {

    @Autowired // Spring Boot tự động tiêm Repository vào đây
    private HocSinhRepository hocSinhRepository;

    // Lấy danh sách toàn bộ học sinh
    public List<HocSinh> getAllHocSinh() {
        return hocSinhRepository.findAll();
    }

    // Lấy thông tin 1 học sinh theo mã
    public Optional<HocSinh> getHocSinhById(String maHS) {
        return hocSinhRepository.findById(maHS);
    }

    // Thêm hoặc Cập nhật học sinh
    public HocSinh saveHocSinh(HocSinh hs) {
        // Ở đây bạn có thể thêm logic như kiểm tra tên không được trống
        if (hs.getHoTen() == null || hs.getHoTen().isEmpty()) {
            throw new IllegalArgumentException("Tên học sinh không được để trống!");
        }
        return hocSinhRepository.save(hs);
    }

    // Xóa học sinh
    public void deleteHocSinh(String maHS) {
        hocSinhRepository.deleteById(maHS);
    }
}
