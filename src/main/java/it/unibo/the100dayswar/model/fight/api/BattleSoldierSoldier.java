package it.unibo.the100dayswar.model.fight.api;

import it.unibo.the100dayswar.model.unit.api.Soldier;

/**
 * Interface rappresenting a battle started by a soldier against another soldier.
 */
public interface BattleSoldierSoldier extends Battle<Soldier, Soldier> {
    /**
     * start a fight between two soldier.
     * 
     * @param attacker soldier that attack
     * @param defender soldier that defend
     */
    @Override
    void startFight(Soldier attacker, Soldier defender);
}
