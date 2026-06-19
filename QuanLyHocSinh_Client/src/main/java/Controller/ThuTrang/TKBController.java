package Controller.ThuTrang;

import Dao.TKBDAO;
import Model.TKB;
import View.ThuTrang.FrmTKB;
import TienIch.XuatExcel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;

public class TKBController {
    private FrmTKB view;
    private TKBDAO dao;

    public TKBController(FrmTKB view) {
        this.view = view;
        this.dao = new TKBDAO();

        // Đổ danh sách lớp vào ComboBox cho Admin/GV
        if (!Model.Auth.isHocSinh()) {
            view.setCboLocMaLop(dao.getDistinctMaLop());
        }

        initEvents();
        loadData(); // Tự động load dữ liệu khi vào trang
    }

    private void initEvents() {
        boolean[] editMode = {false};
        Runnable setIdleState = () -> view.setCrudButtonState(true, false, false, false, false);
        Runnable setAddState = () -> view.setCrudButtonState(false, false, false, true, true);
        Runnable setSelectedState = () -> view.setCrudButtonState(false, true, true, false, true);
        Runnable setEditState = () -> view.setCrudButtonState(false, true, true, true, true);
        setIdleState.run();

        // 1. Nút tải lại / Nút lọc kết quả
        view.addBtnXemDanhSachListener(e -> loadData());
        view.addBtnLocTimKiemListener(e -> loadData()); // Trỏ chung về hàm loadData cực kỳ gọn

        // 2. Các nút CRUD cơ bản
        view.addBtnThemListener(e -> {
            editMode[0] = false;
            view.clearForm();
            view.getTable().clearSelection();
            setAddState.run();
        });

        view.addBtnSuaListener(e -> {
            int row = view.getTable().getSelectedRow();
            if (row == -1) { view.showMessage("Vui lòng chọn một bản ghi"); return; }
            editMode[0] = true;
            view.fillForm(row);
            setEditState.run();
        });

        view.addBtnXoaListener(e -> {
            int row = view.getTable().getSelectedRow();
            if (row == -1) { view.showMessage("Vui lòng chọn dòng cần xóa"); return; }

            int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn xóa TKB này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int id = Integer.parseInt(view.getTable().getValueAt(row, 0).toString());
                dao.delete(id);
                view.showMessage("Đã xóa");
                loadData();
                view.clearForm();
                editMode[0] = false;
                setIdleState.run();
            }
        });

        view.addBtnLuuListener(e -> {
            try {
                TKB t = view.getTKBInput();
                if (t.getMaLop().isEmpty() || t.getMaMH().isEmpty() || t.getMaGV().isEmpty() || t.getMaPhong().isEmpty()) {
                    view.showMessage("Vui lòng nhập đầy đủ thông tin");
                    return;
                }
                if (t.getTietBatDau() > t.getTietKetThuc()) {
                    view.showMessage("Tiết bắt đầu phải nhỏ hơn hoặc bằng tiết kết thúc");
                    return;
                }
                if (dao.isTrungPhongTiet(t)) {
                    view.showMessage("Trùng phòng hoặc trùng tiết");
                    return;
                }

                if (editMode[0]) {
                    view.showMessage("Cập nhật thời khóa biểu thành công");
                } else {
                    dao.insert(t);
                    view.showMessage("Thêm thời khóa biểu thành công");
                }
                loadData();
                view.clearForm();
                editMode[0] = false;
                setIdleState.run();
            } catch (NumberFormatException ex) {
                view.showMessage("Tiết bắt đầu / kết thúc phải là số");
            } catch (Exception ex) {
                view.showMessage("Lỗi khi thêm TKB: " + ex.getMessage());
            }
        });

        view.addBtnMoiListener(e -> {
            view.clearForm();
            editMode[0] = false;
            view.getTable().clearSelection();
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

        view.addBtnXuatExcelListener(e -> XuatExcel.xuatFileExcel(view.getTable(), view));
    }

    // === HÀM LOAD VÀ LỌC DỮ LIỆU TỔNG HỢP ===
    public void loadData() {
        try {
            boolean isHocSinh = Model.Auth.isHocSinh();
            String maNguoiDung = Model.Auth.maNguoiDung;
            String locLop = view.getLocMaLop();
            String locMon = view.getLocMon();
            int locThu = view.getLocThu();

            // Gọi thẳng hàm lọc duy nhất từ DAO
            List<TKB> list = dao.getTKBByFilter(isHocSinh, maNguoiDung, locLop, locMon, locThu);

            view.setTableData(list);

            if(list.isEmpty() && !locMon.isEmpty()) {
                view.showMessage("Không tìm thấy TKB phù hợp với bộ lọc!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}