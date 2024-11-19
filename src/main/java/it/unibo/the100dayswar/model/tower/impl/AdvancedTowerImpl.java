package it.unibo.the100dayswar.model.tower.impl;

import it.unibo.the100dayswar.model.cell.api.Cell;
import it.unibo.the100dayswar.model.player.api.Player;
import it.unibo.the100dayswar.model.tower.api.AdvancedTower;
import it.unibo.the100dayswar.model.tower.api.TowerType;

/**
 * Class that implements an advanced type of tower extending 
 * AbstractTower.
 */
public class AdvancedTowerImpl extends AbstractTower implements AdvancedTower {
    private static final long serialVersionUID = 1L;

    private static final int HEALTH_MULTYPLIER_ADVANCED = 12;
    private static final int UPGRADE_MULTYPLIER_ADVANCED = 2;
    private static final int DAMAGE_MULTYPLIER_ADVANCED = 2;

    private static final int ADVANCED_HEALTH = TowerType.ADVANCED.getPrice() * HEALTH_MULTYPLIER_ADVANCED;
    private static final int ADVANCED_UPGRADE = TowerType.ADVANCED.getPrice() * UPGRADE_MULTYPLIER_ADVANCED;
    private static final int ADVANCED_DAMAGE = TowerType.ADVANCED.getPrice() * UPGRADE_MULTYPLIER_ADVANCED;

    /**
     * Constructs an advanced tower.
     * 
     * @param owner the owner of the advanced tower
     * @param position the position of the advanced tower in the map
     */
    public AdvancedTowerImpl(final Player owner, final Cell position) {
        super(
            TowerType.ADVANCED, 
            owner,
            ADVANCED_HEALTH, 
            position, 
            TowerType.ADVANCED.getPrice(),
            ADVANCED_UPGRADE,
            ADVANCED_DAMAGE
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void upgrade() {
        if (this.level() < MAX_LEVEL) {
            this.incrementLevel();

            this.setHealth(ADVANCED_HEALTH * HEALTH_MULTYPLIER_ADVANCED);

            this.setDamage(this.getDamage() * DAMAGE_MULTYPLIER_ADVANCED);
        }
    }
}
