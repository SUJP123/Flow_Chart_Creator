// Task.java
package flowchart.model;

import java.awt.Color;
import java.awt.*;

public class Task {
    private String label;
    private int x;
    private int y;
    private Shape shape; // Add a Shape property
    private int shapeType;

    public Task(String label) {
        this.label = label;
        this.shape = new Rectangle(); // Default shape is Rectangle
    }

    // Add a getter and setter for the shape
    public Shape getShape() {
        return shape;
    }

    public int getShapeType() {
        return shapeType;
    }

    public void setShapeType(int shapeType) {
        this.shapeType = shapeType;
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

}