package Controller.Dat;

import Dao.ToBoMonDAO;
import Model.ToBoMon;
import View.Dat.QuanLyToBoMonPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;

public class ToBoMonController {

    private QuanLyToBoMonPanel view;
    private ToBoMonDAO dao;
    private String currentMode = ""; // "ADD" hoặc "EDIT"

    public ToBoMonController(QuanLyToBoMonPanel view) {
        this.view = view;
        this.dao = new ToBoMonDAO();

        // 1. Gán sự kiện cho các nút
        initEvents();
        
        // 2. Tải dữ liệu ban đầu
        loadData();
    }

    // Hàm load data từ DAO đổ về View
    private void loadData() {
        List<ToBoMon> list = dao.getAll();
        view.setTableData(list); 
    }

    private void initEvents() {
        // --- Sự kiện click bảng ---
        view.getTableTBM().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int r = view.getTableTBM().getSelectedRow();
                if (r >= 0) {
                    view.getTxtMaToHop().setText(view.getTableTBM().getValueAt(r, 0).toString());
                    view.getTxtTenToHop().setText(view.getTableTBM().getValueAt(r, 1).toString());
                }
            }
        });

        // --- Nút Thêm ---
        view.getBtnThem().addActionListener(e -> {
            view.clearForm();
            currentMode = "ADD";
            view.setButtonState(false); // Chuyển sang trạng thái nhập liệu
            view.getTxtMaToHop().setEnabled(true);
            view.getTxtMaToHop().requestFocus();
        });

        // --- Nút Sửa ---
        view.getBtnSua().addActionListener(e -> {
            if (view.getTableTBM().getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn dòng cần sửa!");
                return;
            }
            currentMode = "EDIT";
            view.setButtonState(false);
            view.getTxtMaToHop().setEnabled(false); // Khóa mã
            view.getTxtTenToHop().requestFocus();
        });

        // --- Nút Xóa ---
        view.getBtnXoa().addActionListener(e -> {
            int r = view.getTableTBM().getSelectedRow();
            if (r == -1) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn dòng cần xóa!");
                return;
            }
            String ma = view.getTableTBM().getValueAt(r, 0).toString();
            int opt = JOptionPane.showConfirmDialog(view, 
                    "Bạn có chắc chắn muốn xóa tổ hợp này?\nCác giáo viên thuộc tổ này sẽ bị mất thông tin tổ bộ môn.", 
                    "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            
            if (opt == JOptionPane.YES_OPTION) {
                if (dao.delete(ma)) {
                    JOptionPane.showMessageDialog(view, "Xóa thành công!");
                    loadData();
                    view.clearForm();
                } else {
                    JOptionPane.showMessageDialog(view, "Xóa thất bại!");
                }
            }
        });

        // --- Nút Lưu ---
        view.getBtnLuu().addActionListener(e -> {
            String ma = view.getTxtMaToHop().getText().trim();
            String ten = view.getTxtTenToHop().getText().trim();

            if (ma.isEmpty() || ten.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            ToBoMon tbm = new ToBoMon(ma, ten, ""); // Model
            boolean kq = false;

            if ("ADD".equals(currentMode)) {
                kq = dao.insert(tbm);
            } else if ("EDIT".equals(currentMode)) {
                kq = dao.update(tbm);
            }

            if (kq) {
                JOptionPane.showMessageDialog(view, "Lưu thành công!");
                loadData();
                view.setButtonState(true); // Trở về trạng thái bình thường
                view.clearForm();
                currentMode = "";
            } else {
                JOptionPane.showMessageDialog(view, "Lưu thất bại! Có thể mã đã tồn tại.");
            }
        });

        // --- Nút Hủy ---
        view.getBtnHuy().addActionListener(e -> {
            view.setButtonState(true);
            view.clearForm();
            currentMode = "";
            view.getTableTBM().clearSelection();
        });
    }
}