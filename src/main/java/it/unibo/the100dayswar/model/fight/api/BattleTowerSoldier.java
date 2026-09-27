package it.unibo.the100dayswar.model.fight.api;

import it.unibo.the100dayswar.model.soldier.api.Soldier;
import it.unibo.the100dayswar.model.tower.api.Tower;

/**
 * Interface rappresenting a battle started by a tower against a soldier.
 */
public interface BattleTowerSoldier extends Battle<Tower, Soldier> {
    /**
     * start a fight between a tower and a soldier.
     * 
     * @param attacker tower that attack
     * @param defender soldier that defend
     */
    @Override
    boolean startFight(Tower attacker, Soldier defender);
}
