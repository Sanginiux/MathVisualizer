import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class RecordsPanel extends JPanel {
    private DatabaseManager db;
    private DefaultTableModel tableModel;
    private JTable table;
    private static final String[] COLUMNS = {"ID","Shape","Dimensions","Area","Perimeter","Saved At"};

    public RecordsPanel(DatabaseManager db) {
        this.db = db;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 247, 255));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        buildUI(); loadRecords();
    }

    private void buildUI() {
        JLabel title = new JLabel("📋  Saved Shape Records", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(new Color(50, 60, 120));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        styleTable();
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(new Color(180, 190, 220), 1));
        add(sp, BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btns.setBackground(new Color(245, 247, 255));
        JButton ref = btn("🔄  Refresh",       new Color(70, 130, 200));
        JButton del = btn("🗑  Delete Selected", new Color(200, 70, 70));
        JButton cnt = btn("📄  Show Count",     new Color(100, 160, 100));
        btns.add(ref); btns.add(del); btns.add(cnt);
        add(btns, BorderLayout.SOUTH);

        ref.addActionListener(e -> loadRecords());
        cnt.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Total saved shapes: " + tableModel.getRowCount(), "Count", JOptionPane.INFORMATION_MESSAGE));
        del.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Select a row first.", "No Selection", JOptionPane.WARNING_MESSAGE); return; }
            int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
            if (JOptionPane.showConfirmDialog(this, "Delete record ID=" + id + "?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (db.deleteShape(id)) { tableModel.removeRow(row); }
            }
        });
    }

    public void loadRecords() {
        tableModel.setRowCount(0);
        for (String[] row : db.getAllShapes()) tableModel.addRow(row);
    }

    private void styleTable() {
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.setGridColor(new Color(200, 210, 230));
        table.setSelectionBackground(new Color(173, 216, 230));
        table.setFillsViewportHeight(true);
        JTableHeader hdr = table.getTableHeader();
        hdr.setFont(new Font("Arial", Font.BOLD, 13));
        hdr.setBackground(new Color(70, 100, 180));
        hdr.setForeground(Color.WHITE);
        hdr.setReorderingAllowed(false);
        int[] widths = {40, 90, 200, 90, 90, 150};
        for (int i = 0; i < widths.length; i++) table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                if (!sel) comp.setBackground(r % 2 == 0 ? Color.WHITE : new Color(235, 240, 255));
                return comp;
            }
        });
    }

    private JButton btn(String text, Color color) {
        JButton b = new JButton(text);
        b.setFont(new Font("Arial", Font.BOLD, 13));
        b.setBackground(color); b.setForeground(Color.WHITE);
        b.setFocusPainted(false); b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(180, 36));
        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(color.darker()); }
            @Override public void mouseExited (MouseEvent e) { b.setBackground(color); }
        });
        return b;
    }
}
