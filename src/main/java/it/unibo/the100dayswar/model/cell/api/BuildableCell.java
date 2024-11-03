package it.unibo.the100dayswar.model.cell.api;

import java.util.Optional;

import it.unibo.the100dayswar.model.unit.api.Unit;

/**
 * Interface a model a  BuildableCell where you can build a tower o put a soldier down.
 */

public interface BuildableCell extends Cell {
    /**
     * Cell state getter.
     * @return true if the cell is buildable.
     */
    boolean isBuildable();

    /**
     * 
     * @return the unit in the cell.
     */
    Optional<Unit> getUnit();

    /**
     * Setter of the occupation the cell.
     * @param unit is the unit on the cell.
     */
    void setOccupation(Optional<Unit> unit);
}