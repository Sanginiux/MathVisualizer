import java.awt.*;

/**
 * PentagonShape.java
 * ─────────────────────────────────────────────────────────────────
 * Represents a Regular Pentagon (5 equal sides).
 * Uses trigonometry to compute the 5 vertex positions and draws
 * them with fillPolygon() / drawPolygon().
 *
 * Formulas (regular polygon with side s):
 *   Area      = (5/4) × s² × cot(π/5)
 *   Perimeter = 5 × s
 * ─────────────────────────────────────────────────────────────────
 */
public class PentagonShape extends Shape {

    private double side;   // Length of one side

    public PentagonShape(double side) {
        super("Pentagon");
        this.side = side;
    }

    public double getSide() { return side; }

    /**
     * draw() – Calculates 5 equally-spaced vertices around a circle,
     * starting from the top, then fills and outlines the polygon.
     *
     * For a regular n-gon:
     *   vertex[i].x = cx + R × sin(2π·i/n)
     *   vertex[i].y = cy - R × cos(2π·i/n)
     * (The - on cos makes vertex 0 point upward.)
     */
    @Override
    public void draw(Graphics2D g2d, int px, int py, int pWidth, int pHeight) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);

        int padding = 55;
        int radius  = Math.min(pWidth, pHeight) / 2 - padding;
        if (radius < 15) radius = 15;

        int cx = px + pWidth  / 2;
        int cy = py + pHeight / 2 + 10; // slightly below centre for balance

        int n = 5;
        int[] xPts = new int[n];
        int[] yPts = new int[n];

        for (int i = 0; i < n; i++) {
            double angle = 2 * Math.PI * i / n - Math.PI / 2; // start from top
            xPts[i] = (int)(cx + radius * Math.cos(angle));
            yPts[i] = (int)(cy + radius * Math.sin(angle));
        }

        // Fill
        GradientPaint gp = new GradientPaint(
                cx - radius, cy, new Color(167, 243, 208, 210),
                cx,          cy - radius, new Color(16, 185, 129, 200));
        g2d.setPaint(gp);
        g2d.fillPolygon(xPts, yPts, n);

        // Border
        g2d.setColor(new Color(5, 120, 85));
        g2d.setStroke(new BasicStroke(2.5f));
        g2d.drawPolygon(xPts, yPts, n);

        // Centre dot
        g2d.setColor(new Color(5, 120, 85));
        g2d.fillOval(cx - 4, cy - 4, 8, 8);

        // Side label on top edge
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.setColor(new Color(4, 90, 65));
        String label = "s = " + side;
        FontMetrics fm = g2d.getFontMetrics();
        int midX = (xPts[0] + xPts[1]) / 2;
        int midY = (yPts[0] + yPts[1]) / 2;
        g2d.drawString(label, midX - fm.stringWidth(label) / 2, midY - 8);

        // Formula labels
        g2d.setStroke(new BasicStroke(1));
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawString(String.format("Area = (5/4)·s²·cot(π/5) = %.2f", getArea()),
                       px + 10, py + pHeight - 28);
        g2d.drawString(String.format("Perimeter = 5·s = %.2f",           getPerimeter()),
                       px + 10, py + pHeight - 10);
    }

    /** Area of regular pentagon = (5 × s²) / (4 × tan(π/5)) */
    @Override
    public double getArea() {
        return (5.0 * side * side) / (4.0 * Math.tan(Math.PI / 5));
    }

    @Override public double getPerimeter()         { return 5 * side; }
    @Override public String getDimensionsString()  { return "side=" + side; }
}
