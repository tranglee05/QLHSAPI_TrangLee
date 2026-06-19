package TienIch; 

import java.awt.Component;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XuatExcel {

    public static void xuatFileExcel(JTable table, Component parent) {
        
        // 1. Setup cái hộp thoại để người dùng chọn chỗ lưu file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
        // Chỉ lọc hiển thị file excel .xlsx cho đỡ rối
        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx"));

        // Hiện dialog lên
        int userSelection = fileChooser.showSaveDialog(parent);

        // Nếu người dùng bấm nút Save (Approve)
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            // Check xem user có gõ đuôi .xlsx chưa, nếu quên thì mình tự thêm vào cho đúng chuẩn
            if (!fileToSave.getAbsolutePath().endsWith(".xlsx")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".xlsx");
            }

            // Bắt đầu tạo file Excel (dùng try-with-resources để nó tự đóng connection cho gọn)
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("DuLieu"); // Tạo sheet tên là DuLieu
                TableModel model = table.getModel(); // Lấy data nguồn từ cái bảng JTable

                // --- PHẦN HEADER (TIÊU ĐỀ) ---
                Row headerRow = sheet.createRow(0); // Dòng đầu tiên (index 0)
                
                // Tạo style cho header nhìn cho đẹp (in đậm, font to xíu)
                CellStyle headerStyle = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                font.setFontHeightInPoints((short) 12);
                headerStyle.setFont(font);
                
                // Chạy vòng lặp để điền tên các cột vào dòng đầu
                for (int col = 0; col < model.getColumnCount(); col++) {
                    Cell cell = headerRow.createCell(col);
                    cell.setCellValue(model.getColumnName(col));
                    cell.setCellStyle(headerStyle);
                }

                // --- PHẦN DỮ LIỆU (BODY) ---
                // Duyệt qua từng dòng của bảng
                for (int row = 0; row < model.getRowCount(); row++) {
                    // Tạo dòng Excel mới (phải +1 vì dòng 0 đã là header rồi)
                    Row excelRow = sheet.createRow(row + 1);
                    
                    for (int col = 0; col < model.getColumnCount(); col++) {
                        Cell cell = excelRow.createCell(col);
                        Object value = model.getValueAt(row, col);

                        // Check null cho chắc ăn rồi mới set giá trị
                        if (value != null) {
                            // Nếu là số thì lưu dạng số (để Excel còn tính toán cộng trừ được)
                            // Còn lại thì lưu dạng text bình thường
                            if (value instanceof Number) {
                                cell.setCellValue(((Number) value).doubleValue());
                            } else {
                                cell.setCellValue(value.toString());
                            }
                        }
                    }
                }

                // --- FORMAT LẠI ---
                // Tự động giãn chiều rộng cột cho vừa với nội dung
                for (int col = 0; col < model.getColumnCount(); col++) {
                    sheet.autoSizeColumn(col);
                }

                // --- GHI FILE ---
                // Ghi dữ liệu từ ram xuống ổ cứng
                try (FileOutputStream out = new FileOutputStream(fileToSave)) {
                    workbook.write(out);
                }

                // --- XONG XUÔI ---
                // Hỏi xem user có muốn mở file lên luôn không
                int open = JOptionPane.showConfirmDialog(parent, 
                        "Xuất file thành công!\nBạn có muốn mở file ngay không?", 
                        "Thông báo", JOptionPane.YES_NO_OPTION);
                
                // Nếu chọn Yes và máy có hỗ trợ mở file thì mở luôn
                if (open == JOptionPane.YES_OPTION && Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(fileToSave);
                }

            } catch (Exception e) {
                // Có lỗi thì in log ra console và báo cho user biết
                e.printStackTrace();
                JOptionPane.showMessageDialog(parent, "Lỗi khi xuất file: " + e.getMessage());
            }
        }
    }
}