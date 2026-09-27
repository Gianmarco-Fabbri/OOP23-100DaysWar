package it.unibo.the100dayswar.view.joystick;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;

import it.unibo.the100dayswar.controller.events.GameUpdateEvent;
import it.unibo.the100dayswar.controller.maincontroller.api.MainController;
import it.unibo.the100dayswar.view.pausemenu.PauseMenu;
import it.unibo.the100dayswar.view.quit.ExitWindow;
import it.unibo.the100dayswar.view.rules.RulesViewer;

/** 
 * Class that represents the control panel in the joystick view.
 * This panel allows the player to manage game actions such as pause, resume, and quit.
 */
public class ControlView extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final Dimension SIZE = new Dimension(200, 200);
    private static final int INSETS = 5;
    private static final Dimension BUTTON_SIZE = new Dimension(200, 60);
    private static final String ICON_BUTTON = "startmenu/genericbutton.jpg";
    private static final float FONT_SIZE = 12f;

    private final JButton attack;
    private final JButton pause;
    private final JButton skip;
    private final JButton rules;
    private final JButton quit;

    private final transient MainController mainController;

    /**
     * Constructor for the ControlView class.
     * 
     * @param mainController the main controller
     */
    public ControlView(final MainController mainController) {
        this.mainController = mainController;
        super.setLayout(new GridBagLayout());
        final GridBagConstraints gbc = createGridBagConstraints();

        this.attack = ButtonFactory.createCustomButton("Attack", ICON_BUTTON, BUTTON_SIZE, FONT_SIZE, Color.WHITE);
        this.pause = ButtonFactory.createCustomButton("Pause", ICON_BUTTON, BUTTON_SIZE, FONT_SIZE, Color.WHITE);
        this.skip = ButtonFactory.createCustomButton("Skip Turn", ICON_BUTTON, BUTTON_SIZE, FONT_SIZE, Color.WHITE);
        this.rules = ButtonFactory.createCustomButton("Read Rules", ICON_BUTTON, BUTTON_SIZE, FONT_SIZE, Color.WHITE);
        this.quit = ButtonFactory.createCustomButton("Quit", ICON_BUTTON, BUTTON_SIZE, FONT_SIZE, Color.WHITE);

        setButtonActions();
        arrangeButtons(gbc);

        super.setPreferredSize(SIZE);
    }

    /**
     * Disables all control buttons.
     */
    public void disableButtons() {
        this.attack.setEnabled(false);
        this.pause.setEnabled(false);
        this.skip.setEnabled(false);
        this.rules.setEnabled(false);
        this.quit.setEnabled(false);
    }

    /**
     * Enables all control buttons.
     */
    public void enableButtons() {
        this.attack.setEnabled(true);
        this.pause.setEnabled(true);
        this.skip.setEnabled(true);
        this.rules.setEnabled(true);
        this.quit.setEnabled(true);
    }

    /**
     * Sets actions for the control buttons.
     */
    private void setButtonActions() {
        attack.addActionListener(e -> attackAction());
        pause.addActionListener(e -> pauseAction());
        skip.addActionListener(e -> skipTurn());
        rules.addActionListener(e -> rulesAction());
        quit.addActionListener(e -> exitAction());
    }

    /**
     * Arranges the buttons in the panel.
     * 
     * @param gbc the GridBagConstraints to use.
     */
    private void arrangeButtons(final GridBagConstraints gbc) {
        gbc.gridx = 1;
        gbc.gridy = 0;
        super.add(attack, gbc);

        gbc.gridy = 1;
        super.add(pause, gbc);

        gbc.gridy = 2;
        super.add(skip, gbc);

        gbc.gridy = 3;
        super.add(rules, gbc);

        gbc.gridy = 4;
        super.add(quit, gbc);
    }

    /**
     * Creates and configures the GridBagConstraints.
     * 
     * @return a configured GridBagConstraints object
     */
    private GridBagConstraints createGridBagConstraints() {
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(INSETS, INSETS, INSETS, INSETS);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        return gbc;
    }

    /**
     * The action to be performed when the attack button is clicked.
     */
    private void attackAction() {
        mainController.getGameController().attack();
        mainController.getEventBus().publish(new GameUpdateEvent());
    }

    /**
     * Skip the current turn without doing anything.
     */
    private void skipTurn() {
        mainController.getGameController().skip();
        mainController.getEventBus().publish(new GameUpdateEvent());
    }

    /** 
     * The action to be performed when the pause button is clicked.
     */
    private void pauseAction() {
        new PauseMenu(null, mainController).initialize();
    }

    /**
     * The action to be performed when the rules button is clicked.
     */
    private void rulesAction() {
       new RulesViewer().intitialize();
    }

    /**
     * The action to be performed when the quit button is clicked.
     */
    private void exitAction() {
        ExitWindow.showDialog(null, mainController);
    }
}
