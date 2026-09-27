package it.unibo.the100dayswar.view.quit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import it.unibo.the100dayswar.controller.maincontroller.api.MainController;
import it.unibo.the100dayswar.commons.utilities.impl.IconLoader;
import it.unibo.the100dayswar.commons.utilities.impl.LoadPixelFont;

/**
 * Utility class that implements the exitWindow after pressing the
 * Exit button in StartWindow.
 */
public final class ExitWindow extends JDialog {
    private static final long serialVersionUID = 1L;

    private static final String RESOURCES = "startmenu/";
    private static final String BACKGROUND_PATH = RESOURCES + "adventure.jpg";

    private static final float EXIT_WINDOW_TEXT_SIZE = 30f;
    private static final Font FONT = LoadPixelFont.getFontWithSize(EXIT_WINDOW_TEXT_SIZE);
    private static final float FONT_BUTTON_NORMALIZER = (float) 1.5;

    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 600;

    private static final int BUTTON_WIDTH = 70;
    private static final int BUTTON_HEIGHT = 50;

    private final transient MainController mainController;

    /**
     * Created a custom exit window.
     * 
     * @param parent the parent JFrame.
     * @param mainController the main controller.
     */
    private ExitWindow(final JFrame parent, final MainController mainController) {
        super(parent, "Exit Confirmation", true);
        this.mainController = mainController;
        final ImageIcon backgroundImage = (ImageIcon) IconLoader.loadIcon(BACKGROUND_PATH);

        final JPanel backgroundPanel = createBackgroundPanel(backgroundImage);

        final JLabel label = createLabel(FONT);
        backgroundPanel.add(label, BorderLayout.CENTER);

        final JPanel buttonPanel = createButtonPanel(FONT);
        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);

        postInitialization(backgroundPanel, parent);
    }

    /**
     * Utility method to show the dialog.
     * 
     * @param parent the parent JFrame
     * @param mainController the main controller
     */
    public static void showDialog(final JFrame parent, final MainController mainController) {
        new ExitWindow(parent, mainController).setVisible(true);
    }

    /**
     * Creates the background panel from a given backgroundImage.
     * 
     * @param backgroundImage the image in the background
     * 
     * @return the background panel
     */
    private JPanel createBackgroundPanel(final ImageIcon backgroundImage) {
        final JPanel backgroundPanel = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(final Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(
                        backgroundImage.getImage(),
                        0, 0,
                        getWidth(), getHeight(),
                        this
                    );
                }
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));

        return backgroundPanel;
    }

    /**
     * Creates the label in which display the text.
     * 
     * @param font the font of the text
     * 
     * @return the label
     */
    private JLabel createLabel(final Font font) {
        final JLabel label = new JLabel("Are you sure?");
        label.setFont(font);
        label.setForeground(Color.WHITE);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        return label;
    }

    /**
     * Creates a JPanel containing the two option buttons.
     * 
     * @param font the font of the texts in the button
     * 
     * @return the button panel
     */
    private JPanel createButtonPanel(final Font font) {
        final float buttonFontSize = font.getSize2D() / FONT_BUTTON_NORMALIZER;
        final Font buttonFont = font.deriveFont(buttonFontSize);

        final JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        final JButton yesButton = new JButton("Yes");
        yesButton.setFont(buttonFont);
        yesButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        final JButton noButton = new JButton("No");
        noButton.setFont(buttonFont);
        noButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);

        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final Window[] windows = getWindows();
                for (final Window window : windows) {
                    window.dispose();
                }
                terminateThreads();
            }
        });

        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                dispose();
            }
        });

        return buttonPanel;
    }

    /**
     * Terminates all the threads that are running.
     */
    private void terminateThreads() {
        mainController.getGameInstance().stopTimer();
    }

    /**
     * Posts initialize the window.
     * 
     * @param backgroundPanel the background panel of the window
     * @param parent the parent of the window
     */
    private void postInitialization(final JPanel backgroundPanel, final JFrame parent) {
        this.getContentPane().add(backgroundPanel);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(parent);
    }
}
