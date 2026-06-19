package Controller.Dat;

import Dao.GiaovienDAO;
import Dao.ToBoMonDAO;
import Model.Giaovien;
import Model.ToBoMon;
import View.Dat.QuanLyGiaoVienPanel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GiaoVienController {

    private QuanLyGiaoVienPanel view;
    private GiaovienDAO dao;
    private ToBoMonDAO tbmDao;
    private String currentMode = ""; 

    public GiaoVienController(QuanLyGiaoVienPanel view) {
        this.view = view;
        this.dao = new GiaovienDAO();
        this.tbmDao = new ToBoMonDAO();

        initController();
    }

    private void initController() {
        loadComboBox();
        loadTable();
        setButtonState(true); 

        view.getTableGV().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fillForm();
            }
        });

        view.getBtnThem().addActionListener(e -> them());
        view.getBtnSua().addActionListener(e -> sua());
        view.getBtnXoa().addActionListener(e -> xoa());
        view.getBtnLuu().addActionListener(e -> luu());
        view.getBtnHuy().addActionListener(e -> huy());
        view.getBtnXem().addActionListener(e -> loadTable());
        view.getBtnTimKiem().addActionListener(e -> searchData());
    }

    private void loadComboBox() {
        view.getCboMaToHop().removeAllItems();
        List<ToBoMon> list = tbmDao.getAll();
        for (ToBoMon t : list) {
            view.getCboMaToHop().addItem(t);
        }
    }

    private void loadTable() {
        // Sử dụng phương thức mới với kiểm tra quyền
        List<Giaovien> list = dao.getAllWithPermission();
        view.getTableModel().setRowCount(0);
        for (Giaovien gv : list) {
            view.getTableModel().addRow(new Object[]{
                gv.getMaGV(), gv.getHoTen(), gv.getNgaysinh(), gv.getSdt(), gv.getMaTH()
            });
        }
    }

    private void fillForm() {
        int r = view.getTableGV().getSelectedRow();
        if (r < 0) return;
        
        view.getTxtMaGV().setText(view.getTableGV().getValueAt(r, 0).toString());
        view.getTxtHoTen().setText(view.getTableGV().getValueAt(r, 1).toString());
        
        try {
            // Dữ liệu từ DB ở dạng "yyyy-MM-dd", parse để set vào JSpinner
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(view.getTableGV().getValueAt(r, 2).toString());
            view.getSpNgaySinh().setValue(date);
        } catch (Exception e) {}
        
        view.getTxtSDT().setText(view.getTableGV().getValueAt(r, 3).toString());

        String maTH = view.getTableGV().getValueAt(r, 4).toString();
        for (int i = 0; i < view.getCboMaToHop().getItemCount(); i++) {
            if (view.getCboMaToHop().getItemAt(i).getMaToHop().equals(maTH)) {
                view.getCboMaToHop().setSelectedIndex(i);
                break;
            }
        }
    }

    private void them() {
        clearForm();
        currentMode = "ADD";
        setButtonState(false); 
        view.getTxtMaGV().setEnabled(true);
        view.getTxtMaGV().requestFocus();
    }

    private void sua() {
        if (view.getTableGV().getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn giáo viên cần sửa!");
            return;
        }
        currentMode = "EDIT";
        setButtonState(false);
        view.getTxtMaGV().setEnabled(false); 
    }

    private void xoa() {
        int r = view.getTableGV().getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn dòng cần xóa!");
            return;
        }
        if (JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            String ma = view.getTxtMaGV().getText();
            if (dao.delete(ma)) {
                JOptionPane.showMessageDialog(view, "Đã xóa!");
                loadTable();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(view, "Xóa thất bại!");
            }
        }
    }

    private void luu() {
        if (view.getTxtMaGV().getText().isEmpty() || view.getTxtHoTen().getText().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập đủ thông tin!");
            return;
        }

        Giaovien gv = new Giaovien();
        gv.setMaGV(view.getTxtMaGV().getText());
        gv.setHoTen(view.getTxtHoTen().getText());
        gv.setSdt(view.getTxtSDT().getText());
        // Lấy ngày từ JSpinner và format thành "yyyy-MM-dd" để lưu vào DB
        gv.setNgaysinh(new SimpleDateFormat("yyyy-MM-dd").format(view.getSpNgaySinh().getValue()));

        ToBoMon tbm = (ToBoMon) view.getCboMaToHop().getSelectedItem();
        if (tbm != null) gv.setMaTH(tbm.getMaToHop());

        boolean ok = false;
        if ("ADD".equals(currentMode)) ok = dao.insert(gv);
        else if ("EDIT".equals(currentMode)) ok = dao.update(gv);

        if (ok) {
            JOptionPane.showMessageDialog(view, "Lưu thành công!");
            loadTable();
            setButtonState(true);
            clearForm();
        } else {
            JOptionPane.showMessageDialog(view, "Thao tác thất bại! Có thể mã đã tồn tại.");
        }
    }

    private void huy() {
        setButtonState(true);
        clearForm();
        view.getTableGV().clearSelection();
    }
    private void searchData() {
    String keyword = view.getTxtTimKiem().getText().trim();

    if (keyword.isEmpty()) {
        JOptionPane.showMessageDialog(view, "Vui lòng nhập từ khóa tìm kiếm!");
        return;
    }

    List<Giaovien> list = dao.searchGiaoVien(keyword);

    view.getTableModel().setRowCount(0); 

    for (Giaovien gv : list) {
        view.getTableModel().addRow(new Object[]{
            gv.getMaGV(),
            gv.getHoTen(),
            gv.getNgaysinh(),
            gv.getSdt(),
            gv.getMaTH()
        });
    }

    if (list.isEmpty()) {
        JOptionPane.showMessageDialog(view,
                "Không tìm thấy giáo viên nào với từ khóa: " + keyword);
    }
}
    private void clearForm() {
        view.getTxtMaGV().setText("");
        view.getTxtHoTen().setText("");
        // ĐÃ SỬA DÒNG NÀY (Thêm ngoặc đơn)
        view.getTxtSDT().setText(""); 
        view.getTxtMaGV().setEnabled(true);
    }

    private void setButtonState(boolean normal) {
        view.getBtnThem().setEnabled(normal);
        view.getBtnSua().setEnabled(normal);
        view.getBtnXoa().setEnabled(normal);
        view.getBtnLuu().setEnabled(!normal);
        view.getBtnHuy().setEnabled(!normal);
        view.getTableGV().setEnabled(normal); 
    }
}