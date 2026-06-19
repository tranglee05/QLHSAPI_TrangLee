package Controller.Dai;

import Dao.HocSinhDAO;
import Model.Auth;
import Model.HocSinh;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class HocSinhController {

    private HocSinhDAO dao = new HocSinhDAO();

    //sửa ngày 09/04/2026
    public void loadTable(DefaultTableModel model) {
        model.setRowCount(0);
        List<HocSinh> list;

        // KIỂM TRA PHÂN QUYỀN
        if (Model.Auth.isHocSinh()) {
            // Chỉ lấy hồ sơ của chính học sinh đang đăng nhập
            HocSinh hs = dao.getByMaHS(Model.Auth.maNguoiDung);
            list = new java.util.ArrayList<>();
            if (hs != null) {
                list.add(hs);
            }
        } else {
            // Nếu là Admin/Giáo viên thì lấy tất cả
            list = dao.getAll();
        }

        // Đổ dữ liệu lên bảng
        for (HocSinh hs : list) {
            model.addRow(new Object[]{
                    hs.getMaHS(),
                    hs.getHoTen(),
                    hs.getNgaySinh(),
                    hs.getGioiTinh(),
                    hs.getDiaChi(),
                    hs.getMaLop(),
                    hs.getMaDT()
            });
        }
    }

    public boolean them(HocSinh hs) {
        return dao.insert(hs);
    }

 
    public boolean sua(HocSinh hs) {
        return dao.update(hs);
    }


    public boolean xoa(String maHS) {
        return dao.delete(maHS);
    }
    
    public void loadComboMaLop(JComboBox<String> cbo) {
        cbo.removeAllItems();
        for (String ma : dao.getAllMaLop()) {
            cbo.addItem(ma);
        }
    }

    public void loadComboMaDT(JComboBox<String> cbo) {
        cbo.removeAllItems();
        for (String ma : dao.getAllMaDT()) {
            cbo.addItem(ma);
        }
    }


 
    public void timKiem(String keyword, DefaultTableModel model) {
        model.setRowCount(0);
        List<HocSinh> list = dao.search(keyword);

        for (HocSinh hs : list) {
            model.addRow(new Object[]{
                hs.getMaHS(),
                hs.getHoTen(),
                hs.getNgaySinh(),
                hs.getGioiTinh(),
                hs.getDiaChi(),
                hs.getMaLop(),
                hs.getMaDT()
            });
        }
    }
    public HocSinh getThongTinCaNhan() {
        return dao.getByMaHS(Auth.maNguoiDung);
    }
}