package it.unibo.the100dayswar.model.fight.impl;

import it.unibo.the100dayswar.model.fight.api.Battle;
import it.unibo.the100dayswar.model.map.api.GameMap;
import it.unibo.the100dayswar.model.tower.api.Tower;
import it.unibo.the100dayswar.model.unit.api.Combatant;
import it.unibo.the100dayswar.model.soldier.api.Soldier;

/**
 * Factory class for creating instances of {@link Battle} using the factory pattern.
 */
public final class BattleFactory {

    private BattleFactory() {
    }

    /**
     * Creates the appropriate {@link Battle} implementation for the given pair of
     * combatants. No line-of-sight map is injected; use the overload that accepts a
     * {@link GameMap} when LOS checking is required for tower vs. soldier battles.
     *
     * @param <T>      the type of the attacker
     * @param <U>      the type of the defender
     * @param attacker the attacking combatant
     * @param defender the defending combatant
     * @return the {@link Battle} instance matching the combatant types
     */
    @SuppressWarnings("unchecked")
    public static <T extends Combatant, U extends Combatant> Battle<T, U> createBattle(
            final T attacker, final U defender) {
        return switch (attacker) {
            case Soldier s when defender instanceof Soldier -> (Battle<T, U>) new BattleSoldierSoldierImpl();
            case Soldier s when defender instanceof Tower   -> (Battle<T, U>) new BattleSoldierTowerImpl();
            case Tower   t when defender instanceof Soldier -> (Battle<T, U>) new BattleTowerSoldierImpl();
            default -> throw new IllegalArgumentException("Unsupported combatant types");
        };
    }

    /**
     * Creates the appropriate {@link Battle} implementation for the given pair of
     * combatants, injecting the supplied {@link GameMap} for line-of-sight checks
     * when the battle is of the tower-vs-soldier type.
     *
     * @param <T>      the type of the attacker
     * @param <U>      the type of the defender
     * @param attacker the attacking combatant
     * @param defender the defending combatant
     * @param map      the game map used for obstacle/LOS checking
     * @return the {@link Battle} instance matching the combatant types
     */
    @SuppressWarnings("unchecked")
    public static <T extends Combatant, U extends Combatant> Battle<T, U> createBattle(
            final T attacker, final U defender, final GameMap map) {
        return switch (attacker) {
            case Soldier s when defender instanceof Soldier -> (Battle<T, U>) new BattleSoldierSoldierImpl();
            case Soldier s when defender instanceof Tower   -> (Battle<T, U>) new BattleSoldierTowerImpl();
            case Tower   t when defender instanceof Soldier -> (Battle<T, U>) new BattleTowerSoldierImpl(map);
            default -> throw new IllegalArgumentException("Unsupported combatant types");
        };
    }
}
