package it.unibo.the100dayswar.view.gameview;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import it.unibo.the100dayswar.controller.maincontroller.api.MainController;
import it.unibo.the100dayswar.view.backgroundpanel.BackgroundPanel;
import it.unibo.the100dayswar.view.joystick.JoystickView;
import it.unibo.the100dayswar.view.map.MapView;
import it.unibo.the100dayswar.view.statistics.StatisticsView;

/**
 * Class that represents the main game view, displaying the map,
 * statistics, and joystick.
 */
public class GameView extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final int FRAME_WIDTH = 1200;
    private static final int FRAME_HEIGHT = 900;
    private static final double MAP_WEIGHT_X = 0.6;
    private static final double SIDE_PANEL_WEIGHT_X = 0.4;
    private static final int MAP_HEIGHT = 850;
    private static final double STATISTICS_WEIGHT_Y = 0.3;
    private static final int TOP_BOTTOM_PADDING = 5;
    private static final double JOYSTICK_WEIGHT_Y = 0.7;
    private static final int SIDE_PADDING = 10;

    private final transient MainController mainController;

    /**
     * Constructor for the GameView class.
     * @param mainController the main controller
     */
    public GameView(final MainController mainController) {
        super("Game View");
        this.mainController = mainController;
    }

    /**
     * Initializes the frame, setting up the UI and final configuration.
     */
    public final void initialize() {
        SwingUtilities.invokeLater(() -> {
            setUI();
            setPostInitialize();
        });
    }

    /**
     * Configures the frame settings after the UI is set up.
     */
    private void setPostInitialize() {
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    /**
     * Sets up the main user interface, including the background and layout.
     */
    private void setUI() {
        final JPanel backgroundPanel = new BackgroundPanel("/gameview/background.jpg");
        backgroundPanel.setLayout(new GridBagLayout());
        this.setContentPane(backgroundPanel);

        final MapView mapView = new MapView(mainController);
        final StatisticsView statisticsView = new StatisticsView(mainController);
        statisticsView.initialize();
        final JoystickView joystickView = new JoystickView(mainController);

        mapView.setOpaque(false);
        statisticsView.setOpaque(false);
        joystickView.setOpaque(false);

        mapView.setPreferredSize(new Dimension(FRAME_WIDTH, MAP_HEIGHT));

        final GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = MAP_WEIGHT_X;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(SIDE_PADDING, SIDE_PADDING, SIDE_PADDING, SIDE_PADDING);
        gbc.anchor = GridBagConstraints.CENTER;
        backgroundPanel.add(mapView, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.weightx = SIDE_PANEL_WEIGHT_X;
        gbc.weighty = STATISTICS_WEIGHT_Y;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(SIDE_PADDING, SIDE_PADDING, TOP_BOTTOM_PADDING, SIDE_PADDING);
        gbc.anchor = GridBagConstraints.NORTH;
        backgroundPanel.add(statisticsView, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weighty = JOYSTICK_WEIGHT_Y;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(TOP_BOTTOM_PADDING, SIDE_PADDING, SIDE_PADDING, SIDE_PADDING);
        gbc.anchor = GridBagConstraints.SOUTH;
        backgroundPanel.add(joystickView, gbc);

        backgroundPanel.revalidate();
        backgroundPanel.repaint();
    }

    // Removed loadBackgroundImage()

}
