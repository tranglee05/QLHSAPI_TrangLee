/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;
import Model.Phuckhao;
import Connection.ConnectDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Connection.ConnectDB;
/**
 *
 * @author ADMIN
 */
public class PhuckhaoDAO {
    public List<Phuckhao> getAll() {
        List<Phuckhao> list = new ArrayList<>();
 
        String sql = "SELECT * FROM PhucKhao ORDER BY NgayGui DESC";

        try (Connection c = ConnectDB.getConnection(); 
             PreparedStatement p = c.prepareStatement(sql)) {

            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                Phuckhao pk = new Phuckhao();
                pk.setMaPK(rs.getInt("MaPK"));
                pk.setMaHS(rs.getString("MaHS"));
                pk.setMaMH(rs.getString("MaMH"));
                pk.setLyDo(rs.getNString("LyDo"));
                pk.setNgayGui(rs.getTimestamp("NgayGui"));
                pk.setTrangThai(rs.getNString("TrangThai"));
                list.add(pk);
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return list;
    }

    public boolean insert(Phuckhao pk) {
        String sql = "INSERT INTO PhucKhao(MaHS, MaMH, LyDo, TrangThai) VALUES(?,?,?,?)";
        try (Connection c = ConnectDB.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, pk.getMaHS());
            p.setString(2, pk.getMaMH());
            p.setNString(3, pk.getLyDo());
            p.setNString(4, pk.getTrangThai());
            return p.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean update(Phuckhao pk) {
        String sql = "UPDATE PhucKhao SET MaHS=?, MaMH=?, LyDo=?, TrangThai=? WHERE MaPK=?";
        try (Connection c = ConnectDB.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, pk.getMaHS());
            p.setString(2, pk.getMaMH());
            p.setNString(3, pk.getLyDo());
            p.setNString(4, pk.getTrangThai());
            p.setInt(5, pk.getMaPK());
            return p.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM PhucKhao WHERE MaPK=?";
        try (Connection c = ConnectDB.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, id);
            return p.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public List<Phuckhao> search(String keyword) {
        List<Phuckhao> list = new ArrayList<>();
        String sql = "SELECT * FROM PhucKhao WHERE MaHS LIKE ? OR MaMH LIKE ?";
        try (Connection c = ConnectDB.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, "%" + keyword + "%");
            p.setString(2, "%" + keyword + "%");
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                list.add(new Phuckhao(rs.getInt("MaPK"), rs.getString("MaHS"), rs.getString("MaMH"),
                        rs.getString("LyDo"), rs.getTimestamp("NgayGui"), rs.getString("TrangThai")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    //thêm ngày 13/04/2026
    // Thêm hàm này vào cuối file PhuckhaoDAO.java
    public List<Phuckhao> getByMaHS(String maHS) {
        List<Phuckhao> list = new ArrayList<>();
        String sql = "SELECT * FROM PhucKhao WHERE MaHS = ? ORDER BY NgayGui DESC";

        try (Connection c = ConnectDB.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setString(1, maHS);
            ResultSet rs = p.executeQuery();

            while (rs.next()) {
                Phuckhao pk = new Phuckhao();
                pk.setMaPK(rs.getInt("MaPK"));
                pk.setMaHS(rs.getString("MaHS"));
                pk.setMaMH(rs.getString("MaMH"));
                pk.setLyDo(rs.getNString("LyDo"));
                pk.setNgayGui(rs.getTimestamp("NgayGui"));
                pk.setTrangThai(rs.getNString("TrangThai"));
                list.add(pk);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
