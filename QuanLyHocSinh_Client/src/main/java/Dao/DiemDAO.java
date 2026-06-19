package Dao;

import Connection.ConnectDB;
import Model.Diem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiemDAO {

    public List<Diem> getDiemByFilter(String maLop, String maMH, int hocKy) {
        List<Diem> list = new ArrayList<>();
 
        String sql = "SELECT d.*, hs.HoTen, mh.TenMH FROM Diem d " +
                     "JOIN HocSinh hs ON d.MaHS = hs.MaHS " +
                     "JOIN MonHoc mh ON d.MaMH = mh.MaMH " +
                     "WHERE hs.MaLop = ? AND d.MaMH = ? AND d.HocKy = ?";
        
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setString(1, maLop);
            ps.setString(2, maMH);
            ps.setInt(3, hocKy);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Diem d = new Diem();
                d.setMaHS(rs.getString("MaHS"));
                d.setTenHS(rs.getString("HoTen"));
                d.setMaMH(rs.getString("MaMH"));
                d.setTenMH(rs.getString("TenMH"));
                d.setHocKy(rs.getInt("HocKy")); 
                
     
                d.setDiem15p(rs.getDouble("Diem15p"));
                d.setDiem1Tiet(rs.getDouble("Diem1Tiet"));
                d.setDiemGiuaKy(rs.getDouble("DiemGiuaKy"));
                d.setDiemCuoiKy(rs.getDouble("DiemCuoiKy")); 
                
                list.add(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateDiem(Diem d) {
     
        String sql = "UPDATE Diem SET Diem15p=?, Diem1Tiet=?, DiemGiuaKy=?, DiemCuoiKy=?, DiemTongKet=? " +
                     "WHERE MaHS=? AND MaMH=? AND HocKy=?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setDouble(1, d.getDiem15p());
            ps.setDouble(2, d.getDiem1Tiet());
            ps.setDouble(3, d.getDiemGiuaKy());
            ps.setDouble(4, d.getDiemCuoiKy());
            ps.setDouble(5, d.getDiemTongKet()); 
            
            ps.setString(6, d.getMaHS());
            ps.setString(7, d.getMaMH());
            ps.setInt(8, d.getHocKy());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Diem> searchDiem(String keyword) {
        List<Diem> list = new ArrayList<>();
        String sql = "SELECT d.*, hs.HoTen, mh.TenMH FROM Diem d " +
                     "JOIN HocSinh hs ON d.MaHS = hs.MaHS " +
                     "JOIN MonHoc mh ON d.MaMH = mh.MaMH " +
                     "WHERE d.MaHS LIKE ? OR hs.HoTen LIKE ?";
        
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            String key = "%" + keyword + "%"; 
            ps.setString(1, key);
            ps.setString(2, key);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Diem d = new Diem();
                d.setMaHS(rs.getString("MaHS"));
                d.setTenHS(rs.getString("HoTen"));
                d.setMaMH(rs.getString("MaMH"));
                d.setTenMH(rs.getString("TenMH"));
                d.setHocKy(rs.getInt("HocKy"));
                
                d.setDiem15p(rs.getDouble("Diem15p"));
                d.setDiem1Tiet(rs.getDouble("Diem1Tiet"));
                d.setDiemGiuaKy(rs.getDouble("DiemGiuaKy"));
                d.setDiemCuoiKy(rs.getDouble("DiemCuoiKy"));
                
                list.add(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Integer> getDistinctHocKy() {
        List<Integer> list = new ArrayList<>();
        String sql = "SELECT DISTINCT HocKy FROM Diem ORDER BY HocKy";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getInt("HocKy"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<Diem> getDiemByMaHS(String maHS) {
        List<Diem> list = new ArrayList<>();

        // ĐÃ SỬA: Thêm JOIN để lấy được cột HoTen từ bảng HocSinh
        String sql = "SELECT d.*, hs.HoTen, mh.TenMH FROM Diem d " +
                "JOIN HocSinh hs ON d.MaHS = hs.MaHS " +
                "JOIN MonHoc mh ON d.MaMH = mh.MaMH " +
                "WHERE d.MaHS = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maHS);

            // DEBUG
            System.out.println("SQL MAHS = " + maHS);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Diem d = new Diem();
                // ĐÃ SỬA: Viết hoa đúng chuẩn tên cột
                d.setMaHS(rs.getString("MaHS"));
                d.setTenHS(rs.getString("HoTen")); // Giờ thì lấy được HoTen bình thường nhé
                d.setMaMH(rs.getString("MaMH"));
                d.setTenMH(rs.getString("TenMH"));
                d.setHocKy(rs.getInt("HocKy"));
                d.setDiem15p(rs.getDouble("Diem15p"));
                d.setDiem1Tiet(rs.getDouble("Diem1Tiet"));
                d.setDiemGiuaKy(rs.getDouble("DiemGiuaKy"));
                d.setDiemCuoiKy(rs.getDouble("DiemCuoiKy"));
                list.add(d);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    // 1. Lấy toàn bộ điểm (Sửa lỗi Cannot resolve method 'getAll')
    public List<Diem> getAll() {
        List<Diem> list = new ArrayList<>();
        // Lưu ý: Tên bảng "Diem" có thể khác trong SQL của bạn, hãy sửa cho khớp
        String sql = "SELECT d.*, mh.TenMH FROM Diem d " +
                     "JOIN MonHoc mh ON d.MaMH = mh.MaMH";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Diem d = new Diem();
                d.setMaHS(rs.getString("MaHS"));
                d.setMaMH(rs.getString("MaMH"));
                d.setTenMH(rs.getString("TenMH"));
                d.setHocKy(rs.getInt("HocKy"));
                d.setDiem15p(rs.getDouble("Diem15p"));
                d.setDiem1Tiet(rs.getDouble("Diem1Tiet"));
                d.setDiemGiuaKy(rs.getDouble("DiemGiuaKy"));
                d.setDiemCuoiKy(rs.getDouble("DiemCuoiKy"));
                list.add(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}