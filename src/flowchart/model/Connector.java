package flowchart.model;

import java.awt.*;

public class Connector {
    private Task source;
    private Task destination;
    private Point controlPoint;

    public Connector(Task source, Task destination) {
        this.source = source;
        this.destination = destination;
        this.controlPoint = new Point();
    }

    public Task getSource() {
        return source;
    }

    public Task getDestination() {
        return destination;
    }

    public Point getControlPoint() {  // Add this method
        return controlPoint;
    }

    public void setControlPoint(Point controlPoint) {  // Add this method
        this.controlPoint = controlPoint;
    }
}