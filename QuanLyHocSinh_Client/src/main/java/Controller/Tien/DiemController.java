package Controller.Tien; 

import Dao.DiemDAO;
import Dao.LopDAO;
import Dao.MonHocDAO;
import Model.Auth;
import Model.Diem;
import Model.LopGVCN;
import Model.MonHoc;
import View.Tien.QuanLyDiemPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import TienIch.XuatExcel;
import Model.Auth;

public class DiemController { 
    
    private QuanLyDiemPanel view;
    private DiemDAO dao;

    public DiemController(QuanLyDiemPanel view) {
        this.view = view;
        this.dao = new DiemDAO();
        
        loadComboBoxData();
        initEvents();
        loadData(); 
    }

    private void loadComboBoxData() {
        LopDAO lopDAO = new LopDAO();
        MonHocDAO monHocDAO = new MonHocDAO();

        List<LopGVCN> lops = lopDAO.getAllLop();
        List<String> maLops = new ArrayList<>();
        for (LopGVCN l : lops) {
            maLops.add(l.getMaLop());
        }
        view.setMaLopData(maLops);

        List<MonHoc> mons = monHocDAO.getAll();
        List<String> maMons = new ArrayList<>();
        for (MonHoc m : mons) {
            maMons.add(m.getMaMH());
        }
        view.setMonHocData(maMons);

        List<Integer> hks = dao.getDistinctHocKy();
        if (hks.isEmpty()) {
            hks.add(1);
            hks.add(2);
        }
        view.setHocKyData(hks);
    }

    private void initEvents() {
        view.addBtnXemListener(e -> loadData());
        view.addBtnTimKiemListener(e -> searchData());
        view.addBtnCapNhatListener(e -> {
            Diem d = view.getDiemInput();
            if (d == null) {
                view.showMessage("Điểm số phải là số thực (Ví dụ: 8.5)!"); 
                return;
            }
            if (d.getMaHS().isEmpty()) {
                view.showMessage("Vui lòng click chọn học sinh trên bảng trước!"); 
                return;
            }
            if (dao.updateDiem(d)) {
                view.showMessage("Đã cập nhật điểm thành công!");
                loadData();
            } else {
                view.showMessage("Cập nhật thất bại! Hãy kiểm tra kết nối CSDL.");
            }
        });
        view.addTableMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = view.getTable().getSelectedRow();
                view.fillFormInput(row); // Gọi hàm bên View để điền text
            }
        });
        view.addBtnXuatExcelListener(e -> {
            XuatExcel.xuatFileExcel(view.getTable(), view);
        });
    }

    private void searchData() {
        String keyword = view.getTuKhoaTimKiem();

        if (keyword.isEmpty()) {
            view.showMessage("Nhập tên hoặc mã HS để tìm kiếm nhé!");
            return;
        }

        List<Diem> list = dao.searchDiem(keyword);
        view.setTableData(list);

        if (list.isEmpty()) {
            view.showMessage("Không tìm thấy học sinh nào với từ khóa: " + keyword);
        }
    }


    private void loadData() {
        List<Diem> list;

        if (Auth.isHocSinh()) {
            String maHocSinh = Auth.maNguoiDung.toUpperCase();
            list = dao.getDiemByMaHS(maHocSinh);
            if (view.getBtnCapNhat() != null) {
                view.getBtnCapNhat().setVisible(false);
            }

        } else {
            String maLop = view.getMaLopFilter();
            if (maLop.isEmpty()) return;

            String maMon = view.getMaMonFilter();
            int hocKy = view.getHocKyFilter();
            list = dao.getDiemByFilter(maLop, maMon, hocKy);
        }
        view.setTableData(list);
    }
}