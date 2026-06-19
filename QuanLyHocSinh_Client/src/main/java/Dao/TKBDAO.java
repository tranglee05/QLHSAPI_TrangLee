package Dao;

import Connection.ConnectDB;
import Model.TKB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TKBDAO {

    // Lấy danh sách các lớp đã có TKB để đưa lên ComboBox Lọc
    public List<String> getDistinctMaLop() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT MaLop FROM ThoiKhoaBieu ORDER BY MaLop";
        try (Connection c = ConnectDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Hàm Lọc Đa Năng: Dùng chung cho cả Học sinh (chỉ lọc môn, thứ) và Admin/GV (lọc được cả lớp)
    public List<TKB> getTKBByFilter(boolean isHocSinh, String maNguoiDung, String maLop, String monHoc, int thu) {
        List<TKB> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        if (isHocSinh) {
            // Học sinh thì tự động JOIN với bảng HocSinh để ép cứng lịch của lớp đó
            sql.append("SELECT T.*, M.TenMH FROM ThoiKhoaBieu T " +
                    "LEFT JOIN MonHoc M ON T.MaMH = M.MaMH " +
                    "JOIN HocSinh HS ON T.MaLop = HS.MaLop " +
                    "WHERE HS.MaHS = ? ");
            params.add(maNguoiDung);
        } else {
            // Admin / GV
            sql.append("SELECT T.*, M.TenMH FROM ThoiKhoaBieu T " +
                    "LEFT JOIN MonHoc M ON T.MaMH = M.MaMH " +
                    "WHERE 1=1 ");
            // Nếu có chọn mã lớp cụ thể
            if (maLop != null && !maLop.equals("Tất cả") && !maLop.isEmpty()) {
                sql.append("AND T.MaLop = ? ");
                params.add(maLop);
            }
        }

        // Lọc theo Môn Học (Tên môn hoặc Mã môn)
        if (monHoc != null && !monHoc.trim().isEmpty()) {
            sql.append("AND (T.MaMH LIKE ? OR M.TenMH LIKE ?) ");
            params.add("%" + monHoc.trim() + "%");
            params.add("%" + monHoc.trim() + "%");
        }

        // Lọc theo Thứ
        if (thu > 0) {
            sql.append("AND T.Thu = ? ");
            params.add(thu);
        }

        // Sắp xếp theo Thứ và Tiết bắt đầu cho đẹp
        sql.append("ORDER BY T.Thu ASC, T.TietBatDau ASC");

        try (Connection c = ConnectDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new TKB(
                        rs.getInt("MaTKB"), rs.getString("MaLop"), rs.getString("MaMH"),
                        rs.getString("TenMH"), rs.getString("MaGV"), rs.getString("MaPhong"),
                        rs.getInt("Thu"), rs.getInt("TietBatDau"), rs.getInt("TietKetThuc")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public boolean isTrungPhongTiet(TKB t) {
        String sql = "SELECT COUNT(*) FROM ThoiKhoaBieu WHERE MaPhong = ? AND Thu = ? AND NOT (TietKetThuc < ? OR TietBatDau > ?)";
        try (Connection c = ConnectDB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, t.getMaPhong());
            ps.setInt(2, t.getThu());
            ps.setInt(3, t.getTietBatDau());
            ps.setInt(4, t.getTietKetThuc());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public void insert(TKB t) {
        String sql = "INSERT INTO ThoiKhoaBieu (MaLop, MaMH, MaGV, MaPhong, Thu, TietBatDau, TietKetThuc) VALUES (?,?,?,?,?,?,?)";
        try (Connection c = ConnectDB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, t.getMaLop()); ps.setString(2, t.getMaMH()); ps.setString(3, t.getMaGV());
            ps.setString(4, t.getMaPhong()); ps.setInt(5, t.getThu()); ps.setInt(6, t.getTietBatDau()); ps.setInt(7, t.getTietKetThuc());
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void delete(int id) {
        String sql = "DELETE FROM ThoiKhoaBieu WHERE MaTKB = ?";
        try (Connection c = ConnectDB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
}