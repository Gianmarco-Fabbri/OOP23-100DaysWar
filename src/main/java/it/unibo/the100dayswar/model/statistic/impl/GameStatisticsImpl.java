package it.unibo.the100dayswar.model.statistic.impl;

import it.unibo.the100dayswar.model.map.api.MapManager;
import it.unibo.the100dayswar.model.player.api.Player;
import it.unibo.the100dayswar.model.statistic.api.GameStatistics;
import it.unibo.the100dayswar.commons.utilities.impl.Pair;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * Implementation of GameStatistics.
 */
public class GameStatisticsImpl implements GameStatistics {
    private static final long serialVersionUID = 1L;

    private final Map<Player, Integer> soldiers = new HashMap<>();
    private final Map<Player, Integer> towers = new HashMap<>();
    private final Map<Player, Double> cellsPercentage = new HashMap<>();
    private final Map<Player, Integer> balances = new HashMap<>();
    private final List<Player> players;
    private final MapManager mapManager;

    /**
     * Constructor for GameStatisticImpl.
     * @param players the list of players.
     * @param mapManager the map manager containing the map data.
     */
    public GameStatisticsImpl(final List<Player> players, final MapManager mapManager) {
        this.mapManager = mapManager;
        this.players = new ArrayList<>(players);
        players.forEach(player -> {
            soldiers.put(player, 0);
            towers.put(player, 0);
            cellsPercentage.put(player, 0.0);
            balances.put(player, player.getBankAccount().getBalance());
        });
    }

    /**
     * Updates the number of soldiers for each player.
     */
    private void updateSoldiers() {
        players.forEach(player -> 
            soldiers.put(player, player.getSoldiers().size())
        );
    }

    /**
     * Updates the number of towers for each player.
     */
    private void updateTowers() {
        players.forEach(player -> 
            towers.put(player, player.getTowers().size())
        );
    }

    /**
     * Updates the percentage of cells owned for each player.
     */
    private void updateCellsPercentage() {
        final double totalCellsPercentage = mapManager.getMapAsAStream().count() / 100.0;
        mapManager.getPlayersCells().forEach((player, cells) -> 
            cellsPercentage.put(player, cells.size() / totalCellsPercentage)
        );
    }

    /**
     * Updates the balance for each player.
     */
    private void updateBalances() {
        players.forEach(player -> 
            balances.put(player, player.getBankAccount().getBalance())
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAllStatistics() {
        updateSoldiers();
        updateTowers();
        updateCellsPercentage();
        updateBalances();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pair<List<Player>, List<Integer>> getSoldiers() {
        return sortStatistic(soldiers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pair<List<Player>, List<Integer>> getTowers() {
        return sortStatistic(towers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pair<List<Player>, List<Double>> getCellsPercentage() {
        return sortStatistic(cellsPercentage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pair<List<Player>, List<Integer>> getBalances() {
        return sortStatistic(balances);
    }

    /**
     * Sorts the map by value.
     * @param <T> the type of the value.
     * @param map the map to sort.
     * @return a Pair of players and their values.
     */
    private <T extends Comparable<T>> Pair<List<Player>, List<T>> sortStatistic(final Map<Player, T> map) {
       final List<Map.Entry<Player, T>> entries = new ArrayList<>(map.entrySet());
        entries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        final List<Player> players = new ArrayList<>();
        final List<T> values = new ArrayList<>();
        for (final Map.Entry<Player, T> entry : entries) {
            players.add(entry.getKey());
            values.add(entry.getValue());
        }
        return new Pair<>(players, values);
    }
}
