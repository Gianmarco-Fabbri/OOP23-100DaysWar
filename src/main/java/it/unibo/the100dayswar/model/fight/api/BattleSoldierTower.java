package it.unibo.the100dayswar.model.fight.api;

import it.unibo.the100dayswar.model.soldier.api.Soldier;
import it.unibo.the100dayswar.model.tower.api.Tower;

/**
 * Interface rappresenting a battle started by a soldier against tower.
 */
public interface BattleSoldierTower extends Battle<Soldier, Tower> {
    /**
     * start a fight between a soldier and a tower.
     * 
     * @param attacker soldier that attack
     * @param defender tower that defend
     */
    @Override
    boolean startFight(Soldier attacker, Tower defender);
}
