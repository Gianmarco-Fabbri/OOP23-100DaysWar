package it.unibo.the100dayswar.model.map.impl;

import java.awt.Dimension;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.Arrays;
import java.util.Objects;

import it.unibo.the100dayswar.commons.utilities.api.Position;
import it.unibo.the100dayswar.model.cell.api.Cell;
import it.unibo.the100dayswar.model.cell.impl.CellImpl;
import it.unibo.the100dayswar.model.map.api.GameMap;
import it.unibo.the100dayswar.model.unit.api.Unit;



/**
 * The implementation of the map.
 */
public class GameMapImpl implements GameMap {
    private static final long serialVersionUID = 1L;

    private final Dimension size;
    private final Cell[][] map;

    /**
     * The constructor of the map.
     * @param width is the width.
     * @param height is the height.
     * @param grid is the matrix of Cells.
     */
    public GameMapImpl(final int width, final int height, final Cell[][] grid) {
        this.size = new Dimension(width, height);
        this.map = new CellImpl[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            this.map[i] = Arrays.copyOf(grid[i], grid[i].length);
        }
    }

    /**
     * The constructor of the map.
     * 
     * @param gameMap the map.
     */
    public GameMapImpl(final GameMap gameMap) {
        this.size = gameMap.getSize();
        this.map = gameMap.getMap();
    }

    /**
     * {@inheritDoc}
     * @param position is the position (x,y) of the cell.
     * @return the Cell in that specific position or error if the position in not valid.
     */
    @Override
    public Cell getCell(final Position position) {
        return getCellByPosition(position).orElseThrow(() -> new IllegalArgumentException("Invalid position: " + position));
    }

    /**
     * {@inheritDoc}
     * @return the size of the game map.
     */
    @Override
    public Dimension getSize() {
        return new Dimension(this.size);
    }
    /**
     * ausiliar funcion for getCell.
     * @param position is the postion (x,y) of the cell.
     * @return the cell if in that position exisit a cell.
     */
    private Optional<Cell> getCellByPosition(final Position position) {
        if (!isInMap(position)) {
            return Optional.empty();
        }
        return Optional.of(map[position.getX()][position.getY()]);
    }
    /**
     * ausiliar funcion for getCellByPosition.
     * @param pos is a position.
     * @return true if the position is in the map.
     */
    private boolean isInMap(final Position pos) {
        return pos.getX() >= 0 && pos.getX() < size.getWidth() && pos.getY() >= 0 && pos.getY() < size.getHeight();
    }

    /**
     * {@inheritDoc}
     * @return a stream of all the cells.
     */
    @Override
    public Stream<Cell> getAllCells() {
       return Arrays.stream(map)
                 .flatMap(Arrays::stream)
                 .filter(Objects::nonNull);
    }

    /**
     * {@inheritDoc}
     * @param cell is the cell that will be occupied.
     * @param unit is the unit that will occupy the cell.
     */
    @Override
    public void setOccupationOnCell(final Cell cell, final Optional<Unit> unit) {
        if (
            isInMap(cell.getPosition())
            && unit.isPresent()
            ) {
                map[cell.getPosition().getX()][cell.getPosition().getY()].setOccupation(unit);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Cell[][] getMap() {
        return Arrays.stream(this.map)
                    .map(row -> Arrays.stream(row)
                                    .map(cell -> cell == null ? null : new CellImpl(cell))
                                    .toArray(Cell[]::new))
                    .toArray(Cell[][]::new);
    }
}
