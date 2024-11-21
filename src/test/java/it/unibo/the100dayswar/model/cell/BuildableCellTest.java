package it.unibo.the100dayswar.model.cell;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import java.util.Optional;

import it.unibo.the100dayswar.commons.utilities.impl.PositionImpl;
import it.unibo.the100dayswar.model.cell.impl.BuildableCellImpl;
import it.unibo.the100dayswar.model.player.impl.PlayerImpl;
import it.unibo.the100dayswar.model.soldier.impl.SoldierImpl;

class BuildableCellTest {

    @Test
    void testIsBuildable() {
        final BuildableCellImpl cell = new BuildableCellImpl(new PositionImpl(0, 0), true, false);

        assertTrue(cell.isBuildable());
        assertFalse(cell.isSpawn());
    }

    @Test
    void testIsFree() {
        final BuildableCellImpl cell = new BuildableCellImpl(new PositionImpl(0, 1), true, true);

        assertTrue(cell.isFree());

        final PlayerImpl player = new PlayerImpl("Player1", cell);
        final SoldierImpl soldier = new SoldierImpl(player);
        cell.setOccupation(Optional.of(soldier));

        assertFalse(cell.isFree());
    }

    @Test
    void testSetAndGetOccupation() {
        final BuildableCellImpl cell = new BuildableCellImpl(new PositionImpl(0, 2), true, true);

        assertTrue(cell.getUnit().isEmpty());

        final PlayerImpl player = new PlayerImpl("Player1", cell);
        final SoldierImpl soldier = new SoldierImpl(player);
        cell.setOccupation(Optional.of(soldier));

        assertTrue(cell.getUnit().isPresent());
        assertEquals(soldier, cell.getUnit().get());
    }

}