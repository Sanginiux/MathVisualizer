import java.awt.*;

/**
 * TriangleShape.java
 * ─────────────────────────────────────────────────────────────────
 * Represents a Triangle using base and height.
 * Draws an isosceles triangle using drawPolygon().
 *
 * Formulas:
 *   Area      = 0.5 × base × height
 *   Perimeter = base + 2 × slant side
 *   Slant     = √((base/2)² + height²)
 * ─────────────────────────────────────────────────────────────────
 */
public class TriangleShape extends Shape {

    private double base;
    private double height;

    public TriangleShape(double base, double height) {
        super("Triangle");
        this.base   = base;
        this.height = height;
    }

    public double getBase()           { return base;   }
    public double getTriangleHeight() { return height; }

    /**
     * draw() – Uses fillPolygon() and drawPolygon() to paint
     * an isosceles triangle.  Three vertices:
     *   Apex        : top-centre of drawing area
     *   Bottom-left : bottom-left corner
     *   Bottom-right: bottom-right corner
     */
    @Override
    public void draw(Graphics2D g2d, int px, int py, int pWidth, int pHeight) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);

        int padding = 55;
        int maxW = pWidth  - padding * 2;
        int maxH = pHeight - padding * 2 - 40;
        if (maxW < 20) maxW = 20;
        if (maxH < 20) maxH = 20;

        // Scale while keeping base:height ratio
        double ratio = base / height;
        int drawW, drawH;
        if (ratio >= 1) {
            drawW = maxW;
            drawH = (int)(maxW / ratio);
            if (drawH > maxH) { drawH = maxH; drawW = (int)(maxH * ratio); }
        } else {
            drawH = maxH;
            drawW = (int)(maxH * ratio);
            if (drawW > maxW) { drawW = maxW; drawH = (int)(maxW / ratio); }
        }

        int cx    = px + pWidth  / 2;
        int baseY = py + padding + drawH + 20;
        int apexY = baseY - drawH;
        int lx    = cx - drawW / 2;
        int rx    = cx + drawW / 2;

        int[] xPts = { cx, lx, rx };
        int[] yPts = { apexY, baseY, baseY };

        // Fill with gradient
        GradientPaint gp = new GradientPaint(
                lx, baseY, new Color(147, 197, 253, 210),
                cx, apexY, new Color(59,  130, 246, 200));
        g2d.setPaint(gp);
        g2d.fillPolygon(xPts, yPts, 3);

        // Border
        g2d.setColor(new Color(29, 78, 216));
        g2d.setStroke(new BasicStroke(2.5f));
        g2d.drawPolygon(xPts, yPts, 3);

        // Dashed altitude (height line)
        g2d.setColor(new Color(29, 78, 216, 140));
        g2d.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10, new float[]{5, 4}, 0));
        g2d.drawLine(cx, apexY, cx, baseY);

        // Labels
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics fm = g2d.getFontMetrics();

        // Base label
        g2d.setColor(new Color(20, 60, 180));
        String bLabel = "b = " + base;
        g2d.drawString(bLabel, cx - fm.stringWidth(bLabel) / 2, baseY + 18);

        // Height label (rotated 90°)
        Graphics2D g2c = (Graphics2D) g2d.create();
        g2c.rotate(-Math.PI / 2, cx - 16, (apexY + baseY) / 2);
        g2c.setFont(new Font("Arial", Font.BOLD, 12));
        g2c.setColor(new Color(20, 60, 180));
        String hLabel = "h = " + height;
        g2c.drawString(hLabel,
                cx - 16 - fm.stringWidth(hLabel) / 2,
                (apexY + baseY) / 2 + 5);
        g2c.dispose();

        // Formula labels
        g2d.setStroke(new BasicStroke(1));
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawString(String.format("Area = ½·b·h = %.2f",       getArea()),
                       px + 10, py + pHeight - 28);
        g2d.drawString(String.format("Perimeter = b+2s = %.2f",   getPerimeter()),
                       px + 10, py + pHeight - 10);
    }

    @Override public double getArea()      { return 0.5 * base * height; }

    @Override
    public double getPerimeter() {
        double slant = Math.sqrt((base / 2.0) * (base / 2.0) + height * height);
        return base + 2 * slant;
    }

    @Override
    public String getDimensionsString() { return "base=" + base + ", height=" + height; }
}
