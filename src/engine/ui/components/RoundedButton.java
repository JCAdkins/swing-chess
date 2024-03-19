package engine.ui.components;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

class RoundedButton extends JButton {

    public RoundedButton(String text) {
        super(text);
        setContentAreaFilled(false); // Make button transparent
        setOpaque(false); // Make button transparent
        setForeground(Color.BLUE); // White text color
        setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16)); // Custom font and size

        // Create rounded border with padding
        Border border = BorderFactory.createEmptyBorder(10, 20, 10, 20);

        setBorder(BorderFactory.createCompoundBorder(
                border, // Compound border
                new RoundedBorder(20)) // Rounded border
        );

        setMaximumSize(new Dimension(Integer.MAX_VALUE, getPreferredSize().height));
        setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getModel().isArmed()) {
            g.setColor(Color.LIGHT_GRAY); // Gray color when button is pressed
        } else {
            g.setColor(getBackground());
        }
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40); // Rounded rectangle shape
        super.paintComponent(g);
    }

    public void setWidth(int buttonWidth) {
        this.setPreferredSize(new Dimension(buttonWidth, 50));
    }
}
