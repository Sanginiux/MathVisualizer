import java.awt.*;

public class RectangleShape extends Shape {
    private double length, rectWidth;
    public RectangleShape(double length, double width) { super("Rectangle"); this.length = length; this.rectWidth = width; }
    public double getLength()    { return length; }
    public double getRectWidth() { return rectWidth; }

    @Override
    public void draw(Graphics2D g2d, int px, int py, int pWidth, int pHeight) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int padding = 50;
        int maxW = pWidth - padding * 2, maxH = pHeight - padding * 2;
        double ratio = length / rectWidth;
        int rectW, rectH;
        if (ratio >= 1) { rectW = maxW; rectH = (int)(maxW / ratio); if (rectH > maxH) { rectH = maxH; rectW = (int)(maxH * ratio); } }
        else            { rectH = maxH; rectW = (int)(maxH * ratio); if (rectW > maxW) { rectW = maxW; rectH = (int)(maxW / ratio); } }
        if (rectW < 10) rectW = 10; if (rectH < 10) rectH = 10;
        int drawX = px + (pWidth - rectW) / 2, drawY = py + (pHeight - rectH) / 2;

        g2d.setColor(new Color(255, 182, 193, 180));
        g2d.fillRect(drawX, drawY, rectW, rectH);
        g2d.setColor(new Color(180, 20, 80));
        g2d.setStroke(new BasicStroke(2.5f));
        g2d.drawRect(drawX, drawY, rectW, rectH);

        g2d.setFont(new Font("Arial", Font.BOLD, 13));
        g2d.setColor(new Color(120, 0, 60));
        FontMetrics fm = g2d.getFontMetrics();
        String ll = "l = " + length;
        g2d.drawString(ll, drawX + (rectW - fm.stringWidth(ll)) / 2, drawY - 8);

        g2d.setStroke(new BasicStroke(1));
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawString(String.format("Area = l×w = %.2f",          getArea()),      px + 10, py + pHeight - 28);
        g2d.drawString(String.format("Perimeter = 2(l+w) = %.2f",  getPerimeter()), px + 10, py + pHeight - 10);
    }

    @Override public double getArea()             { return length * rectWidth; }
    @Override public double getPerimeter()        { return 2 * (length + rectWidth); }
    @Override public String getDimensionsString() { return "length=" + length + ", width=" + rectWidth; }
}
