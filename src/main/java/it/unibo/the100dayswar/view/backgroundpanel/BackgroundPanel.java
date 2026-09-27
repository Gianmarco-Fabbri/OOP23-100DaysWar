package it.unibo.the100dayswar.view.backgroundpanel;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import it.unibo.the100dayswar.commons.utilities.impl.IconLoader;

/**
 * Class that represents a panel with a background image.
 */
public class BackgroundPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private final ImageIcon backgroundImage;

    /**
     * Constructor of the class.
     * It loads the Image using the IconLoader utility class.
     * 
     * @param imagePath
     */
    public BackgroundPanel(final String imagePath) {
        this.backgroundImage = (ImageIcon) IconLoader.loadIcon(imagePath);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintComponent(final Graphics graphic) {
        super.paintComponent(graphic);
        if (backgroundImage != null && backgroundImage.getImage() != null && backgroundImage.getIconWidth() > 0) {
            graphic.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
        } else {
            // Se l'immagine non è trovata, disegniamo uno sfondo di fallback nero
            graphic.setColor(java.awt.Color.BLACK);
            graphic.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
