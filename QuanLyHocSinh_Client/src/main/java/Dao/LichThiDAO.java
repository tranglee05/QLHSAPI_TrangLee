package Dao;

import Connection.ConnectDB;
import Model.LichThi;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LichThiDAO {


    public List<LichThi> getAllLichThi() {
        List<LichThi> list = new ArrayList<>();
        String sql = "SELECT * FROM LichThi"; 
        
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                LichThi lt = new LichThi();
                lt.setMaLT(rs.getInt("MaLT"));
                lt.setTenKyThi(rs.getString("TenKyThi"));
                lt.setMaMH(rs.getString("MaMH"));
                lt.setNgayThi(rs.getString("NgayThi"));
           
                lt.setGioBatDau(formatTime(rs.getString("GioBatDau")));
                lt.setGioKetThuc(formatTime(rs.getString("GioKetThuc")));
                
                lt.setMaPhong(rs.getString("MaPhong"));
                list.add(lt);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<LichThi> searchLichThi(String keyword) {
        List<LichThi> list = new ArrayList<>();
        String sql = "SELECT * FROM LichThi WHERE TenKyThi LIKE ? OR MaMH LIKE ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String key = "%" + keyword + "%";
            ps.setString(1, key);
            ps.setString(2, key);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LichThi lt = new LichThi();
                lt.setMaLT(rs.getInt("MaLT"));
                lt.setTenKyThi(rs.getString("TenKyThi"));
                lt.setMaMH(rs.getString("MaMH"));
                lt.setNgayThi(rs.getString("NgayThi"));
                

                lt.setGioBatDau(formatTime(rs.getString("GioBatDau")));
                lt.setGioKetThuc(formatTime(rs.getString("GioKetThuc")));
                
                lt.setMaPhong(rs.getString("MaPhong"));
                list.add(lt);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }


    private String formatTime(String rawTime) {
        if (rawTime == null) return "";
      
        if (rawTime.length() >= 5) {
            return rawTime.substring(0, 5); 
        }
        return rawTime;
    }


    
    public boolean addLichThi(LichThi lt) {
        String sql = "INSERT INTO LichThi(TenKyThi, MaMH, NgayThi, GioBatDau, GioKetThuc, MaPhong) VALUES (?,?,?,?,?,?)";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lt.getTenKyThi());
            ps.setString(2, lt.getMaMH());
            ps.setString(3, lt.getNgayThi());
            ps.setString(4, lt.getGioBatDau());
            ps.setString(5, lt.getGioKetThuc());
            ps.setString(6, lt.getMaPhong());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean updateLichThi(LichThi lt) {
        String sql = "UPDATE LichThi SET TenKyThi=?, MaMH=?, NgayThi=?, GioBatDau=?, GioKetThuc=?, MaPhong=? WHERE MaLT=?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lt.getTenKyThi());
            ps.setString(2, lt.getMaMH());
            ps.setString(3, lt.getNgayThi());
            ps.setString(4, lt.getGioBatDau());
            ps.setString(5, lt.getGioKetThuc());
            ps.setString(6, lt.getMaPhong());
            ps.setInt(7, lt.getMaLT());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean deleteLichThi(int maLT) {
        String sql = "DELETE FROM LichThi WHERE MaLT=?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maLT);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }
}