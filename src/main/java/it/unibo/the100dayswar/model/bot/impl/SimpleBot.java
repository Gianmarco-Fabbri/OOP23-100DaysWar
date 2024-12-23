package it.unibo.the100dayswar.model.bot.impl;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import it.unibo.the100dayswar.model.bot.api.BotPlayer;
import it.unibo.the100dayswar.model.bot.api.BotStrategy;
import it.unibo.the100dayswar.model.cell.api.Cell;
import it.unibo.the100dayswar.model.cell.impl.CellImpl;
import it.unibo.the100dayswar.model.map.api.MapManager;
import it.unibo.the100dayswar.model.player.impl.PlayerImpl;

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

    /**
     * Constructor for the bot player.
     *
     * @param mapManager the map manager of the game
     */
    public SimpleBot(final MapManager mapManager) {
        super(BOT_NAME, mapManager.getBotSpawn());
        ActionType.clear();
        ActionType.add(mapManager);
        this.strategy = new SimpleBotStrategy();
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
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SimpleBot)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        final SimpleBot other = (SimpleBot) obj;
        return Objects.equals(this.enemySpawnPoint, other.enemySpawnPoint)
                && Objects.equals(this.gameMapCells, other.gameMapCells);
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), enemySpawnPoint, gameMapCells);
    }
}
