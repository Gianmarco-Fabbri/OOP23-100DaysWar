package it.unibo.the100dayswar.controller.statisticscontoller.impl;

import java.util.ArrayList;
import java.util.List;

import it.unibo.the100dayswar.controller.maincontroller.api.MainController;
import it.unibo.the100dayswar.controller.statisticscontoller.api.StatisticController;
import it.unibo.the100dayswar.model.player.api.Player;

/**
 * The implementation of the statistic controller.
 */
public class StatisticControllerImpl implements StatisticController {
    private final MainController mainController;

    /**
     * Constructor.
     * @param mainController main controller
     */
    public StatisticControllerImpl(final MainController mainController) {
        this.mainController = mainController;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getSoldiers(final Player player) {
        return mainController.getGameInstance()
                .getGameStatistics()
                .getSoldiers()
                .getSecond()
                .get(mainController.getGameInstance()
                    .getGameStatistics()
                    .getSoldiers()
                    .getFirst().indexOf(player));
            }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getTowers(final Player player) {
        return mainController.getGameInstance()
                .getGameStatistics()
                .getTowers()
                .getSecond()
                .get(mainController.getGameInstance()
                    .getGameStatistics()
                    .getTowers()
                    .getFirst().indexOf(player));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getCellsPercentage(final Player player) {
        return mainController.getGameInstance()
                .getGameStatistics()
                .getCellsPercentage()
                .getSecond()
                .get(mainController.getGameInstance()
                    .getGameStatistics()
                    .getCellsPercentage()
                    .getFirst().indexOf(player));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getBalance(final Player player) {
        return mainController.getGameInstance()
                .getGameStatistics()
                .getBalances()
                .getSecond()
                .get(mainController.getGameInstance()
                    .getGameStatistics()
                    .getBalances()
                    .getFirst().indexOf(player));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Player> getPlayers() {
        return new ArrayList<>(mainController.getGameInstance()
        .getGameStatistics()
        .getBalances()
        .getFirst());
}

    /**
     * {@inheritDoc}
     */
   @Override 
    public void updateStatistics() {
        mainController.getGameInstance().getGameStatistics().updateAllStatistics();
    }

}
