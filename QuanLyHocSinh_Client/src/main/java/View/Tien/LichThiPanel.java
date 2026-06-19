package View.Tien;

import Model.LichThi;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.List;
import TienIch.ButtonStyleHelper;
import TienIch.TableSortHelper;

public class LichThiPanel extends JPanel {

    // --- Khai báo Component ---
    // Phần tìm kiếm
    private JTextField txtTimKiem;
    private JButton btnTimKiem, btnXemTatCa;
    
    // Phần bảng dữ liệu
    private JTable table;
    private DefaultTableModel model;
    
    // Nút chức năng đặc biệt
    private JButton btnXuatExcel;
    
    // Form nhập liệu chi tiết
    private JTextField txtMaLT, txtMaMH, txtNgayThi, txtGioBatDau, txtGioKetThuc, txtMaPhong;
    private JComboBox<String> cboTenKyThi;
    
    // Các nút thao tác CRUD
    private JButton btnThem, btnSua, btnXoa, btnLuu, btnHuy, btnMoi;

    public LichThiPanel() {
        initComponents();
    }

    private void initComponents() {
        // Setup layout chính: Border (Bắc - Trung - Nam) có padding 10px
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. PHẦN TRÊN (NORTH): Tiêu đề + Thanh tìm kiếm
        JPanel pnlNorth = new JPanel(new BorderLayout(5, 5));
        
        // Tiêu đề to đậm
        //thêm ngày 09/04/2026
        String titleText = (Model.Auth.isHocSinh() || Model.Auth.isGiaoVien()) ? "XEM LỊCH THI" : "QUẢN LÝ LỊCH THI";
        JLabel lblTitle = new JLabel(titleText, JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(0, 102, 204));
        pnlNorth.add(lblTitle, BorderLayout.NORTH);

        // Panel chứa ô tìm kiếm + nút
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pnlSearch.setBorder(new TitledBorder("Tìm kiếm"));
        pnlSearch.add(new JLabel("Nhập Môn hoặc Kỳ thi:"));
        txtTimKiem = new JTextField(20); pnlSearch.add(txtTimKiem);
        
        btnTimKiem = new JButton("Tìm Kiếm");
        btnXemTatCa = new JButton("Xem Tất Cả");
        ButtonStyleHelper.styleButtonSearch(btnTimKiem);
        ButtonStyleHelper.styleButtonView(btnXemTatCa);
        pnlSearch.add(btnTimKiem); pnlSearch.add(btnXemTatCa);
        
        pnlNorth.add(pnlSearch, BorderLayout.CENTER);
        add(pnlNorth, BorderLayout.NORTH);

        // 2. PHẦN GIỮA (CENTER): Bảng danh sách lịch thi
        String[] cols = {"Mã LT", "Kỳ Thi", "Mã Môn", "Ngày Thi", "Bắt Đầu", "Kết Thúc", "Phòng"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        TableSortHelper.enableTableSorting(table);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setDefaultRenderer(new TienIch.CustomTableHeaderRenderer());
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 3. PHẦN DƯỚI (SOUTH): Form nhập liệu + Nút bấm
        JPanel pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.setBorder(new TitledBorder("Thông tin chi tiết"));

        // Dùng GridBagLayout để căn chỉnh các ô nhập cho thẳng hàng lối
        JPanel pnlInput = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); 
        gbc.fill = GridBagConstraints.HORIZONTAL; 

        // --- Dòng 1: Mã LT + Kỳ Thi ---
        gbc.gridx=0; gbc.gridy=0; gbc.weightx = 0; // Label không giãn
        pnlInput.add(new JLabel("Mã LT:"), gbc);
        
        gbc.gridx=1; gbc.gridy=0; gbc.weightx = 1.0; // Textfield giãn hết cỡ
        txtMaLT=new JTextField(); txtMaLT.setEditable(true); pnlInput.add(txtMaLT, gbc);
        
        gbc.gridx=2; gbc.gridy=0; gbc.weightx = 0;
        pnlInput.add(new JLabel("Kỳ Thi:"), gbc);
        
        gbc.gridx=3; gbc.gridy=0; gbc.weightx = 1.0; 
        cboTenKyThi = new JComboBox<>(new String[]{"", "Giữa Kỳ 1", "Cuối Kỳ 1", "Giữa Kỳ 2", "Cuối Kỳ 2"});
        cboTenKyThi.setSelectedIndex(0);
        pnlInput.add(cboTenKyThi, gbc);

        // --- Dòng 2: Mã Môn + Ngày Thi ---
        gbc.gridx=0; gbc.gridy=1; gbc.weightx = 0;
        pnlInput.add(new JLabel("Mã Môn (FK):"), gbc);
        
        gbc.gridx=1; gbc.gridy=1; gbc.weightx = 1.0;
        txtMaMH=new JTextField(); pnlInput.add(txtMaMH, gbc);

        gbc.gridx=2; gbc.gridy=1; gbc.weightx = 0;
        pnlInput.add(new JLabel("Ngày Thi (yyyy-mm-dd):"), gbc);
        
        gbc.gridx=3; gbc.gridy=1; gbc.weightx = 1.0;
        txtNgayThi=new JTextField(); pnlInput.add(txtNgayThi, gbc);

        // --- Dòng 3: Giờ Bắt Đầu + Kết Thúc ---
        gbc.gridx=0; gbc.gridy=2; gbc.weightx = 0;
        pnlInput.add(new JLabel("Giờ Bắt Đầu (HH:mm):"), gbc);
        
        gbc.gridx=1; gbc.gridy=2; gbc.weightx = 1.0;
        txtGioBatDau=new JTextField(); pnlInput.add(txtGioBatDau, gbc);

        gbc.gridx=2; gbc.gridy=2; gbc.weightx = 0;
        pnlInput.add(new JLabel("Giờ Kết Thúc (HH:mm):"), gbc);
        
        gbc.gridx=3; gbc.gridy=2; gbc.weightx = 1.0;
        txtGioKetThuc=new JTextField(); pnlInput.add(txtGioKetThuc, gbc);

        // --- Dòng 4: Mã Phòng ---
        gbc.gridx=0; gbc.gridy=3; gbc.weightx = 0;
        pnlInput.add(new JLabel("Mã Phòng (FK):"), gbc);
        
        gbc.gridx=1; gbc.gridy=3; gbc.weightx = 1.0;
        txtMaPhong=new JTextField(); pnlInput.add(txtMaPhong, gbc);

        pnlSouth.add(pnlInput, BorderLayout.CENTER);

        // Panel chứa các nút bấm phía dưới cùng
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
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
        btnMoi = new JButton("Làm Mới");
        ButtonStyleHelper.styleButtonView(btnMoi);
        
        Dimension sz = new Dimension(90, 35);
        btnThem.setPreferredSize(sz);
        btnSua.setPreferredSize(sz);
        btnXoa.setPreferredSize(sz);
        btnLuu.setPreferredSize(sz);
        btnHuy.setPreferredSize(sz);
        btnMoi.setPreferredSize(sz);
        
        pnlBtn.add(btnThem);
        pnlBtn.add(btnSua);
        pnlBtn.add(btnXoa);
        pnlBtn.add(btnLuu);
        pnlBtn.add(btnHuy);
        pnlBtn.add(btnMoi);
        
        // Nút Xuất Excel (Style xanh lá)
        btnXuatExcel = new JButton("Xuất Excel");
        ButtonStyleHelper.styleButtonExport(btnXuatExcel);
        btnXuatExcel.setPreferredSize(new Dimension(120, 35));
        pnlBtn.add(btnXuatExcel);
        
        pnlSouth.add(pnlBtn, BorderLayout.SOUTH);
        
        add(pnlSouth, BorderLayout.SOUTH);
        //thêm ngày 13/04/2026
        if (Model.Auth.isHocSinh() || Model.Auth.isGiaoVien()) {
            pnlSouth.setVisible(false);
        }

        setCrudButtonState(true, false, false, false, false);
    }

