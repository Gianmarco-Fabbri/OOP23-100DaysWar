package it.unibo.the100dayswar.view.map;

import it.unibo.the100dayswar.controller.maincontroller.api.MainController;
import it.unibo.the100dayswar.controller.mapcontroller.api.MapController;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import java.net.URL;

import javax.swing.SwingUtilities;

import it.unibo.the100dayswar.controller.events.BattleResultEvent;
import it.unibo.the100dayswar.controller.events.GameUpdateEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import it.unibo.the100dayswar.commons.utilities.impl.Pair;
import it.unibo.the100dayswar.model.cell.api.Cell;
import it.unibo.the100dayswar.model.unit.api.Unit;
import it.unibo.the100dayswar.commons.utilities.api.Position;

/**
 * View for rendering the game map with a background image and a grid overlay.
 */
@SuppressFBWarnings({"SE_TRANSIENT_FIELD_NOT_RESTORED", "RV_RETURN_VALUE_IGNORED"})
public class MapView extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int CELL_SIZE = 50;
    private static final int OPACITY = 64;
    private static final int ARC_SIZE = 12;
    private static final float FONT_SIZE = 15f;
    private static final int MESSAGE_DURATION = 3000;
    private static final int MESSAGE_Y_OFFSET = 5;
    private static final String MAP_IMAGE_PATH = "/map/map.png";
    private final transient Image mapImage;
    private final transient Map<String, Image> cellImageCache;
    private final transient MainController mainController;
    private String battleMessage;
    private transient javax.swing.Timer battleMessageTimer;

    /**
     * Constructor for MapView.
     * @param mainController the main controller
     */
    public MapView(final MainController mainController) {
        this.mainController = mainController;
        this.cellImageCache = new HashMap<>();
        this.mapImage = loadImage(MAP_IMAGE_PATH);
        super.setLayout(null);

        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                handleCellClick(e.getX(), e.getY());
            }
        });

        mainController.getEventBus().on(GameUpdateEvent.class)
                .subscribe(event -> SwingUtilities.invokeLater(this::repaint));

        mainController.getEventBus().on(BattleResultEvent.class)
                .subscribe(event -> SwingUtilities.invokeLater(() -> showBattleResult(event)));
    }

    /**
     * Loads an image from the given path.
     * @param path the image path.
     * @return the loaded Image.
     */
    private Image loadImage(final String path) {
        try {
            final URL imageUrl = MapView.class.getResource(path);
            if (imageUrl != null) {
                return ImageIO.read(imageUrl);
            } else {
                throw new IllegalStateException("Image not found: " + path);
            }
        } catch (IOException e) {
            Logger.getLogger(MapView.class.getName()).log(Level.SEVERE, "Error loading image: " + path, e);
            return null;
        }
    }

    /**
     * Paints the map view.
     * @param g the graphics object.
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final MapController mapController = mainController.getMapController();
        final int totalWidth = mapController.getMapWidth() * CELL_SIZE;
        final int totalHeight = mapController.getMapHeight() * CELL_SIZE;
        if (mapImage != null) {
            g.drawImage(mapImage, 0, 0, totalWidth, totalHeight, this);
        }

        final List<CellView> cellsView = mapController.getCellsView();

        final Pair<Unit, Cell> controllerSelected = mapController.getSelectedCell();
        final Position selectedPos = controllerSelected.getSecond() != null ? controllerSelected.getSecond().getPosition() : null;

        for (final CellView cellView : cellsView) {
            final int xPos = cellView.getX() * CELL_SIZE;
            final int yPos = cellView.getY() * CELL_SIZE;

            final String imagePath = cellView.getImagePath();
            final Image cellImage = cellImageCache.computeIfAbsent(imagePath, this::loadImage);
            if (cellImage != null) {
                g.drawImage(cellImage, xPos, yPos, CELL_SIZE, CELL_SIZE, this);
            }

            g.setColor(Color.BLACK);
            g.drawRect(xPos, yPos, CELL_SIZE, CELL_SIZE);

            if (selectedPos != null && cellView.getX() == selectedPos.getX() && cellView.getY() == selectedPos.getY()) {
                g.setColor(new Color(0, 0, 0, OPACITY));
                g.fillRect(xPos, yPos, CELL_SIZE, CELL_SIZE);
            }
        }

        if (battleMessage != null) {
            final int boxPadding = 10;
            final int boxHeight = 36;
            final int boxY = 8;
            final java.awt.FontMetrics fm = g.getFontMetrics(g.getFont().deriveFont(java.awt.Font.BOLD, FONT_SIZE));
            final int textWidth = fm.stringWidth(battleMessage);
            final int boxWidth = textWidth + boxPadding * 2;
            final int boxX = (getWidth() - boxWidth) / 2;
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRoundRect(boxX, boxY, boxWidth, boxHeight, ARC_SIZE, ARC_SIZE);
            g.setColor(Color.WHITE);
            g.setFont(g.getFont().deriveFont(java.awt.Font.BOLD, FONT_SIZE));
            g.drawString(battleMessage,
                this.getWidth() / 2 - g.getFontMetrics().stringWidth(battleMessage) / 2,
                boxY + boxHeight / 2 + MESSAGE_Y_OFFSET);
        }
    }

    /**
     * Returns the preferred size of the map view.
     * @return the preferred size.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(
            mainController.getMapController().getMapWidth() * CELL_SIZE,
            mainController.getMapController().getMapHeight() * CELL_SIZE
            );
    }

    /**
     * Handles cell click and retrieves the clicked cell data.
     *
     * @param mouseX the X coordinate of the mouse click
     * @param mouseY the Y coordinate of the mouse click
     */
    private void handleCellClick(final int mouseX, final int mouseY) {
        final int cellX = mouseX / CELL_SIZE;
        final int cellY = mouseY / CELL_SIZE;
        final Optional<CellView> clickedCell = mainController.getMapController().getCellsView().stream()
                .filter(cell -> cell.getX() == cellX && cell.getY() == cellY)
                .findFirst();

        clickedCell.ifPresent(cell -> {
            mainController.getMapController().onCellClick(cellX, cellY);
            repaint();
        });
    }

    /**
     * Displays the battle result overlay for 3 seconds.
     *
     * @param event the BattleResultEvent carrying roll values and outcome
     */
    private void showBattleResult(final BattleResultEvent event) {
        if (event.type() == BattleResultEvent.BattleType.SOLDIER_VS_SOLDIER) {
            final String winner;
            if (event.attackerWon()) {
                winner = "\u2713 Vittoria!";
            } else if (event.attackerRoll() == event.defenderRoll()) {
                winner = "\u26A0 Pareggio! Entrambi eliminati";
            } else {
                winner = "\u2717 Sconfitta!";
            }
            this.battleMessage = "\u2694 Tu: " + event.attackerRoll()
                    + "  Nemico: " + event.defenderRoll() + "  " + winner;
        } else if (event.type() == BattleResultEvent.BattleType.TOWER_VS_SOLDIER) {
            this.battleMessage = "\uD83D\uDDFC Torre colpisce! Danno: " + event.towerDamage()
                + " (Vita: " + event.targetHealth() + ")";
        }

        if (battleMessageTimer != null && battleMessageTimer.isRunning()) {
            battleMessageTimer.stop();
        }
        battleMessageTimer = new javax.swing.Timer(MESSAGE_DURATION, e -> {
            this.battleMessage = null;
            repaint();
        });
        battleMessageTimer.setRepeats(false);
        battleMessageTimer.start();
        repaint();
    }

}
