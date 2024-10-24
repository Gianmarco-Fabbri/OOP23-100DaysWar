package it.unibo.the100dayswar.model.player.api;


import java.util.Set;

import it.unibo.the100dayswar.commons.patterns.Observer;
import it.unibo.the100dayswar.commons.utilities.impl.GameEvent;
import it.unibo.the100dayswar.model.map.api.BuildableCell;
import it.unibo.the100dayswar.model.unit.api.Buyable;
import it.unibo.the100dayswar.model.unit.api.Movable;
import it.unibo.the100dayswar.model.unit.api.Unit;
import it.unibo.the100dayswar.model.unit.api.Soldier;

/**
 * Interface that defines the behavior of the possible moves that can be performed by a player.
 * This interface extends the basic Player interface by defining methods corresponding to the actions 
 * that a player can perform.
 */
public interface Player extends Observer<Player> {
    /**
     * Returns the username of the player.
     *
     * @return the username of the player
     */
    String getUsername();
    /**
     * Returns the bank account of the player.
     *
     * @return the bank account of the player
     */
    BankAccount getBankAccount();
    /**
     * Returns the spawn point of player's units.
     *
     * @return the spawn point of the player
     */
    BuildableCell getSpawnPoint();
    /**
     * The units owned by the player.
     * 
     * @return the units owned by the player
     */
    Set<Unit> getUnits();
    /**
     * Buys a unit, such as a soldier or tower, and deducts the cost from the player's bank account.
     *
     * @param unit the unit to buy
     * @throws IllegalStateException if the player does not have enough resources
     */
    void buyUnit(Buyable unit);
    /**
     * Upgrades a given unit to increase its power, at the cost of resources.
     *
     * @param unit the unit to upgrade
     * @throws IllegalStateException if the player does not have enough resources to upgrade
     */
    void upgradeUnit(Unit unit);
    /**
     * Moves a unit from one cell to another on the game map.
     *
     * @param unit the unit to move
     * @throws IllegalArgumentException if the move is not allowed
     */
    void moveUnit(Movable unit);
    /**
     * Adds resources to the player's bank account.
     *
     * @param amount the amount of resources to add
     */
    void earnResources(int amount);
    /**
     * Returns the soldiers owned by the player.
     *
     * @return the soldiers owned by the player
     */
    Set<Soldier> getSoldiers();
    /**
     * Returns a copy of the player.
     *
     * @return a copy of the player
     * @throws CloneNotSupportedException 
     */
    Player copy() throws CloneNotSupportedException;
    /**
     * Method that updates the player based on the event that occurred.
     * 
     * @param event is the event that occurred.
     * @param player is the player that will be updated.
     */
    @Override
    void update(GameEvent event, Player player);
}
