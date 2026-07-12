import java.awt.*;

/**
 * HexagonShape.java
 * ─────────────────────────────────────────────────────────────────
 * Represents a Regular Hexagon (6 equal sides).
 * Same polygon-vertex approach as Pentagon but with n = 6.
 *
 * Formulas:
 *   Area      = (3√3 / 2) × s²
 *   Perimeter = 6 × s
 * ─────────────────────────────────────────────────────────────────
 */
public class HexagonShape extends Shape {

    private double side;

    public HexagonShape(double side) {
        super("Hexagon");
        this.side = side;
    }

    public double getSide() { return side; }

    /**
     * draw() – Calculates 6 vertices using cosine/sine,
     * then paints with fillPolygon() / drawPolygon().
     * A flat-top hexagon is drawn (first vertex at top-right).
     */
    @Override
    public void draw(Graphics2D g2d, int px, int py, int pWidth, int pHeight) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);

        int padding = 50;
        int radius  = Math.min(pWidth, pHeight) / 2 - padding;
        if (radius < 15) radius = 15;

        int cx = px + pWidth  / 2;
        int cy = py + pHeight / 2;

        int n = 6;
        int[] xPts = new int[n];
        int[] yPts = new int[n];

        // Start angle = 0 gives a flat-topped hexagon (pointy sides left/right)
        // Start angle = -30° (π/6) gives pointy-top hexagon
        double startAngle = -Math.PI / 2; // pointy-top
        for (int i = 0; i < n; i++) {
            double angle = startAngle + 2 * Math.PI * i / n;
            xPts[i] = (int)(cx + radius * Math.cos(angle));
            yPts[i] = (int)(cy + radius * Math.sin(angle));
        }

        // Fill – honeycomb amber/gold colours
        GradientPaint gp = new GradientPaint(
                cx - radius, cy, new Color(253, 230, 138, 220),
                cx,          cy - radius, new Color(217, 119, 6, 200));
        g2d.setPaint(gp);
        g2d.fillPolygon(xPts, yPts, n);

        // Border
        g2d.setColor(new Color(146, 64, 14));
        g2d.setStroke(new BasicStroke(2.5f));
        g2d.drawPolygon(xPts, yPts, n);

        // Interior lines for honeycomb effect (light)
        g2d.setColor(new Color(146, 64, 14, 60));
        g2d.setStroke(new BasicStroke(0.8f));
        for (int i = 0; i < n; i++) {
            g2d.drawLine(cx, cy, xPts[i], yPts[i]);
        }

        // Centre dot
        g2d.setColor(new Color(120, 50, 10));
        g2d.fillOval(cx - 4, cy - 4, 8, 8);

        // Side label on top-right edge
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.setColor(new Color(100, 40, 5));
        String label = "s = " + side;
        FontMetrics fm = g2d.getFontMetrics();
        int midX = (xPts[0] + xPts[1]) / 2;
        int midY = (yPts[0] + yPts[1]) / 2;
        g2d.drawString(label, midX + 6, midY - 4);

        // Formula labels
        g2d.setStroke(new BasicStroke(1));
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawString(String.format("Area = (3√3/2)·s² = %.2f", getArea()),
                       px + 10, py + pHeight - 28);
        g2d.drawString(String.format("Perimeter = 6·s = %.2f",   getPerimeter()),
                       px + 10, py + pHeight - 10);
    }

    /** Area = (3√3 / 2) × s² */
    @Override
    public double getArea() {
        return (3.0 * Math.sqrt(3) / 2.0) * side * side;
    }

    @Override public double getPerimeter()        { return 6 * side; }
    @Override public String getDimensionsString() { return "side=" + side; }
}
