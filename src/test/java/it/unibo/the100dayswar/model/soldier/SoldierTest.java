package it.unibo.the100dayswar.model.soldier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.the100dayswar.model.soldier.api.Soldier;
import it.unibo.the100dayswar.model.soldier.impl.SoldierImpl;
import it.unibo.the100dayswar.model.tower.api.Tower;
import it.unibo.the100dayswar.model.tower.impl.AdvancedTowerImpl;
import it.unibo.the100dayswar.commons.utilities.impl.PositionImpl;
import it.unibo.the100dayswar.model.player.api.Player;
import it.unibo.the100dayswar.model.player.impl.HumanPlayerImpl;
import it.unibo.the100dayswar.model.cell.api.Cell;
import it.unibo.the100dayswar.model.cell.impl.CellImpl;

/**
 * Test suite for SoldierImpl.
 */
class SoldierTest {

    private static final int INITIAL_SOLDIER_HEALTH = 200;
    private static final int HEALTH_AFTER_UPGRADE = 250;
    private static final int DEF_SOLDIER_DAMAGE = 30;
    private static final int INITIAL_SOLDIER_LEVEL = 1;
    private static final int UPGRADED_SOLDIER_LEVEL = 2;
    private static final int TARGET_HEALTH = 350;

    private Player testPlayer;
    private Soldier soldier;

    /** 
     * Setup the test environment.
     */
    @BeforeEach
    void setUp() {
        testPlayer = new HumanPlayerImpl("testPlayer", 
            new CellImpl(new PositionImpl(1, 1), 
            true, 
            true));
        soldier = new SoldierImpl(testPlayer);
    }

    @Test
    void testInitialization() {
        assertEquals(testPlayer, soldier.getOwner());
        assertEquals(testPlayer.getSpawnPoint().getPosition(), soldier.getPosition().getPosition());
        final Cell testCell = new CellImpl(new PositionImpl(2, 2), true, true);
        soldier.move(testCell);
        assertEquals(testCell, soldier.getPosition());
    }

    @Test
    void testUpgrade() {
        assertEquals(INITIAL_SOLDIER_LEVEL, soldier.getLevel());
        assertEquals(INITIAL_SOLDIER_HEALTH, soldier.currentHealth());
        soldier.upgrade();
        assertEquals(UPGRADED_SOLDIER_LEVEL, soldier.getLevel());
        assertEquals(HEALTH_AFTER_UPGRADE, soldier.currentHealth());
    }

    @Test
    void testAttackSoldierWithDifferentLevel() {
        final Soldier target = new SoldierImpl(testPlayer);
        target.upgrade();
        target.upgrade();
        assertEquals(INITIAL_SOLDIER_HEALTH, soldier.currentHealth());
        assertEquals(TARGET_HEALTH, target.currentHealth());
        soldier.performAttack(target);
        final boolean attackerWon = soldier.currentHealth() == INITIAL_SOLDIER_HEALTH
            && target.currentHealth() == 0;
        final boolean defenderWon = soldier.currentHealth() == 0
            && target.currentHealth() == TARGET_HEALTH;
        final boolean draw = soldier.currentHealth() == 0 && target.currentHealth() == 0;
        assertTrue(attackerWon || defenderWon || draw);
    }

    @Test
    void testAttackTower() {
        final Tower tower = new AdvancedTowerImpl(testPlayer, new CellImpl(new PositionImpl(1, 1), true, true));
        final int towerHealth = tower.currentHealth();
        assertEquals(INITIAL_SOLDIER_HEALTH, soldier.currentHealth());
        soldier.performAttack(tower);
        assertTrue(
            soldier.currentHealth() == INITIAL_SOLDIER_HEALTH 
            && tower.currentHealth() == towerHealth - DEF_SOLDIER_DAMAGE * soldier.getLevel()
        );
    }
}
