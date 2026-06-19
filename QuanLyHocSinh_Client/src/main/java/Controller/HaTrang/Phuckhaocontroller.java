/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller.HaTrang;
import Dao.PhuckhaoDAO;
import Model.Phuckhao;
import View.HaTrang.QuanLyPhucKhaoPanel;
import java.awt.event.*;
import java.util.List;
import javax.swing.JOptionPane;
/**
 *
 * @author ADMIN
 */
public class Phuckhaocontroller {
    private QuanLyPhucKhaoPanel view;
    private PhuckhaoDAO dao;
    private List<Phuckhao> listCurrent;

    public Phuckhaocontroller(QuanLyPhucKhaoPanel view) {
        this.view = view;
        this.dao = new PhuckhaoDAO();
        initEvents();
        loadData();
    }

    private void initEvents() {
        boolean[] editMode = {false};
        Runnable setIdleState = () -> view.setCrudButtonState(true, false, false, false, false);
        Runnable setAddState = () -> view.setCrudButtonState(false, false, false, true, true);
        Runnable setSelectedState = () -> view.setCrudButtonState(false, true, true, false, true);
        Runnable setEditState = () -> view.setCrudButtonState(false, true, true, true, true);
        setIdleState.run();

        // Table click - select row and fill form
        view.getTable().addMouseListener(new MouseAdapter() {
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

        // Add button - clear form for new entry
        view.getBtnThem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editMode[0] = false;
                view.refresh();
                view.getTable().clearSelection();
                setAddState.run();
            }
        });

        // Edit button - enable edit mode
        view.getBtnSua().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = view.getTable().getSelectedRow();
                if (row != -1) {
                    editMode[0] = true;
                    view.fillForm(row);
                    setEditState.run();
                } else {
                    JOptionPane.showMessageDialog(view, "Chọn dòng cần sửa!");
                }
            }
        });

        // Save button (handles both add and edit)
        view.getBtnLuu().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editMode[0]) {
                    // Edit mode: update existing
                    int row = view.getTable().getSelectedRow();
                    if (row != -1 && validateForm()) {
                        Phuckhao pk = getForm();
                        pk.setMaPK(listCurrent.get(row).getMaPK());
                        if (dao.update(pk)) {
                            JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
                            loadData();
                            view.refresh();
                            editMode[0] = false;
                            setIdleState.run();
                        }
                    } else {
                        JOptionPane.showMessageDialog(view, "Chọn dòng cần sửa!");
                    }
                } else {
                    // Add mode: insert new
                    if (validateForm()) {
                        Phuckhao pk = getForm();
                        if (dao.insert(pk)) {
                            JOptionPane.showMessageDialog(view, "Gửi yêu cầu thành công!");
                            loadData(); 
                            view.refresh();
                            editMode[0] = false;
                            setIdleState.run();
                        } else {
                            JOptionPane.showMessageDialog(view, "Thêm thất bại! Kiểm tra lại Mã HS/MH có tồn tại không.");
                        }
                    }
                }
            }
        });

        // Delete button with confirmation
        view.getBtnXoa().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = view.getTable().getSelectedRow();
                if (row != -1) {
                    int id = listCurrent.get(row).getMaPK();
                    int confirm = JOptionPane.showConfirmDialog(
                        view, "Bạn có chắc chắn muốn xóa?", "Xác nhận",
                        JOptionPane.YES_NO_OPTION
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        if (dao.delete(id)) {
                            loadData();
                            view.refresh();
                            editMode[0] = false;
                            setIdleState.run();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(view, "Vui lòng chọn dòng cần xóa!");
                }
            }
        });

        // Search/Filter button
        view.getBtnLoc().addActionListener(new ActionListener() {
            @Override 
            public void actionPerformed(ActionEvent e) {
                String tuKhoa = view.getLocKeyword().trim();
                listCurrent = dao.search(tuKhoa); 
                view.loadTable(listCurrent);
                if (listCurrent.isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Không tìm thấy kết quả nào!");
                }
            }
        });

        // Reset/Cancel button
        view.getBtnHuy().addActionListener(new ActionListener() {
            @Override 
            public void actionPerformed(ActionEvent e) { 
                view.refresh(); 
                loadData();
                editMode[0] = false;
                setIdleState.run();
            }
        });
    }
    //thêm ngày 13/4/2026
    // Tìm hàm load dữ liệu lên bảng trong Phuckhaocontroller và sửa lại thế này:
    private void loadData() { // (Hoặc tên hàm load của bạn)
        try {
            List<Phuckhao> list;

            if (Model.Auth.isHocSinh()) {
                // Nếu là học sinh, chỉ lấy danh sách của chính mình
                list = dao.getByMaHS(Model.Auth.maNguoiDung);
            } else {
                // Nếu là Admin/Giáo viên thì lấy tất cả
                list = dao.getAll();
            }

            view.loadTable(list);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean validateForm() {
        if (view.getMaHS().trim().isEmpty() || view.getMaMH().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Mã HS và Mã MH không được để trống!");
            return false;
        }
        return true;
    }

    private Phuckhao getForm() {
        Phuckhao pk = new Phuckhao();
        pk.setMaHS(view.getMaHS().trim());
        pk.setMaMH(view.getMaMH().trim());
        pk.setTrangThai(view.getTrangThai().trim());
        pk.setLyDo(view.getLyDo().trim());
        return pk;
    }
}
