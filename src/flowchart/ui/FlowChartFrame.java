package flowchart.ui;

import javax.swing.*;
import java.awt.*;

public class FlowChartFrame extends JFrame {
    private FlowChartPanel flowChartPanel;

    public FlowChartFrame() {
        setTitle("Flow Chart Creator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600); // Set your preferred initial size
        setLocationRelativeTo(null); // Center the frame on the screen

        flowChartPanel = new FlowChartPanel();
        add(flowChartPanel, BorderLayout.CENTER);

        // Add any additional components or menus if needed

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FlowChartFrame();
            }
        });
    }
}
