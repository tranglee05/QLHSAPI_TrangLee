package View.Tien;

import Model.Diem;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.List;
import TienIch.ButtonStyleHelper;

public class QuanLyDiemPanel extends JPanel {

    // --- Khai báo các Component ---
    // Bộ lọc dữ liệu (Lớp, Môn, Học Kỳ)
    private JComboBox<String> cboLocMaLop, cboLocMon, cboLocHocKy;
    private JButton btnLocDuLieu;
    
    // Bảng hiển thị điểm
    private JTable tableDiem;
    private DefaultTableModel tableModel;
    
    // Form nhập liệu / Cập nhật điểm
    private JTextField txtMaHS, txtTenHS, txtDiem15p, txtDiem1Tiet, txtDiemGiuaKy, txtDiemCuoiKy;
    private JButton btnCapNhat;
    
    // Tìm kiếm & Tiện ích
    private JTextField txtTimKiem;
    private JButton btnTimKiem;
    private JButton btnXuatExcel;

    public QuanLyDiemPanel() {
        initComponents();
    }

    private void initComponents() {
        // Setup layout tổng thể: Border (Padding 10px)
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. PHẦN TRÊN (NORTH): Tiêu đề + Thanh công cụ (Lọc & Search)
        JPanel pnlNorth = new JPanel(new BorderLayout(5, 5));

        // Tiêu đề to đậm
        //thêm ngày 09/04/2026
        String titleText = Model.Auth.isHocSinh() ? "XEM ĐIỂM HỌC SINH" : "QUẢN LÝ ĐIỂM HỌC SINH";
        JLabel lblTitle = new JLabel(titleText, JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(0, 102, 204));
        pnlNorth.add(lblTitle, BorderLayout.NORTH);

        JPanel pnlToolBar = new JPanel(new GridLayout(2, 1, 5, 5));

        // Panel Bộ Lọc
        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        pnlFilter.setBorder(new TitledBorder("Lọc theo lớp (Mặc định)"));
        
        pnlFilter.add(new JLabel("Mã Lớp:"));
        cboLocMaLop = new JComboBox<>(); 
        pnlFilter.add(cboLocMaLop);

        pnlFilter.add(new JLabel("Môn:"));
        cboLocMon = new JComboBox<>(); 
        pnlFilter.add(cboLocMon);

        pnlFilter.add(new JLabel("Học Kỳ:"));
        cboLocHocKy = new JComboBox<>(); 
        pnlFilter.add(cboLocHocKy);

        btnLocDuLieu = new JButton("Lọc");
        ButtonStyleHelper.styleButtonFilter(btnLocDuLieu);
        pnlFilter.add(btnLocDuLieu);
        pnlToolBar.add(pnlFilter);

        // Panel Tìm Kiếm Nhanh
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        pnlSearch.setBorder(new TitledBorder("Tìm kiếm nhanh"));
        
        pnlSearch.add(new JLabel("Nhập Tên hoặc Mã HS:"));
        txtTimKiem = new JTextField(20);
        pnlSearch.add(txtTimKiem);
        
        btnTimKiem = new JButton("Tìm Kiếm");
        ButtonStyleHelper.styleButtonSearch(btnTimKiem);

        pnlSearch.add(btnTimKiem);
        
        pnlToolBar.add(pnlSearch);
        
        pnlNorth.add(pnlToolBar, BorderLayout.CENTER);
        this.add(pnlNorth, BorderLayout.NORTH);

        // 2. PHẦN GIỮA (CENTER): Bảng Điểm
        String[] columnNames = {"Mã HS", "Họ Tên", "Môn", "HK", "Điểm 15p", "1 Tiết", "Giữa Kỳ", "Cuối Kỳ", "Tổng Kết"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tableDiem = new JTable(tableModel);
        tableDiem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableDiem.setRowHeight(25);
        tableDiem.getTableHeader().setDefaultRenderer(new TienIch.CustomTableHeaderRenderer());
        this.add(new JScrollPane(tableDiem), BorderLayout.CENTER);

        // 3. PHẦN DƯỚI (SOUTH): Form Cập Nhật + Nút Bấm
        JPanel pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.setBorder(new TitledBorder("Cập nhật điểm"));
        
        // Dùng GridBagLayout để căn chỉnh các ô nhập điểm cho thẳng hàng
        JPanel pnlInput = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); gbc.fill = GridBagConstraints.HORIZONTAL;

        // Hàng 1: Thông tin HS (Readonly)
        gbc.gridx=0; gbc.gridy=0; pnlInput.add(new JLabel("Mã HS:"), gbc);
        gbc.gridx=1; gbc.gridy=0; txtMaHS=new JTextField(12); txtMaHS.setEditable(false); pnlInput.add(txtMaHS, gbc);
        gbc.gridx=2; gbc.gridy=0; pnlInput.add(new JLabel("Họ Tên:"), gbc);
        gbc.gridx=3; gbc.gridy=0; txtTenHS=new JTextField(12); txtTenHS.setEditable(false); pnlInput.add(txtTenHS, gbc);

        // Hàng 2: Điểm 15p & 1 Tiết
        gbc.gridx=0; gbc.gridy=1; pnlInput.add(new JLabel("Điểm 15p:"), gbc);
        gbc.gridx=1; gbc.gridy=1; txtDiem15p=new JTextField(); pnlInput.add(txtDiem15p, gbc);
        
        gbc.gridx=2; gbc.gridy=1; pnlInput.add(new JLabel("Điểm 1 Tiết:"), gbc);
        gbc.gridx=3; gbc.gridy=1; txtDiem1Tiet=new JTextField(); pnlInput.add(txtDiem1Tiet, gbc);

        // Hàng 3: Giữa Kỳ & Cuối Kỳ
        gbc.gridx=0; gbc.gridy=2; pnlInput.add(new JLabel("Điểm Giữa Kỳ:"), gbc);
        gbc.gridx=1; gbc.gridy=2; txtDiemGiuaKy=new JTextField(); pnlInput.add(txtDiemGiuaKy, gbc);
        
        gbc.gridx=2; gbc.gridy=2; pnlInput.add(new JLabel("Điểm Cuối Kỳ:"), gbc);
        gbc.gridx=3; gbc.gridy=2; txtDiemCuoiKy=new JTextField(); pnlInput.add(txtDiemCuoiKy, gbc);

        pnlSouth.add(pnlInput, BorderLayout.CENTER);
        
        // Panel chứa nút Lưu và Xuất Excel
        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnCapNhat = new JButton("Lưu / Cập Nhật Điểm");
        ButtonStyleHelper.styleButtonSave(btnCapNhat);
        btnCapNhat.setPreferredSize(new Dimension(200, 40));
        pnlButton.add(btnCapNhat);
        
        // Nút Xuất Excel (Style xanh lá)
        btnXuatExcel = new JButton("Xuất Excel");
        ButtonStyleHelper.styleButtonExport(btnXuatExcel);

        btnXuatExcel.setPreferredSize(new Dimension(130, 40));
        pnlButton.add(btnXuatExcel);
        
        pnlSouth.add(pnlButton, BorderLayout.SOUTH);
        this.add(pnlSouth, BorderLayout.SOUTH);
        //Thêm ngày 09/04/2026
        if (Model.Auth.isHocSinh()) {
            pnlSearch.setVisible(false);
            pnlSouth.setVisible(false);
        }
    }

