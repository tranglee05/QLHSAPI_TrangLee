package Dao;

import Model.DoiTuongUuTien;
import Connection.ConnectDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoiTuongUuTienDAO {

    public List<DoiTuongUuTien> getAll() {
        List<DoiTuongUuTien> list = new ArrayList<>();
        String sql = "SELECT * FROM DoiTuongUuTien";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new DoiTuongUuTien(
                        rs.getString("MaDT"),
                        rs.getString("TenDT"),
                        rs.getDouble("TiLeGiamHocPhi")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(DoiTuongUuTien dt) {
        String sql = """
    INSERT INTO DoiTuongUuTien (MaDT, TenDT, TiLeGiamHocPhi)
    VALUES (?, ?, ?)
    """;


        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dt.getMaDT());
            ps.setString(2, dt.getTenDT());
            ps.setDouble(3, dt.getTiLeGiam());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(DoiTuongUuTien dt) {
        String sql = "UPDATE DoiTuongUuTien SET TenDT=?, TiLeGiamHocPhi=? WHERE MaDT=?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dt.getTenDT());
            ps.setDouble(2, dt.getTiLeGiam());
            ps.setString(3, dt.getMaDT());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(String maDT) {
        String sql = "DELETE FROM DoiTuongUuTien WHERE MaDT=?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maDT);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    
    public List<DoiTuongUuTien> search(String keyword) {
        List<DoiTuongUuTien> list = new ArrayList<>();
        String sql = """
                SELECT * FROM DoiTuongUuTien
                WHERE MaDT LIKE ?
                   OR TenDT LIKE ?
                """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String key = "%" + keyword + "%";
            ps.setString(1, key);
            ps.setString(2, key);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new DoiTuongUuTien(
                        rs.getString("MaDT"),
                        rs.getString("TenDT"),
                        rs.getDouble("TiLeGiamHocPhi")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
