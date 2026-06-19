/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;
import Connection.ConnectDB;
import Model.Thongbao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author ADMIN
 */
public class ThongbaoDAO {
   
    public List<Thongbao> getAll() {
        List<Thongbao> list = new ArrayList<>();
        String sql = "SELECT * FROM ThongBao ORDER BY NgayTao DESC";
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Thongbao tb = new Thongbao();
                tb.setMaTB(rs.getInt("MaTB"));
                tb.setTieuDe(rs.getString("TieuDe"));
                tb.setNoiDung(rs.getString("NoiDung"));
                tb.setNgayTao(new java.util.Date(rs.getTimestamp("NgayTao").getTime()));
                tb.setNguoiGui(rs.getString("NguoiGui"));
                list.add(tb);
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return list;
    }

    
    public List<Thongbao> search(String keyword) {
        List<Thongbao> list = new ArrayList<>();
        String sql = "SELECT * FROM ThongBao WHERE TieuDe LIKE ? OR NguoiGui LIKE ? ORDER BY NgayTao DESC";
        try (Connection c = ConnectDB.getConnection(); 
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
              
                list.add(new Thongbao(
                    rs.getInt("MaTB"), 
                    rs.getString("TieuDe"), 
                    rs.getString("NoiDung"), 
                    new java.util.Date(rs.getTimestamp("NgayTao").getTime()), 
                    rs.getString("NguoiGui")
                ));
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return list;
    }

    public boolean insert(Thongbao tb) {
        String sql = "INSERT INTO ThongBao(TieuDe, NoiDung, NguoiGui) VALUES(?,?,?)";
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
            ps.setNString(1, tb.getTieuDe());
            ps.setNString(2, tb.getNoiDung());
            ps.setNString(3, tb.getNguoiGui());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { 
            e.printStackTrace(); 
            return false; 
        }
    }

    public boolean delete(int maTB) {
        String sql = "DELETE FROM ThongBao WHERE MaTB=?";
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
            ps.setInt(1, maTB);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { 
            e.printStackTrace(); 
            return false; 
        }
    }

    public boolean update(Thongbao tb) {
        String sql = "UPDATE ThongBao SET TieuDe=?, NoiDung=?, NguoiGui=? WHERE MaTB=?";
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
            ps.setNString(1, tb.getTieuDe());
            ps.setNString(2, tb.getNoiDung());
            ps.setNString(3, tb.getNguoiGui());
            ps.setInt(4, tb.getMaTB());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { 
            e.printStackTrace(); 
            return false; 
        }
    }
}
