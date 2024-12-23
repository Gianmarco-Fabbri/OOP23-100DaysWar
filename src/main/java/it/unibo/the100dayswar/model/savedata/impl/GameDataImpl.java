package it.unibo.the100dayswar.model.savedata.impl;

import it.unibo.the100dayswar.model.map.api.MapManager;
import it.unibo.the100dayswar.model.map.impl.MapManagerImpl;
import it.unibo.the100dayswar.model.player.api.Player;
import it.unibo.the100dayswar.model.player.impl.PlayerImpl;
import it.unibo.the100dayswar.model.savedata.api.GameData;
import it.unibo.the100dayswar.model.turn.api.GameTurnManager;

/**
 * Class that saves all the data that need to be serialized.
 */
public class GameDataImpl implements GameData {
    private static final long serialVersionUID = 1L;

    private final Player playerData1;
    private final Player playerData2;
    private final MapManager mapManager;
    private final GameTurnManager gameTurnManager;

    /**
     * Constructor of GameDataImpl, initializes the object
     * with the given params.
     * 
     * @param playerData1 the player to save
     * @param playerData2 the player to save
     * @param mapManager the mapManager of the current game
     * @param gameTurnManager the game turn manager of the current game
     */
    public GameDataImpl(
            final Player playerData1, 
            final Player playerData2, 
            final MapManager mapManager, 
            final GameTurnManager gameTurnManager) {

        if (playerData1.equals(playerData2)) {
            throw new IllegalArgumentException("playerData1 and playerData2 must be different");
        }

        this.playerData1 = new PlayerImpl(playerData1);
        this.playerData2 = new PlayerImpl(playerData2);
        this.mapManager = new MapManagerImpl(mapManager);   // TODO devo avere una deepCopy del mapManager
        this.gameTurnManager = gameTurnManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player getPlayerData1() {
        return new PlayerImpl(playerData1);
    }

     /**
     * {@inheritDoc}
     */
    @Override
    public Player getPlayerData2() {
        return new PlayerImpl(playerData2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MapManager getMapManager() {
        return new MapManagerImpl(mapManager);  // TODO devo ritornare una deepCopy del mapManager
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameTurnManager getGameTurnManager() {
        return gameTurnManager;
    }
}
