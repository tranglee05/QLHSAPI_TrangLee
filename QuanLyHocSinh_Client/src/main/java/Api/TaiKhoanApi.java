package Api;

import Model.TaiKhoan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Connection.ConnectDB;
import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.HashMap;
public class TaiKhoanApi {

    public String checkLogin(String user, String pass) {
        TaiKhoan tk = checkLoginFull(user, pass);
        if (tk != null) {
            return tk.getQuyen();
        }
        return null;
    }

  
    public List<TaiKhoan> getAll() {
        List<TaiKhoan> list = new ArrayList<>();
        String sql = "SELECT * FROM TaiKhoan";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

 
    public boolean insert(TaiKhoan tk) {
        String sql = """
            INSERT INTO TaiKhoan
            (TenDangNhap, MatKhau, Quyen, MaNguoiDung)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tk.getTenDangNhap());
            ps.setString(2, tk.getMatKhau());
            ps.setString(3, tk.getQuyen());
            ps.setString(4, tk.getMaNguoiDung());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(TaiKhoan tk) {
        String sql = """
            UPDATE TaiKhoan
            SET MatKhau = ?, Quyen = ?, MaNguoiDung = ?
            WHERE TenDangNhap = ?
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tk.getMatKhau());
            ps.setString(2, tk.getQuyen());
            ps.setString(3, tk.getMaNguoiDung());
            ps.setString(4, tk.getTenDangNhap());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean delete(String tenDangNhap) {
        String sql = "DELETE FROM TaiKhoan WHERE TenDangNhap = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tenDangNhap);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public List<TaiKhoan> search(String keyword) {
        List<TaiKhoan> list = new ArrayList<>();
        String sql = """
            SELECT * FROM TaiKhoan
            WHERE TenDangNhap LIKE ?
               OR Quyen LIKE ?
               OR MaNguoiDung LIKE ?
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String key = "%" + keyword + "%";
            ps.setString(1, key);
            ps.setString(2, key);
            ps.setString(3, key);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    private TaiKhoan map(ResultSet rs) throws SQLException {
        TaiKhoan tk = new TaiKhoan();
        tk.setTenDangNhap(rs.getString("TenDangNhap"));
        tk.setMatKhau(rs.getString("MatKhau"));
        tk.setQuyen(rs.getString("Quyen"));
        tk.setMaNguoiDung(rs.getString("MaNguoiDung"));
        return tk;
    }
    // Thêm cho phân quyền tài khoản sử dụng API từ Server
    public TaiKhoan checkLoginFull(String tenDangNhap, String matKhau) {
        try {
            // Chuẩn bị dữ liệu gửi đi (JSON)
            Map<String, String> data = new HashMap<>();
            data.put("tenDangNhap", tenDangNhap);
            data.put("matKhau", matKhau);
            
            Gson gson = new Gson();
            String jsonRequest = gson.toJson(data);

            // Tạo request gửi đến Server Spring Boot
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ApiConfig.BASE_URL + "/api/taikhoan/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                    .build();

            // Thực hiện gọi và nhận kết quả
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Nếu thành công (HTTP 200), chuyển JSON trả về thành object TaiKhoan
                return gson.fromJson(response.body(), TaiKhoan.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Đăng nhập thất bại hoặc lỗi server
    }
}