    // --- Getter lấy từ khóa tìm kiếm ---
    public String getKeyword() { return txtTimKiem.getText().trim(); }
    
    // --- Đóng gói dữ liệu nhập thành Object LichThi ---
    public LichThi getLichThiInput() {
        LichThi lt = new LichThi();
        if(!txtMaLT.getText().isEmpty()) lt.setMaLT(Integer.parseInt(txtMaLT.getText()));
        lt.setTenKyThi(cboTenKyThi.getSelectedItem().toString());
        lt.setMaMH(txtMaMH.getText());
        lt.setNgayThi(txtNgayThi.getText());
        lt.setGioBatDau(txtGioBatDau.getText());
        lt.setGioKetThuc(txtGioKetThuc.getText());
        lt.setMaPhong(txtMaPhong.getText());
        return lt;
    }

    // --- Hiển thị danh sách lên bảng ---
    public void setTableData(List<LichThi> list) {
        model.setRowCount(0);
        for(LichThi lt : list) {
            model.addRow(new Object[]{
                lt.getMaLT(), lt.getTenKyThi(), lt.getMaMH(), lt.getNgayThi(), 
                lt.getGioBatDau(), lt.getGioKetThuc(), lt.getMaPhong()
            });
        }
    }
    
    // --- Click vào bảng -> Đổ dữ liệu ngược lại form ---
    public void fillForm(int row) {
        if(row >= 0) {
            txtMaLT.setText(model.getValueAt(row, 0).toString());
            cboTenKyThi.setSelectedItem(model.getValueAt(row, 1).toString());
            txtMaMH.setText(model.getValueAt(row, 2).toString());
            txtNgayThi.setText(model.getValueAt(row, 3).toString());
            txtGioBatDau.setText(model.getValueAt(row, 4).toString());
            txtGioKetThuc.setText(model.getValueAt(row, 5).toString());
            txtMaPhong.setText(model.getValueAt(row, 6).toString());
        }
    }
    
    // --- Reset form trắng tinh ---
    public void clearForm() {
        txtMaLT.setText(""); txtMaMH.setText(""); txtNgayThi.setText("");
        txtGioBatDau.setText(""); txtGioKetThuc.setText(""); txtMaPhong.setText("");
    }
    
    // --- Tiện ích thông báo & Getter Table ---
    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }
    public JTable getTable() { return table; }

   // --- Gán sự kiện (Controller sẽ gọi mấy hàm này) ---
    public void addBtnTimKiemListener(ActionListener ac) { btnTimKiem.addActionListener(ac); }
    public void addBtnXemTatCaListener(ActionListener ac) { btnXemTatCa.addActionListener(ac); }
    public void addBtnThemListener(ActionListener ac) { btnThem.addActionListener(ac); }
    public void addBtnSuaListener(ActionListener ac) { btnSua.addActionListener(ac); }
    public void addBtnXoaListener(ActionListener ac) { btnXoa.addActionListener(ac); }
    public void addBtnLuuListener(ActionListener ac) { btnLuu.addActionListener(ac); }
    public void addBtnHuyListener(ActionListener ac) { btnHuy.addActionListener(ac); }
    public void addBtnMoiListener(ActionListener ac) { btnMoi.addActionListener(ac); }
    public void addTableMouseListener(MouseAdapter ad) { table.addMouseListener(ad); }
    public void addBtnXuatExcelListener(ActionListener ac) { btnXuatExcel.addActionListener(ac); }
    
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
}
