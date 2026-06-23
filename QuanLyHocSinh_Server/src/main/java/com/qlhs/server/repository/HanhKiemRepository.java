package com.qlhs.server.repository;

import com.qlhs.server.entity.HanhKiem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface HanhKiemRepository extends JpaRepository<HanhKiem, HanhKiem.HanhKiemId> {

    @Query(value = "SELECT hk.*, hs.HoTen as tenHS, hs.MaLop as lopHocSinh " +
                   "FROM HanhKiem hk JOIN HocSinh hs ON hk.MaHS = hs.MaHS " +
                   "WHERE hs.MaLop LIKE %:maLop% AND hk.NamHoc LIKE %:namHoc% AND (:hocKy = 0 OR hk.HocKy = :hocKy)", nativeQuery = true)
    List<Map<String, Object>> getHanhKiemByFilter(@Param("maLop") String maLop, @Param("namHoc") String namHoc, @Param("hocKy") int hocKy);

    @Query(value = "SELECT hk.*, hs.HoTen as tenHS, hs.MaLop as lopHocSinh " +
                   "FROM HanhKiem hk JOIN HocSinh hs ON hk.MaHS = hs.MaHS " +
                   "WHERE hk.MaHS LIKE %:keyword% OR hs.HoTen LIKE %:keyword%", nativeQuery = true)
    List<Map<String, Object>> searchHanhKiem(@Param("keyword") String keyword);

    @Query(value = "SELECT hk.*, hs.HoTen as tenHS, hs.MaLop as lopHocSinh " +
                   "FROM HanhKiem hk JOIN HocSinh hs ON hk.MaHS = hs.MaHS " +
                   "WHERE hk.MaHS = :maHS", nativeQuery = true)
    List<Map<String, Object>> getHanhKiemByMaHS(@Param("maHS") String maHS);

    @Query(value = "SELECT DISTINCT NamHoc FROM HanhKiem ORDER BY NamHoc DESC", nativeQuery = true)
    List<String> getDistinctNamHoc();
}
