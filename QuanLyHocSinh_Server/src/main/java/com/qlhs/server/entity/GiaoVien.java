package com.qlhs.server.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "GiaoVien")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiaoVien {
    @Id
    @Column(name = "MaGV")
    private String maGV;

    @Column(name = "HoTen")
    private String hoTen;
}