    // --- Các hàm Getter dữ liệu từ Form (Cho Controller gọi) ---
    public String getMaLopFilter() { 
        return cboLocMaLop.getSelectedItem() != null ? cboLocMaLop.getSelectedItem().toString() : ""; 
    }
    public String getMaMonFilter() { 
        return cboLocMon.getSelectedItem() != null ? cboLocMon.getSelectedItem().toString() : ""; 
    }
    public int getHocKyFilter() { 
        try {
            return cboLocHocKy.getSelectedItem() != null ? Integer.parseInt(cboLocHocKy.getSelectedItem().toString()) : 1;
        } catch (Exception e) {
            return 1;
        }
    }
    public String getTuKhoaTimKiem() { return txtTimKiem.getText().trim(); }

    // --- Các hàm Setter dữ liệu cho ComboBox ---
    public void setMaLopData(List<String> lops) {
        cboLocMaLop.removeAllItems();
        for (String lop : lops) {
            cboLocMaLop.addItem(lop);
        }
    }

    public void setMonHocData(List<String> mons) {
        cboLocMon.removeAllItems();
        for (String mon : mons) {
            cboLocMon.addItem(mon);
        }
    }

    public void setHocKyData(List<Integer> hks) {
        cboLocHocKy.removeAllItems();
        for (Integer hk : hks) {
            cboLocHocKy.addItem(hk.toString());
        }
    }

