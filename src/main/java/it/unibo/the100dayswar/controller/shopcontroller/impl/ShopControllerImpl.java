package it.unibo.the100dayswar.controller.shopcontroller.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import it.unibo.the100dayswar.controller.maincontroller.api.MainController;
import it.unibo.the100dayswar.commons.utilities.impl.Pair;
import it.unibo.the100dayswar.controller.shopcontroller.api.ShopController;
import it.unibo.the100dayswar.model.cell.api.Cell;
import it.unibo.the100dayswar.model.tower.api.TowerType;
import it.unibo.the100dayswar.model.unit.api.Unit;

/**
 * Class that implements the ShopController interface.
 */
public class ShopControllerImpl implements ShopController {
    private static final Logger LOGGER = Logger.getLogger(ShopController.class.getName());
    private final MainController mainController;

    /**
     * Constructor.
     * @param mainController the main controller
     */
    public ShopControllerImpl(final MainController mainController) {
        this.mainController = mainController;
    }
    /** 
     * {@inheritDoc}
     */
    @Override
    public void buySoldier() {
        mainController.getGameInstance().buySoldier();
        final Cell spawn = mainController.getGameInstance().getHumanPlayer().getSpawnPoint();
        final Cell newCell = mainController.getMapController().getMap().getCell(spawn.getPosition());
        mainController.getMapController().setSelectedCell(newCell);
        mainController.getGameController().skip();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void buyBasicTower() {
        final Pair<Unit, Cell> selectedCell = mainController.getMapController().getSelectedCell();
        mainController.getGameInstance().buyTower(TowerType.BASIC, selectedCell.getSecond());
        final Cell newCell = mainController.getMapController().getMap().getCell(selectedCell.getSecond().getPosition());
        mainController.getMapController().setSelectedCell(newCell);
        mainController.getGameController().skip();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void buyAdvancedTower() {
        final Pair<Unit, Cell> selectedCell = mainController.getMapController().getSelectedCell();
        mainController.getGameInstance().buyTower(TowerType.ADVANCED, selectedCell.getSecond());
        final Cell newCell = mainController.getMapController().getMap().getCell(selectedCell.getSecond().getPosition());
        mainController.getMapController().setSelectedCell(newCell);
        mainController.getGameController().skip();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void upgradeUnit() {
        final Pair<Unit, Cell> selectedCell = mainController.getMapController().getSelectedCell();
        final Unit unit = selectedCell.getFirst();
        if (unit == null) {
           LOGGER.log(Level.INFO, "Position is not valid");
           return;
        }
        try {
            mainController.getGameInstance().upgradeUnit(unit);
            final Cell newCell = mainController.getMapController().getMap().getCell(selectedCell.getSecond().getPosition());
            mainController.getMapController().setSelectedCell(newCell);
            mainController.getGameController().skip();
        } catch (IllegalStateException e) {
            LOGGER.log(Level.INFO, "Unable to upgrade unit: " + e.getMessage());
        }
    }
}
