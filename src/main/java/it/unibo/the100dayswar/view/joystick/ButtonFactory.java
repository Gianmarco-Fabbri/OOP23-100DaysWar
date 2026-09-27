package it.unibo.the100dayswar.view.joystick;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import it.unibo.the100dayswar.commons.utilities.impl.IconLoader;
import it.unibo.the100dayswar.commons.utilities.impl.LoadPixelFont;

/**
 * Utility class to create custom buttons for the game.
 */
public final class ButtonFactory {

    /** 
     * Private constructor to hide the implicit public one.
     */
    private ButtonFactory() {
    }

    /**
     * Creates a custom button with the specified parameters.
     * 
     * @param text the text to display
     * @param iconPath the path of the icon to load
     * @param size the preferred size of the button
     * @param fontSize the font size to use
     * @param foregroundColor the text color
     * @return a newly created JButton
     */
    public static JButton createCustomButton(
            final String text,
            final String iconPath,
            final Dimension size,
            final float fontSize,
            final Color foregroundColor
    ) {
        final Icon icon = loadIcon(iconPath, size);
        final JButton button = new JButton(text, icon);

        final Font customFont = LoadPixelFont.getFont().deriveFont(fontSize);
        button.setFont(customFont);
        button.setPreferredSize(size);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setForeground(foregroundColor);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        return button;
    }

    /**
     * Loads and scales the icon from the given path.
     *
     * @param iconPath the path to the icon
     * @param size the size to scale the icon
     * @return the scaled Icon, or null if not found
     */
    private static Icon loadIcon(final String iconPath, final Dimension size) {
        final Icon loadedIcon = IconLoader.loadIcon(iconPath);
        if (loadedIcon instanceof ImageIcon imageIcon && imageIcon.getImage() != null) {
            final Image scaledImage = imageIcon.getImage()
                .getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }
        return loadedIcon;
    }
}
