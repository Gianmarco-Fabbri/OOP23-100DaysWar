package it.unibo.the100dayswar.view.joystick;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import it.unibo.the100dayswar.application.The100DaysWar;
import it.unibo.the100dayswar.commons.utilities.impl.IconLoader;
import it.unibo.the100dayswar.commons.utilities.impl.LoadPixelFont;
import it.unibo.the100dayswar.view.map.MapView;
import it.unibo.the100dayswar.view.pausemenu.PauseMenu;
import it.unibo.the100dayswar.view.rules.RulesViewer;
import it.unibo.the100dayswar.view.startmenu.ExitWindow;
import it.unibo.the100dayswar.view.statistics.StatisticsView;

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

    private final JButton attack;
    private final JButton pause;
    private final JButton skip;
    private final JButton rules;
    private final JButton quit;

    /**
     * Constructor for the ControlView class.
     * 
     * @param mapView the map view to repaint
     * @param statisticsView the statistics view to update
     */
    public ControlView(final MapView mapView, final StatisticsView statisticsView) {
        super.setLayout(new GridBagLayout());
        final GridBagConstraints gbc = createGridBagConstraints();

        this.attack = createButton("Attack");
        this.pause = createButton("Pause");
        this.skip = createButton("Skip Turn");
        this.rules = createButton("Read Rules");
        this.quit = createButton("Quit");

        setButtonActions(mapView, statisticsView);
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
     * Creates a button with consistent styling.
     * 
     * @param text the text to display on the button
     * @return a styled JButton
     */
    private JButton createButton(final String text) {
        final Icon icon = getIcon();
        if (icon == null) {
            throw new IllegalStateException("Icon not found");
        }
        final JButton button = new JButton(text, icon);
        final Font customFont = LoadPixelFont.getFont().deriveFont(12f);
        button.setFont(customFont);
        button.setPreferredSize(BUTTON_SIZE);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        return button;
    }

    /**
     * Loads the icon for the button.
     * 
     * @return the icon for the button
     */
    private Icon getIcon() {
        final Icon icon = IconLoader.loadIcon(ICON_BUTTON);
        if (icon != null) {
            final Image scaledImage = ((ImageIcon) icon).getImage()
                .getScaledInstance(BUTTON_SIZE.width, BUTTON_SIZE.height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }
        return null;
    }

    /**
     * Sets actions for the control buttons.
     * 
     * @param mapView the map view to repaint
     * @param statisticsView the statistics view to update
     */
    private void setButtonActions(final MapView mapView, final StatisticsView statisticsView) {
        attack.addActionListener(e -> attackAction(mapView, statisticsView));
        pause.addActionListener(e -> pauseAction());
        skip.addActionListener(e -> skipTurn(mapView, statisticsView));
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
     * 
     * @param mapView the map view to repaint
     * @param statisticsView the statistics view to update
     */
    private void attackAction(final MapView mapView, final StatisticsView statisticsView) {
        The100DaysWar.CONTROLLER.getGameController().attack();
        mapView.repaint();
        statisticsView.updateStatisticView();
    }

    /**
     * Skip the current turn without doing nothing.
     * 
     * @param mapView the map view to repaint
     * @param statisticsView the statistics view to update
     */
    private void skipTurn(final MapView mapView, final StatisticsView statisticsView) {
        The100DaysWar.CONTROLLER.getGameController().skip();
        mapView.repaint();
        statisticsView.updateStatisticView();
    }

    /** 
     * The action to be performed when the pause button is clicked.
     */
    private void pauseAction() {
        new PauseMenu(null).initialize();
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
        ExitWindow.showDialog(null);
    }
}
