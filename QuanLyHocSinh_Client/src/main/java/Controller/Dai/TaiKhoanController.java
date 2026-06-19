package Controller.Dai;

import Dao.TaiKhoanDAO;
import Model.TaiKhoan;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class TaiKhoanController {

    private TaiKhoanDAO dao = new TaiKhoanDAO();

    
    public void loadTable(DefaultTableModel model) {
        model.setRowCount(0);
        List<TaiKhoan> list = dao.getAll();

        for (TaiKhoan tk : list) {
            model.addRow(new Object[]{
                tk.getTenDangNhap(),
                tk.getMatKhau(),
                tk.getQuyen(),
                tk.getMaNguoiDung(),
            });
        }
    }

  
    public boolean them(TaiKhoan tk) {
        return dao.insert(tk);
    }

 
    public boolean sua(TaiKhoan tk) {
        return dao.update(tk);
    }

   
    public boolean xoa(String tenDangNhap) {
        return dao.delete(tenDangNhap);
    }

  
    public void timKiem(String keyword, DefaultTableModel model) {
        model.setRowCount(0);
        List<TaiKhoan> list = dao.search(keyword);

        for (TaiKhoan tk : list) {
            model.addRow(new Object[]{
                tk.getTenDangNhap(),
                tk.getMatKhau(),
                tk.getQuyen(),
                tk.getMaNguoiDung(),
            });
        }
    }
}
