import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseManager.java
 * Handles all SQLite operations: connect, create table, save, fetch, delete.
 * Works for ALL shapes (old and new) — no changes needed when adding shapes.
 */
public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:shapes.db";
    private Connection connection;

    public DatabaseManager() { connect(); createTable(); }

    private void connect() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("[DB] Connected to shapes.db");
        } catch (SQLException e) { System.err.println("[DB] Connect error: " + e.getMessage()); }
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS shapes ("
                   + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                   + "shape_type TEXT NOT NULL,"
                   + "dimensions TEXT NOT NULL,"
                   + "area REAL NOT NULL,"
                   + "perimeter REAL NOT NULL,"
                   + "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)";
        try (Statement s = connection.createStatement()) {
            s.execute(sql);
            System.out.println("[DB] Table ready.");
        } catch (SQLException e) { System.err.println("[DB] Table error: " + e.getMessage()); }
    }

    public boolean saveShape(String shapeType, String dimensions, double area, double perimeter) {
        String sql = "INSERT INTO shapes (shape_type, dimensions, area, perimeter) VALUES (?,?,?,?)";
        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setString(1, shapeType); p.setString(2, dimensions);
            p.setDouble(3, area);      p.setDouble(4, perimeter);
            p.executeUpdate();
            return true;
        } catch (SQLException e) { System.err.println("[DB] Save error: " + e.getMessage()); return false; }
    }

    public List<String[]> getAllShapes() {
        List<String[]> rows = new ArrayList<>();
        String sql = "SELECT id, shape_type, dimensions, area, perimeter, created_at FROM shapes ORDER BY id DESC";
        try (Statement s = connection.createStatement(); ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                rows.add(new String[]{
                    String.valueOf(rs.getInt("id")),
                    rs.getString("shape_type"),
                    rs.getString("dimensions"),
                    String.format("%.4f", rs.getDouble("area")),
                    String.format("%.4f", rs.getDouble("perimeter")),
                    rs.getString("created_at")
                });
            }
        } catch (SQLException e) { System.err.println("[DB] Fetch error: " + e.getMessage()); }
        return rows;
    }

    public boolean deleteShape(int id) {
        try (PreparedStatement p = connection.prepareStatement("DELETE FROM shapes WHERE id = ?")) {
            p.setInt(1, id); return p.executeUpdate() > 0;
        } catch (SQLException e) { System.err.println("[DB] Delete error: " + e.getMessage()); return false; }
    }

    public void close() {
        try { if (connection != null && !connection.isClosed()) connection.close(); }
        catch (SQLException e) { System.err.println("[DB] Close error: " + e.getMessage()); }
    }
}
