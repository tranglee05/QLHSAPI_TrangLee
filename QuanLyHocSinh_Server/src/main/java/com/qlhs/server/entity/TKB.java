package com.qlhs.server.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ThoiKhoaBieu")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TKB {
    @Id
    @Column(name = "MaTKB")
    private String maTKB;

    @Column(name = "MaLop")
    private String maLop;

    @Column(name = "MaMH")
    private String maMH;

    @Column(name = "MaGV")
    private String maGV;

    @Column(name = "MaPhong")
    private String maPhong;

    @Column(name = "Thu")
    private Integer thu;

    @Column(name = "TietBatDau")
    private Integer tietBatDau;

    @Column(name = "TietKetThuc")
    private Integer tietKetThuc;

    @Transient
    private String tenMH;
}