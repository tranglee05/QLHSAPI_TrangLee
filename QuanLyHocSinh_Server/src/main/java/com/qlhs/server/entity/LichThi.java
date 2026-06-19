package com.qlhs.server.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "LichThi")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LichThi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Khóa chính tự tăng
    @Column(name = "MaLT")
    private int maLT;

    @Column(name = "TenKyThi", columnDefinition = "NVARCHAR(255)")
    private String tenKyThi;

    @Column(name = "MaMH", length = 50)
    private String maMH;

    @Column(name = "NgayThi")
    private String ngayThi;

    @Column(name = "GioBatDau")
    private String gioBatDau;

    @Column(name = "GioKetThuc")
    private String gioKetThuc;

    @Column(name = "MaPhong", length = 50)
    private String maPhong;
}
