package com.qlhs.server.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Entity
@Table(name = "Diem")
@IdClass(Diem.DiemId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Diem {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiemId implements Serializable {
        private String maHS;
        private String maMH;
        private int hocKy;
    }

    @Id
    @Column(name = "MaHS", length = 50)
    private String maHS;

    @Id
    @Column(name = "MaMH", length = 50)
    private String maMH;

    @Id
    @Column(name = "HocKy")
    private int hocKy;

    @Column(name = "Diem15p")
    private double diem15p;

    @Column(name = "Diem1Tiet")
    private double diem1Tiet;

    @Column(name = "DiemGiuaKy")
    private double diemGiuaKy;

    @Column(name = "DiemCuoiKy")
    private double diemCuoiKy;

    @Column(name = "DiemTongKet")
    private double diemTongKet;
}
