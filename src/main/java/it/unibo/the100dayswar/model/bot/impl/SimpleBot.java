package it.unibo.the100dayswar.model.bot.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import it.unibo.the100dayswar.model.bot.api.BotPlayer;
import it.unibo.the100dayswar.model.bot.api.BotStrategy;
import it.unibo.the100dayswar.model.cell.api.Cell;
import it.unibo.the100dayswar.model.cell.impl.CellImpl;
import it.unibo.the100dayswar.model.map.api.MapManager;
import it.unibo.the100dayswar.model.map.impl.MapManagerImpl;
import it.unibo.the100dayswar.model.player.impl.PlayerImpl;
import it.unibo.the100dayswar.model.player.api.Player;

/**
 * A simple implementation of a bot player that uses a strategy
 * to decide which move is the best with simple checks and calculations.
 */
public class SimpleBot extends PlayerImpl implements BotPlayer {
    private static final long serialVersionUID = 1L;
    private static final String BOT_NAME = "Bot1";

    private final Cell enemySpawnPoint;
    private final Set<Cell> gameMapCells;
    private final BotStrategy strategy;
    private final MapManager mapManager;

    /**
     * Constructor for the bot player.
     *
     * @param mapManager the map manager of the game
     */
    public SimpleBot(final MapManager mapManager) {
        super(BOT_NAME, mapManager.getBotSpawn());
        ActionType.clear();
        this.mapManager = mapManager;
        ActionType.add(mapManager);
        this.strategy = new SimpleBotStrategy();
        this.enemySpawnPoint = mapManager.getPlayerSpawn();
        this.gameMapCells = mapManager.getMapAsAStream().collect(Collectors.toSet());
    }

    /**
     * Constructor for the bot player.
     *
     * @param botPlayer the bot player to copy
     */
    public SimpleBot(final BotPlayer botPlayer) {
        super(botPlayer.getUsername(), botPlayer.getSpawnPoint());
        this.mapManager = botPlayer.getMapManager();
        this.strategy = botPlayer.getStrategy();
        this.enemySpawnPoint = mapManager.getPlayerSpawn();
        this.gameMapCells = mapManager.getMapAsAStream().collect(Collectors.toSet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void makeMove() {
        strategy.apply(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Cell getEnemySpawnPoint() {
        return new CellImpl(enemySpawnPoint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Cell> getAllCells() {
        return new HashSet<>(gameMapCells);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MapManager getMapManager() {
       return new MapManagerImpl(this.mapManager);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BotStrategy getStrategy() {
        return this.strategy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player copy() {
        return new SimpleBot(this);
    }
}
