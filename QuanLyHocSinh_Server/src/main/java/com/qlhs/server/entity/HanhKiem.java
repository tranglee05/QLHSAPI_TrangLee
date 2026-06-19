package com.qlhs.server.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "HanhKiem")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HanhKiem {

    @Id // Tạm thời dùng maHS làm Id.
    @Column(name = "MaHS", length = 50)
    private String maHS;

    @Column(name = "MaLop", length = 50)
    private String maLop;

    @Column(name = "HocKy")
    private int hocKy;

    @Column(name = "NamHoc", length = 50)
    private String namHoc;

    @Column(name = "XepLoai", columnDefinition = "NVARCHAR(50)")
    private String xepLoai;

    @Column(name = "NhanXet", columnDefinition = "NVARCHAR(255)")
    private String nhanXet;
}
