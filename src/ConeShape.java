import java.awt.*;

public class ConeShape extends Shape {
    private double radius, height;
    public ConeShape(double radius, double height) { super("Cone"); this.radius = radius; this.height = height; }
    public double getRadius()     { return radius; }
    public double getConeHeight() { return height; }

    @Override
    public void draw(Graphics2D g2d, int px, int py, int pWidth, int pHeight) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int padding = 50;
        int coneW = (int)((pWidth - padding * 2) * 0.65);
        int coneH = (int)((pHeight - padding * 2) * 0.70);
        if (coneW < 30) coneW = 30; if (coneH < 30) coneH = 30;
        int ellipseH = Math.max(20, coneH / 8);
        int baseY = py + padding + coneH, apexX = px + pWidth / 2, apexY = py + padding;
        int baseX1 = apexX - coneW / 2, baseX2 = apexX + coneW / 2;

        GradientPaint gp = new GradientPaint(baseX1, baseY, new Color(255, 140, 0, 200), baseX2, apexY, new Color(255, 200, 80, 160));
        g2d.setPaint(gp);
        g2d.fillPolygon(new int[]{apexX, baseX1, baseX2}, new int[]{apexY, baseY, baseY}, 3);
        g2d.setColor(new Color(180, 80, 0)); g2d.setStroke(new BasicStroke(2.5f));
        g2d.drawLine(apexX, apexY, baseX1, baseY); g2d.drawLine(apexX, apexY, baseX2, baseY);

        g2d.setColor(new Color(210, 110, 0, 220));
        g2d.fillOval(baseX1, baseY - ellipseH / 2, coneW, ellipseH);
        g2d.setColor(new Color(140, 60, 0)); g2d.setStroke(new BasicStroke(2f));
        g2d.drawOval(baseX1, baseY - ellipseH / 2, coneW, ellipseH);

        g2d.setColor(Color.RED); g2d.fillOval(apexX - 4, apexY - 4, 8, 8);
        g2d.setColor(new Color(150, 0, 0));
        g2d.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, new float[]{5, 4}, 0));
        g2d.drawLine(apexX, apexY, apexX, baseY);

        g2d.setFont(new Font("Arial", Font.BOLD, 13)); g2d.setStroke(new BasicStroke(1));
        g2d.setColor(new Color(150, 0, 0)); g2d.drawString("h = " + height, apexX + 6, (apexY + baseY) / 2);
        g2d.setColor(new Color(0, 80, 130));  g2d.drawString("r = " + radius, apexX + coneW / 4, baseY + ellipseH / 2 + 16);
        g2d.setFont(new Font("Arial", Font.PLAIN, 12)); g2d.setColor(Color.DARK_GRAY);
        g2d.drawString(String.format("Surface Area = πr(r+l) = %.2f", getArea()),   px + 8, py + pHeight - 28);
        g2d.drawString(String.format("Volume = ⅓·π·r²·h = %.2f",     getVolume()), px + 8, py + pHeight - 10);
    }

    @Override public double getArea()      { double l = Math.sqrt(radius*radius + height*height); return Math.PI * radius * (radius + l); }
    @Override public double getPerimeter() { return 2 * Math.PI * radius; }
    public    double getVolume()           { return (1.0/3.0) * Math.PI * radius * radius * height; }
    @Override public String getDimensionsString() { return "radius=" + radius + ", height=" + height; }
}
