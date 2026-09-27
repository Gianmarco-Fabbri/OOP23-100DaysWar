package it.unibo.the100dayswar.controller.mapcontroller.api;

import java.util.List;


import it.unibo.the100dayswar.commons.utilities.impl.Pair;
import it.unibo.the100dayswar.model.cell.api.Cell;
import it.unibo.the100dayswar.model.map.api.GameMap;
import it.unibo.the100dayswar.model.unit.api.Unit;
import it.unibo.the100dayswar.view.map.CellView;

/**
 * The controller of the map of the game.
 */
public interface MapController {
    /**
     * Gets the width of the map.
     *
     * @return the width of the map in cells
     */
    int getMapWidth();

    /**
     * Gets the height of the map.
     *
     * @return the height of the map in cells
     */
    int getMapHeight();

    /**
     * Retrieves a list of CellView objects for rendering the map.
     *
     * @return a list of CellView objects
     */
    List<CellView> getCellsView();

    /**
     * Gets the game map.
     *
     * @return the game map
     */
    GameMap getMap();

    /**
     * Handles the cell click event.
     *
     * @param cellX the X coordinate of the clicked cell
     * @param cellY the Y coordinate of the clicked cell
     */
    void onCellClick(int cellX, int cellY);

    /**
     * Gets the selected cell.
     *
     * @return the selected cell
     */
    Pair<Unit, Cell> getSelectedCell();

    /**
     * Gets the adjacent cells of the specified cell.
     *
     * @param cell the cell
     * @return a list of adjacent cells
     */
    List<Cell> getAdjacentCells(Cell cell);

    /**
     * Clears the currently selected cell.
     */
    void clearSelectedCell();

    /**
     * Sets the selected cell.
     *
     * @param cell the cell to select
     */
    void setSelectedCell(Cell cell);
}
