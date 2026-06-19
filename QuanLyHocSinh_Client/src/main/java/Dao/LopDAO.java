package Dao;

import Connection.ConnectDB;
import Model.Lop;
import Model.LopGVCN;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LopDAO {


    public List<LopGVCN> getAllLop() {
        List<LopGVCN> list = new ArrayList<>();
       
        String sql = "SELECT l.MaLop, l.TenLop, l.NienKhoa, l.MaGVCN, gv.HoTen " +
                     "FROM Lop l LEFT JOIN GiaoVien gv ON l.MaGVCN = gv.MaGV"; 
        try {
            Connection conn = ConnectDB.getConnection();
            if (conn == null) {
                System.out.println("❌ LopDAO: Kết nối database là null!");
                return list;
            }
            
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(mapToLopGVCN(rs));
            }
            
            rs.close();
            ps.close();
            conn.close();
            
            System.out.println("✓ LopDAO: Tải được " + list.size() + " lớp");
        } catch (Exception e) { 
            System.out.println("❌ LopDAO Exception: " + e.getMessage());
            e.printStackTrace(); 
        }
        return list;
    }

  
    public boolean insert(Lop lop) {
        String sql = "INSERT INTO Lop(MaLop, TenLop, NienKhoa, MaGVCN) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lop.getMaLop());
            ps.setString(2, lop.getTenLop());
            ps.setString(3, lop.getNienKhoa());
            ps.setString(4, lop.getMaGVCN());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return false; 
        }
    }


    public boolean update(Lop lop) {
        String sql = "UPDATE Lop SET TenLop = ?, NienKhoa = ?, MaGVCN = ? WHERE MaLop = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lop.getTenLop());
            ps.setString(2, lop.getNienKhoa());
            ps.setString(3, lop.getMaGVCN());
            ps.setString(4, lop.getMaLop());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return false; 
        }
    }


    public boolean delete(String maLop) {
        String sql = "DELETE FROM Lop WHERE MaLop = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maLop);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return false; 
        }
    }


    private LopGVCN mapToLopGVCN(ResultSet rs) throws SQLException {
        LopGVCN l = new LopGVCN();
        l.setMaLop(rs.getString("MaLop"));
        l.setTenLop(rs.getString("TenLop"));
        l.setNienKhoa(rs.getString("NienKhoa"));
        l.setMaGVCN(rs.getString("MaGVCN")); 
        l.setTenGVCN(rs.getString("HoTen"));
        if(l.getTenGVCN() == null) l.setTenGVCN("(Chưa phân công)");
        return l;
    }
    public List<LopGVCN> searchLop(String keyword) {
    List<LopGVCN> list = new ArrayList<>();

    String sql = """
        SELECT l.MaLop, l.TenLop, l.NienKhoa, l.MaGVCN, gv.HoTen AS TenGVCN
        FROM Lop l
        LEFT JOIN GiaoVien gv ON l.MaGVCN = gv.MaGV
        WHERE l.MaLop LIKE ?
           OR l.TenLop LIKE ?
           OR l.NienKhoa LIKE ?
           OR gv.HoTen LIKE ?
    """;

    try (Connection conn = ConnectDB.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        String key = "%" + keyword + "%";
        ps.setString(1, key);
        ps.setString(2, key);
        ps.setString(3, key);
        ps.setString(4, key);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            LopGVCN l = new LopGVCN();
            l.setMaLop(rs.getString("MaLop"));
            l.setTenLop(rs.getString("TenLop"));
            l.setNienKhoa(rs.getString("NienKhoa"));
            l.setMaGVCN(rs.getString("MaGVCN"));
            l.setTenGVCN(rs.getString("TenGVCN"));
            list.add(l);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
        return list;
    }

    public List<String> getDistinctNienKhoa() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT NienKhoa FROM Lop WHERE NienKhoa IS NOT NULL AND LTRIM(RTRIM(NienKhoa)) <> '' ORDER BY NienKhoa DESC";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("NienKhoa"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
