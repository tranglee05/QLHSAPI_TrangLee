package Controller.Tien;

import Api.LichThiApi;
import Model.LichThi;
import View.Tien.LichThiPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import TienIch.XuatExcel;
import Model.Auth;
import Model.MonHoc;
import Model.PhongHoc;
import Api.MonHocApiClient;
import Api.PhongHocApiClient;

public class LichThiController {
    
    private LichThiPanel view;
    private LichThiApi dao;
    private List<MonHoc> monHocList;
    
    public LichThiController(LichThiPanel view) {
        this.view = view;
        this.dao = new LichThiApi();
        loadComboBoxData();
        initEvents();
        loadAll();
    }

    private void loadComboBoxData() {
        try {
            List<String> kyThis = dao.getDistinctKyThi();
            view.setKyThiData(kyThis);

            MonHocApiClient monApi = new MonHocApiClient();
            monHocList = monApi.getAll();
            List<String> tenMons = new ArrayList<>();
            for (MonHoc m : monHocList) {
                tenMons.add(m.getTenMH());
            }
            view.setMonHocData(tenMons);

            PhongHocApiClient phongApi = new PhongHocApiClient();
            List<PhongHoc> phongs = phongApi.getAll();
            List<String> tenPhongs = new ArrayList<>();
            for (PhongHoc p : phongs) {
                tenPhongs.add(p.getMaPhong());
            }
            view.setPhongHocData(tenPhongs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initEvents() {
        boolean[] editMode = {false};
        Runnable setIdleState = () -> view.setCrudButtonState(true, false, false, false, false);
        Runnable setAddState = () -> view.setCrudButtonState(false, false, false, true, true);
        Runnable setSelectedState = () -> view.setCrudButtonState(false, true, true, false, true);
        Runnable setEditState = () -> view.setCrudButtonState(false, true, true, true, true);
        setIdleState.run();

        view.addBtnLocDanhSachListener(e -> {
            String kyThi = view.getKyThiFilter();
            String tenMon = view.getMonFilter();
            String phong = view.getPhongFilter();
            
            String maMH = "";
            if (!tenMon.isEmpty() && monHocList != null) {
                for (MonHoc m : monHocList) {
                    if (m.getTenMH().equals(tenMon)) {
                        maMH = m.getMaMH();
                        break;
                    }
                }
            }
            
            List<LichThi> list = dao.getLichThiByFilter(kyThi, maMH, phong);
            view.setTableData(list);
            if (list.isEmpty()) view.showMessage("Không tìm thấy lịch thi phù hợp!");
        });

        view.addBtnTimKiemListener(e -> {
            String kw = view.getKeyword();
            if(kw.isEmpty()) { 
                loadAll();
                return; 
            }
            
            List<LichThi> list = dao.searchLichThi(kw);
            view.setTableData(list);
            
            if(list.isEmpty()) view.showMessage("Không tìm thấy kết quả nào!");
        });
        view.addBtnXemTatCaListener(e -> loadAll());
        view.addBtnThemListener(e -> {
            editMode[0] = false;
            view.clearForm();
            view.getTable().clearSelection();
            setAddState.run();
        });
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
        view.addBtnLuuListener(e -> {
            LichThi lt = view.getLichThiInput();
            if (lt.getMaMH().isEmpty() || lt.getNgayThi().isEmpty()) {
                view.showMessage("Vui lòng nhập Mã môn và Ngày thi!");
                return;
            }
            if (editMode[0]) {
                if(dao.updateLichThi(lt)) {
                    view.showMessage("Cập nhật thành công!");
                    loadAll();
                    view.clearForm();
                    editMode[0] = false;
                    setIdleState.run();
                } else {
                    view.showMessage("Cập nhật thất bại!");
                }
            } else {
                if(dao.addLichThi(lt)) {
                    view.showMessage("Thêm lịch thi thành công!");
                    loadAll();
                    view.clearForm();
                    editMode[0] = false;
                    setIdleState.run();
                } else {
                    view.showMessage("Thêm thất bại! (Kiểm tra xem Mã Môn/Mã Phòng có tồn tại chưa)");
                }
            }
        });
        view.addBtnXoaListener(e -> {
            LichThi lt = view.getLichThiInput();
            if(lt.getMaLT() == 0) {
                 view.showMessage("Vui lòng chọn dòng cần xóa!"); 
                 return;
            }
            int cf = JOptionPane.showConfirmDialog(
                view, "Bạn có chắc muốn xóa lịch thi này?", "Xác nhận",
                JOptionPane.YES_NO_OPTION
            );
            
            if(cf == JOptionPane.YES_OPTION) {
                if(dao.deleteLichThi(lt.getMaLT())) {
                    view.showMessage("Xóa thành công!");
                    loadAll();
                    view.clearForm();
                    editMode[0] = false;
                    setIdleState.run();
                } else {
                    view.showMessage("Xóa thất bại!");
                }
            }
        });
        view.addBtnHuyListener(e -> {
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
        view.addBtnXuatExcelListener(e -> {
            XuatExcel.xuatFileExcel(view.getTable(), view);
        });
    }
    private void loadAll() {
        view.setTableData(dao.getAllLichThi());
    }
}
