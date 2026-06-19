package Dao;

import Connection.ConnectDB;
import Model.Giaovien;
import Model.Auth;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GiaovienDAO {

    public List<Giaovien> getAll() {
        List<Giaovien> list = new ArrayList<>();
        String sql = "SELECT MaGV, HoTen, NgaySinh, SDT, MaToHop FROM GiaoVien";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapGiaovien(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy danh sách giáo viên với kiểm tra quyền
    public List<Giaovien> getAllWithPermission() {
        List<Giaovien> list = new ArrayList<>();
        
        // Nếu là admin, lấy tất cả
        if (Auth.isAdmin()) {
            return getAll();
        }
        
        // Nếu là giáo viên, chỉ lấy thông tin của chính mình
        if (Auth.isGiaoVien()) {
            Giaovien gv = getByMaGV(Auth.maNguoiDung);
            if (gv != null) {
                list.add(gv);
            }
            return list;
        }
        
        // Học sinh không có quyền xem danh sách giáo viên
        return list;
    }

    public boolean insert(Giaovien gv) {
        String sql = """
            INSERT INTO GiaoVien (MaGV, HoTen, NgaySinh, SDT, MaToHop)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, gv.getMaGV());
            ps.setString(2, gv.getHoTen());
            ps.setString(3, gv.getNgaysinh());
            ps.setString(4, gv.getSdt());
            ps.setString(5, gv.getMaTH());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean update(Giaovien gv) {
        // Kiểm tra quyền sửa dữ liệu
        if (!Auth.canEditData(gv.getMaGV())) {
            System.out.println("Bạn không có quyền sửa thông tin giáo viên này!");
            return false;
        }

        String sql = """
            UPDATE GiaoVien
            SET HoTen = ?, NgaySinh = ?, SDT = ?, MaToHop = ?
            WHERE MaGV = ?
        """;

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, gv.getHoTen());
            ps.setString(2, gv.getNgaysinh());
            ps.setString(3, gv.getSdt());
            ps.setString(4, gv.getMaTH());
            ps.setString(5, gv.getMaGV());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maGV) {
        // Kiểm tra quyền xóa dữ liệu
        if (!Auth.canEditData(maGV)) {
            System.out.println("Bạn không có quyền xóa thông tin giáo viên này!");
            return false;
        }

        String sql = "DELETE FROM GiaoVien WHERE MaGV = ?";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maGV);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Giaovien> searchGiaoVien(String keyword) {
        List<Giaovien> list = new ArrayList<>();

        String sql = """
            SELECT MaGV, HoTen, NgaySinh, SDT, MaToHop
            FROM GiaoVien
            WHERE MaGV LIKE ?
               OR HoTen LIKE ?
               OR SDT LIKE ?
               OR MaToHop LIKE ?
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
                list.add(mapGiaovien(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private Giaovien mapGiaovien(ResultSet rs) throws SQLException {
        Giaovien gv = new Giaovien();
        gv.setMaGV(rs.getString("MaGV"));
        gv.setHoTen(rs.getString("HoTen"));
        gv.setNgaysinh(rs.getString("NgaySinh"));
        gv.setSdt(rs.getString("SDT"));
        gv.setMaTH(rs.getString("MaToHop"));
        return gv;
    }

    // Lấy thông tin giáo viên theo mã
    public Giaovien getByMaGV(String maGV) {
        String sql = "SELECT MaGV, HoTen, NgaySinh, SDT, MaToHop FROM GiaoVien WHERE MaGV = ?";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maGV);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapGiaovien(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy thông tin giáo viên với kiểm tra quyền
    public Giaovien getByMaGVWithPermission(String maGV) {
        // Kiểm tra quyền xem
        if (!Auth.canViewGiaoVien(maGV)) {
            return null;
        }
        return getByMaGV(maGV);
    }
}
