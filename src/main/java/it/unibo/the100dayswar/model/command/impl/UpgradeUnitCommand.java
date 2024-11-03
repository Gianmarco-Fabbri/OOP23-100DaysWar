package it.unibo.the100dayswar.model.command.impl;

import it.unibo.the100dayswar.model.command.api.UpgradeCommand;
import it.unibo.the100dayswar.model.player.api.Player;
import it.unibo.the100dayswar.model.unit.api.Buyable;

/**
 * An implementations of the command pattern that represents the upgrade of a buyable unit.
 */
public class UpgradeUnitCommand implements UpgradeCommand {
    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final Player player, final Buyable unit) {
        player.spendResources(unit.costToUpgrade());
        unit.upgrade();
    }
}