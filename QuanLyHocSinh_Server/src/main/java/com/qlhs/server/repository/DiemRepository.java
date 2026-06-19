package com.qlhs.server.repository;

import com.qlhs.server.entity.Diem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiemRepository extends JpaRepository<Diem, String> {
    // Tìm điểm của một học sinh
    List<Diem> findByMaHS(String maHS);
}
