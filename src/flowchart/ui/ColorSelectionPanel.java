package flowchart.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ColorSelectionPanel extends JPanel {
    private Color selectedColor;

    public ColorSelectionPanel() {
        JButton chooseColorButton = new JButton("Choose Color");
        chooseColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedColor = JColorChooser.showDialog(ColorSelectionPanel.this, "Choose Color", selectedColor);
                // Optionally, you can update a preview or perform other actions here
            }
        });

        add(chooseColorButton);
    }

    public Color getSelectedColor() {
        return selectedColor;
    }
}