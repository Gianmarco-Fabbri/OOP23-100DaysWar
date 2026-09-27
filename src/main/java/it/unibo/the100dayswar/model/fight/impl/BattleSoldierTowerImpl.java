package it.unibo.the100dayswar.model.fight.impl;

import it.unibo.the100dayswar.model.soldier.api.Soldier;
import it.unibo.the100dayswar.model.tower.api.Tower;
import it.unibo.the100dayswar.model.fight.api.BattleSoldierTower;

/**
 * Implementation of the battle between a soldier (attacker) and a tower (defender).
 */
public class BattleSoldierTowerImpl implements BattleSoldierTower {

    private static final int DEFAULT_DAMAGE = 30;
    /** 
     * start a fight between a soldier and a tower.
     */
    @Override
    public boolean startFight(final Soldier attacker, final Tower defender) {
        if (calculateDistance(attacker, defender)) {
            final int damage = DEFAULT_DAMAGE * attacker.getLevel();
            defender.takeDamage(damage);
        }
        return true;
    }

    private boolean calculateDistance(final Soldier soldier, final Tower tower) {
        final int distanceX = Math.abs(tower.getPosition().getPosition().getX() - soldier.getPosition().getPosition().getX());
        final int distanceY = Math.abs(tower.getPosition().getPosition().getY() - soldier.getPosition().getPosition().getY());
        return distanceX <= 1 && distanceY <= 1;
    }
}
