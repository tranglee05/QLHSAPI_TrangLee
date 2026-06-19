package Dao;

import Model.HocSinh;
import Connection.ConnectDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HocSinhDAO {

    public List<HocSinh> getAll() {
        List<HocSinh> list = new ArrayList<>();
        String sql = "SELECT * FROM HocSinh";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new HocSinh(
                        rs.getString("MaHS"),
                        rs.getString("HoTen"),
                        rs.getString("NgaySinh"),
                        rs.getString("GioiTinh"),
                        rs.getString("DiaChi"),
                        rs.getString("MaLop"),
                        rs.getString("MaDT")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

  
    public boolean insert(HocSinh hs) {
        String sql = """
                INSERT INTO HocSinh
                (MaHS, HoTen, NgaySinh, GioiTinh, DiaChi, MaLop, MaDT)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, hs.getMaHS());
            ps.setString(2, hs.getHoTen());
            ps.setString(3, hs.getNgaySinh());
            ps.setString(4, hs.getGioiTinh());
            ps.setString(5, hs.getDiaChi());
            ps.setString(6, hs.getMaLop());
            ps.setString(7, hs.getMaDT());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(HocSinh hs) {
        String sql = """
                UPDATE HocSinh
                SET HoTen = ?, NgaySinh = ?, GioiTinh = ?, DiaChi = ?,
                    MaLop = ?, MaDT = ?
                WHERE MaHS = ?
                """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, hs.getHoTen());
            ps.setString(2, hs.getNgaySinh());
            ps.setString(3, hs.getGioiTinh());
            ps.setString(4, hs.getDiaChi());
            ps.setString(5, hs.getMaLop());
            ps.setString(6, hs.getMaDT());
            ps.setString(7, hs.getMaHS());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean delete(String maHS) {
        String sql = "DELETE FROM HocSinh WHERE MaHS = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maHS);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<HocSinh> search(String keyword) {
        List<HocSinh> list = new ArrayList<>();
        String sql = """
                SELECT * FROM HocSinh
                WHERE MaHS LIKE ?
                   OR HoTen LIKE ?
                """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String key = "%" + keyword + "%";
            ps.setString(1, key);
            ps.setString(2, key);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new HocSinh(
                        rs.getString("MaHS"),
                        rs.getString("HoTen"),
                        rs.getString("NgaySinh"),
                        rs.getString("GioiTinh"),
                        rs.getString("DiaChi"),
                        rs.getString("MaLop"),
                        rs.getString("MaDT")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public List<String> getAllMaLop() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT MaLop FROM Lop";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(rs.getString("MaLop"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getAllMaDT() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT MaDT FROM DoiTuongUuTien";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(rs.getString("MaDT"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public HocSinh getByMaHS(String maHS) {
        String sql = "SELECT * FROM HocSinh WHERE MaHS = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maHS);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new HocSinh(
                        rs.getString("MaHS"),
                        rs.getString("HoTen"),
                        rs.getString("NgaySinh"),
                        rs.getString("GioiTinh"),
                        rs.getString("DiaChi"),
                        rs.getString("MaLop"),
                        rs.getString("MaDT")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}