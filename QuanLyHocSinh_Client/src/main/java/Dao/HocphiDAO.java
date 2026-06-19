/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;
import Connection.ConnectDB;
import Model.Hocphi;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author ADMIN
 */
public class HocphiDAO {
    public List<Hocphi> getHocPhiByLop(String maLop, int hocKy, String namHoc) {
        List<Hocphi> list = new ArrayList<>();

        System.out.println("\n=== HocphiDAO.getHocPhiByLop ===");
        System.out.println("Tham số: maLop=" + (maLop == null || maLop.isEmpty() ? "ALL" : maLop) + 
                          ", hocKy=" + (hocKy == 0 ? "ALL" : hocKy) + 
                          ", namHoc=" + (namHoc == null || namHoc.isEmpty() ? "ALL" : namHoc));

        // Build dynamic WHERE clause based on parameters
        StringBuilder whereClause = new StringBuilder();
        ArrayList<Object> params = new ArrayList<>();

        if (maLop != null && !maLop.isEmpty()) {
            if (whereClause.length() > 0) whereClause.append(" AND ");
            whereClause.append("hs.MaLop = ?");
            params.add(maLop);
        }

        if (hocKy != 0) {
            if (whereClause.length() > 0) whereClause.append(" AND ");
            whereClause.append("hp.HocKy = ?");
            params.add(hocKy);
        }

        if (namHoc != null && !namHoc.isEmpty()) {
            if (whereClause.length() > 0) whereClause.append(" AND ");
            whereClause.append("hp.NamHoc = ?");
            params.add(namHoc);
        }

        String sql = "SELECT hp.*, hs.MaLop FROM HocPhi hp " +
                     "JOIN HocSinh hs ON hp.MaHS = hs.MaHS";
        
        if (whereClause.length() > 0) {
            sql += " WHERE " + whereClause.toString();
        }
        
        sql += " ORDER BY hs.MaLop, hp.HocKy, hp.NamHoc";

        try {
            Connection cons = ConnectDB.getConnection();
            if (cons == null) {
                System.out.println("❌ HocphiDAO: Kết nối database là null!");
                return list;
            }
            
            System.out.println("✓ Kết nối thành công");
            System.out.println("✓ SQL: " + sql);
            
            PreparedStatement ps = cons.prepareStatement(sql);
            
            // Set parameters
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            System.out.println("✓ PreparedStatement tạo thành công");

            ResultSet rs = ps.executeQuery();
            System.out.println("✓ executeQuery() thành công");

            while (rs.next()) {
                Hocphi hp = new Hocphi();
                hp.setMaHP(rs.getInt("MaHP"));
                hp.setMaHS(rs.getString("MaHS"));
                hp.setMaLop(rs.getString("MaLop"));
                hp.setHocKy(rs.getInt("HocKy"));
                hp.setNamHoc(rs.getString("NamHoc"));
                hp.setTongTien(rs.getLong("TongTien"));
                hp.setMienGiam(rs.getLong("MienGiam"));
                hp.setPhaiDong(rs.getLong("PhaiDong"));
                hp.setTrangThai(rs.getString("TrangThai"));
                list.add(hp);
            }
            
            System.out.println("✓ Tìm được " + list.size() + " dòng");
            
            rs.close();
            ps.close();
            cons.close();
            
        } catch (Exception e) {
            System.out.println("❌ Exception trong getHocPhiByLop: " + e.getClass().getName());
            System.out.println("❌ Message: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("=== Kết thúc getHocPhiByLop ===\n");
        return list;
    }

    public List<Hocphi> getAllHocPhi() {
        List<Hocphi> list = new ArrayList<>();
        String sql = "SELECT hp.*, hs.MaLop FROM HocPhi hp " +
                     "JOIN HocSinh hs ON hp.MaHS = hs.MaHS " +
                     "ORDER BY hs.MaLop, hp.HocKy, hp.NamHoc";

        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Hocphi hp = new Hocphi();
                hp.setMaHP(rs.getInt("MaHP"));
                hp.setMaHS(rs.getString("MaHS"));
                hp.setMaLop(rs.getString("MaLop"));
                hp.setHocKy(rs.getInt("HocKy"));
                hp.setNamHoc(rs.getString("NamHoc"));
                hp.setTongTien(rs.getLong("TongTien"));
                hp.setMienGiam(rs.getLong("MienGiam"));
                hp.setPhaiDong(rs.getLong("PhaiDong"));
                hp.setTrangThai(rs.getString("TrangThai"));
                list.add(hp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getNamHocByMaLop(String maLop) {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT hp.NamHoc FROM HocPhi hp " +
                     "JOIN HocSinh hs ON hp.MaHS = hs.MaHS " +
                     "WHERE hs.MaLop = ? " +
                     "ORDER BY hp.NamHoc DESC";

        try {
            Connection cons = ConnectDB.getConnection();
            if (cons == null) {
                System.out.println("❌ HocphiDAO.getNamHocByMaLop: Kết nối database là null!");
                return list;
            }

            PreparedStatement ps = cons.prepareStatement(sql);
            ps.setString(1, maLop);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String namHoc = rs.getString("NamHoc");
                if (namHoc != null && !namHoc.isEmpty()) {
                    list.add(namHoc);
                }
            }

            System.out.println("✓ NamHoc cho " + maLop + ": " + list.size() + " năm");

            rs.close();
            ps.close();
            cons.close();

        } catch (Exception e) {
            System.out.println("❌ Exception trong getNamHocByMaLop: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    
    public boolean saveHocPhi(Hocphi hp) {
        
        String sqlCheckHS = "SELECT COUNT(*) FROM HocSinh WHERE MaHS = ?";

    
        String sqlCheckExist = "SELECT COUNT(*) FROM HocPhi WHERE MaHS=? AND HocKy=? AND NamHoc=?";

        String sqlInsert = "INSERT INTO HocPhi(MaHS, HocKy, NamHoc, TongTien, MienGiam, PhaiDong, TrangThai) VALUES(?,?,?,?,?,?,?)";
        String sqlUpdate = "UPDATE HocPhi SET TongTien=?, MienGiam=?, PhaiDong=?, TrangThai=? WHERE MaHS=? AND HocKy=? AND NamHoc=?";

        try (Connection cons = ConnectDB.getConnection()) {
 
            PreparedStatement psCheckHS = cons.prepareStatement(sqlCheckHS);
            psCheckHS.setString(1, hp.getMaHS());
            ResultSet rsHS = psCheckHS.executeQuery();
            if (rsHS.next() && rsHS.getInt(1) == 0) {
                System.out.println("Lỗi: Mã học sinh không tồn tại trong hệ thống!");
                return false; 
            }

            PreparedStatement psCheckEx = cons.prepareStatement(sqlCheckExist);
            psCheckEx.setString(1, hp.getMaHS());
            psCheckEx.setInt(2, hp.getHocKy());
            psCheckEx.setString(3, hp.getNamHoc());
            ResultSet rsEx = psCheckEx.executeQuery();
            rsEx.next();
            boolean isExist = rsEx.getInt(1) > 0;

            if (!isExist) {
                
                PreparedStatement ps = cons.prepareStatement(sqlInsert);
                ps.setString(1, hp.getMaHS());
                ps.setInt(2, hp.getHocKy());
                ps.setString(3, hp.getNamHoc());
                ps.setLong(4, hp.getTongTien());
                ps.setLong(5, hp.getMienGiam());
                ps.setLong(6, hp.getPhaiDong());
                ps.setString(7, hp.getTrangThai());
                return ps.executeUpdate() > 0;
            } else {
                
                PreparedStatement ps = cons.prepareStatement(sqlUpdate);
                ps.setLong(1, hp.getTongTien());
                ps.setLong(2, hp.getMienGiam());
                ps.setLong(3, hp.getPhaiDong());
                ps.setString(4, hp.getTrangThai());
                ps.setString(5, hp.getMaHS());
                ps.setInt(6, hp.getHocKy());
                ps.setString(7, hp.getNamHoc());
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace(); 
            return false;
        }
    }


    public boolean deleteHocPhi(int maHP) {
        String sql = "DELETE FROM HocPhi WHERE MaHP=?";
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {

            ps.setInt(1, maHP);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    // Sửa ngày 09/04/2026
    public List<Hocphi> getByMaHS(String maHS) {
        List<Hocphi> list = new ArrayList<>();
        String sql = "SELECT hp.*, hs.MaLop FROM HocPhi hp " +
                "JOIN HocSinh hs ON hp.MaHS = hs.MaHS " +
                "WHERE hp.MaHS = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maHS);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Hocphi hp = new Hocphi();

                hp.setMaHP(rs.getInt("MaHP"));
                hp.setMaHS(rs.getString("MaHS"));
                hp.setMaLop(rs.getString("MaLop"));
                hp.setHocKy(rs.getInt("HocKy"));
                hp.setNamHoc(rs.getString("NamHoc"));
                hp.setTongTien(rs.getLong("TongTien"));
                hp.setMienGiam(rs.getLong("MienGiam"));
                hp.setPhaiDong(rs.getLong("PhaiDong"));
                hp.setTrangThai(rs.getString("TrangThai"));

                list.add(hp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