    // Đóng gói dữ liệu nhập thành Object Diem
    public Diem getDiemInput() {
        Diem d = new Diem();
        d.setMaHS(txtMaHS.getText());
        d.setMaMH(getMaMonFilter()); // Lấy môn đang chọn ở filter
        d.setHocKy(getHocKyFilter()); // Lấy học kỳ đang chọn ở filter
        try {
            // Parse điểm, nếu lỗi format thì return null để Controller xử lý
            d.setDiem15p(Double.parseDouble(txtDiem15p.getText()));
            d.setDiem1Tiet(Double.parseDouble(txtDiem1Tiet.getText()));
            d.setDiemGiuaKy(Double.parseDouble(txtDiemGiuaKy.getText()));
            d.setDiemCuoiKy(Double.parseDouble(txtDiemCuoiKy.getText()));
        } catch (Exception e) { return null; }
        return d;
    }

    // --- Hàm hiển thị dữ liệu (Setter) ---
    // Đổ list điểm lên bảng
    public void setTableData(List<Diem> list) {
        tableModel.setRowCount(0);
        for (Diem d : list) {
            // Làm tròn điểm trung bình 2 chữ số thập phân
            double dtb = Math.round(d.getDiemTongKet() * 100.0) / 100.0;
            tableModel.addRow(new Object[]{
                d.getMaHS(), 
                d.getTenHS(), 
                d.getTenMH() != null ? d.getTenMH() : d.getMaMH(), 
                d.getHocKy(),
                d.getDiem15p(), 
                d.getDiem1Tiet(),   
                d.getDiemGiuaKy(), 
                d.getDiemCuoiKy(),   
                dtb               
            });
        }
    }

    // Click vào dòng -> Đổ ngược dữ liệu vào form nhập
    public void fillFormInput(int row) {
        if (row >= 0) {
            txtMaHS.setText(tableModel.getValueAt(row, 0).toString());
            txtTenHS.setText(tableModel.getValueAt(row, 1).toString());
            
            txtDiem15p.setText(tableModel.getValueAt(row, 4).toString());
            txtDiem1Tiet.setText(tableModel.getValueAt(row, 5).toString());
            txtDiemGiuaKy.setText(tableModel.getValueAt(row, 6).toString());
            txtDiemCuoiKy.setText(tableModel.getValueAt(row, 7).toString());
        }
    }

    // --- Tiện ích & Gán sự kiện ---
    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }
    public JTable getTable() { return tableDiem; }

    public void addBtnXemListener(ActionListener action) { btnLocDuLieu.addActionListener(action); }
    public void addBtnTimKiemListener(ActionListener action) { btnTimKiem.addActionListener(action); } 
    public void addBtnCapNhatListener(ActionListener action) { btnCapNhat.addActionListener(action); }
    public void addTableMouseListener(MouseAdapter adapter) { tableDiem.addMouseListener(adapter); }
    public void addBtnXuatExcelListener(ActionListener ac) { btnXuatExcel.addActionListener(ac); }

    public DefaultTableModel getTableModel() {
        return (DefaultTableModel) tableDiem.getModel();
    }
    public JButton getBtnCapNhat() {
        return btnCapNhat;
    }
}