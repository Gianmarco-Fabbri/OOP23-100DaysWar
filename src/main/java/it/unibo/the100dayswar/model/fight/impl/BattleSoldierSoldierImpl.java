package it.unibo.the100dayswar.model.fight.impl;

import it.unibo.the100dayswar.model.dice.api.Dice;
import it.unibo.the100dayswar.model.dice.impl.DiceImpl;
import it.unibo.the100dayswar.model.fight.api.BattleSoldierSoldier;
import it.unibo.the100dayswar.model.soldier.api.Soldier;
import it.unibo.the100dayswar.commons.utilities.impl.Pair;

/**
 * Implementation of the battle between two soldiers.
 */
public class BattleSoldierSoldierImpl implements BattleSoldierSoldier {

    private final Dice dice;
    private int lastAttackerRoll;
    private int lastDefenderRoll;

    /** 
     * Constructor for the class BattleSoldierSoldier.
     */
    public BattleSoldierSoldierImpl() {
        this.dice = new DiceImpl();
    }
    /**
     * start a fight between two soldier.
     */
    @Override
    public boolean startFight(final Soldier attacker, final Soldier defender) {
        final Pair<Soldier, Soldier> result = resolveFight(attacker, defender);
        if (result.getFirst() != null && result.getSecond() != null) {
            result.getSecond().setHealth(0);

        } else {
            attacker.setHealth(0);
            defender.setHealth(0);
        }
        return true;
    }
    /**
     * Resolve the fight between two soldiers.
     * 
     * @param attacker
     * @param defender
     * @return the pair of soldiers, the first is the winner, the second is the loser, both are null in case of draw
      */
    private Pair<Soldier, Soldier> resolveFight(final Soldier attacker, final Soldier defender) {
        lastAttackerRoll = calculateRollFromSoldier(attacker);
        lastDefenderRoll = calculateRollFromSoldier(defender);

        if (lastAttackerRoll > lastDefenderRoll) {
            return new Pair<>(attacker, defender);
        } else if (lastAttackerRoll < lastDefenderRoll) {
            return new Pair<>(defender, attacker);
        } else {
            return new Pair<>(null, null);
        }
    }

    /**
     * Returns the total dice roll of the attacker in the last fight.
     * @return the attacker's roll
     */
    public int getLastAttackerRoll() {
        return lastAttackerRoll;
    }

    /**
     * Returns the total dice roll of the defender in the last fight.
     * @return the defender's roll
     */
    public int getLastDefenderRoll() {
        return lastDefenderRoll;
    }
    /**
     * Calculate the roll of a specific soldier.
     * 
     * @param soldier
     * @return the roll of the soldier
      */
    private int calculateRollFromSoldier(final Soldier soldier) {
        int total = 0;
        for (int i = 0; i < soldier.getLevel(); i++) {
            total += dice.roll();
        }
        return total;
    }
}
