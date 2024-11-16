package it.unibo.the100dayswar.model.fight.impl;

import it.unibo.the100dayswar.model.fight.api.BattleSoldierSoldier;
import it.unibo.the100dayswar.model.fight.api.Dice;
import it.unibo.the100dayswar.model.soldier.api.Soldier;
import it.unibo.the100dayswar.commons.utilities.impl.Pair;

/**
 * Implementation of the battle between two soldiers.
 */
public class BattleSoldierSoldierImpl implements BattleSoldierSoldier {

    private final Dice dice;
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
    public void startFight(final Soldier attacker, final Soldier defender) {
        final Pair<Soldier, Soldier> result = resolveFight(attacker, defender);
        if (result.getFirst() != null && result.getSecond() != null) {
            final Soldier loser = result.getSecond();
            loser.setHealth(0);

        } else {
            result.getFirst().setHealth(0);
            result.getSecond().setHealth(0);
        }
    }
    /**
     * Resolve the fight between two soldiers.
     * 
     * @param attacker
     * @param defender
     * @return the pair of soldiers, the first is the winner, the second is the loser, both are null in case of draw
      */
    private Pair<Soldier, Soldier> resolveFight(final Soldier attacker, final Soldier defender) {
        final int attackerRoll = calculateRollFromSoldier(attacker);
        final int defenderRoll = calculateRollFromSoldier(defender);

        if (attackerRoll > defenderRoll) {
            return new Pair<>(attacker, defender);
        } else if (attackerRoll < defenderRoll) {
            return new Pair<>(defender, attacker);
        } else {
            return new Pair<>(null, null);
        }
    }
    /**
     * Calculate the roll of a specific soldier.
     * 
     * @param soldier
     * @return the roll of the soldier
      */
    private int calculateRollFromSoldier(final Soldier soldier) {
        int total = 0;
        for (int i = 0; i < soldier.level(); i++) {
            total += dice.roll();
        }
        return total;
    }
}
