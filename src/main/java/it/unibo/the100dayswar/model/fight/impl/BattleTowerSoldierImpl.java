package it.unibo.the100dayswar.model.fight.impl;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.the100dayswar.commons.utilities.impl.PositionImpl;
import it.unibo.the100dayswar.model.cell.api.Cell;
import it.unibo.the100dayswar.model.fight.api.BattleTowerSoldier;
import it.unibo.the100dayswar.model.map.api.GameMap;
import it.unibo.the100dayswar.model.soldier.api.Soldier;
import it.unibo.the100dayswar.model.tower.api.Tower;

/**
 * Implementation of the battle between a tower (attacker) and a soldier (defender).
 * Supports optional line-of-sight checking via Bresenham's algorithm when a
 * {@link GameMap} is provided.
 */
public class BattleTowerSoldierImpl implements BattleTowerSoldier {

    private static final int TOWER_RANGE = 2;

    private final GameMap map;

    /**
     * Constructs a {@code BattleTowerSoldierImpl} with line-of-sight support.
     *
     * @param map the game map used to check for obstacles between the tower and
     *            the target soldier
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
            justification = "GameMap is an interface; storing the reference is intentional and safe")
    public BattleTowerSoldierImpl(final GameMap map) {
        this.map = map;
    }

    /**
     * No-arg constructor retained for backward compatibility.
     * When used, line-of-sight checking is disabled.
     */
    public BattleTowerSoldierImpl() {
        this.map = null;
    }

    /**
     * Starts a fight between a tower (attacker) and a soldier (defender).
     * The tower deals damage only when the soldier is within range and there is
     * a clear line of sight (no non-buildable cells in between).
     */
    @Override
    public boolean startFight(final Tower attacker, final Soldier defender) {
        if (!calculateDistance(attacker, defender)) {
            return false;
        }
        if (map != null && !hasLineOfSight(attacker, defender)) {
            return false; // obstacle in between — tower cannot fire
        }
        defender.takeDamage(attacker.getDamage());
        return true;
    }

    /**
     * Checks whether the soldier is within the tower's attack range.
     *
     * @param tower   the attacking tower
     * @param soldier the target soldier
     * @return {@code true} if the soldier is within range
     */
    private boolean calculateDistance(final Tower tower, final Soldier soldier) {
        final Cell towerCell = tower.getPosition();
        final Cell soldierCell = soldier.getPosition();
        final int distanceX = Math.abs(towerCell.getPosition().getX() - soldierCell.getPosition().getX());
        final int distanceY = Math.abs(towerCell.getPosition().getY() - soldierCell.getPosition().getY());
        return distanceX <= TOWER_RANGE && distanceY <= TOWER_RANGE;
    }

    /**
     * Determines whether there is a clear line of sight between the tower and the
     * soldier using Bresenham's line algorithm. Only intermediate cells (neither
     * the tower's cell nor the soldier's cell) are checked for obstacles.
     *
     * @param tower   the attacking tower
     * @param soldier the target soldier
     * @return {@code true} if no non-buildable cell blocks the path
     */
    private boolean hasLineOfSight(final Tower tower, final Soldier soldier) {
        final int startX = tower.getPosition().getPosition().getX();
        final int startY = tower.getPosition().getPosition().getY();
        final int endX = soldier.getPosition().getPosition().getX();
        final int endY = soldier.getPosition().getPosition().getY();

        int x0 = startX;
        int y0 = startY;

        final int dx = Math.abs(endX - x0);
        final int dy = Math.abs(endY - y0);
        final int sx = x0 < endX ? 1 : -1;
        final int sy = y0 < endY ? 1 : -1;
        int err = dx - dy;

        while (!(x0 == endX && y0 == endY)) {
            // Skip the starting cell (tower) and the ending cell (soldier)
            final boolean isStart = x0 == startX && y0 == startY;
            final boolean isEnd   = x0 == endX   && y0 == endY;
            if (!isStart && !isEnd) {
                final Cell cell = map.getCell(new PositionImpl(x0, y0));
                if (!cell.isBuildable()) {
                    return false;
                }
            }
            final int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x0  += sx;
            }
            if (e2 < dx) {
                err += dx;
                y0  += sy;
            }
        }
        return true;
    }
}
