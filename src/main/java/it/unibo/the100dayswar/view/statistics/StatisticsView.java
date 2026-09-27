package it.unibo.the100dayswar.view.statistics;

import it.unibo.the100dayswar.controller.maincontroller.api.MainController;
import it.unibo.the100dayswar.commons.utilities.impl.LoadPixelFont;
import it.unibo.the100dayswar.model.player.api.Player;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import it.unibo.the100dayswar.view.backgroundpanel.BackgroundPanel;
import it.unibo.the100dayswar.controller.events.GameUpdateEvent;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * The view that displays the statistics of the players.
 */
@SuppressFBWarnings("RV_RETURN_VALUE_IGNORED")
public class StatisticsView extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int BORDER_THICKNESS = 5;
    private static final float TITLE_FONT_SIZE = 15f;
    private static final int ROWS = 5;
    private static final int COLUMNS = 2;
    private static final int HORIZONTAL_GAP = 10;
    private static final int VERTICAL_GAP = 10;

    private final DecimalFormat df;
    private final Map<Player, Map<String, JLabel>> playerLabels = new HashMap<>();
    private final JLabel dayLabel;
    private final transient MainController mainController;

    /**
     * Constructor of the statistics view.
     * @param mainController the main controller
     */
    public StatisticsView(final MainController mainController) {
        this.mainController = mainController;
        df = new DecimalFormat("#.##");

        this.dayLabel = new JLabel("", JLabel.CENTER);
        this.dayLabel.setFont(LoadPixelFont.getFont().deriveFont(TITLE_FONT_SIZE));
        this.dayLabel.setAlignmentX(CENTER_ALIGNMENT);
    }

    /**
     * Inizializza la view dopo la costruzione dell'oggetto.
     */
    public void initialize() {
        SwingUtilities.invokeLater(() -> {
            postInitializeView();
            addDayLabel();
            initializeView();
        });

        mainController.getEventBus().on(GameUpdateEvent.class)
                .subscribe(event -> SwingUtilities.invokeLater(this::updateStatisticView));
    }

    /**
     * Inizializza il layout (BoxLayout) dopo aver creato l'oggetto.
     */
    private void postInitializeView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    /**
     * Aggiunge in cima la label che mostra il giorno corrente.
     */
    private void addDayLabel() {
        updateDayLabel();
        this.add(dayLabel);
        this.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    /**
     * Inizializza i pannelli delle statistiche per ogni giocatore.
     */
    private void initializeView() {
        final List<Player> players = mainController.getStatisticController().getPlayers();

        players.forEach(player -> {
            add(createPlayerStatisticsPanel(player));
            add(Box.createRigidArea(new Dimension(0, 10)));
        });
    }

    /**
     * Crea un pannello per le statistiche di un singolo player.
     *
     * @param player the player whose statistics are displayed.
     * @return a JPanel containing the player's statistics.
     */
    private JPanel createPlayerStatisticsPanel(final Player player) {
        final JPanel panel = new BackgroundPanel("/statistic/statistics_background.png") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(final Graphics g) {
                final Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                super.paintComponent(g2d);
                g2d.dispose();
            }
        };

        panel.setLayout(new GridLayout(ROWS, COLUMNS, HORIZONTAL_GAP, VERTICAL_GAP));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.BLACK, BORDER_THICKNESS),
            player.getUsername(),
            TitledBorder.CENTER,
            TitledBorder.TOP,
            LoadPixelFont.getFont().deriveFont(TITLE_FONT_SIZE)
        ));

        final Map<String, JLabel> labels = new HashMap<>();
        labels.put("Soldiers", createLabel("Soldiers: "
            + mainController.getStatisticController().getSoldiers(player)));
        labels.put("Towers", createLabel("Towers: "
            + mainController.getStatisticController().getTowers(player)));
        labels.put("CellsOwned", createLabel("Cells Owned(%): "
            + mainController.getStatisticController().getCellsPercentage(player)));
        labels.put("Balance", createLabel("Balance: "
            + mainController.getStatisticController().getBalance(player)));

        labels.values().forEach(panel::add);
        playerLabels.put(player, labels);
        return panel;
    }

    /**
     * Helper method to create a JLabel with consistent styling.
     *
     * @param text the text for the label.
     * @return a styled JLabel.
     */
    private JLabel createLabel(final String text) {
        final JLabel label = new JLabel(text, JLabel.LEFT);
        label.setFont(LoadPixelFont.getFont());
        return label;
    }

    /**
     * Aggiorna il testo della dayLabel con il giorno corrente.
     */
    private void updateDayLabel() {
        final int currentDay = mainController.getGameInstance().getGameDay();
        this.dayLabel.setText("Day: " + currentDay);
    }

    /**
     * Updates the view with the latest statistics for all players.
     */
    public void updateStatisticView() {
        updateDayLabel();

        mainController.getStatisticController().updateStatistics();
        playerLabels.forEach((player, labels) -> {
            labels.get("Soldiers").setText("Soldiers: "
                + mainController.getStatisticController().getSoldiers(player));
            labels.get("Towers").setText("Towers: "
                + mainController.getStatisticController().getTowers(player));
            labels.get("CellsOwned").setText("Cells Owned(%): "
                + df.format(mainController.getStatisticController().getCellsPercentage(player)));
            labels.get("Balance").setText("Balance: "
                + mainController.getStatisticController().getBalance(player));
        });
        repaint();
    }
}
