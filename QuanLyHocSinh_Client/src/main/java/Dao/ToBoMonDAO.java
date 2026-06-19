package Dao;

import Connection.ConnectDB;
import Model.ToBoMon;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ToBoMonDAO {

    // 1. LẤY TẤT CẢ
    public List<ToBoMon> getAll() {
        List<ToBoMon> list = new ArrayList<>();
        // Chỉ lấy từ bảng ToHopMon, không JOIN linh tinh
        String sql = "SELECT * FROM ToHopMon";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()){
                list.add(new ToBoMon(
                    rs.getString("MaToHop"),
                    rs.getString("TenToHop"),
                    "" // Tham số trưởng bộ môn để trống
                ));
            }
        } catch (Exception ex) { ex.printStackTrace(); }
        return list;
    }

    // 2. THÊM (BỔ SUNG)
    public boolean insert(ToBoMon t) {
        String sql = "INSERT INTO ToHopMon(MaToHop, TenToHop) VALUES(?, ?)";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getMaToHop());
            ps.setString(2, t.getTenToHop());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // 3. SỬA (BỔ SUNG)
    public boolean update(ToBoMon t) {
        String sql = "UPDATE ToHopMon SET TenToHop=? WHERE MaToHop=?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getTenToHop());
            ps.setString(2, t.getMaToHop());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // 4. XÓA (BỔ SUNG)
    public boolean delete(String ma) {
        String sql = "DELETE FROM ToHopMon WHERE MaToHop=?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ma);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }
}