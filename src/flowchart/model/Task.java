// Task.java
package flowchart.model;

import java.awt.Color;

public class Task {
    private String label;
    private int x;
    private int y;
    private Color color; // Add Color attribute

    public Task(String label) {
        this.label = label;
        this.x = 0;
        this.y = 0;
        this.color = Color.BLUE; // Default color
    }

    public String getLabel() {
        return label;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}