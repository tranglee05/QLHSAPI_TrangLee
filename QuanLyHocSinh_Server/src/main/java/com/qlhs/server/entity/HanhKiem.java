package com.qlhs.server.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "HanhKiem")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(HanhKiem.HanhKiemId.class)
public class HanhKiem {

    @Id
    @Column(name = "MaHS", length = 50)
    private String maHS;

    @Transient
    private String maLop;

    @Id
    @Column(name = "HocKy")
    private int hocKy;

    @Id
    @Column(name = "NamHoc", length = 50)
    private String namHoc;

    @Column(name = "XepLoai", columnDefinition = "NVARCHAR(50)")
    private String xepLoai;

    @Column(name = "NhanXet", columnDefinition = "NVARCHAR(255)")
    private String nhanXet;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HanhKiemId implements java.io.Serializable {
        private String maHS;
        private String namHoc;
        private int hocKy;
    }
}
