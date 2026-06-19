/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.ThuTrang;

import Model.MonHoc;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import TienIch.ButtonStyleHelper;
import TienIch.TableSortHelper;

/**
 *
 * @author Admin
 */
public class FrmMonHoc extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtMaMH, txtTenMH, txtTimKiem;
    private JButton btnXem, btnTimKiem, btnThem, btnSua, btnXoa, btnLuu, btnHuy;

    public FrmMonHoc() {
        initComponents();
    }

   private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== 1. PANEL NORTH: CHỨA TẤT CẢ PHẦN ĐẦU =====
        // Sử dụng BorderLayout để xếp 3 món (Tiêu đề - Xem - Tìm) dọc xuống
        JPanel pnlNorth = new JPanel(new BorderLayout(0, 5)); // 5 là khoảng cách giữa các phần

        // -- Món 1: Tiêu đề (Đặt ở NORTH) --
        JLabel title = new JLabel("QUẢN LÝ MÔN HỌC", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(0, 102, 204));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // Tạo khoảng hở dưới chữ
        pnlNorth.add(title, BorderLayout.NORTH);

        // -- Món 2: Khung Xem danh sách (Đặt ở CENTER) --
        JPanel pnlView = new JPanel();
        pnlView.setBorder(new TitledBorder("Xem danh sách"));
        btnXem = new JButton("Xem danh sách");
        ButtonStyleHelper.styleButtonView(btnXem);
        pnlView.add(btnXem);
        pnlNorth.add(pnlView, BorderLayout.CENTER);

        // -- Món 3: Khung Tìm kiếm (Đặt ở SOUTH) --
        JPanel pnlSearch = new JPanel();
        pnlSearch.setBorder(new TitledBorder("Tìm kiếm"));
        pnlSearch.add(new JLabel("Mã / Tên môn:"));
        txtTimKiem = new JTextField(20);
        pnlSearch.add(txtTimKiem);
        btnTimKiem = new JButton("Tìm");
        ButtonStyleHelper.styleButtonSearch(btnTimKiem);
        pnlSearch.add(btnTimKiem);
        pnlNorth.add(pnlSearch, BorderLayout.SOUTH);

        // Đưa pnlNorth (đã chứa đủ 3 món) ra giao diện chính
        add(pnlNorth, BorderLayout.NORTH);


        // ===== 2. TABLE (CENTER) =====
        model = new DefaultTableModel(new String[]{"Mã MH", "Tên môn"}, 0);
        table = new JTable(model);
        TableSortHelper.enableTableSorting(table);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setDefaultRenderer(new TienIch.CustomTableHeaderRenderer());
        add(new JScrollPane(table), BorderLayout.CENTER);


        // ===== 3. FORM INPUT (SOUTH) =====
        JPanel pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.setBorder(new TitledBorder("Cập nhật môn học"));

        JPanel pnlInput = new JPanel(new GridLayout(2, 2, 10, 5));
        pnlInput.add(new JLabel("Mã môn:"));
        txtMaMH = new JTextField();
        pnlInput.add(txtMaMH);

        pnlInput.add(new JLabel("Tên môn:"));
        txtTenMH = new JTextField();
        pnlInput.add(txtTenMH);

        pnlSouth.add(pnlInput, BorderLayout.CENTER);

        JPanel pnlBtn = new JPanel(new FlowLayout());
        btnThem = new JButton("Thêm");
        ButtonStyleHelper.styleButtonAdd(btnThem);
        btnSua = new JButton("Sửa");
        ButtonStyleHelper.styleButtonEdit(btnSua);
        btnXoa = new JButton("Xóa");
        ButtonStyleHelper.styleButtonDelete(btnXoa);
        btnLuu = new JButton("Lưu");
        ButtonStyleHelper.styleButtonSave(btnLuu);
        btnHuy = new JButton("Hủy");
        ButtonStyleHelper.styleButtonCancel(btnHuy);

        Dimension sz = new Dimension(90, 35);
        btnThem.setPreferredSize(sz);
        btnSua.setPreferredSize(sz);
        btnXoa.setPreferredSize(sz);
        btnLuu.setPreferredSize(sz);
        btnHuy.setPreferredSize(sz);

        pnlBtn.add(btnThem);
        pnlBtn.add(btnSua);
        pnlBtn.add(btnXoa);
        pnlBtn.add(btnLuu);
        pnlBtn.add(btnHuy);

        pnlSouth.add(pnlBtn, BorderLayout.SOUTH);
        add(pnlSouth, BorderLayout.SOUTH);

        setCrudButtonState(true, false, false, false, false);
    }

    // ===== DATA =====
    public MonHoc getMonHocInput() {
        return new MonHoc(
            txtMaMH.getText().trim(),
            txtTenMH.getText().trim()
        );
    }

    public String getTuKhoa() {
        return txtTimKiem.getText().trim();
    }

    public JTextField getTxtTimKiem() { return txtTimKiem; }

    public void setTableData(List<MonHoc> list) {
        model.setRowCount(0);
        for (MonHoc m : list) {
            model.addRow(new Object[]{m.getMaMH(), m.getTenMH()});
        }
    }

    public void fillForm(int row) {
        txtMaMH.setText(model.getValueAt(row, 0).toString());
        txtTenMH.setText(model.getValueAt(row, 1).toString());
        txtMaMH.setEditable(false);
    }

    public void clearForm() {
        txtMaMH.setText("");
        txtTenMH.setText("");
        txtMaMH.setEditable(true);
    }

    public JTable getTable() {
        return table;
    }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    // ===== GETTERS =====
    public JButton getBtnThem() { return btnThem; }
    public JButton getBtnSua() { return btnSua; }
    public JButton getBtnXoa() { return btnXoa; }
    public JButton getBtnLuu() { return btnLuu; }
    public JButton getBtnHuy() { return btnHuy; }
    public JButton getBtnXem() { return btnXem; }
    public JButton getBtnTimKiem() { return btnTimKiem; }

    public void setCrudButtonState(boolean them, boolean sua, boolean xoa, boolean luu, boolean huy) {
        btnThem.setEnabled(them);
        btnSua.setEnabled(sua);
        btnXoa.setEnabled(xoa);
        btnLuu.setEnabled(luu);
        btnHuy.setEnabled(huy);
    }

    // ===== EVENTS =====
    public void addBtnXemListener(ActionListener l) {
        btnXem.addActionListener(l);
    }

    public void addBtnTimKiemListener(ActionListener l) {
        btnTimKiem.addActionListener(l);
    }

    public void addBtnThemListener(ActionListener l) {
        btnThem.addActionListener(l);
    }

    public void addBtnSuaListener(ActionListener l) {
        btnSua.addActionListener(l);
    }

    public void addBtnXoaListener(ActionListener l) {
        btnXoa.addActionListener(l);
    }

    public void addBtnLuuListener(ActionListener l) {
        btnLuu.addActionListener(l);
    }

    public void addBtnHuyListener(ActionListener l) {
        btnHuy.addActionListener(l);
    }

    public void addTableMouseListener(MouseAdapter l) {
        table.addMouseListener(l);
    }
}
