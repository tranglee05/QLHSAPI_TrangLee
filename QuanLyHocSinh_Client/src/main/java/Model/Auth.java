package Model;

public class Auth {

    public static String currentUser = "";
    public static String currentRole = "";
    public static String maNguoiDung = "";

    public static void clear() {
        currentUser = "";
        currentRole = "";
        maNguoiDung = "";
    }

    public static boolean isLogin() {
        return currentUser != null && !currentUser.isEmpty();
    }

    // Chuẩn hóa role
    private static String role() {
        return currentRole == null ? "" : currentRole.trim().toLowerCase();
    }

    public static boolean isAdmin() {
        return role().equals("admin");
    }

    public static boolean isGiaoVien() {
        return role().equals("giaovien")
                || role().equals("giao vien")
                || role().equals("giáo viên");
    }

    public static boolean isHocSinh() {
        return role().equals("hocsinh")
                || role().equals("hoc sinh")
                || role().equals("học sinh");
    }

    // Kiểm tra quyền xem dữ liệu học sinh
    public static boolean canViewHocSinh(String maHSTarget) {
        // Admin xem được tất cả
        if (isAdmin()) {
            return true;
        }
        // Học sinh chỉ xem được chính mình
        if (isHocSinh()) {
            return maNguoiDung.equals(maHSTarget);
        }
        // Giáo viên không xem được dữ liệu học sinh cụ thể (xem danh sách lớp thôi)
        return false;
    }

    // Kiểm tra quyền xem dữ liệu giáo viên
    public static boolean canViewGiaoVien(String maGVTarget) {
        // Admin xem được tất cả
        if (isAdmin()) {
            return true;
        }
        // Giáo viên chỉ xem được chính mình
        if (isGiaoVien()) {
            return maNguoiDung.equals(maGVTarget);
        }
        // Học sinh không xem được dữ liệu giáo viên
        return false;
    }

    // Kiểm tra quyền sửa/xóa dữ liệu
    public static boolean canEditData(String maNguoiDungData) {
        // Admin có quyền sửa tất cả
        if (isAdmin()) {
            return true;
        }
        // Giáo viên/học sinh chỉ sửa được dữ liệu của chính mình
        return maNguoiDung.equals(maNguoiDungData);
    }
}