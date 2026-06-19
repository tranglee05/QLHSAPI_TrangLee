/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.HaTrang;

import Controller.HaTrang.Hocphicontroller;
import Model.Hocphi;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.border.TitledBorder;
import TienIch.ButtonStyleHelper;
import TienIch.TableSortHelper;
import Dao.LopDAO;

public class QuanLyHocPhiPanel extends JPanel {
    private JComboBox<String> cboMaLop, cboNamHoc;
    private JComboBox<String> cboHocKy;
    private JButton btnLoc, btnThem, btnSua, btnXoa, btnLuu, btnHuy;
    private JTable tableHocPhi;
    private DefaultTableModel tableModel;
    private JTextField txtMaHS, txtTongTien, txtMienGiam, txtPhaiDong;
    private JComboBox<String> cboTrangThai;
    
    // Thêm các component UI mới cho học sinh
    private JLabel lblTongTienVal, lblMienGiamVal, lblPhaiDongVal, lblTrangThaiBadge;
    private JButton btnThanhToanHocSinh;

    public QuanLyHocPhiPanel() {
        initComponents();
        
        // Khởi tạo Controller (Controller sẽ gán sự kiện cho các nút)
        // Controller sẽ tự động tải dữ liệu sau khi khởi tạo xong
        Hocphicontroller controller = new Hocphicontroller(this);
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- PANEL NORTH (TITLE & FILTER) ---
        JPanel pnlNorth = new JPanel(new BorderLayout(10, 10));
        pnlNorth.setOpaque(false);

        //thêm ngày 09/04/2026
        String titleText = Model.Auth.isHocSinh() ? "HỌC PHÍ" : "HỆ THỐNG QUẢN LÝ HỌC PHÍ";
        JLabel lblTitle = new JLabel(titleText, JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(41, 128, 185));
        pnlNorth.add(lblTitle, BorderLayout.NORTH);

        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        pnlFilter.setBackground(Color.WHITE);
        pnlFilter.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));

        pnlFilter.add(new JLabel("Mã Lớp:"));
        cboMaLop = new JComboBox<>();
        try {
            loadLopComboBox();
        } catch (Exception e) {
            System.out.println("⚠️ Lỗi tải lớp: " + e.getMessage());
            e.printStackTrace();
        }
        cboMaLop.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                Object selected = cboMaLop.getSelectedItem();
                if (selected != null && !selected.toString().equals("(Không có dữ liệu)")) {
                    loadNamHocByMaLop(selected.toString());
                }
            }
        });
        pnlFilter.add(cboMaLop);

        pnlFilter.add(new JLabel("Học Kỳ:"));
        cboHocKy = new JComboBox<>(new String[]{"", "1", "2"});
        cboHocKy.setSelectedIndex(0);
        pnlFilter.add(cboHocKy);

        pnlFilter.add(new JLabel("Năm Học:"));
        cboNamHoc = new JComboBox<>();
        try {
            loadNamHocComboBox();
        } catch (Exception e) {
            System.out.println("⚠️ Lỗi tải năm học: " + e.getMessage());
            e.printStackTrace();
        }
        pnlFilter.add(cboNamHoc);

        btnLoc = new JButton("Xem Danh Sách");
        ButtonStyleHelper.styleButtonView(btnLoc);
        btnLoc.setBackground(new Color(52, 152, 219));
        // btnLoc.setForeground(Color.WHITE); // Bạn có thể thêm màu chữ nếu muốn
        btnLoc.setFocusPainted(false);
        pnlFilter.add(btnLoc);

        pnlNorth.add(pnlFilter, BorderLayout.SOUTH);
        add(pnlNorth, BorderLayout.NORTH);

        // --- TABLE ---
        String[] cols = {"ID", "Mã HS", "Mã Lớp", "Kỳ", "Năm học", "Tổng tiền", "Miễn giảm", "Phải đóng", "Trạng thái"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableHocPhi = new JTable(tableModel);
        TableSortHelper.enableTableSorting(tableHocPhi);
        tableHocPhi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableHocPhi.setRowHeight(30);
        tableHocPhi.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableHocPhi.getTableHeader().setDefaultRenderer(new TienIch.CustomTableHeaderRenderer());
        
        // Căn giữa dữ liệu bảng
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for(int i=0; i<tableHocPhi.getColumnCount(); i++) {
            tableHocPhi.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(tableHocPhi);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách học phí"));
        add(scrollPane, BorderLayout.CENTER);

        // --- PANEL SOUTH (INPUT & BUTTONS) ---
        JPanel pnlSouth = new JPanel(new BorderLayout(10, 10));
        pnlSouth.setOpaque(false);

        JPanel pnlInput = new JPanel(new GridLayout(3, 4, 15, 10));
        pnlInput.setBackground(Color.WHITE);
        pnlInput.setBorder(BorderFactory.createCompoundBorder(
            new TitledBorder("Thông tin chi tiết"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        pnlInput.add(new JLabel("Mã Học Sinh:"));
        txtMaHS = new JTextField();
        pnlInput.add(txtMaHS);

        pnlInput.add(new JLabel("Tổng Tiền:"));
        txtTongTien = new JTextField();
        pnlInput.add(txtTongTien);

        pnlInput.add(new JLabel("Miễn Giảm:"));
        txtMienGiam = new JTextField();
        pnlInput.add(txtMienGiam);

        pnlInput.add(new JLabel("Phải Đóng:"));
        txtPhaiDong = new JTextField();
        txtPhaiDong.setEditable(false);
        txtPhaiDong.setBackground(new Color(245, 245, 245));
        pnlInput.add(txtPhaiDong);

        pnlInput.add(new JLabel("Trạng Thái:"));
        cboTrangThai = new JComboBox<>(new String[]{"Chưa đóng", "Đã đóng", "Bảo lưu"});
        pnlInput.add(cboTrangThai);

        pnlSouth.add(pnlInput, BorderLayout.CENTER);

        // Buttons
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlButtons.setOpaque(false);

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

        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnLuu);
        pnlButtons.add(btnHuy);

        pnlSouth.add(pnlButtons, BorderLayout.SOUTH);
        add(pnlSouth, BorderLayout.SOUTH);
        //thêm nga 09/04/2026
        if (Model.Auth.isHocSinh()) {
            pnlSouth.removeAll(); // Xóa giao diện mặc định
            
            // Thiết kế giao diện Card mới
            JPanel pnlCard = new JPanel(new GridLayout(1, 3, 20, 0));
            pnlCard.setOpaque(false);
            pnlCard.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

            lblTongTienVal = new JLabel("0 đ", JLabel.CENTER);
            lblTongTienVal.setFont(new Font("Segoe UI", Font.BOLD, 22));
            lblTongTienVal.setForeground(new Color(41, 128, 185)); // Xanh dương
            pnlCard.add(createInfoCard("TỔNG HỌC PHÍ", lblTongTienVal, new Color(236, 240, 241)));

            lblMienGiamVal = new JLabel("0 đ", JLabel.CENTER);
            lblMienGiamVal.setFont(new Font("Segoe UI", Font.BOLD, 22));
            lblMienGiamVal.setForeground(new Color(39, 174, 96)); // Xanh lá
            pnlCard.add(createInfoCard("ĐƯỢC MIỄN GIẢM", lblMienGiamVal, new Color(233, 247, 239)));

            lblPhaiDongVal = new JLabel("0 đ", JLabel.CENTER);
            lblPhaiDongVal.setFont(new Font("Segoe UI", Font.BOLD, 26));
            lblPhaiDongVal.setForeground(new Color(192, 57, 43)); // Đỏ nổi bật
            pnlCard.add(createInfoCard("THỰC TẾ PHẢI ĐÓNG", lblPhaiDongVal, new Color(250, 229, 211)));

            pnlSouth.add(pnlCard, BorderLayout.CENTER);

            // Nhóm Trạng thái & Nút Thanh Toán
            JPanel pnlAction = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
            pnlAction.setOpaque(false);

            lblTrangThaiBadge = new JLabel("CHƯA CHỌN KỲ", JLabel.CENTER);
            lblTrangThaiBadge.setFont(new Font("Segoe UI", Font.BOLD, 18));
            lblTrangThaiBadge.setOpaque(true);
            lblTrangThaiBadge.setBackground(Color.LIGHT_GRAY);
            lblTrangThaiBadge.setForeground(Color.WHITE);
            lblTrangThaiBadge.setPreferredSize(new Dimension(200, 45));
            pnlAction.add(lblTrangThaiBadge);

            btnThanhToanHocSinh = new JButton("Thanh Toán Học Phí");
            btnThanhToanHocSinh.setFont(new Font("Segoe UI", Font.BOLD, 16));
            btnThanhToanHocSinh.setBackground(new Color(46, 204, 113));
            btnThanhToanHocSinh.setForeground(Color.BLACK);
            btnThanhToanHocSinh.setOpaque(true);
            btnThanhToanHocSinh.setFocusPainted(false);
            btnThanhToanHocSinh.setPreferredSize(new Dimension(220, 45));
            btnThanhToanHocSinh.setVisible(false); // Ẩn khi chưa có dòng nào được chọn
            pnlAction.add(btnThanhToanHocSinh);

            pnlSouth.add(pnlAction, BorderLayout.SOUTH);

            // Sự kiện click Nút Thanh toán
            btnThanhToanHocSinh.addActionListener(e -> {
                JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Mã QR Thanh Toán", true);
                dialog.setSize(400, 450);
                dialog.setLocationRelativeTo(this);
                dialog.setLayout(new BorderLayout());
                dialog.getContentPane().setBackground(Color.WHITE);
                
                JLabel lblTitleQR = new JLabel("Quét Mã QR Để Thanh Toán", JLabel.CENTER);
                lblTitleQR.setFont(new Font("Segoe UI", Font.BOLD, 18));
                lblTitleQR.setForeground(new Color(41, 128, 185));
                lblTitleQR.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
                
                JLabel lblQR = new JLabel();
                lblQR.setHorizontalAlignment(JLabel.CENTER);
                try {
                    // Đọc trực tiếp từ đường dẫn file
                    ImageIcon icon = new ImageIcon("src/main/java/TienIch/thanhToanHocPhi.png");
                    // Tùy chỉnh kích thước ảnh QR (ví dụ 250x250)
                    Image img = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
                    lblQR.setIcon(new ImageIcon(img));
                } catch (Exception ex) {
                    lblQR.setText("Không thể tải ảnh QR");
                }
                
                String maHsHienTai = txtMaHS.getText().isEmpty() ? Model.Auth.maNguoiDung : txtMaHS.getText();
                JLabel lblGhiChu = new JLabel("Nội dung chuyển khoản: " + maHsHienTai + " - Nop hoc phi", JLabel.CENTER);
                lblGhiChu.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                lblGhiChu.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
                
                dialog.add(lblTitleQR, BorderLayout.NORTH);
                dialog.add(lblQR, BorderLayout.CENTER);
                dialog.add(lblGhiChu, BorderLayout.SOUTH);
                
                dialog.setVisible(true);
            });
        }

        // Event click bảng
        tableHocPhi.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int r = tableHocPhi.getSelectedRow();
                if (r >= 0) {
                    // Vẫn update giá trị gốc cho Admin/Giáo viên (hoặc lưu ngầm)
                    txtMaHS.setText(tableModel.getValueAt(r, 1).toString());
                    txtMaHS.setEditable(false);
                    txtTongTien.setText(tableModel.getValueAt(r, 5).toString());
                    txtMienGiam.setText(tableModel.getValueAt(r, 6).toString());
                    txtPhaiDong.setText(tableModel.getValueAt(r, 7).toString());

                    String maLop = tableModel.getValueAt(r, 2).toString();
                    String hocKy = tableModel.getValueAt(r, 3).toString();
                    String namHoc = tableModel.getValueAt(r, 4).toString();

                    if (containsItem(cboMaLop, maLop)) {
                        cboMaLop.setSelectedItem(maLop);
                    }
                    loadNamHocByMaLop(maLop);
                    if (containsItem(cboNamHoc, namHoc)) {
                        cboNamHoc.setSelectedItem(namHoc);
                    } else {
                        cboNamHoc.addItem(namHoc);
                        cboNamHoc.setSelectedItem(namHoc);
                    }
                    cboHocKy.setSelectedItem(hocKy);
                    
                    Object trangThaiValue = tableModel.getValueAt(r, 8);
                    String trangThai = (trangThaiValue == null || trangThaiValue.toString().trim().isEmpty()) 
                                     ? "Chưa đóng" 
                                     : trangThaiValue.toString();
                    cboTrangThai.setSelectedItem(trangThai);
                    
                    // Cập nhật giao diện Card nếu đang là học sinh
                    if (Model.Auth.isHocSinh()) {
                        if (lblTongTienVal != null) {
                            try {
                                long tt = Long.parseLong(txtTongTien.getText().isEmpty() ? "0" : txtTongTien.getText());
                                lblTongTienVal.setText(String.format("%,d đ", tt));
                            } catch (Exception ex) { lblTongTienVal.setText(txtTongTien.getText() + " đ"); }
                        }
                        if (lblMienGiamVal != null) {
                            try {
                                long mg = Long.parseLong(txtMienGiam.getText().isEmpty() ? "0" : txtMienGiam.getText());
                                lblMienGiamVal.setText(String.format("%,d đ", mg));
                            } catch (Exception ex) { lblMienGiamVal.setText(txtMienGiam.getText() + " đ"); }
                        }
                        if (lblPhaiDongVal != null) {
                            try {
                                long pd = Long.parseLong(txtPhaiDong.getText().isEmpty() ? "0" : txtPhaiDong.getText());
                                lblPhaiDongVal.setText(String.format("%,d đ", pd));
                            } catch (Exception ex) { lblPhaiDongVal.setText(txtPhaiDong.getText() + " đ"); }
                        }
                        if (lblTrangThaiBadge != null) {
                            lblTrangThaiBadge.setText(trangThai.toUpperCase());
                            if (trangThai.equalsIgnoreCase("Đã đóng") || trangThai.equalsIgnoreCase("Đã thanh toán") || trangThai.equalsIgnoreCase("Bảo lưu")) {
                                if (trangThai.equalsIgnoreCase("Bảo lưu")) {
                                    lblTrangThaiBadge.setBackground(new Color(243, 156, 18)); // Vàng cam cho Bảo lưu
                                } else {
                                    lblTrangThaiBadge.setBackground(new Color(39, 174, 96)); // Xanh lá cho Đã đóng
                                }
                                if (btnThanhToanHocSinh != null) btnThanhToanHocSinh.setVisible(false);
                            } else {
                                lblTrangThaiBadge.setBackground(new Color(231, 76, 60)); // Đỏ cho Chưa đóng
                                if (btnThanhToanHocSinh != null) btnThanhToanHocSinh.setVisible(true);
                            }
                        }
                    }
                }
            }
        });

        setCrudButtonState(true, false, false, false, false);
    }

    private boolean containsItem(JComboBox<String> combo, String value) {
        if (value == null) {
            return false;
        }
        for (int i = 0; i < combo.getItemCount(); i++) {
            String item = combo.getItemAt(i);
            if (value.equals(item)) {
                return true;
            }
        }
        return false;
    }
    
    // Hàm Helper tạo giao diện từng Card
    private JPanel createInfoCard(String title, JLabel lblValue, Color bgColor) {
        JPanel pnl = new JPanel(new BorderLayout(5, 5));
        pnl.setBackground(bgColor);
        pnl.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));
        
        JLabel lblTitle = new JLabel(title, JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(new Color(100, 100, 100));
        
        pnl.add(lblTitle, BorderLayout.NORTH);
        pnl.add(lblValue, BorderLayout.CENTER);
        return pnl;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setPreferredSize(new Dimension(120, 35));
        return btn;
    }

    private void loadLopComboBox() {
        System.out.println("\n=== Bắt đầu loadLopComboBox ===");
        try {
            LopDAO lopDAO = new LopDAO();
            System.out.println("✓ Tạo LopDAO thành công");
            
            var lopList = lopDAO.getAllLop();
            System.out.println("✓ Gọi getAllLop() thành công, kết quả: " + (lopList == null ? "null" : lopList.size() + " lớp"));
            
            cboMaLop.removeAllItems();
            System.out.println("✓ Clear combo box");
            
            if (lopList == null || lopList.isEmpty()) {
                System.out.println("⚠️ CẢNH BÁO: Không có lớp nào từ database!");
                cboMaLop.addItem("(Không có dữ liệu)");
                return;
            }
            
            int count = 0;
            for (var lop : lopList) {
                if (lop != null && lop.getMaLop() != null && !lop.getMaLop().isEmpty()) {
                    cboMaLop.addItem(lop.getMaLop());
                    count++;
                }
            }
            
            System.out.println("✓ Đã thêm " + count + " lớp vào combo box");
            
            if (cboMaLop.getItemCount() > 0) {
                cboMaLop.setSelectedIndex(0);
                System.out.println("✓ Chọn lớp mặc định: " + cboMaLop.getSelectedItem());
            }
            
            System.out.println("=== Kết thúc loadLopComboBox thành công ===\n");
            
        } catch (Exception e) {
            System.out.println("❌ EXCEPTION trong loadLopComboBox: " + e.getClass().getName());
            System.out.println("❌ Message: " + e.getMessage());
            e.printStackTrace();
            cboMaLop.addItem("(Lỗi tải dữ liệu)");
        }
    }

    private void loadNamHocComboBox() {
        System.out.println("\n=== Bắt đầu loadNamHocComboBox ===");
        try {
            LopDAO lopDAO = new LopDAO();
            System.out.println("✓ Tạo LopDAO thành công");
            
            var lopList = lopDAO.getAllLop();
            System.out.println("✓ Gọi getAllLop() thành công, kết quả: " + (lopList == null ? "null" : lopList.size() + " lớp"));
            
            cboNamHoc.removeAllItems();
            System.out.println("✓ Clear combo box");
            
            if (lopList == null || lopList.isEmpty()) {
                System.out.println("⚠️ CẢNH BÁO: Không có lớp nào từ database!");
                cboNamHoc.addItem("(Không có dữ liệu)");
                return;
            }
            
            java.util.Set<String> namHocSet = new java.util.HashSet<>();
            for (var lop : lopList) {
                if (lop != null && lop.getNienKhoa() != null && !lop.getNienKhoa().isEmpty()) {
                    namHocSet.add(lop.getNienKhoa());
                }
            }
            
            System.out.println("✓ Tìm được " + namHocSet.size() + " năm học khác nhau");
            
            var sortedNamHoc = namHocSet.stream().sorted().toArray(String[]::new);
            
            if (sortedNamHoc.length == 0) {
                System.out.println("⚠️ CẢNH BÁO: Không có năm học nào!");
                cboNamHoc.addItem("(Không có dữ liệu)");
                return;
            }
            
            for (String nh : sortedNamHoc) {
                cboNamHoc.addItem(nh);
            }
            
            System.out.println("✓ Đã thêm " + sortedNamHoc.length + " năm học vào combo box");
            
            if (sortedNamHoc.length > 0) {
                cboNamHoc.setSelectedIndex(0);
                System.out.println("✓ Chọn năm học mặc định: " + cboNamHoc.getSelectedItem());
            }
            
            System.out.println("=== Kết thúc loadNamHocComboBox thành công ===\n");
            
        } catch (Exception e) {
            System.out.println("❌ EXCEPTION trong loadNamHocComboBox: " + e.getClass().getName());
            System.out.println("❌ Message: " + e.getMessage());
            e.printStackTrace();
            cboNamHoc.addItem("(Lỗi tải dữ liệu)");
        }
    }

    private void loadNamHocByMaLop(String maLop) {
        System.out.println("\n=== Bắt đầu loadNamHocByMaLop: " + maLop + " ===");
        try {
            Dao.HocphiDAO hocphiDAO = new Dao.HocphiDAO();
            var namHocList = hocphiDAO.getNamHocByMaLop(maLop);
            
            cboNamHoc.removeAllItems();
            System.out.println("✓ Tìm được " + namHocList.size() + " năm học cho lớp " + maLop);
            
            if (namHocList.isEmpty()) {
                System.out.println("⚠️ CẢNH BÁO: Không có dữ liệu học phí cho lớp " + maLop);
                cboNamHoc.addItem("(Không có dữ liệu)");
                return;
            }
            
            for (String nh : namHocList) {
                cboNamHoc.addItem(nh);
            }
            
            if (cboNamHoc.getItemCount() > 0) {
                cboNamHoc.setSelectedIndex(0);
                System.out.println("✓ Chọn năm học mặc định: " + cboNamHoc.getSelectedItem());
            }
            
            System.out.println("=== Kết thúc loadNamHocByMaLop thành công ===\n");
            
        } catch (Exception e) {
            System.out.println("❌ EXCEPTION trong loadNamHocByMaLop: " + e.getClass().getName());
            System.out.println("❌ Message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Hàm loadTable được Controller gọi để đổ dữ liệu vào
    public void loadTable(List<Hocphi> list) {
        tableModel.setRowCount(0);
        int stt = 1; 
        for (Hocphi hp : list) {
            String trangThai = (hp.getTrangThai() == null || hp.getTrangThai().trim().isEmpty()) 
                            ? "Chưa đóng" 
                            : hp.getTrangThai();
            tableModel.addRow(new Object[]{
                stt++, // ID hiển thị là STT
                hp.getMaHS(), 
                hp.getMaLop(),
                hp.getHocKy(), 
                hp.getNamHoc(),
                hp.getTongTien(), 
                hp.getMienGiam(), 
                hp.getPhaiDong(), 
                trangThai
            });
        }
    }

    public void refreshForm() {
        txtMaHS.setText("");
        txtMaHS.setEditable(true);
        txtTongTien.setText("");
        txtMienGiam.setText("");
        txtPhaiDong.setText("");
        cboTrangThai.setSelectedIndex(0);
        tableHocPhi.clearSelection();
    }

    // ===== Getters cho Controller =====
    public JComboBox<String> getCboMaLop() { return cboMaLop; }
    public JComboBox<String> getCboHocKy() { return cboHocKy; }
    public JComboBox<String> getCboNamHoc() { return cboNamHoc; }
    public JButton getBtnLoc() { return btnLoc; }
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
    public JTable getTableHocPhi() { return tableHocPhi; }
    public JTextField getTxtMaHS() { return txtMaHS; }
    public JTextField getTxtTongTien() { return txtTongTien; }
    public JTextField getTxtMienGiam() { return txtMienGiam; }
    public JComboBox<String> getCboTrangThai() { return cboTrangThai; }
}
