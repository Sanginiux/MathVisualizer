import java.awt.Graphics2D;

/**
 * Shape.java – Abstract base class for all shapes.
 * Every shape class MUST implement these four methods.
 */
public abstract class Shape {
    protected String name;

    public Shape(String name) { this.name = name; }
    public String getName()   { return name; }

    public abstract void   draw(Graphics2D g2d, int x, int y, int width, int height);
    public abstract double getArea();
    public abstract double getPerimeter();
    public abstract String getDimensionsString();
}
