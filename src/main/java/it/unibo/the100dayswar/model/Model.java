package it.unibo.the100dayswar.model;

import java.util.stream.Stream;
import it.unibo.the100dayswar.commons.utilities.impl.Pair;
import it.unibo.the100dayswar.model.bot.api.BotPlayer;
import it.unibo.the100dayswar.model.cell.api.Cell;
import it.unibo.the100dayswar.model.map.api.GameMap;
import it.unibo.the100dayswar.model.player.api.HumanPlayer;
import it.unibo.the100dayswar.model.player.api.Player;
import it.unibo.the100dayswar.model.statistic.api.GameStatistics;
import it.unibo.the100dayswar.model.tower.api.TowerType;
import it.unibo.the100dayswar.model.unit.api.Unit;

/** 
 * The interface of the model of the game.
 */
public interface Model {

    /**
     * Buy a tower of the specified type.
     * 
     * @param type the type of the tower
     * @param position the position of the tower
     */
    void buyTower(TowerType type, Cell position);

    /**
     * Buy a soldier.
     */
    void buySoldier();

    /**
     * Add a player to the game.
     * 
     * @param username the name of the player
     */
    void addPlayer(String username);

    /**
     * Move a soldier following the specified direction.
     * 
     * @param source the pair of the soldier and the cell
     * 
     * @return true if the soldier was moved correctly, false otherwise
     */
    boolean moveSoldier(Pair<Unit, Cell> source);

    /**
     * Save the current game.
     * 
     * @param path the path of the saved file
     * 
     * @return true if the game is saved correctly
     */
    boolean saveGame(String path);

    /**
     * Upgrade the unit.
     * 
     * @param unit the unit to upgrade
     */
    void upgradeUnit(Unit unit);

    /**
     * Gets the width of the map.
     * 
     * @return the width of the map
     */
    double getMapWidth();

    /**
     * Gets the height of the map.
     * 
     * @return the height of the map
     */
    double getMapHeight();

    /**
     * Gets the map as a 2D array of cells.
     * 
     * @return the map
     */
    GameMap getMap();

    /**
     * Get the map as a stream of cells.
     * 
     * @return the map as a stream of cells
     */
    Stream<Cell> getMapStream();

    /**
     * Gets the GameStatistics.
     * 
     * @return the GameStatistics
     */
    GameStatistics getGameStatistics();

    /**
     * Skip the current turn.
     */
    void skipTurn();

    /**
     * Pause the game.
     */
    void pauseGame();

    /**
     * Resume the game.
     */
    void resumeGame();

    /**
     * Stop the timer and release the resources.
     */
    void stopTimer();

    /**
     * Gets the human player in the game.
     * 
     * @return the human player
     */
    HumanPlayer getHumanPlayer();

    /**
     * Gets the bot player in the game.
     * 
     * @return the bot player
     */
    BotPlayer getBotPlayer();

    /**
     * Gets the current player whose turn is active.
     * 
     * @return the current player
     */
    Player getCurrentPlayer();

    /**
     * Checks if the game is over.
     * 
     * @return true if the game is over, false otherwise
     */
    boolean isOver();
    /**
     * Determines the winner of the game.
     * 
     * These are the conditions for winning the game:
     * - A player has captured the opposing player's spawn point by moving one of 
     *   their soldiers onto the spawn cell.
     * - If neither player has captured the other's spawn point by the end of the 
     *   100th day, the player controlling the majority of cells on the map is 
     *   declared the winner.
     * 
     * @return the player who won the game
     */
    Player getWinner();

    /**
     * Gets the current game day.
     * 
     * @return the current game day
     */
    int getGameDay();

    /**
     * Set the update callback.
     *
     * @param callback the callback
     */
    void setUpdateCallback(Runnable callback);
    /**
     * Set the tower shot callback.
     *
     * @param callback the callback
     */
    void setTowerShotCallback(java.util.function.Consumer<Pair<Integer, Integer>> callback);
}
