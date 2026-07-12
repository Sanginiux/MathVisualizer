import javax.swing.*;

/**
 * Main.java – Entry point.
 * Sets Nimbus Look-and-Feel, opens DB, launches GUI.
 */
public class Main {
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) { UIManager.setLookAndFeel(info.getClassName()); break; }
            }
        } catch (Exception ignored) {}

        DatabaseManager db = new DatabaseManager();
        SwingUtilities.invokeLater(() -> {
            new MainGUI(db);
            System.out.println("[App] Mathematical Structures Visualizer v2.0 started.");
        });
    }
}
