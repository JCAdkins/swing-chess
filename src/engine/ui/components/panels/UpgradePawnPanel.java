package engine.ui.components.panels;

import engine.Engine;
import engine.hardware.pieces.*;
import engine.ui.components.RoundedButton;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static engine.helpers.GlobalHelper.*;

public class UpgradePawnPanel extends JPanel {
    private final ImageIcon bishopIcon;
    private final ImageIcon knightIcon;
    private final ImageIcon queenIcon;
    private final ImageIcon rookIcon;

    public UpgradePawnPanel(Engine engine){
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
        JLabel messageLabel = new JLabel("Upgrade Your Pawn");
        messageLabel.setForeground(Color.WHITE); // White text color
        messageLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28)); // Custom font
        messagePanel.add(messageLabel);

        // Create a separator panel to separate the message from the lower half
        JPanel separatorPanel = new JPanel();
        separatorPanel.setBackground(Color.GRAY); // White color for the separator
        separatorPanel.setPreferredSize(new Dimension(400, 2)); // Height of the separator
        separatorPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(separatorPanel);

        separatorPanel.setLayout(new GridLayout(3,2,10,10));
        separatorPanel.setOpaque(true); // Transparent background
        separatorPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add padding

        // Create ImageIcon objects for each piece image
        bishopIcon = new ImageIcon();
        knightIcon = new ImageIcon();
        queenIcon = new ImageIcon();
        rookIcon = new ImageIcon();

        RoundedButton bishopButton = new RoundedButton(bishopIcon);
        RoundedButton knightButton = new RoundedButton(knightIcon);
        RoundedButton queenButton = new RoundedButton(queenIcon);
        RoundedButton rookButton = new RoundedButton(rookIcon);

        bishopButton.setPreferredSize(new Dimension(bishopIcon.getIconWidth(), bishopButton.getHeight()));
        knightButton.setPreferredSize(new Dimension(knightIcon.getIconWidth(), knightButton.getHeight()));
        queenButton.setPreferredSize(new Dimension(queenIcon.getIconWidth(), queenButton.getHeight()));
        rookButton.setPreferredSize(new Dimension(rookIcon.getIconWidth(), rookButton.getHeight()));

        bishopButton.addActionListener(e -> engine.upgradePlayerPawn(Bishop.class, bishopIcon.getImage()));
        knightButton.addActionListener(e -> engine.upgradePlayerPawn(Knight.class, knightIcon.getImage()));
        queenButton.addActionListener(e -> engine.upgradePlayerPawn(Queen.class, queenIcon.getImage()));
        rookButton.addActionListener(e -> engine.upgradePlayerPawn(Rook.class, rookIcon.getImage()));

        separatorPanel.add(bishopButton);
        separatorPanel.add(knightButton);
        separatorPanel.add(queenButton);
        separatorPanel.add(rookButton);
    }

    public void setImageIcons(Image[] imageIcons){
        this.bishopIcon.setImage(imageIcons[BISHOP]);
        this.knightIcon.setImage(imageIcons[KNIGHT]);
        this.queenIcon.setImage(imageIcons[QUEEN]);
        this.rookIcon.setImage(imageIcons[ROOK]);
    }
}
