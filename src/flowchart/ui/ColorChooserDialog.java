package flowchart.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ColorChooserDialog extends JDialog {
    private Color selectedColor;

    public ColorChooserDialog(Frame owner, String title, Color initialColor) {
        super(owner, title, true);

        selectedColor = initialColor;

        JButton chooseButton = new JButton("Choose Color");
        chooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(ColorChooserDialog.this, "Choose Color", selectedColor);
                if (newColor != null) {
                    selectedColor = newColor;
                }
            }
        });

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the dialog
            }
        });

        JPanel panel = new JPanel();
        panel.add(chooseButton);
        panel.add(okButton);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(owner);
    }

    public Color getSelectedColor() {
        return selectedColor;
    }
}
