/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller.ThuTrang;

import Dao.MonHocDAO;
import Model.MonHoc;
import View.ThuTrang.FrmMonHoc;
import java.awt.event.*;
import java.util.List;

/**
 *
 * @author Admin
 */
public class MonHocController {
  private FrmMonHoc view;
    private MonHocDAO dao;

    public MonHocController(FrmMonHoc view) {
        this.view = view;
        this.dao = new MonHocDAO();
        initEvents();
        loadData();
    }

    private void initEvents() {
        boolean[] editMode = {false};  // Biến theo dõi chế độ chỉnh sửa
        Runnable setIdleState = () -> view.setCrudButtonState(true, false, false, false, false);
        Runnable setAddState = () -> view.setCrudButtonState(false, false, false, true, true);
        Runnable setSelectedState = () -> view.setCrudButtonState(false, true, true, false, true);
        Runnable setEditState = () -> view.setCrudButtonState(false, true, true, true, true);
        setIdleState.run();

        view.addBtnXemListener(e -> loadData());

        view.addBtnTimKiemListener(e -> {
            String key = view.getTuKhoa();
            if (key.isEmpty()) {
                view.showMessage("Vui lòng nhập mã hoặc tên môn");
                return;
            }

            List<MonHoc> list = dao.search(key);
            view.setTableData(list);

            if (list.isEmpty()) {
                view.showMessage("Không tìm thấy môn học");
            }
        });

        // Nút Thêm: bật chế độ chỉnh sửa, xóa nội dung form
        view.addBtnThemListener(e -> {
            editMode[0] = false;
            view.clearForm();
            view.getTxtTimKiem().setText("");
            view.getTable().clearSelection();
            setAddState.run();
        });

        // Nút Sửa: bật chế độ chỉnh sửa từ dữ liệu chọn
        view.addBtnSuaListener(e -> {
            int row = view.getTable().getSelectedRow();
            if (row == -1) {
                view.showMessage("Vui lòng chọn một bản ghi");
                return;
            }
            editMode[0] = true;
            view.fillForm(row);
            setEditState.run();
        });

        // Nút Xóa: xóa bản ghi đã chọn
        view.addBtnXoaListener(e -> {
            int row = view.getTable().getSelectedRow();
            if (row == -1) {
                view.showMessage("Vui lòng chọn một bản ghi");
                return;
            }

            String ma = view.getTable().getValueAt(row, 0).toString();
            int confirm = javax.swing.JOptionPane.showConfirmDialog(
                view, "Bạn có chắc chắn muốn xóa?", "Xác nhận", 
                javax.swing.JOptionPane.YES_NO_OPTION
            );
            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                dao.delete(ma);
                view.showMessage("Đã xoá");
                loadData();
                view.clearForm();
                editMode[0] = false;
                setIdleState.run();
            }
        });

        // Nút Lưu: lưu dữ liệu (thêm hoặc sửa)
        view.addBtnLuuListener(e -> {
            MonHoc m = view.getMonHocInput();
            if (m.getMaMH().isEmpty()) {
                view.showMessage("Mã môn không được rỗng");
                return;
            }

            if (dao.exists(m.getMaMH()) && !editMode[0]) {
                view.showMessage("Mã môn đã tồn tại");
                return;
            }

            if (editMode[0]) {
                dao.update(m);
            } else {
                dao.insert(m);
            }

            view.showMessage("Lưu thành công");
            loadData();
            view.clearForm();
            editMode[0] = false;
            setIdleState.run();
        });

        view.addBtnHuyListener(e -> {
            view.clearForm();
            loadData();
            editMode[0] = false;
            setIdleState.run();
        });


        view.addTableMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = view.getTable().getSelectedRow();
                if (row >= 0) {
                    editMode[0] = true;
                    view.fillForm(row);
                    setSelectedState.run();
                }
            }
        });
    }

    private void loadData() {
        view.setTableData(dao.getAll());
    }
}
