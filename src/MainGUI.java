import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * MainGUI.java  ── UPDATED (Phase 3)
 * ─────────────────────────────────────────────────────────────────
 * Changes from original:
 *   • SHAPES array now includes Triangle, Pentagon, Hexagon, Cylinder
 *   • updateInputFields() handles all 8 shapes
 *   • handleDraw() creates the correct object for all 8 shapes
 *   • No database changes needed (schema already supports any shape)
 * ─────────────────────────────────────────────────────────────────
 */
public class MainGUI extends JFrame {

    private DatabaseManager db;
    private DrawPanel       drawPanel;
    private RecordsPanel    recordsPanel;

    private JComboBox<String> shapeSelector;
    private JPanel            inputPanel;
    private JTextField        field1, field2;
    private JLabel            label1, label2;
    private JLabel            areaLabel, perimeterLabel, statusLabel;

    // ── UPDATED: All 8 shapes in dropdown ────────────────────────
    private static final String[] SHAPES = {
        "-- Select Shape --",
        "Circle", "Square", "Rectangle", "Cone",        // Original 4
        "Triangle", "Pentagon", "Hexagon", "Cylinder"   // NEW 4
    };

    private static final Color PRIMARY   = new Color(52, 86, 180);
    private static final Color SECONDARY = new Color(245, 247, 255);
    private static final Color ACCENT    = new Color(70, 170, 100);
    private static final Color WARN      = new Color(200, 70, 70);

    public MainGUI(DatabaseManager db) {
        this.db = db;
        initWindow();
        buildUI();
        setVisible(true);
    }

