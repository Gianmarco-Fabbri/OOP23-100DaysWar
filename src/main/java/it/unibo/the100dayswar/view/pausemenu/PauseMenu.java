package it.unibo.the100dayswar.view.pausemenu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import it.unibo.the100dayswar.commons.utilities.impl.LoadPixelFont;
import it.unibo.the100dayswar.controller.maincontroller.api.MainController;
import it.unibo.the100dayswar.view.backgroundpanel.BackgroundPanel;
import it.unibo.the100dayswar.view.quit.ExitWindowFromPauseMenu;
import it.unibo.the100dayswar.view.startmenu.StartMenuView;

/**
 * Class that implements the pause menu window.
 */
public class PauseMenu extends JDialog {
    private static final long serialVersionUID = 1L;
    private static final String SAVING_PATH = null;

    private static final float PAUSE_MENU_FONT_SIZE = 30f;
    private static final Font FONT = LoadPixelFont.getFontWithSize(PAUSE_MENU_FONT_SIZE);

    private static final String RESOURCES = "pausemenu/";
    private static final String BACKGROUND_IMAGE = RESOURCES + "pausebackground.jpg";

    private static final int MARGINS = 30;
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 60;
    private static final Dimension BUTTON_SIZE = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);

    private static final int POST_INIT_WIDTH = 500;
    private static final int POST_INIT_HEIGHT = 600;

    private final transient MainController mainController;

    /**
     * Constructor of the pausing window.
     * 
     * @param parent the panel frame of the pausing window
     * @param mainController the main controller
     */
    public PauseMenu(final JFrame parent, final MainController mainController) {
        super(parent, "Paused", true);
        this.mainController = mainController;
    }

    /**
     * Initialize the window.
     */
    public void initialize() {
        SwingUtilities.invokeLater(() -> {
            buildUI();
            postInitialization();
        });
    }

    /**
     * Build the UI.
     */
    private void buildUI() {
        final JPanel panel = new BackgroundPanel(BACKGROUND_IMAGE);
        panel.setLayout(new GridBagLayout());

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(MARGINS, MARGINS, MARGINS, MARGINS);

        /*
         * RESUME
         */
        gbc.gridx = 0;
        gbc.gridy = 0;
        final JButton btnResume = createButton("RESUME", FONT);
        btnResume.addActionListener(e -> resumeGame());
        panel.add(btnResume, gbc);

        /*
         * MAIN MENU
         */
        gbc.gridy++;
        final JButton btnMainMenu = createButton("MENU", FONT);
        btnMainMenu.addActionListener(e -> returnToMainMenu());
        panel.add(btnMainMenu, gbc);

        /*
         * EXIT
         */
        gbc.gridy++;
        final JButton btnExit = createButton("EXIT", FONT);
        btnExit.addActionListener(e -> exitGame());
        panel.add(btnExit, gbc);

        add(panel, BorderLayout.CENTER);
    }

    /**
     * Posts initialize the window.
     */
    private void postInitialization() {
        setSize(POST_INIT_WIDTH, POST_INIT_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Create a generic button from the given parameters.
     * 
     * @param text the text of the button
     * @param font the font of the text
     * 
     * @return the button created
     */
    private JButton createButton(final String text, final Font font) {
        final JButton button = new JButton(text);
        button.setFont(font);
        button.setPreferredSize(BUTTON_SIZE);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        return button;
    }

    /**
     * Reasume the game.
     */
    private void resumeGame() {
        dispose();
    }

    /**
     * Returns to the main menu.
     */
    private void returnToMainMenu() {
        SaveWindow.saveDialog(this, SAVING_PATH, mainController, saved -> {
            if (saved) {
                for (final Window window : getWindows()) {
                    if (window instanceof JFrame || window instanceof JDialog) {
                        window.dispose();
                    }
                }
                new StartMenuView(mainController).initialize();
            }
        });
    }

    /**
     * Quits the game.
     */
    private void exitGame() {
        if (ExitWindowFromPauseMenu.exitDialog(this)) {
            SaveWindow.saveDialog(this, SAVING_PATH, mainController, saved -> {
                if (saved) {
                    final Window[] windows = getWindows();
                    for (final Window window : windows) {
                        window.dispose();
                    }
                }
            });
        }
    }
}
