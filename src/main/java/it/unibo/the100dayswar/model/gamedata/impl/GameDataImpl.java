package it.unibo.the100dayswar.model.gamedata.impl;

import it.unibo.the100dayswar.model.bot.api.BotPlayer;
import it.unibo.the100dayswar.model.bot.impl.SimpleBot;
import it.unibo.the100dayswar.model.gamedata.api.GameData;
import it.unibo.the100dayswar.model.map.api.MapManager;
import it.unibo.the100dayswar.model.map.impl.MapManagerImpl;
import it.unibo.the100dayswar.model.player.api.HumanPlayer;
import it.unibo.the100dayswar.model.player.impl.HumanPlayerImpl;
import it.unibo.the100dayswar.model.turn.api.GameTurnManager;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Class that saves all the data that need to be serialized.
 */
public class GameDataImpl implements GameData {
    private static final long serialVersionUID = 1L;

    private final HumanPlayer humanData;
    private final BotPlayer botData;
    private final MapManager mapManager;
    private final GameTurnManager gameTurnManager;

    /**
     * Constructor of GameDataImpl, initializes the object
     * with the given params.
     * 
     * @param human the human player to save
     * @param bot the bot player to save
     * @param mapManager the mapManager of the current game
     * @param gameTurnManager the game turn manager of the current game
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Intentional")
    public GameDataImpl(
            final HumanPlayer human,
            final BotPlayer bot,
            final MapManager mapManager, 
            final GameTurnManager gameTurnManager
            ) {
        this.humanData = new HumanPlayerImpl(human);
        this.botData = new SimpleBot(bot);
        this.mapManager = new MapManagerImpl(mapManager);
        this.gameTurnManager = gameTurnManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HumanPlayer getHumanData() {
        return new HumanPlayerImpl(humanData);
    }

     /**
     * {@inheritDoc}
     */
    @Override
    public BotPlayer getBotData() {
        return new SimpleBot(botData);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MapManager getMapManager() {
        return new MapManagerImpl(mapManager);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "Intentional")
    @Override
    public GameTurnManager getGameTurnManager() {
        return gameTurnManager;
    }
}
