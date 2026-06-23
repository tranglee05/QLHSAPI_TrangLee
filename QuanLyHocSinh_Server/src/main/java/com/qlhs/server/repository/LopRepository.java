package com.qlhs.server.repository;

import com.qlhs.server.entity.Lop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface LopRepository extends JpaRepository<Lop, String> {
    
    @Query(value = "SELECT l.MaLop as maLop, l.TenLop as tenLop, l.NienKhoa as nienKhoa, l.MaGVCN as maGVCN, gv.HoTen as tenGVCN " +
                   "FROM Lop l LEFT JOIN GiaoVien gv ON l.MaGVCN = gv.MaGV", nativeQuery = true)
    List<Map<String, Object>> findAllLopWithGVCN();
}