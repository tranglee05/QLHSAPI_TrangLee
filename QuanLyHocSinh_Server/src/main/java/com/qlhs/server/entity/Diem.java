package com.qlhs.server.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Diem")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Diem {

    @Id // Tạm thời dùng maHS làm ID cho dễ học. Thực tế nên là khóa phức hợp.
    @Column(name = "MaHS", length = 50)
    private String maHS;

    @Column(name = "MaMH", length = 50)
    private String maMH;

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
}
