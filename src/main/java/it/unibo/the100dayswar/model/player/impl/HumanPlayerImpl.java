package it.unibo.the100dayswar.model.player.impl;

import it.unibo.the100dayswar.model.cell.api.Cell;
import it.unibo.the100dayswar.model.player.api.HumanPlayer;
import it.unibo.the100dayswar.model.player.api.Player;

/**
 * Implementation of the HumanPlayer interface.
 * It has only the constructors.
 */
public class HumanPlayerImpl extends PlayerImpl implements HumanPlayer {
    private static final long serialVersionUID = 1L;
    /**
     * Constructor for the human player from the given parameters.
     *
     * @param username   the username of the player
     * @param spawnPoint the spawn point of the player
     */
    public HumanPlayerImpl(final String username, final Cell spawnPoint) {
        super(username, spawnPoint);
    }
    /**
     * Constructor for the human player from the given player.
     * 
     * @param player player to copy
     */
    public HumanPlayerImpl(final HumanPlayer player) {
        super(player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player copy() {
        return new HumanPlayerImpl(this);
    }
}
