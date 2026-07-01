package com.qlhs.server.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

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
    private LocalDate ngayThi;

    @Column(name = "GioBatDau")
    private LocalTime gioBatDau;

    @Column(name = "GioKetThuc")
    private LocalTime gioKetThuc;

    @Column(name = "MaPhong", length = 50)
    private String maPhong;
}
