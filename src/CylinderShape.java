import java.awt.*;

/**
 * CylinderShape.java
 * ─────────────────────────────────────────────────────────────────
 * Represents a Cylinder in a 2D side-view:
 *   - A rectangle for the body
 *   - An ellipse at the top (visible rim)
 *   - An ellipse at the bottom (base, partly hidden)
 *
 * Formulas:
 *   Total Surface Area = 2πr² + 2πrh
 *   Volume             = πr²h
 *   Lateral Area       = 2πrh
 * ─────────────────────────────────────────────────────────────────
 */
public class CylinderShape extends Shape {

    private double radius;
    private double height;

    public CylinderShape(double radius, double height) {
        super("Cylinder");
        this.radius = radius;
        this.height = height;
    }

    public double getRadius()         { return radius; }
    public double getCylinderHeight() { return height; }

    /**
     * draw() – Creates a 3D-look 2D cylinder:
     *   1. Draw bottom ellipse (base)
     *   2. Fill rectangle (body) on top, covering bottom half of base ellipse
     *   3. Draw top ellipse (rim)
     *   4. Dimension lines and labels
     */
    @Override
    public void draw(Graphics2D g2d, int px, int py, int pWidth, int pHeight) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);

        int padding  = 50;
        int maxW = (int)((pWidth  - padding * 2) * 0.60);
        int maxH = (int)((pHeight - padding * 2) * 0.68);
        if (maxW < 30) maxW = 30;
        if (maxH < 30) maxH = 30;

        // Scale body dimensions to panel
        double hRatio = height / (radius * 2);
        int bodyW, bodyH;
        if (hRatio >= 1) {
            bodyH = maxH;
            bodyW = (int)(maxH / hRatio);
            if (bodyW > maxW) { bodyW = maxW; bodyH = (int)(maxW * hRatio); }
        } else {
            bodyW = maxW;
            bodyH = (int)(maxW * hRatio);
            if (bodyH > maxH) { bodyH = maxH; bodyW = (int)(maxH / hRatio); }
        }
        // Minimum sensible width
        if (bodyW < 40) bodyW = 40;

        int ellipseH = Math.max(16, bodyW / 5);  // height of the ellipse rim
        int cx       = px + pWidth / 2;
        int topY     = py + (pHeight - bodyH - ellipseH) / 2;
        int bodyX    = cx - bodyW / 2;
        int bodyY    = topY + ellipseH / 2;       // body starts at centre of top ellipse

        // ── 1. Bottom ellipse (dark = shadow) ────────────────
        g2d.setColor(new Color(100, 160, 220, 200));
        g2d.fillOval(bodyX, bodyY + bodyH - ellipseH / 2, bodyW, ellipseH);
        g2d.setColor(new Color(40, 100, 180));
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawOval(bodyX, bodyY + bodyH - ellipseH / 2, bodyW, ellipseH);

        // ── 2. Rectangle body (gradient) ─────────────────────
        GradientPaint gp = new GradientPaint(
                bodyX,          bodyY, new Color(186, 220, 255, 230),
                bodyX + bodyW,  bodyY, new Color(100, 160, 230, 230));
        g2d.setPaint(gp);
        g2d.fillRect(bodyX, bodyY, bodyW, bodyH);

        // Side borders of rectangle
        g2d.setColor(new Color(40, 100, 180));
        g2d.setStroke(new BasicStroke(2.5f));
        g2d.drawLine(bodyX,          bodyY, bodyX,          bodyY + bodyH);
        g2d.drawLine(bodyX + bodyW,  bodyY, bodyX + bodyW,  bodyY + bodyH);

        // ── 3. Top ellipse (bright = lit surface) ────────────
        g2d.setColor(new Color(210, 235, 255, 230));
        g2d.fillOval(bodyX, topY, bodyW, ellipseH);
        g2d.setColor(new Color(40, 100, 180));
        g2d.setStroke(new BasicStroke(2.5f));
        g2d.drawOval(bodyX, topY, bodyW, ellipseH);

        // ── Height dimension arrow (right side) ──────────────
        int arrowX = bodyX + bodyW + 16;
        g2d.setColor(new Color(30, 80, 160));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawLine(arrowX, bodyY, arrowX, bodyY + bodyH);
        // Arrowheads
        g2d.drawLine(arrowX, bodyY,          arrowX - 4, bodyY + 8);
        g2d.drawLine(arrowX, bodyY,          arrowX + 4, bodyY + 8);
        g2d.drawLine(arrowX, bodyY + bodyH,  arrowX - 4, bodyY + bodyH - 8);
        g2d.drawLine(arrowX, bodyY + bodyH,  arrowX + 4, bodyY + bodyH - 8);

        // ── Radius line (on top ellipse) ──────────────────────
        g2d.setColor(new Color(200, 0, 0));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawLine(cx, topY + ellipseH / 2, bodyX + bodyW, topY + ellipseH / 2);
        g2d.fillOval(cx - 3, topY + ellipseH / 2 - 3, 6, 6); // centre dot

        // ── Labels ───────────────────────────────────────────
        g2d.setFont(new Font("Arial", Font.BOLD, 12));

        // Height label
        g2d.setColor(new Color(30, 80, 160));
        String hLabel = "h = " + height;
        g2d.drawString(hLabel, arrowX + 6, bodyY + bodyH / 2 + 5);

        // Radius label
        g2d.setColor(new Color(180, 0, 0));
        String rLabel = "r = " + radius;
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(rLabel,
                cx + (bodyW / 4) - fm.stringWidth(rLabel) / 2,
                topY - 6);

        // Formula labels
        g2d.setStroke(new BasicStroke(1));
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawString(String.format("Surface Area = 2πr(r+h) = %.2f", getArea()),
                       px + 8, py + pHeight - 28);
        g2d.drawString(String.format("Volume = πr²h = %.2f",           getVolume()),
                       px + 8, py + pHeight - 10);
    }

    /** Total Surface Area = 2πr² + 2πrh = 2πr(r + h) */
    @Override
    public double getArea() {
        return 2 * Math.PI * radius * (radius + height);
    }

    /** Perimeter = circumference of base circle */
    @Override
    public double getPerimeter() {
        return 2 * Math.PI * radius;
    }

    /** Volume = π × r² × h */
    public double getVolume() {
        return Math.PI * radius * radius * height;
    }

    @Override
    public String getDimensionsString() {
        return "radius=" + radius + ", height=" + height;
    }
}
