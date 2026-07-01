package com.qlhs.server.repository;

import com.qlhs.server.entity.Diem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DiemRepository extends JpaRepository<Diem, Diem.DiemId> {

    String BASE_QUERY = "SELECT d.MaHS as maHS, hs.HoTen as tenHS, d.MaMH as maMH, mh.TenMH as tenMH, " +
                        "d.HocKy as hocKy, d.Diem15p as diem15p, d.Diem1Tiet as diem1Tiet, " +
                        "d.DiemGiuaKy as diemGiuaKy, d.DiemCuoiKy as diemCuoiKy " +
                        "FROM Diem d " +
                        "JOIN HocSinh hs ON d.MaHS = hs.MaHS " +
                        "JOIN MonHoc mh ON d.MaMH = mh.MaMH ";

    @Query(value = BASE_QUERY, nativeQuery = true)
    List<Map<String, Object>> findAllDiemWithDetails();

    @Query(value = BASE_QUERY + "WHERE (:maLop = '' OR hs.MaLop = :maLop) AND (:maMH = '' OR d.MaMH = :maMH) AND (:hocKy = 0 OR d.HocKy = :hocKy)", nativeQuery = true)
    List<Map<String, Object>> findDiemByFilter(@Param("maLop") String maLop, @Param("maMH") String maMH, @Param("hocKy") int hocKy);

    @Query(value = BASE_QUERY + "WHERE d.MaHS LIKE %:keyword% OR hs.HoTen LIKE %:keyword%", nativeQuery = true)
    List<Map<String, Object>> searchDiem(@Param("keyword") String keyword);

    @Query(value = BASE_QUERY + "WHERE d.MaHS = :maHS", nativeQuery = true)
    List<Map<String, Object>> findDiemByMaHSWithDetails(@Param("maHS") String maHS);

    @Query(value = "SELECT DISTINCT HocKy FROM Diem ORDER BY HocKy", nativeQuery = true)
    List<Integer> findDistinctHocKy();
}
