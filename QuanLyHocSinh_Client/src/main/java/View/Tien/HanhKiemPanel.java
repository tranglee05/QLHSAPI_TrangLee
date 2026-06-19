package View.Tien;

import Model.HanhKiem;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.List;
import TienIch.ButtonStyleHelper;
import TienIch.TableSortHelper;

public class HanhKiemPanel extends JPanel {
    
    // --- Khai báo Component ---
    private JButton btnXuatExcel;
    // Bộ lọc và tìm kiếm
    private JTextField txtTimKiem; 
    private JComboBox<String> cboLocMaLop, cboLocNamHoc, cboHocKy, cboXepLoai;
    
    // Bảng hiển thị
    private JTable table;
    private DefaultTableModel model;
    
    // Nút chức năng
    private JButton btnXem, btnTimKiem, btnThem, btnSua, btnXoa, btnLuu, btnHuy, btnMoi;
    // Form nhập liệu chi tiết
    private JTextField txtMaHS, txtTenHS;
    private JTextArea txtNhanXet;

    public HanhKiemPanel() {
        initComponents();
    }

    private void initComponents() {
        // Setup layout chính: Kiểu Border (Bắc - Trung - Nam)
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. PHẦN TRÊN (NORTH): Tiêu đề + Bộ lọc + Tìm kiếm
        JPanel pnlNorth = new JPanel(new BorderLayout(5, 5));
        
        // Tiêu đề to đùng
        //thêm ngày 09/04/2026
        String titleText = Model.Auth.isHocSinh() ? "XEM HẠNH KIỂM" : "QUẢN LÝ HẠNH KIỂM";
        JLabel lblTitle = new JLabel(titleText, JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(0, 102, 204));
        pnlNorth.add(lblTitle, BorderLayout.NORTH);

        JPanel pnlTools = new JPanel(new GridLayout(2, 1, 5, 5));

        // Panel Lọc (Mã lớp, Năm học, Học kỳ)
        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        pnlFilter.setBorder(new TitledBorder("Lọc theo lớp (Mặc định)"));
        
        pnlFilter.add(new JLabel("Mã Lớp:"));
        cboLocMaLop = new JComboBox<>();
        cboLocMaLop.addItem("");
        cboLocMaLop.setSelectedIndex(0);
        pnlFilter.add(cboLocMaLop); 
        
        pnlFilter.add(new JLabel("Năm Học:"));
        cboLocNamHoc = new JComboBox<>();
        cboLocNamHoc.addItem("");
        cboLocNamHoc.setSelectedIndex(0);
        pnlFilter.add(cboLocNamHoc);
        
        pnlFilter.add(new JLabel("Học Kỳ:"));
        cboHocKy = new JComboBox<>(new String[]{"", "1", "2"}); 
        cboHocKy.setSelectedIndex(0);
        pnlFilter.add(cboHocKy);
        
        btnXem = new JButton("Lọc Danh Sách");
        ButtonStyleHelper.styleButtonFilter(btnXem);
        pnlFilter.add(btnXem);
        pnlTools.add(pnlFilter);

        // Panel Tìm kiếm nhanh
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        pnlSearch.setBorder(new TitledBorder("Tìm kiếm nhanh"));
        
        pnlSearch.add(new JLabel("Nhập Tên hoặc Mã HS:"));
        txtTimKiem = new JTextField(20); pnlSearch.add(txtTimKiem);
        
        btnTimKiem = new JButton("Tìm Kiếm");
        ButtonStyleHelper.styleButtonSearch(btnTimKiem);
        pnlSearch.add(btnTimKiem);
        pnlTools.add(pnlSearch);

        pnlNorth.add(pnlTools, BorderLayout.CENTER);
        add(pnlNorth, BorderLayout.NORTH);
        
        // Nút Xuất Excel (Style màu xanh lá dễ nhìn)
        btnXuatExcel = new JButton("Xuất Excel");
        ButtonStyleHelper.styleButtonExport(btnXuatExcel);
        btnXuatExcel.setPreferredSize(new Dimension(120, 35));

        // 2. PHẦN GIỮA (CENTER): Bảng dữ liệu
        String[] cols = {"Mã HS", "Tên HS", "Lớp", "Năm Học", "Học Kỳ", "Xếp Loại", "Nhận Xét"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        TableSortHelper.enableTableSorting(table);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setDefaultRenderer(new TienIch.CustomTableHeaderRenderer());
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 3. PHẦN DƯỚI (SOUTH): Form nhập liệu + Nút tác vụ
        JPanel pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.setBorder(new TitledBorder("Cập nhật Hạnh Kiểm"));
        
        // GridBagLayout để căn chỉnh form nhập liệu cho đẹp
        JPanel pnlInput = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); gbc.fill = GridBagConstraints.HORIZONTAL;

        // Dòng 1: Mã HS + Tên HS
        gbc.gridx=0; gbc.gridy=0; pnlInput.add(new JLabel("Mã HS:"), gbc);
        gbc.gridx=1; gbc.gridy=0; txtMaHS = new JTextField(15); txtMaHS.setEditable(false); pnlInput.add(txtMaHS, gbc);
        
        gbc.gridx=2; gbc.gridy=0; pnlInput.add(new JLabel("Tên HS:"), gbc);
        gbc.gridx=3; gbc.gridy=0; txtTenHS = new JTextField(15); txtTenHS.setEditable(false); pnlInput.add(txtTenHS, gbc);
        
        // Dòng 2: Xếp loại
        gbc.gridx=0; gbc.gridy=1; pnlInput.add(new JLabel("Xếp Loại:"), gbc);
        gbc.gridx=1; gbc.gridy=1; 
        cboXepLoai = new JComboBox<>(new String[]{"", "Tốt", "Khá", "Trung bình", "Yếu"}); 
        pnlInput.add(cboXepLoai, gbc);
        
        // Dòng 3: Nhận xét mở rộng
        gbc.gridx=0; gbc.gridy=2; pnlInput.add(new JLabel("Nhận Xét:"), gbc);
        gbc.gridx=1; gbc.gridy=2; gbc.gridwidth=3;
        txtNhanXet = new JTextArea(4, 40); // Tăng kích thước khu vực nhận xét
        txtNhanXet.setLineWrap(true);
        txtNhanXet.setWrapStyleWord(true);
        pnlInput.add(new JScrollPane(txtNhanXet), gbc);

        pnlSouth.add(pnlInput, BorderLayout.CENTER);

        // Panel chứa các nút bấm chuẩn
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.CENTER));
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
        
        // Nút Xuất Excel (Style xanh lá)
        btnXuatExcel = new JButton("Xuất Excel");
        ButtonStyleHelper.styleButtonExport(btnXuatExcel);
        btnXuatExcel.setPreferredSize(new Dimension(120, 35));
        pnlBtn.add(btnXuatExcel);
        
        pnlSouth.add(pnlBtn, BorderLayout.SOUTH);
        
        add(pnlSouth, BorderLayout.SOUTH);
        //thêm ngày 09/04/2026
        if (Model.Auth.isHocSinh()) {
            pnlSearch.setVisible(false);
            pnlSouth.setVisible(false);
        }

        setCrudButtonState(true, false, false, false, false);
    }

    public String getMaLopFilter() {
        return cboLocMaLop.getSelectedItem() != null ? cboLocMaLop.getSelectedItem().toString() : ""; 
    }
    public String getNamHocFilter() { 
        return cboLocNamHoc.getSelectedItem() != null ? cboLocNamHoc.getSelectedItem().toString() : ""; 
    }
    public int getHocKyFilter() { 
        try {
            return Integer.parseInt(cboHocKy.getSelectedItem().toString());
        } catch(Exception e) { return 1; }
    }
    public String getTuKhoaTimKiem() { return txtTimKiem.getText().trim(); } 
    
    // --- Các hàm Setter dữ liệu cho ComboBox ---
    public void setMaLopData(List<String> lops) {
        cboLocMaLop.removeAllItems();
        for (String lop : lops) {
            cboLocMaLop.addItem(lop);
        }
    }

    public void setNamHocData(List<String> namHocs) {
        cboLocNamHoc.removeAllItems();
        for (String n : namHocs) {
            cboLocNamHoc.addItem(n);
        }
    }

    public HanhKiem getHanhKiemInput() {
        HanhKiem hk = new HanhKiem();
        hk.setMaHS(txtMaHS.getText());
        hk.setNamHoc(getNamHocFilter());
        hk.setHocKy(getHocKyFilter());
        hk.setXepLoai(cboXepLoai.getSelectedItem().toString());
        hk.setNhanXet(txtNhanXet.getText());
        return hk;
    }

    public void setTableData(List<HanhKiem> list) {
        model.setRowCount(0);
        for (HanhKiem hk : list) {
            model.addRow(new Object[]{
                hk.getMaHS(), hk.getTenHS(), hk.getMaLop(), hk.getNamHoc(), hk.getHocKy(), hk.getXepLoai(), hk.getNhanXet()
            });
        }
    }
    
    // Khi click vào bảng -> Đổ dữ liệu ngược lại form nhập
    public void fillFormInput(int row) {
        if(row >= 0) {
            txtMaHS.setText(model.getValueAt(row, 0).toString());
            txtTenHS.setText(model.getValueAt(row, 1).toString());
            
            String namHoc = model.getValueAt(row, 3).toString();
            String hocKy = model.getValueAt(row, 4).toString();
            cboLocNamHoc.setSelectedItem(namHoc);
            cboHocKy.setSelectedItem(hocKy);
            
            String xepLoai = model.getValueAt(row, 5).toString();
            if(xepLoai != null && !xepLoai.isEmpty()) {
                cboXepLoai.setSelectedItem(xepLoai);
            } else {
                cboXepLoai.setSelectedIndex(0);
            }
            Object nx = model.getValueAt(row, 6);
            txtNhanXet.setText(nx != null ? nx.toString() : "");
        }
    }
    
    public void clearForm() {
        txtMaHS.setText("");
        txtTenHS.setText("");
        txtNhanXet.setText("");
        
        // Set ComboBox về giá trị mặc định (index 0)
        if (cboLocNamHoc.getItemCount() > 0) {
            cboLocNamHoc.setSelectedIndex(0);
        }
        if (cboHocKy.getItemCount() > 0) {
            cboHocKy.setSelectedIndex(0);
        }
        if (cboXepLoai.getItemCount() > 0) {
            cboXepLoai.setSelectedIndex(0);
        }
        
        // Bỏ chọn bảng
        if (table != null) {
            table.clearSelection();
        }
    }
    
    // Tiện ích hiện thông báo
    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }
    public JTable getTable() { return table; }

    // --- GÁN SỰ KIỆN (Controller sẽ dùng các hàm này) ---
    public void addBtnXemListener(ActionListener log) { btnXem.addActionListener(log); }
    public void addBtnTimKiemListener(ActionListener log) { btnTimKiem.addActionListener(log); }
    public void addBtnThemListener(ActionListener log) { btnThem.addActionListener(log); }
    public void addBtnSuaListener(ActionListener log) { btnSua.addActionListener(log); }
    public void addBtnXoaListener(ActionListener log) { btnXoa.addActionListener(log); }
    public void addBtnLuuListener(ActionListener log) { btnLuu.addActionListener(log); }
    public void addBtnHuyListener(ActionListener log) { btnHuy.addActionListener(log); }
    public void addBtnMoiListener(ActionListener log) { btnMoi.addActionListener(log); }
    public void addTableMouseListener(MouseAdapter log) { table.addMouseListener(log); }
    public void addBtnXuatExcelListener(ActionListener log) { btnXuatExcel.addActionListener(log); }
    
    public JButton getBtnThem() { return btnThem; }
    public JButton getBtnSua() { return btnSua; }
    public JButton getBtnXoa() { return btnXoa; }
    public JButton getBtnLuu() { return btnLuu; }
    public JButton getBtnHuy() { return btnHuy; }
    public void setCrudButtonState(boolean them, boolean sua, boolean xoa, boolean luu, boolean huy) {
        btnThem.setEnabled(them);
        btnSua.setEnabled(sua);
        btnXoa.setEnabled(xoa);
        btnLuu.setEnabled(luu);
        btnHuy.setEnabled(huy);
    }

    public void hideButtonForStudent() {
        btnLuu.setVisible(false);
        btnXoa.setVisible(false);
    }
}
