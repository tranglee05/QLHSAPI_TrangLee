package com.qlhs.server.repository;

import com.qlhs.server.entity.TKB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TKBRepository extends JpaRepository<TKB,Integer> {
    List<TKB> findByMaLop(String maLop);
    List<TKB> findByMaMH(String maMH);
    List<TKB> findByMaPhong(String maPhong);
    List<TKB> findByMaGV(String maGV);
    List<TKB> findByThu(int thu);
}
