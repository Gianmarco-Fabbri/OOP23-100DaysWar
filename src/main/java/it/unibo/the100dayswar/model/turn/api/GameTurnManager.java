package it.unibo.the100dayswar.model.turn.api;

import java.io.Serializable;

import it.unibo.the100dayswar.commons.patterns.Observer;
import it.unibo.the100dayswar.commons.utilities.impl.Pair;
import it.unibo.the100dayswar.model.player.api.Player;
import it.unibo.the100dayswar.model.unit.api.Unit;

/**
 * An Interface for managing the turn swith between the two player.
 */
public interface GameTurnManager extends Observer<Pair<Player, Unit>>, Serializable {
    /**
     * get the current player.
     * @return the current player
     */
    Player getCurrentPlayer();
    /**
     * get the current GameDay.
     * @return the current GameDay
     */
    int getDay();
    /**
     * get the current player.
     * @return the index of the current player in the list
     */
    int getCurrentPlayerIndex();
    /**
     * get the current turn.
     * @return the current turn
     */
    int getCurrentTurn();
    /**
     * switch Turn to the other player.
     */
    void switchTurn();
    /**
     * increase the Turn counter.
     */
    void increaseTurn();
    /**
     * called when a player start his turn.
     */
    void playerStartTurn();
    /**
     * unsed to automatically change turn when day passes.
     */
    void onDayPassed();
    /**
     * start the timer for the day.
     */
    void startTimer();
    /**
     * stop the timer for the day.
     */
    void stopTimer();

    /** 
     * Update the turn manager.
     * 
     * @param source the source of the update
     */
    @Override
    void update(Pair<Player, Unit> source);

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
