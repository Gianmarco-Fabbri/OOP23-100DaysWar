package it.unibo.the100dayswar.controller.events;

/**
 * Record representing the result of a battle to be handled by the UI.
 * 
 * @param type the type of battle
 * @param attackerRoll the attacker roll
 * @param defenderRoll the defender roll
 * @param attackerWon if the attacker won
 * @param towerDamage if the type is TOWER_VS_SOLDIER, the damage dealt
 * @param targetHealth if the type is TOWER_VS_SOLDIER, the remaining health
 */
public record BattleResultEvent(
    BattleType type,
    int attackerRoll,
    int defenderRoll,
    boolean attackerWon,
    int towerDamage,
    int targetHealth
) {
    /**
     * Enum for the battle type.
     */
    public enum BattleType {
        /** Soldier vs Soldier battle. */
        SOLDIER_VS_SOLDIER,
        /** Tower vs Soldier battle. */
        TOWER_VS_SOLDIER
    }
}
