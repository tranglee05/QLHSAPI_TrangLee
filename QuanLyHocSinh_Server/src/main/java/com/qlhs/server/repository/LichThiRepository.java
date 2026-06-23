package com.qlhs.server.repository;

import com.qlhs.server.entity.LichThi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Map;

@Repository
public interface LichThiRepository extends JpaRepository<LichThi, Integer> {

    @Query("SELECT l FROM LichThi l WHERE l.tenKyThi LIKE %:keyword% OR l.maMH LIKE %:keyword%")
    List<LichThi> searchLichThi(@Param("keyword") String keyword);

    @Query(value = "SELECT DISTINCT TenKyThi FROM LichThi", nativeQuery = true)
    List<String> getDistinctKyThi();

    @Query(value = "SELECT lt.*, mh.TenMH as tenMH FROM LichThi lt JOIN MonHoc mh ON lt.MaMH = mh.MaMH " +
                   "WHERE lt.TenKyThi LIKE %:tenKyThi% AND lt.MaMH LIKE %:maMH% AND lt.MaPhong LIKE %:maPhong%", nativeQuery = true)
    List<Map<String, Object>> filterLichThi(@Param("tenKyThi") String tenKyThi, @Param("maMH") String maMH, @Param("maPhong") String maPhong);

    @Query(value = "SELECT lt.*, mh.TenMH as tenMH FROM LichThi lt JOIN MonHoc mh ON lt.MaMH = mh.MaMH", nativeQuery = true)
    List<Map<String, Object>> getAllLichThiWithTenMon();

    @Query(value = "SELECT lt.*, mh.TenMH as tenMH FROM LichThi lt JOIN MonHoc mh ON lt.MaMH = mh.MaMH " +
                   "WHERE lt.TenKyThi LIKE %:keyword% OR mh.TenMH LIKE %:keyword% OR lt.MaMH LIKE %:keyword%", nativeQuery = true)
    List<Map<String, Object>> searchLichThiNative(@Param("keyword") String keyword);
}
