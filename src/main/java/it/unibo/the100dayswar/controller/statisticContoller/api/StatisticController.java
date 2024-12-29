package it.unibo.the100dayswar.controller.statisticContoller.api;

import it.unibo.the100dayswar.model.player.api.Player;

public interface StatisticController {

    /**
     * 
     * @param player the player.
     * @return the number of soldiers of the player.
     */
     Integer getSoldiers(Player player);

    /**
     * @param player the player.
     * @return the number of towers of the player.
     */
    Integer getTowers(Player player);

    /**
     * @param player the player.
     * @return the percentage of cells owned by the player.
     */
    Double getCellsPercentage(Player player);

    /**
     * @param player the player.
     * @return the balance of the player.
     */
    Integer getBalance(Player player);

}
