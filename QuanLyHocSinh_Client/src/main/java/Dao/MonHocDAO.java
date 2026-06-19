/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Connection.ConnectDB;
import Model.MonHoc;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class MonHocDAO {
    public List<MonHoc> getAll() {
        List<MonHoc> list = new ArrayList<>();
        String sql = "SELECT * FROM MonHoc";

        try (Connection c = ConnectDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new MonHoc(
                    rs.getString("MaMH"),
                    rs.getString("TenMH")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 🔍 TÌM KIẾM (CHỖ QUAN TRỌNG NHẤT)
    public List<MonHoc> search(String key) {
        List<MonHoc> list = new ArrayList<>();
        String sql = "SELECT * FROM MonHoc WHERE MaMH LIKE ? OR TenMH LIKE ?";

        try (Connection c = ConnectDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, "%" + key + "%");
            ps.setString(2, "%" + key + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new MonHoc(
                    rs.getString("MaMH"),
                    rs.getString("TenMH")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Kiểm tra tồn tại
    public boolean exists(String maMH) {
        String sql = "SELECT COUNT(*) FROM MonHoc WHERE MaMH = ?";
        try (Connection c = ConnectDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, maMH);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Thêm
    public void insert(MonHoc m) {
        String sql = "INSERT INTO MonHoc VALUES (?, ?)";
        try (Connection c = ConnectDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, m.getMaMH());
            ps.setString(2, m.getTenMH());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Sửa
    public void update(MonHoc m) {
        String sql = "UPDATE MonHoc SET TenMH=? WHERE MaMH=?";
        try (Connection c = ConnectDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, m.getTenMH());
            ps.setString(2, m.getMaMH());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Xóa
    public void delete(String maMH) {
        String sql = "DELETE FROM MonHoc WHERE MaMH=?";
        try (Connection c = ConnectDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, maMH);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
