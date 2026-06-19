/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller.HaTrang;

import Dao.HocphiDAO;
import Model.Auth;
import Model.Hocphi;
import View.HaTrang.QuanLyHocPhiPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import java.util.List;

public class Hocphicontroller {
    private QuanLyHocPhiPanel view;
    private HocphiDAO dao;

    public Hocphicontroller(QuanLyHocPhiPanel view) {
        this.view = view;
        this.dao = new HocphiDAO();
        List<Hocphi> list;

        System.out.println("DEBUG Controller: Khởi tạo controller...");

        initEvents();
        loadTatCaDuLieu();
    }

    private void initEvents() {
        boolean[] updateMode = {false};
        Runnable setIdleState = () -> view.setCrudButtonState(true, false, false, false, false);
        Runnable setAddState = () -> view.setCrudButtonState(false, false, false, true, true);
        Runnable setSelectedState = () -> view.setCrudButtonState(false, true, true, false, true);
        Runnable setEditState = () -> view.setCrudButtonState(false, true, true, true, true);
        setIdleState.run();

        // Filter button
        view.getBtnLoc().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                locDuLieu();
            }
        });


        view.getBtnThem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMode[0] = false;
                view.refreshForm();
                view.getTableHocPhi().clearSelection();
                setAddState.run();
            }
        });

        view.getBtnSua().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = view.getTableHocPhi().getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(view, "Vui lòng chọn một dòng cần sửa!");
                    return;
                }
                updateMode[0] = true;
                setEditState.run();
            }
        });

        // Save button (handles both add and update)
        view.getBtnLuu().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (xuLyLuu(updateMode[0])) {
                    updateMode[0] = false;
                    setIdleState.run();
                }
            }
        });

        // Delete button with confirmation
        view.getBtnXoa().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (xoaHocPhi()) {
                    updateMode[0] = false;
                    setIdleState.run();
                }
            }
        });

        // Cancel/Reset button
        view.getBtnHuy().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.refreshForm();
                updateMode[0] = false;
                setIdleState.run();
            }
        });

        view.getTableHocPhi().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = view.getTableHocPhi().getSelectedRow();
                if (selectedRow >= 0) {
                    updateMode[0] = true;
                    setSelectedState.run();
                }
            }
        });

        System.out.println("DEBUG Controller: Controller khởi tạo xong!");

        loadTatCaDuLieu();
    }
    //Sửa ngày 09/04/2026
    private void loadTatCaDuLieu() {
        try {
            List<Hocphi> listAll;
            if (Auth.isHocSinh()) {
                listAll = dao.getByMaHS(Auth.maNguoiDung);
            } else {
                listAll = dao.getAllHocPhi();
            }

            view.loadTable(listAll);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void locDuLieu() {
        try {
            Object cboMaLopSelected = view.getCboMaLop().getSelectedItem();
            Object cboHocKySelected = view.getCboHocKy().getSelectedItem();
            Object cboNamHocSelected = view.getCboNamHoc().getSelectedItem();

            // Nếu có selection (không null) thì lấy giá trị, nếu trống thì pass empty string
            String maLop = (cboMaLopSelected != null) ? cboMaLopSelected.toString().trim() : "";
            String hocKyStr = (cboHocKySelected != null) ? cboHocKySelected.toString().trim() : "";
            String namHoc = (cboNamHocSelected != null) ? cboNamHocSelected.toString().trim() : "";

            // Nếu tất cả đều trống → load toàn bộ dữ liệu
            // Nếu có ít nhất một giá trị → filter theo giá trị đó
            int hocKy = hocKyStr.isEmpty() ? 0 : Integer.parseInt(hocKyStr);
            
            System.out.println("DEBUG: Lọc dữ liệu - Lớp: " + (maLop.isEmpty() ? "Tất cả" : maLop) + 
                             ", Kỳ: " + (hocKy == 0 ? "Tất cả" : hocKy) + 
                             ", Năm: " + (namHoc.isEmpty() ? "Tất cả" : namHoc));

            List<Hocphi> list = dao.getHocPhiByLop(maLop, hocKy, namHoc);
            
            System.out.println("DEBUG: Số dòng tìm được: " + list.size());

            view.loadTable(list);

            if (list.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Không tìm thấy dữ liệu cho lớp " + maLop +
                                            ", Kỳ " + hocKy + ", Năm " + namHoc);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Lỗi định dạng: " + ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Lỗi khi lọc: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private boolean xuLyLuu(boolean isUpdate) {
        try {
           
            String maHS = view.getTxtMaHS().getText().trim();
            String tongTienStr = view.getTxtTongTien().getText().trim();
            String mienGiamStr = view.getTxtMienGiam().getText().trim();
            Object hocKyObj = view.getCboHocKy().getSelectedItem();
            Object namHocObj = view.getCboNamHoc().getSelectedItem();
            Object trangThaiObj = view.getCboTrangThai().getSelectedItem();
            String hocKyStr = hocKyObj == null ? "" : hocKyObj.toString().trim();
            String namHoc = namHocObj == null ? "" : namHocObj.toString().trim();
            String trangThaiStr = trangThaiObj == null ? "" : trangThaiObj.toString().trim();

            if ("(Không có dữ liệu)".equalsIgnoreCase(namHoc) || "(Lỗi tải dữ liệu)".equalsIgnoreCase(namHoc)) {
                namHoc = "";
            }

             
            if (maHS.isEmpty() || tongTienStr.isEmpty() || hocKyStr.isEmpty() || namHoc.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Mã học sinh, Tổng tiền, Học kỳ và Năm học không được để trống!");
                return false;
            }

            Hocphi hp = new Hocphi();
            hp.setMaHS(maHS);
            hp.setHocKy(Integer.parseInt(hocKyStr));
            hp.setNamHoc(namHoc);
            
            long tongTien = Long.parseLong(tongTienStr);
            long mienGiam = mienGiamStr.isEmpty() ? 0 : Long.parseLong(mienGiamStr);
            long phaiDong = tongTien - mienGiam;

            hp.setTongTien(tongTien);
            hp.setMienGiam(mienGiam);
            hp.setPhaiDong(phaiDong);
            hp.setTrangThai(trangThaiStr.isEmpty() ? "Chưa đóng" : trangThaiStr);

            
            if (dao.saveHocPhi(hp)) {
                String thongBao = isUpdate ? "Cập nhật học phí thành công!" : "Thêm mới học phí thành công!";
                JOptionPane.showMessageDialog(view, thongBao);
                locDuLieu();
                view.refreshForm(); 
                return true;
            } else {
                JOptionPane.showMessageDialog(view, "Lưu thất bại! Kiểm tra lại mã HS hoặc kết nối DB.");
                return false;
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Tiền phải nhập định dạng số!");
            return false;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Lỗi: " + ex.getMessage());
            return false;
        }
    }

    private boolean xoaHocPhi() {
        int selectedRow = view.getTableHocPhi().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn một dòng trên bảng để xóa!");
            return false;
        }

        int xacNhan = JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn xóa học phí này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (xacNhan == JOptionPane.YES_OPTION) {
        
            int maHP = (int) view.getTableHocPhi().getValueAt(selectedRow, 0);
            if (dao.deleteHocPhi(maHP)) {
                JOptionPane.showMessageDialog(view, "Xóa thành công!");
                locDuLieu();
                view.refreshForm();
                return true;
            } else {
                JOptionPane.showMessageDialog(view, "Xóa thất bại!");
                return false;
            }
        }
        return false;
    }
}
