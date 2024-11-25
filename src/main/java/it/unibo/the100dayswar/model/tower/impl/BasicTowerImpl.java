package it.unibo.the100dayswar.model.tower.impl;

import it.unibo.the100dayswar.model.cell.api.Cell;
import it.unibo.the100dayswar.model.player.api.Player;
import it.unibo.the100dayswar.model.tower.api.BasicTower;
import it.unibo.the100dayswar.model.tower.api.TowerType;

/**
 * Class that implements a basic type of tower extending 
 * AbstractTower.
 */
public class BasicTowerImpl extends AbstractTower implements BasicTower {
    private static final long serialVersionUID = 1L;

    private static final int HEALTH_MULTYPLIER_BASIC = 10;
    private static final int UPGRADE_MULTYPLIER_BASIC = 2;
    private static final int DAMAGE_MULTYPLIER_BASIC = 2;

    private static final int BASIC_HEALTH = TowerType.BASIC.getPrice() * HEALTH_MULTYPLIER_BASIC;
    private static final int BASIC_UPGRADE = TowerType.BASIC.getPrice() * UPGRADE_MULTYPLIER_BASIC;
    private static final int BASIC_DAMAGE = TowerType.BASIC.getPrice() * DAMAGE_MULTYPLIER_BASIC;


    /**
     * Constructs a basic tower.
     * 
     * @param owner the owner of the basic tower
     * @param position the positon of basic tower in the map
     */
    public BasicTowerImpl(final Player owner, final Cell position) {
        super(
            TowerType.BASIC, 
            owner,
            BASIC_HEALTH, 
            position, 
            TowerType.BASIC.getPrice(),
            BASIC_UPGRADE,
            BASIC_DAMAGE
        );
    }

    /*
     * TODO metodo identico a quello in advanced
     * cambia solamente il nome delle costanti.
     * Probabilmente è meglio metterlo nella classe
     * astratta.
     */
    /**
     * {@inheritDoc}
     */
    @Override
    public void upgrade() {
        if (this.getLevel() < MAX_LEVEL) {
            this.incrementLevel();

            this.setHealth(this.currentHealth() * UPGRADE_MULTYPLIER_BASIC);

            this.setDamage(this.getDamage() * UPGRADE_MULTYPLIER_BASIC);
        }
    }
}
