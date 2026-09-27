package it.unibo.the100dayswar.view.startmenu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Optional;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import it.unibo.the100dayswar.controller.maincontroller.api.MainController;
import it.unibo.the100dayswar.commons.utilities.impl.IconLoader;
import it.unibo.the100dayswar.commons.utilities.impl.LoadPixelFont;
import it.unibo.the100dayswar.view.backgroundpanel.BackgroundPanel;
import it.unibo.the100dayswar.view.gameview.GameView;
import it.unibo.the100dayswar.view.quit.ExitWindow;
import it.unibo.the100dayswar.view.rules.RulesViewer;

/**
 * Class that models the starting menu of the game.
 */
public class StartMenuView extends JFrame {
    private static final long serialVersionUID = 1L;

    private static final String LOADING_PATH = null;    // The LOADING_PATH is 'null' by default. 
    private static final String RESOURCES = "startmenu/";
    private static final String BUTTON_GENERAL_ICON = RESOURCES + "genericbutton.jpg";
    private static final String BACKGROUND_IMAGE = RESOURCES + "background2.jpg";

    private static final int WIDTH = 200;
    private static final int HEIGHT = 80;
    private static final int MARGINS = 20;

    private final transient MainController mainController;

    /**
     * Constructor of the class.
     * @param mainController the main controller
     */
    public StartMenuView(final MainController mainController) {
        super("The100DaysWar");
        this.mainController = mainController;
    }

    /**
     * Initialize the class.
     * 
     * @implNote this method must be final to avoid ConstructorCallsOverridableMethod.
     */
    public final void initialize() {
        SwingUtilities.invokeLater(() -> {
            buildUI();
            postInitialization();
        });
    }

    /**
     * Builds the UI components.
     */
    private void buildUI() {
        final JPanel panel = new BackgroundPanel(BACKGROUND_IMAGE);
        panel.setLayout(new GridBagLayout());

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(MARGINS, MARGINS, MARGINS, MARGINS);

        final Font buttonFont = LoadPixelFont.getFont();

        gbc.gridx = 0;
        gbc.gridy = 0;
        final JButton btnStart = createButton("START", BUTTON_GENERAL_ICON, buttonFont);
        btnStart.addActionListener(st -> startAction());
        panel.add(btnStart, gbc);

        gbc.gridy++;
        final JButton btnResume = createButton("RESUME", BUTTON_GENERAL_ICON, buttonFont);
        btnResume.addActionListener(re -> resumeAction());
        panel.add(btnResume, gbc);

        gbc.gridy++;
        final JButton btnRules = createButton("RULES", BUTTON_GENERAL_ICON, buttonFont);
        btnRules.addActionListener(ru -> rulesAction());
        panel.add(btnRules, gbc);

        gbc.gridy++;
        final JButton btnExit = createButton("EXIT", BUTTON_GENERAL_ICON, buttonFont);
        btnExit.addActionListener(ex -> exitAction());
        panel.add(btnExit, gbc);

        add(panel, BorderLayout.CENTER);
    }

    /**
     * Final initialization step for frame configuration.
     */
    private void postInitialization() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setVisible(true);
    }

    /**
     * Defines the actions after pressing START.
     */
    private void startAction() {
        final Optional<String> name = NameWindow.askUsername(this);
        if (name.isPresent()) {
            mainController.startNewGame(name.get());
            new GameView(mainController).initialize();
            dispose();
        }
    }

    /**
     * Defines the actions after pressing RESUME.
     */
    private void resumeAction() {
        mainController.loadOldGame(LOADING_PATH).thenAccept(success -> {
            SwingUtilities.invokeLater(() -> {
                if (success) {
                    new GameView(mainController).initialize();
                    dispose();
                } else {
                    NoOldGameFoundWindow.show(this);
                }
            });
        });
    }

    /**
     * Defines the actions after pressing RULES.
     */
    private void rulesAction() {
       new RulesViewer().intitialize();
    }

    /**
     * Defines the actions after pressing EXIT.
     */
    private void exitAction() {
        ExitWindow.showDialog(this, mainController);
    }

    /**
     * Creates a button with the given text, icon, and font.
     * 
     * @param text the text of the button
     * @param iconPath the path to the button's icon
     * @param font the font of the button
     * @return the created button
     */
    private JButton createButton(final String text, final String iconPath, final Font font) {
        final Icon icon = IconLoader.loadIcon(iconPath);
        final JButton button = new JButton(text, icon);
        button.setFont(font);
        button.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);

        button.setForeground(Color.WHITE);

        /*
         * Sets the image as a background
         */
        button.setContentAreaFilled(false); 
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        return button;
    }
}
