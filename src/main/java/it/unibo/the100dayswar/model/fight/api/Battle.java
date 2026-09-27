package it.unibo.the100dayswar.model.fight.api;

import it.unibo.the100dayswar.model.unit.api.Combatant;

/**
 * generic interface for a battle between two combatant.
 * 
 * @param <T> type of attacker
 * @param <U> type of defender
 */
public interface Battle<T extends Combatant, U extends Combatant> {
    /**
     * Start the fight between the attacher and defender.
     * 
     * @param attacker the unit attacker
     * @param defender the unit defender
     * @return true if the attack occurred
     */
    boolean startFight(T attacker, U defender);
}
