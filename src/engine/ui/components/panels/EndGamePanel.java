package engine.ui.components.panels;

import engine.Engine;
import engine.ui.components.RoundedButton;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class EndGamePanel extends JPanel {
    private final JLabel messageLabel;

    public EndGamePanel(Engine engine) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        setBackground(Color.DARK_GRAY); // Semi-transparent black background
        setPreferredSize(new Dimension(400, 100));

        // Create a panel for the message label with an empty border for spacing
        JPanel messagePanel = new JPanel();
        messagePanel.setOpaque(false); // Transparent background
        messagePanel.setBorder(new EmptyBorder(20, 10, 20, 10)); // Add padding
        messagePanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Center the message label
        add(messagePanel, BorderLayout.NORTH);

        // Create the message label with custom font, size, and style
        messageLabel = new JLabel();
        messageLabel.setForeground(Color.WHITE); // White text color
        messageLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28)); // Custom font
        messagePanel.add(messageLabel);

        // Create a separator panel to separate the message from the lower half
        JPanel separatorPanel = new JPanel();
        separatorPanel.setBackground(Color.GRAY); // White color for the separator
        separatorPanel.setPreferredSize(new Dimension(400, 2)); // Height of the separator
        separatorPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(separatorPanel);

        separatorPanel.setLayout(new BoxLayout(separatorPanel, BoxLayout.Y_AXIS));
        separatorPanel.setOpaque(true); // Transparent background
        separatorPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add padding

        // Create rounded buttons for replay, settings, and quit
        RoundedButton replayButton = new RoundedButton("Play Again");
        RoundedButton settingsButton = new RoundedButton("Settings");
        RoundedButton quitButton = new RoundedButton("Quit");

        // Add action listeners to the buttons
        replayButton.addActionListener(e -> engine.replayGame());
        settingsButton.addActionListener(e -> engine.changeSettings());
        quitButton.addActionListener(e -> engine.quit());

        // Add buttons to the button panel
        separatorPanel.add(Box.createVerticalGlue());
        separatorPanel.add(replayButton);
        separatorPanel.add(Box.createVerticalGlue());
        separatorPanel.add(settingsButton);
        separatorPanel.add(Box.createVerticalGlue());
        separatorPanel.add(quitButton);
        separatorPanel.add(Box.createVerticalGlue());

        int buttonWidth = (int) (getWidth() * 0.8);

        replayButton.setWidth(buttonWidth);
        settingsButton.setWidth(buttonWidth);
        quitButton.setWidth(buttonWidth);


        // Ensure buttons are visible
        replayButton.setVisible(true);
        settingsButton.setVisible(true);
        quitButton.setVisible(true);
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }
}
