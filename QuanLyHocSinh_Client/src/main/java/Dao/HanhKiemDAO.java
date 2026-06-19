package Dao;

import Connection.ConnectDB;
import Model.HanhKiem;
import Model.Auth;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HanhKiemDAO {


    public List<HanhKiem> getHanhKiemByFilter(String maLop, String namHoc, int hocKy) {
        List<HanhKiem> list = new ArrayList<>();
        String sql = "SELECT hk.*, hs.HoTen, hs.MaLop AS LopHocSinh FROM HanhKiem hk " +
                     "JOIN HocSinh hs ON hk.MaHS = hs.MaHS " +
                     "WHERE hs.MaLop = ? AND hk.NamHoc = ? AND hk.HocKy = ?";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maLop);
            ps.setString(2, namHoc);
            ps.setInt(3, hocKy);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToModel(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public List<HanhKiem> searchHanhKiem(String keyword) {
        List<HanhKiem> list = new ArrayList<>();
        String sql = "SELECT hk.*, hs.HoTen, hs.MaLop AS LopHocSinh FROM HanhKiem hk " +
                     "JOIN HocSinh hs ON hk.MaHS = hs.MaHS " +
                     "WHERE hk.MaHS LIKE ? OR hs.HoTen LIKE ?";
        
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            String key = "%" + keyword + "%";
            ps.setString(1, key);
            ps.setString(2, key);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToModel(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public boolean saveHanhKiem(HanhKiem hk) {
        // Kiểm tra quyền
        if (!Auth.canEditData(hk.getMaHS())) {
            System.out.println("Bạn không có quyền cập nhật hạnh kiểm cho học sinh này!");
            return false;
        }

        String checkSql = "SELECT count(*) FROM HanhKiem WHERE MaHS = ? AND NamHoc = ? AND HocKy = ?";
        try (Connection conn = ConnectDB.getConnection()) {
            PreparedStatement psCheck = conn.prepareStatement(checkSql);
            psCheck.setString(1, hk.getMaHS());
            psCheck.setString(2, hk.getNamHoc());
            psCheck.setInt(3, hk.getHocKy());
            ResultSet rs = psCheck.executeQuery();
            rs.next();
            boolean exists = rs.getInt(1) > 0;

            if (exists) {
                
                String updateSql = "UPDATE HanhKiem SET XepLoai = ?, NhanXet = ? WHERE MaHS = ? AND NamHoc = ? AND HocKy = ?";
                PreparedStatement psUp = conn.prepareStatement(updateSql);
                psUp.setString(1, hk.getXepLoai());
                psUp.setString(2, hk.getNhanXet());
                psUp.setString(3, hk.getMaHS());
                psUp.setString(4, hk.getNamHoc());
                psUp.setInt(5, hk.getHocKy());
                return psUp.executeUpdate() > 0;
            } else {
           
                String insertSql = "INSERT INTO HanhKiem(MaHS, NamHoc, HocKy, XepLoai, NhanXet) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement psIn = conn.prepareStatement(insertSql);
                psIn.setString(1, hk.getMaHS());
                psIn.setString(2, hk.getNamHoc());
                psIn.setInt(3, hk.getHocKy());
                psIn.setString(4, hk.getXepLoai());
                psIn.setString(5, hk.getNhanXet());
                return psIn.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    

    public boolean deleteHanhKiem(String maHS, String namHoc, int hocKy) {
        // Kiểm tra quyền
        if (!Auth.canEditData(maHS)) {
            System.out.println("Bạn không có quyền xóa hạnh kiểm cho học sinh này!");
            return false;
        }

        String sql = "DELETE FROM HanhKiem WHERE MaHS = ? AND NamHoc = ? AND HocKy = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHS);
            ps.setString(2, namHoc);
            ps.setInt(3, hocKy);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public List<String> getDistinctNamHoc() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT NamHoc FROM HanhKiem ORDER BY NamHoc DESC";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("NamHoc"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private HanhKiem mapResultSetToModel(ResultSet rs) throws SQLException {
        HanhKiem hk = new HanhKiem();
        hk.setMaHS(rs.getString("MaHS"));
        hk.setTenHS(rs.getString("HoTen"));
        hk.setMaLop(rs.getString("LopHocSinh"));
        hk.setHocKy(rs.getInt("HocKy"));
        hk.setNamHoc(rs.getString("NamHoc"));
        hk.setXepLoai(rs.getString("XepLoai") != null ? rs.getString("XepLoai") : "");
        hk.setNhanXet(rs.getString("NhanXet") != null ? rs.getString("NhanXet") : "");
        return hk;
    }
    public List<HanhKiem> getHanhKiemByMaHS(String maHS) {
        List<HanhKiem> list = new ArrayList<>();

        String sql = """
        SELECT hk.*, hs.HoTen, hs.MaLop AS LopHocSinh
        FROM HanhKiem hk
        JOIN HocSinh hs ON hk.MaHS = hs.MaHS
        WHERE hk.MaHS = ?
    """;

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maHS);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToModel(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Lấy hạnh kiểm với kiểm tra quyền
    public List<HanhKiem> getHanhKiemByMaHSWithPermission(String maHS) {
        // Kiểm tra quyền xem hạnh kiểm
        if (!Auth.canViewHocSinh(maHS)) {
            return new ArrayList<>();
        }
        return getHanhKiemByMaHS(maHS);
    }

    public List<HanhKiem> searchHanhKiemByMaHS(String maHS, String keyword) {
        List<HanhKiem> list = new ArrayList<>();

        String sql = """
        SELECT hk.*, hs.HoTen, hs.MaLop AS LopHocSinh
        FROM HanhKiem hk
        JOIN HocSinh hs ON hk.MaHS = hs.MaHS
        WHERE hk.MaHS = ?
        AND (hk.MaHS LIKE ? OR hs.HoTen LIKE ?)
    """;

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String key = "%" + keyword + "%";

            ps.setString(1, maHS);
            ps.setString(2, key);
            ps.setString(3, key);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToModel(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
