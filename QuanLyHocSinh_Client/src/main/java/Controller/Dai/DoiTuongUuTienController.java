package Controller.Dai;

import Model.DoiTuongUuTien;
import Dao.DoiTuongUuTienDAO;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class DoiTuongUuTienController {

    private DoiTuongUuTienDAO dao = new DoiTuongUuTienDAO();

    public void loadTable(DefaultTableModel model) {
        model.setRowCount(0);
        List<DoiTuongUuTien> list = dao.getAll();

        for (DoiTuongUuTien dt : list) {
            model.addRow(new Object[]{
                    dt.getMaDT(),
                    dt.getTenDT(),
                    dt.getTiLeGiam() * 100
            });
        }
    }

    public boolean them(DoiTuongUuTien dt) {
        return dao.insert(dt);
    }

    public boolean sua(DoiTuongUuTien dt) {
        return dao.update(dt);
    }


    public boolean xoa(String maDT) {
        return dao.delete(maDT);
    }


    public void timKiem(String keyword, DefaultTableModel model) {
        model.setRowCount(0);
        List<DoiTuongUuTien> list = dao.search(keyword);

        for (DoiTuongUuTien dt : list) {
            model.addRow(new Object[]{
                    dt.getMaDT(),
                    dt.getTenDT(),
                    dt.getTiLeGiam() * 100
            });
        }
    }
}
