package flowchart.app;

import flowchart.ui.FlowChartFrame;

import javax.swing.*;

public class FlowChartApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlowChartFrame frame = new FlowChartFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}