package flowchart.ui;

import flowchart.model.Task;
import flowchart.model.Connector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

public class FlowChartPanel extends JPanel {
    private List<Task> tasks;
    private Task selectedTask;
    private List<Connector> connectors = new ArrayList<>();
    private Connector selectedConnector;
    private int offsetX, offsetY;
    private boolean isDraggingTask;
    private boolean isDraggingConnector;
    private boolean isDraggingArrow;  // Added variable for arrow dragging
    private int arrowStartX, arrowStartY;
    private int arrowControlX, arrowControlY;
    private Connector draggingConnector;

    public FlowChartPanel() {
        tasks = new ArrayList<>();
        selectedTask = null;

        addMouseListener(new TaskClickListener());
        addMouseMotionListener(new TaskDragListener());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Task task : tasks) {
            drawTask(g, task);
        }

        for (Connector connector : connectors) {
            drawConnector(g, connector);
        }

        if (isDraggingConnector && selectedConnector != null) {
            drawDraggingConnector(g, selectedConnector);
        }
    }

    private void drawTask(Graphics g, Task task) {
        g.setColor(Color.WHITE);
        g.fillRect(task.getX(), task.getY(), 100, 50);
        g.setColor(Color.BLACK);
        g.drawRect(task.getX(), task.getY(), 100, 50);
        g.setColor(Color.BLACK);
        g.drawString(task.getLabel(), task.getX() + 10, task.getY() + 30);
    }

    private void drawConnector(Graphics g, Connector connector) {
        Task source = connector.getSource();
        Task destination = connector.getDestination();

        g.setColor(Color.BLACK);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));

        int x1 = source.getX() + 100;
        int y1 = source.getY() + 25;
        int x2 = destination.getX();
        int y2 = destination.getY() + 25;

        drawArrow(g2d, x1, y1, x2, y2);

        g2d.setStroke(new BasicStroke(1));
    }

    private void drawArrow(Graphics2D g2d, int x1, int y1, int x2, int y2) {
        int arrowSize = 10;

        // Draw a straight arrow
        g2d.drawLine(x1, y1, x2, y2);

        // Draw arrowhead at the end of the line
        double angle = Math.atan2(y2 - y1, x2 - x1);
        drawArrowhead(g2d, x2, y2, angle, arrowSize);
    }

    private void drawArrowhead(Graphics2D g2d, int x, int y, double angle, int arrowSize) {
        GeneralPath arrowHead = new GeneralPath();
        arrowHead.moveTo((float) (x - arrowSize * Math.cos(angle - Math.PI / 6)),
                (float) (y - arrowSize * Math.sin(angle - Math.PI / 6)));
        arrowHead.lineTo(x, y);
        arrowHead.lineTo((float) (x - arrowSize * Math.cos(angle + Math.PI / 6)),
                (float) (y - arrowSize * Math.sin(angle + Math.PI / 6)));
        arrowHead.closePath();

        g2d.fill(arrowHead);
    }

    private void drawDraggingConnector(Graphics g, Connector connector) {
        Task source = connector.getSource();
        int x1 = source.getX() + 100;
        int y1 = source.getY() + 25;

        Point mousePoint = getMousePosition();
        if (mousePoint != null) {
            int x2 = (int) mousePoint.getX();
            int y2 = (int) mousePoint.getY();

            g.setColor(Color.BLACK);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(2));

            drawArrow(g2d, x1, y1, x2, y2);

            g2d.setStroke(new BasicStroke(1));
        }
    }

    public void addConnector(Connector connector) {
        connectors.add(connector);
        repaint();
    }

    private class TaskClickListener extends MouseAdapter {
        private Connector selectedConnector;
        private boolean isCreatingConnector;

        @Override
        public void mouseClicked(MouseEvent e) {
            Task clickedTask = getClickedTask(e);

            if (clickedTask != null) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    // Delete the task and its connections
                    deleteTask(clickedTask);
                } else {
                    handleTaskClick(clickedTask);
                }
            } else if (SwingUtilities.isRightMouseButton(e) && selectedTask != null) {
                // Delete the selected task
                deleteTask(selectedTask);
            } else if (selectedTask == null) {
                String label = JOptionPane.showInputDialog(FlowChartPanel.this, "Enter task label:");

                if (label != null && !label.isEmpty()) {
                    Task newTask = new Task(label);
                    newTask.setX(e.getX() - 50); // Adjust the position based on the mouse click
                    newTask.setY(e.getY() - 25);
                    addTask(newTask);
                    selectedTask = newTask;
                }
            } else {
                selectedTask = null; // Reset selectedTask if no action was taken
            }

            repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (isMouseNearArrow(e.getX(), e.getY())) {
                // Clicked near the arrow, initiate dragging
                isDraggingArrow = true;
                arrowStartX = selectedTask.getX() + 100;
                arrowStartY = selectedTask.getY() + 25;
                arrowControlX = (arrowStartX + e.getX()) / 2;
                arrowControlY = (arrowStartY + e.getY()) / 2;

                // Create a new straight arrow with control points
                if (selectedTask != null) {
                    draggingConnector = new Connector(selectedTask, null);
                    draggingConnector.setControlPoint(new Point(arrowControlX, arrowControlY));
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (isDraggingArrow) {
                Task endTask = getClickedTask(e);
                Connector connector = new Connector(selectedTask, endTask);
                connector.setControlPoint(new Point(arrowControlX, arrowControlY));
                addConnector(connector);
                isDraggingArrow = false;
                draggingConnector = null;
                repaint();
            }
        }
    }


    private class TaskDragListener extends MouseAdapter {
        private Connector selectedConnector;
        private boolean isDraggingConnector;

        @Override
        public void mouseDragged(MouseEvent e) {
            if (selectedTask != null && !isDraggingConnector) {
                selectedTask.setX(e.getX() - offsetX);
                selectedTask.setY(e.getY() - offsetY);
                repaint();
            }

            if (isDraggingConnector) {
                draggingConnector.setControlPoint(new Point(e.getX(), e.getY()));
                repaint();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (isMouseNearArrow(e.getX(), e.getY())) {
                // Clicked near the arrow, initiate dragging
                isDraggingArrow = true;
                arrowStartX = selectedTask.getX() + 100;
                arrowStartY = selectedTask.getY() + 25;
                arrowControlX = (arrowStartX + e.getX()) / 2;
                arrowControlY = (arrowStartY + e.getY()) / 2;

                // Create a new straight arrow with control points
                if (selectedTask != null) {
                    draggingConnector = new Connector(selectedTask, null);
                    draggingConnector.setControlPoint(new Point(arrowControlX, arrowControlY));
                }
            } else {
                // Check if a connector is clicked
                selectedConnector = getClickedConnector(e);
                isDraggingConnector = selectedConnector != null;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (isDraggingArrow) {
                Task endTask = getClickedTask(e);
                Connector connector = new Connector(selectedTask, endTask);
                connector.setControlPoint(new Point(arrowControlX, arrowControlY));
                addConnector(connector);
                isDraggingArrow = false;
                draggingConnector = null;
                repaint();
            }

            if (isDraggingConnector) {
                isDraggingConnector = false;
                selectedConnector = null;
            }
        }
    }


    private void deleteTask(Task task) {
        // Remove the task and its connections
        tasks.remove(task);
        connectors.removeIf(connector -> connector.getSource() == task || connector.getDestination() == task);
        selectedTask = null; // Reset selectedTask after deleting a task
        repaint();
    }

    private Connector getClickedConnector(MouseEvent e) {
        for (Connector connector : connectors) {
            Task source = connector.getSource();
            Task destination = connector.getDestination();
            int x1 = source.getX() + 100;
            int y1 = source.getY() + 25;
            int x2 = destination.getX();
            int y2 = destination.getY() + 25;

            // Check if the mouse is over the line
            if (isMouseOverLine(e.getX(), e.getY(), x1, y1, x2, y2)) {
                return connector;
            }
        }
        return null;
    }

    private boolean isMouseOverLine(int mouseX, int mouseY, int x1, int y1, int x2, int y2) {
        // Define a small region around the line to make it easier to click
        int tolerance = 5;

        // Check if the mouse is close to the line
        return mouseX >= Math.min(x1, x2) - tolerance && mouseX <= Math.max(x1, x2) + tolerance &&
                mouseY >= Math.min(y1, y2) - tolerance && mouseY <= Math.max(y1, y2) + tolerance;
    }

    private void deleteConnectors(Task task) {
        // Remove the connectors associated with the task
        connectors.removeIf(connector -> connector.getSource() == task || connector.getDestination() == task);
        repaint();
    }

    private boolean isMouseNearArrow(int mouseX, int mouseY) {
        // Check if the mouse is close to the arrow
        int tolerance = 5;
        return mouseX >= Math.min(arrowStartX, arrowControlX) - tolerance &&
                mouseX <= Math.max(arrowStartX, arrowControlX) + tolerance &&
                mouseY >= Math.min(arrowStartY, arrowControlY) - tolerance &&
                mouseY <= Math.max(arrowStartY, arrowControlY) + tolerance;
    }

    private Task getClickedTask(MouseEvent e) {
        for (Task task : tasks) {
            if (isMouseOverTask(e, task)) {
                return task;
            }
        }
        return null;
    }

    private void addTask(Task task) {
        tasks.add(task);
    }

    private void handleTaskClick(Task clickedTask) {
        if (selectedTask == null) {
            selectedTask = clickedTask;
        } else if (selectedTask != clickedTask) {
            Connector connector = new Connector(selectedTask, clickedTask);
            addConnector(connector);  // Adding the connector here
            selectedTask = null; // Reset selectedTask after creating a connection
            repaint();
        }
    }

    private boolean isMouseOverTask(MouseEvent e, Task task) {
        int x = task.getX();
        int y = task.getY();
        return e.getX() >= x && e.getX() <= x + 100 && e.getY() >= y && e.getY() <= y + 50;
    }
}
