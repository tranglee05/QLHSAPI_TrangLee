package TienIch;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class TableSortHelper {
    /**
     * Gắn TableRowSorter vào JTable để tự động sắp xếp khi nhấp vào header cột
     * @param table JTable cần gắn sorter
     */
    public static void enableTableSorting(JTable table) {
        if (table == null) return;
        
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
    }
}
