import javax.swing.*;
import java.awt.*;

/**
 * DrawPanel.java – Custom JPanel drawing canvas.
 * Calls shape.draw() inside paintComponent().
 */
public class DrawPanel extends JPanel {

    private Shape currentShape = null;

    public DrawPanel() {
        setPreferredSize(new Dimension(480, 420));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 190, 220), 2, true),
            BorderFactory.createEmptyBorder(4, 4, 4, 4)));
    }

    public void setShape(Shape shape) {
        this.currentShape = shape;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Gradient background
        GradientPaint bg = new GradientPaint(0, 0, new Color(245, 248, 255), 0, getHeight(), new Color(220, 230, 250));
        g2d.setPaint(bg);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Light grid
        g2d.setColor(new Color(200, 210, 230, 120));
        g2d.setStroke(new BasicStroke(0.5f));
        for (int xg = 0; xg < getWidth();  xg += 30) g2d.drawLine(xg, 0, xg, getHeight());
        for (int yg = 0; yg < getHeight(); yg += 30) g2d.drawLine(0, yg, getWidth(), yg);

        if (currentShape == null) {
            g2d.setFont(new Font("Arial", Font.ITALIC, 15));
            g2d.setColor(new Color(160, 170, 200));
            String msg = "Select a shape and click  ▶  Draw Shape";
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(msg, (getWidth() - fm.stringWidth(msg)) / 2, getHeight() / 2);
        } else {
            currentShape.draw(g2d, 0, 0, getWidth(), getHeight());
        }
    }
}
