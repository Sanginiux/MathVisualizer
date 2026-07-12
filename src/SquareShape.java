import java.awt.*;

public class SquareShape extends Shape {
    private double side;
    public SquareShape(double side) { super("Square"); this.side = side; }
    public double getSide() { return side; }

    @Override
    public void draw(Graphics2D g2d, int x, int y, int width, int height) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int padding = 50;
        int size = Math.min(width, height) - padding * 2;
        if (size < 10) size = 10;
        int drawX = x + (width - size) / 2, drawY = y + (height - size) / 2;

        g2d.setColor(new Color(144, 238, 144, 180));
        g2d.fillRect(drawX, drawY, size, size);
        g2d.setColor(new Color(34, 139, 34));
        g2d.setStroke(new BasicStroke(2.5f));
        g2d.drawRect(drawX, drawY, size, size);

        g2d.setFont(new Font("Arial", Font.BOLD, 13));
        g2d.setColor(new Color(0, 100, 0));
        String lbl = "s = " + side;
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(lbl, drawX + (size - fm.stringWidth(lbl)) / 2, drawY - 8);
        g2d.setStroke(new BasicStroke(1));
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawString(String.format("Area = s² = %.2f",         getArea()),      x + 10, y + height - 28);
        g2d.drawString(String.format("Perimeter = 4s = %.2f",    getPerimeter()), x + 10, y + height - 10);
    }

    @Override public double getArea()             { return side * side; }
    @Override public double getPerimeter()        { return 4 * side; }
    @Override public String getDimensionsString() { return "side=" + side; }
}
