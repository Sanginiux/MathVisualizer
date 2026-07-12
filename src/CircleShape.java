import java.awt.*;

public class CircleShape extends Shape {
    private double radius;

    public CircleShape(double radius) { super("Circle"); this.radius = radius; }
    public double getRadius() { return radius; }

    @Override
    public void draw(Graphics2D g2d, int x, int y, int width, int height) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int padding = 40;
        int diameter = Math.min(width, height) - padding * 2;
        if (diameter < 10) diameter = 10;
        int drawX = x + (width - diameter) / 2;
        int drawY = y + (height - diameter) / 2;

        g2d.setColor(new Color(100, 149, 237, 180));
        g2d.fillOval(drawX, drawY, diameter, diameter);
        g2d.setColor(new Color(30, 70, 160));
        g2d.setStroke(new BasicStroke(2.5f));
        g2d.drawOval(drawX, drawY, diameter, diameter);

        int cx = drawX + diameter / 2, cy = drawY + diameter / 2;
        g2d.setColor(Color.RED);
        g2d.fillOval(cx - 4, cy - 4, 8, 8);
        g2d.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, new float[]{6, 4}, 0));
        g2d.drawLine(cx, cy, drawX + diameter, cy);

        g2d.setStroke(new BasicStroke(1));
        g2d.setFont(new Font("Arial", Font.BOLD, 13));
        g2d.setColor(new Color(150, 0, 0));
        g2d.drawString("r = " + radius, cx + 8, cy - 6);
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawString(String.format("Area = π·r² = %.2f",          getArea()),      x + 10, y + height - 28);
        g2d.drawString(String.format("Circumference = 2πr = %.2f",  getPerimeter()), x + 10, y + height - 10);
    }

    @Override public double getArea()             { return Math.PI * radius * radius; }
    @Override public double getPerimeter()        { return 2 * Math.PI * radius; }
    @Override public String getDimensionsString() { return "radius=" + radius; }
}
