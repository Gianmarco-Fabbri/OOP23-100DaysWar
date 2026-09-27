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

/** 
 * Class that represents the part of the joystick that 
 * contains the buttons to move a soldier around the map.
 */
public class MovementView extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final Dimension SIZE = new Dimension(200, 200);
    private static final int INSETS = 1;
    private static final Dimension BUTTON_SIZE = new Dimension(210, 70);
    private static final String ICON_BUTTON = "startmenu/genericbutton.jpg";
    private static final float FONT_SIZE = 9f;

    private final JButton up;
    private final JButton down;
    private final JButton left;
    private final JButton right;

    private final transient MainController mainController;

    /**
     * Constructor for the MovementView class.
     *
     * @param mainController  the main controller
     */
    public MovementView(final MainController mainController) {
        this.mainController = mainController;
        super.setLayout(new GridBagLayout());
        final GridBagConstraints gbc = createGridBagConstraints();

        this.up = ButtonFactory.createCustomButton("UP", ICON_BUTTON, BUTTON_SIZE, FONT_SIZE, Color.WHITE);
        this.down = ButtonFactory.createCustomButton("DOWN", ICON_BUTTON, BUTTON_SIZE, FONT_SIZE, Color.WHITE);
        this.left = ButtonFactory.createCustomButton("LEFT", ICON_BUTTON, BUTTON_SIZE, FONT_SIZE, Color.WHITE);
        this.right = ButtonFactory.createCustomButton("RIGHT", ICON_BUTTON, BUTTON_SIZE, FONT_SIZE, Color.WHITE);

        setButtonActions();
        arrangeButtons(gbc);
        super.setPreferredSize(SIZE);
    }

    /**
     * Disables all joystick buttons.
     */
    public void disableButtons() {
        this.up.setEnabled(false);
        this.down.setEnabled(false);
        this.left.setEnabled(false);
        this.right.setEnabled(false);
    }

    /**
     * Enables all joystick buttons.
     */
    public void enableButtons() {
        this.up.setEnabled(true);
        this.down.setEnabled(true);
        this.left.setEnabled(true);
        this.right.setEnabled(true);
    }

    /**
     * Sets actions for the joystick buttons.
     */
    private void setButtonActions() {
        up.addActionListener(e -> moveUp());
        down.addActionListener(e -> moveDown());
        left.addActionListener(e -> moveLeft());
        right.addActionListener(e -> moveRight());
    }

    /**
     * Moves the soldier up.
     */
    private void moveUp() {
        mainController.getMovementController().moveUp();
        mainController.getEventBus().publish(new GameUpdateEvent());
    }

    /**
     * Moves the soldier down.
     */
    private void moveDown() {
        mainController.getMovementController().moveDown();
        mainController.getEventBus().publish(new GameUpdateEvent());
    }

    /**
     * Moves the soldier to the left.
     */
    private void moveLeft() {
        mainController.getMovementController().moveLeft();
        mainController.getEventBus().publish(new GameUpdateEvent());
    }

    /**
     * Moves the soldier to the right.
     */
    private void moveRight() {
        mainController.getMovementController().moveRight();
        mainController.getEventBus().publish(new GameUpdateEvent());
    }

    /**
     * Arranges the buttons in the panel.
     *
     * @param gbc the GridBagConstraints to use.
     */
    private void arrangeButtons(final GridBagConstraints gbc) {
        gbc.gridx = 1;
        gbc.gridy = 0;
        super.add(up, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        super.add(left, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        super.add(right, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        super.add(down, gbc);
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
}
