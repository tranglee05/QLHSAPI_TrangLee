/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Connection.ConnectDB;
import Model.PhongHoc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Admin
 */
public class PhongHocDAO {
  

    public List<PhongHoc> getAll() {
        List<PhongHoc> list = new ArrayList<>();
        String sql = """
            SELECT MaPhong, TenPhong, SucChua, LoaiPhong, TinhTrang
            FROM PhongHoc
        """;

        try (Connection c = ConnectDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new PhongHoc(
                        rs.getString("MaPhong"),
                        rs.getString("TenPhong"),
                        rs.getInt("SucChua"),
                        rs.getString("LoaiPhong"),
                        rs.getString("TinhTrang")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

   
    public List<PhongHoc> search(String maPhong, String loai, String tinhTrang) {
        List<PhongHoc> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT MaPhong, TenPhong, SucChua, LoaiPhong, TinhTrang FROM PhongHoc WHERE 1=1 ");

        if (!maPhong.isEmpty()) sql.append(" AND MaPhong LIKE ?");
        if (!loai.equals("Tất cả")) sql.append(" AND LoaiPhong = ?");
        if (!tinhTrang.equals("Tất cả")) sql.append(" AND TinhTrang = ?");

        try (Connection c = ConnectDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {

            int idx = 1;
            if (!maPhong.isEmpty()) ps.setString(idx++, "%" + maPhong + "%");
            if (!loai.equals("Tất cả")) ps.setString(idx++, loai);
            if (!tinhTrang.equals("Tất cả")) ps.setString(idx++, tinhTrang);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new PhongHoc(
                        rs.getString("MaPhong"),
                        rs.getString("TenPhong"),
                        rs.getInt("SucChua"),
                        rs.getString("LoaiPhong"),
                        rs.getString("TinhTrang")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    
    public boolean exists(String maPhong) {
        String sql = "SELECT 1 FROM PhongHoc WHERE MaPhong=?";
        try (Connection c = ConnectDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, maPhong);
            return ps.executeQuery().next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public void insert(PhongHoc p) {
        String sql = "INSERT INTO PhongHoc VALUES (?,?,?,?,?)";
        try (Connection c = ConnectDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getMaPhong());
            ps.setString(2, p.getTenPhong());
            ps.setInt(3, p.getSucChua());
            ps.setString(4, p.getLoaiPhong());
            ps.setString(5, p.getTinhTrang());

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(PhongHoc p) {
        String sql = """
            UPDATE PhongHoc
            SET TenPhong=?, SucChua=?, LoaiPhong=?, TinhTrang=?
            WHERE MaPhong=?
        """;

        try (Connection c = ConnectDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getTenPhong());
            ps.setInt(2, p.getSucChua());
            ps.setString(3, p.getLoaiPhong());
            ps.setString(4, p.getTinhTrang());
            ps.setString(5, p.getMaPhong());

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(String maPhong) {
        String sql = "DELETE FROM PhongHoc WHERE MaPhong=?";
        try (Connection c = ConnectDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, maPhong);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

public boolean phongDangHoc(String maPhong) {
    String sql = "SELECT 1 FROM ThoiKhoaBieu WHERE MaPhong=?";
    try (Connection c = ConnectDB.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setString(1, maPhong);
        return ps.executeQuery().next();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}

public void updateTinhTrang(String maPhong, String tinhTrang) {
    String sql = "UPDATE PhongHoc SET TinhTrang=? WHERE MaPhong=?";
    try (Connection c = ConnectDB.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setString(1, tinhTrang);
        ps.setString(2, maPhong);
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    public void phongDangHocBulkUpdate() {
        String sql = """
            UPDATE PhongHoc
            SET TinhTrang = CASE
                WHEN MaPhong IN (SELECT DISTINCT MaPhong FROM ThoiKhoaBieu)
                THEN 'Đang học'
                ELSE 'Trống'
            END
        """;
        
        try (Connection c = ConnectDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.executeUpdate();
            System.out.println("✓ Cập nhật trạng thái tất cả phòng trong 1 query");
        } catch (Exception e) {
            System.out.println("❌ Lỗi bulk update: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<PhongHoc> getAllWithTinhTrang() {
        List<PhongHoc> list = new ArrayList<>();
        String sql = """
            SELECT p.MaPhong, p.TenPhong, p.SucChua, p.LoaiPhong,
                   CASE
                       WHEN p.TinhTrang = 'Bảo trì' THEN 'Bảo trì'
                       WHEN p.MaPhong IN (SELECT DISTINCT MaPhong FROM ThoiKhoaBieu)
                       THEN 'Đang học'
                       ELSE 'Trống'
                   END AS TinhTrangThucTe
            FROM PhongHoc p
        """;

        try (Connection c = ConnectDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new PhongHoc(
                        rs.getString("MaPhong"),
                        rs.getString("TenPhong"),
                        rs.getInt("SucChua"),
                        rs.getString("LoaiPhong"),
                        rs.getString("TinhTrangThucTe")
                ));
            }
            System.out.println("✓ Tải " + list.size() + " phòng với trạng thái trong 1 query");
        } catch (Exception e) {
            System.out.println("❌ Lỗi getAllWithTinhTrang: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

}