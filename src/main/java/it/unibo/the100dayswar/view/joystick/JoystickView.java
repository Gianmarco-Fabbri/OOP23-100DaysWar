package it.unibo.the100dayswar.view.joystick;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import it.unibo.the100dayswar.commons.utilities.impl.LoadPixelFont;
import it.unibo.the100dayswar.controller.maincontroller.api.MainController;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;

/**
 * Class that represents the main joystick view, combining
 * the MovementView, ShopView, and a placeholder panel into a single panel.
 */
public class JoystickView extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int BORDER_THICKNESS = 5;
    private static final float TITLE_FONT_SIZE = 15f;
    private static final float BACKGROUND_OPACITY = 0.3f;
    private final ShopView shopView;
    private final MovementView movementView;
    private final ControlView controlView;
    private final transient BufferedImage backgroundImage;

    /**
     * Constructor for the JoystickView class.
     * 
     * @param mainController the main controller
     */
    public JoystickView(final MainController mainController) {
        this.backgroundImage = loadBackgroundImage();
        super.setLayout(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();

        this.movementView = new MovementView(mainController);
        this.shopView = new ShopView(mainController);
        this.controlView = new ControlView(mainController);

        customizePanel(this.movementView, "Movement");
        customizePanel(this.shopView, "Shop");
        customizePanel(this.controlView, "Control");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        super.add(movementView, gbc);

        gbc.gridx = 1;
        super.add(shopView, gbc);

        gbc.gridx = 2;
        super.add(controlView, gbc);
    }

    /**
     * Loads the background image.
     * 
     * @return the loaded image
     */
    private BufferedImage loadBackgroundImage() {
        try (InputStream is = JoystickView.class.getResourceAsStream("/joystick/joystick_background.png")) {
            if (is != null) {
                return ImageIO.read(is);
            } else {
                throw new IllegalStateException("Image not found: /joystick/joystick_background.png");
            }
        } catch (IOException e) {
            Logger.getLogger(JoystickView.class.getName()).severe("Error loading image: /joystick/joystick_background.png");
        }
        return null;
    }

    /**
     * Customizes the given panel with a title, thicker border, and a custom font.
     *
     * @param panel the panel to customize
     * @param title the title for the panel
     */
    private void customizePanel(final JPanel panel, final String title) {
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.BLACK, BORDER_THICKNESS),
            title,
            TitledBorder.CENTER,
            TitledBorder.TOP,
            LoadPixelFont.getFont().deriveFont(TITLE_FONT_SIZE),
            Color.BLACK
        ));
        panel.setOpaque(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            final Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, BACKGROUND_OPACITY));
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            g2d.dispose();
        }
    }
}