    private void initWindow() {
        setTitle("📐  Mathematical Structures Visualizer  v2.0");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(980, 640));
        setPreferredSize(new Dimension(1040, 700));
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { db.close(); System.exit(0); }
        });
        setLocationRelativeTo(null);
        pack();
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        add(buildHeader(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.BOLD, 13));
        tabs.setBackground(SECONDARY);
        tabs.addTab("📐  Draw Shape",   buildDrawTab());
        recordsPanel = new RecordsPanel(db);
        tabs.addTab("📋  View Records", recordsPanel);
        tabs.addChangeListener(e -> { if (tabs.getSelectedIndex() == 1) recordsPanel.loadRecords(); });
        add(tabs, BorderLayout.CENTER);

        statusLabel = new JLabel("  Ready — select a shape to begin.");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(80, 90, 120));
        statusLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 210, 230)),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        add(statusLabel, BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel hdr = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, new Color(40, 70, 160), getWidth(), 0, new Color(80, 140, 220)));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        hdr.setPreferredSize(new Dimension(0, 64));
        hdr.setLayout(new BorderLayout());
        JLabel t = new JLabel("  📐  Mathematical Structures Visualizer", SwingConstants.LEFT);
        t.setFont(new Font("Arial", Font.BOLD, 22));
        t.setForeground(Color.WHITE);
        hdr.add(t, BorderLayout.CENTER);
        JLabel s = new JLabel("8 Shapes  ·  Draw  ·  Calculate  ·  Save  ", SwingConstants.RIGHT);
        s.setFont(new Font("Arial", Font.ITALIC, 13));
        s.setForeground(new Color(200, 220, 255));
        hdr.add(s, BorderLayout.EAST);
        return hdr;
    }

    private JPanel buildDrawTab() {
        JPanel tab = new JPanel(new BorderLayout(10, 10));
        tab.setBackground(SECONDARY);
        tab.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        tab.add(buildControlPanel(), BorderLayout.WEST);
        tab.add(buildCanvasArea(),   BorderLayout.CENTER);
        return tab;
    }

    private JPanel buildControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 230), 1, true),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        panel.setPreferredSize(new Dimension(268, 0));

        panel.add(sectionLabel("1  ·  Select Shape"));
        panel.add(Box.createVerticalStrut(6));
        shapeSelector = new JComboBox<>(SHAPES);
        shapeSelector.setFont(new Font("Arial", Font.PLAIN, 13));
        shapeSelector.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        shapeSelector.setAlignmentX(Component.LEFT_ALIGNMENT);
        shapeSelector.addActionListener(e -> updateInputFields());
        panel.add(shapeSelector);
        panel.add(Box.createVerticalStrut(18));

        panel.add(sectionLabel("2  ·  Enter Dimensions"));
        panel.add(Box.createVerticalStrut(6));
        inputPanel = new JPanel(new GridLayout(0, 2, 8, 8));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        label1 = new JLabel("—"); field1 = createField("0");
        label2 = new JLabel("—"); field2 = createField("0");
        inputPanel.add(label1); inputPanel.add(field1);
        inputPanel.add(label2); inputPanel.add(field2);
        panel.add(inputPanel);
        panel.add(Box.createVerticalStrut(18));

        panel.add(sectionLabel("3  ·  Actions"));
        panel.add(Box.createVerticalStrut(8));
        JButton drawBtn  = actionButton("▶  Draw Shape",          ACCENT);
        JButton saveBtn  = actionButton("💾  Save to Database",    PRIMARY);
        JButton clearBtn = actionButton("✖  Clear Canvas",         WARN);
        panel.add(drawBtn);  panel.add(Box.createVerticalStrut(8));
        panel.add(saveBtn);  panel.add(Box.createVerticalStrut(8));
        panel.add(clearBtn);
        panel.add(Box.createVerticalStrut(18));

        panel.add(sectionLabel("4  ·  Computed Values"));
        panel.add(Box.createVerticalStrut(8));
        areaLabel      = infoLabel("Area:       —");
        perimeterLabel = infoLabel("Perim./Vol: —");
        panel.add(areaLabel); panel.add(Box.createVerticalStrut(4)); panel.add(perimeterLabel);
        panel.add(Box.createVerticalGlue());

        drawBtn.addActionListener(e  -> handleDraw(false));
        saveBtn.addActionListener(e  -> handleDraw(true));
        clearBtn.addActionListener(e -> { drawPanel.setShape(null); areaLabel.setText("Area:       —"); perimeterLabel.setText("Perim./Vol: —"); setStatus("Canvas cleared."); });
        return panel;
    }

    private JPanel buildCanvasArea() {
        JPanel w = new JPanel(new BorderLayout(0, 6));
        w.setBackground(SECONDARY);
        JLabel hint = new JLabel("  Shape will appear here after clicking  ▶  Draw Shape", SwingConstants.LEFT);
        hint.setFont(new Font("Arial", Font.ITALIC, 12));
        hint.setForeground(new Color(130, 140, 170));
        w.add(hint, BorderLayout.NORTH);
        drawPanel = new DrawPanel();
        w.add(drawPanel, BorderLayout.CENTER);
        return w;
    }

    // ── CORE LOGIC ───────────────────────────────────────────────

    private void handleDraw(boolean save) {
        String sel = (String) shapeSelector.getSelectedItem();
        if (sel == null || sel.startsWith("--")) { showError("Please select a shape."); return; }

        double v1 = 0, v2 = 0;
        try {
            v1 = Double.parseDouble(field1.getText().trim());
            if (v1 <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            showError(label1.getText() + " must be a positive number."); field1.requestFocus(); return;
        }

        // Second field required for: Rectangle, Cone, Triangle, Cylinder
        boolean needsTwo = sel.equals("Rectangle") || sel.equals("Cone")
                        || sel.equals("Triangle")  || sel.equals("Cylinder");
        if (needsTwo) {
            try {
                v2 = Double.parseDouble(field2.getText().trim());
                if (v2 <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                showError(label2.getText() + " must be a positive number."); field2.requestFocus(); return;
            }
        }

        // ── Build the shape object ────────────────────────────
        Shape shape;
        switch (sel) {
            // Original shapes
            case "Circle":    shape = new CircleShape(v1);             break;
            case "Square":    shape = new SquareShape(v1);             break;
            case "Rectangle": shape = new RectangleShape(v1, v2);      break;
            case "Cone":      shape = new ConeShape(v1, v2);           break;
            // NEW shapes
            case "Triangle":  shape = new TriangleShape(v1, v2);       break;
            case "Pentagon":  shape = new PentagonShape(v1);           break;
            case "Hexagon":   shape = new HexagonShape(v1);            break;
            case "Cylinder":  shape = new CylinderShape(v1, v2);       break;
            default: return;
        }

        drawPanel.setShape(shape);
        areaLabel.setText(String.format("Area:       %.4f", shape.getArea()));
        perimeterLabel.setText(String.format("Perim./Vol: %.4f", shape.getPerimeter()));

        if (save) {
            boolean ok = db.saveShape(shape.getName(), shape.getDimensionsString(),
                                      shape.getArea(), shape.getPerimeter());
            if (ok) {
                setStatus("✅  " + sel + " drawn and saved to database!");
                JOptionPane.showMessageDialog(this, sel + " saved successfully!\nSwitch to 'View Records' to see all saved shapes.",
                        "Saved", JOptionPane.INFORMATION_MESSAGE);
            } else {
                setStatus("⚠  Shape drawn but DB save failed.");
            }
        } else {
            setStatus("✅  " + sel + " drawn. Click 'Save to Database' to store it.");
        }
    }

    /**
     * updateInputFields() – UPDATED to handle all 8 shapes.
     * Shows correct labels and hides fields that aren't needed.
     */
    private void updateInputFields() {
        String sel = (String) shapeSelector.getSelectedItem();
        if (sel == null) return;
        switch (sel) {
            case "Circle":    showFields("Radius:",       true,  "—",       false); break;
            case "Square":    showFields("Side length:",  true,  "—",       false); break;
            case "Rectangle": showFields("Length:",       true,  "Width:",  true);  break;
            case "Cone":      showFields("Radius:",       true,  "Height:", true);  break;
            case "Triangle":  showFields("Base:",         true,  "Height:", true);  break;
            case "Pentagon":  showFields("Side length:",  true,  "—",       false); break;
            case "Hexagon":   showFields("Side length:",  true,  "—",       false); break;
            case "Cylinder":  showFields("Radius:",       true,  "Height:", true);  break;
            default:          showFields("—",             false, "—",       false); break;
        }
        drawPanel.setShape(null);
        areaLabel.setText("Area:       —");
        perimeterLabel.setText("Perim./Vol: —");
    }

    private void showFields(String l1, boolean s1, String l2, boolean s2) {
        label1.setText(l1); label1.setVisible(s1); field1.setVisible(s1); field1.setText("");
        label2.setText(l2); label2.setVisible(s2); field2.setVisible(s2); field2.setText("");
        inputPanel.revalidate(); inputPanel.repaint();
    }

    // ── UI HELPERS ───────────────────────────────────────────────

    private JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", Font.BOLD, 12));
        l.setForeground(PRIMARY);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JLabel infoLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Monospaced", Font.PLAIN, 13));
        l.setForeground(new Color(50, 60, 110));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JTextField createField(String def) {
        JTextField tf = new JTextField(def);
        tf.setFont(new Font("Arial", Font.PLAIN, 14));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 190, 220)),
            BorderFactory.createEmptyBorder(4, 6, 4, 6)));
        return tf;
    }

    private JButton actionButton(String text, Color color) {
        JButton b = new JButton(text);
        b.setFont(new Font("Arial", Font.BOLD, 13));
        b.setBackground(color); b.setForeground(Color.WHITE);
        b.setFocusPainted(false); b.setBorderPainted(false);
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(color.darker()); }
            @Override public void mouseExited (MouseEvent e) { b.setBackground(color); }
        });
        return b;
    }

    private void setStatus(String msg)    { statusLabel.setText("  " + msg); }
    private void showError(String msg)    { JOptionPane.showMessageDialog(this, msg, "Input Error", JOptionPane.ERROR_MESSAGE); }
}
