package com.qlhs.server.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Lop")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lop {
    @Id
    @Column(name = "MaLop")
    private String maLop;

    @Column(name = "TenLop")
    private String tenLop;
}