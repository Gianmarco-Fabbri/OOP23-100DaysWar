package it.unibo.the100dayswar.model.bot.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import it.unibo.the100dayswar.commons.patterns.Observable;
import it.unibo.the100dayswar.commons.patterns.Observer;
import it.unibo.the100dayswar.commons.utilities.impl.Pair;
import it.unibo.the100dayswar.commons.utilities.impl.Score;
import it.unibo.the100dayswar.model.bot.api.BotPlayer;
import it.unibo.the100dayswar.model.cell.api.Cell;
import it.unibo.the100dayswar.model.pathfinder.impl.BfsPathFinder;
import it.unibo.the100dayswar.model.soldier.api.Soldier;
import it.unibo.the100dayswar.model.soldier.impl.SoldierImpl;
import it.unibo.the100dayswar.model.tower.api.TowerType;
import it.unibo.the100dayswar.model.tower.impl.BasicTowerImpl;
import it.unibo.the100dayswar.model.unit.api.Unit;
import it.unibo.the100dayswar.model.tower.api.Tower;
import it.unibo.the100dayswar.model.pathfinder.api.PathFinder;

/**
 * An enum that represents the possible actions that a bot can take.
 */
public enum ActionType {

    /**
     * Represents the action of purchasing a soldier.
     */
    PURCHASE_SOLDIER {
        private static final int DEFAULT_SCORE = 2;

        /**
         * {@inheritDoc}
         */
        @Override
        protected boolean canPerform(final BotPlayer botPlayer) {
            return botPlayer.getBankAccount().getBalance() >= Soldier.DEFAULT_COST
                    && botPlayer.getMapManager().getBotSpawn().isFree();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected Score evaluate(final BotPlayer botPlayer) {
            return evaluateOrNonPerformable(botPlayer, () -> {
                final int soldierCount = botPlayer.getSoldiers().size();
                // Higher priority if soldier count is low
                return new Score(DEFAULT_SCORE + Math.max(0, 3 - soldierCount) + 1);
            });
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void execute(final BotPlayer botPlayer) {
            if (canPerform(botPlayer)) {
                final Soldier soldier = new SoldierImpl(botPlayer);
                attachObserverToUnit(soldier);
                // notifyObservers(new Pair<>(soldier, botPlayer.getSpawnPoint()));
                botPlayer.buyUnit(soldier);
            }
        }
    },

    /**
     * Represents the action of purchasing a tower in a random position near the
     * spawn point
     * so the tower can defend it.
     */
    PURCHASE_TOWER {
        private static final int DEFAULT_SCORE = 1;

        /**
         * {@inheritDoc}
         */
        @Override
        protected boolean canPerform(final BotPlayer botPlayer) {
            return botPlayer.getBankAccount().getBalance() >= TowerType.BASIC.getPrice();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected Score evaluate(final BotPlayer botPlayer) {
            return evaluateOrNonPerformable(botPlayer, () -> {
                final int soldierCount = botPlayer.getSoldiers().size();
                final int towerCount = botPlayer.getTowers().size();
                if (towerCount <= soldierCount / 2) {
                    // If the bot has less towers than half of the soldiers, it should buy one
                    return new Score(HIGH_PRIORITY_SCORE);
                }
                return new Score(DEFAULT_SCORE);
            });
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void execute(final BotPlayer botPlayer) {
            if (canPerform(botPlayer)) {
                final Cell towerPosition = findRandomTowerPosition(botPlayer);
                if (towerPosition != null) {
                    final Tower tower = new BasicTowerImpl(botPlayer, towerPosition);
                    attachObserverToUnit(tower);
                    botPlayer.buyUnit(tower);
                    // notifyObservers(new Pair<>(tower, tower.getPosition()));
                }
            }
        }

        /**
         * Finds a random cell near the spawn point but not adjacent.
         *
         * @param botPlayer The bot player placing the tower
         * @return A random cell where to put the tower, or null if no valid cell exists
         */
        private Cell findRandomTowerPosition(final BotPlayer botPlayer) {
            final Cell spawnPoint = botPlayer.getSpawnPoint();

            // Filter cells: near the spawn point but not adjacent, and buildable
            final List<Cell> validCells = botPlayer.getAllCells().stream()
                    .filter(cell -> !cell.isAdiacent(spawnPoint))
                    .filter(cell -> isNearSpawn(cell, spawnPoint))
                    .filter(Cell::isFree)
                    .collect(Collectors.toList());

            return validCells.isEmpty() ? null : validCells.get(RANDOM.nextInt(validCells.size()));
        }

        /**
         * Checks if a cell is near the spawn point but not adjacent.
         *
         * @param cell       The cell to check
         * @param spawnPoint The spawn point of the bot
         * @return True if the cell is near but not adjacent, false otherwise
         */
        private boolean isNearSpawn(final Cell cell, final Cell spawnPoint) {
            final int distance = Math.abs(cell.getPosition().getX() - spawnPoint.getPosition().getX())
                    + Math.abs(cell.getPosition().getY() - spawnPoint.getPosition().getY());
            return distance > 1 && distance <= 3;
        }
    },

    /**
     * Represents the action of upgrading a unit.
     * For a simpler logic, this action will upgrade the unit with the lowest cost
     * to upgrade.
     */
    UPGRADE_UNIT {
        private static final int BASE_SCORE = 2;
        private static final int INCREMENT_SCORE = 5;
        // These numbers are used to give the upgrade move a high priority every 5 turns
        private static final int UPGRADE_TURN_INTERVAL = 5;
        private int actionCounter;

        /**
         * {@inheritDoc}
         */
        @Override
        protected boolean canPerform(final BotPlayer botPlayer) {
            return botPlayer.getUnits().stream()
                    .filter(Unit::canUpgrade)
                    .anyMatch(unit -> botPlayer.getBankAccount().getBalance() >= unit.getUpgradeCost());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected Score evaluate(final BotPlayer botPlayer) {
            return evaluateOrNonPerformable(botPlayer, () -> {
                actionCounter++;
                if (actionCounter >= UPGRADE_TURN_INTERVAL) {
                    return new Score(BASE_SCORE + INCREMENT_SCORE);
                }
                return new Score(BASE_SCORE);
            });
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void execute(final BotPlayer botPlayer) {
            if (canPerform(botPlayer)) {
                actionCounter = 0;
                botPlayer.getUnits().stream()
                        .filter(Unit::canUpgrade)
                        .min(Comparator.comparingInt(Unit::getUpgradeCost))
                        .ifPresent(botPlayer::upgradeUnit);
            }
        }
    },

    /**
     * Represents the action of moving a unit.
     * For a simpler logic, this action chooses a random unit that can move
     * and moves the unit towards the enemy's spawn point using the shortest path.
     */
    MOVE_UNIT {
        private static final int DEFAULT_SCORE = 4;

        /**
         * {@inheritDoc}
         */
        @Override
        protected boolean canPerform(final BotPlayer botPlayer) {
            return !botPlayer.getSoldiers().isEmpty();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected Score evaluate(final BotPlayer botPlayer) {
            return evaluateOrNonPerformable(botPlayer, () -> new Score(DEFAULT_SCORE));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void execute(final BotPlayer botPlayer) {
            final Set<Soldier> soldiers = botPlayer.getSoldiers();
            if (canPerform(botPlayer)) {
                // Choose a random soldier
                final Soldier unitToMove = List.copyOf(soldiers).get(RANDOM.nextInt(soldiers.size()));
                // Using BFS algorithm to find the next cell in the path to the enemy spawn
                // point
                final Cell destination = determineDestination(botPlayer, unitToMove);
                if (destination != null && destination.isFree()) {
                    notifyObservers(new Pair<>(unitToMove, destination));
                }
            }
        }

        /**
         * Determines the destination for the unit using BFS logic.
         *
         * @param botPlayer the bot player
         * @param unit      the unit to move
         * @return the destination cell
         */
        private Cell determineDestination(final BotPlayer botPlayer, final Unit unit) {
            final PathFinder pathFinder = new BfsPathFinder(botPlayer.getAllCells());
            final Cell start = unit.getPosition();
            final Cell destination = botPlayer.getEnemySpawnPoint();
            if (start == null || destination == null) {
                return null;
            }
            final List<Cell> path = pathFinder.findPath(start, destination);
            if (path == null || path.isEmpty()) {
                return null;
            }
            return (path.size() > 1) ? path.get(1) : null;
        }
    };

    private static final long serialVersionUID = 1L;
    private static final int NON_PERFORMABLE_SCORE = -1;
    private static final int HIGH_PRIORITY_SCORE = 10;
    private static final Random RANDOM = new Random();

    /**
     * The istance of the ActionNotifier.
     */
    private static final ActionNotifier NOTIFIER = new ActionNotifier();

    /**
     * A static class, shared by all types of this enum, that has the
     * task of manage the observers of the actions.
     */
    private static final class ActionNotifier implements Observable<Pair<Unit, Cell>>, Serializable {
        private static final long serialVersionUID = 1L;
        private final Set<Observer<Pair<Unit, Cell>>> observers = new HashSet<>();

        /**
         * Return the set of observers.
         *
         * @return the set of observers
         */
        public Set<Observer<Pair<Unit, Cell>>> getObservers() {
            return new HashSet<>(observers);
        }

        /**
         * @param observer the observer to attach
         */
        @Override
        public void attach(final Observer<Pair<Unit, Cell>> observer) {
            observers.add(observer);
        }

        /**
         * @param observer the observer to detach
         */
        @Override
        public void detach(final Observer<Pair<Unit, Cell>> observer) {
            observers.remove(observer);
        }

        /**
         * Removes all attached observers.
         */
        public void removeAllObservers() {
            observers.clear();
        }

        /**
         * Notifies all attached observers with the given data.
         *
         * @param data the data to pass to the observers
         */
        public void notifyObservers(final Pair<Unit, Cell> data) {
            final List<Observer<Pair<Unit, Cell>>> observersCopy = new ArrayList<>(this.observers);
            for (final Observer<Pair<Unit, Cell>> observer : observersCopy) {
                observer.update(data);
            }
        }
    }

    /**
     * Registers an observer.
     *
     * @param observer the observer to register
     */
    public static void add(final Observer<Pair<Unit, Cell>> observer) {
        NOTIFIER.attach(observer);
    }

    /**
     * Registers an observer to the given observable.
     *
     * @param observable the observable to attach the observer to
     */
    public void attachObserverToUnit(final Observable<Pair<Unit, Cell>> observable) {
        NOTIFIER.getObservers().forEach(observable::attach);
    }

    /**
     * Unregisters an observer.
     *
     * @param observer the observer to unregister
     */
    public static void delete(final Observer<Pair<Unit, Cell>> observer) {
        NOTIFIER.detach(observer);
    }

    /**
     * Clears all attached observers.
     */
    public static void clear() {
        NOTIFIER.removeAllObservers();
    }

    /**
     * Notifies all registered observers with the given data.
     *
     * @param data the data to notify observers with
     */
    protected void notifyObservers(final Pair<Unit, Cell> data) {
        NOTIFIER.notifyObservers(data);
    }

    /**
     * Determines if the bot player can perform the action.
     *
     * @param botPlayer the bot player
     * @return true if the bot player can perform the action, false otherwise
     */
    protected abstract boolean canPerform(BotPlayer botPlayer);

    /**
     * Evaluates the action based on the bot player's state.
     *
     * @param botPlayer the bot player
     * @return the score of the action
     */
    protected abstract Score evaluate(BotPlayer botPlayer);

    /**
     * Executes the action.
     *
     * @param botPlayer the bot player
     */
    protected abstract void execute(BotPlayer botPlayer);

    /**
     * Utility method that returns a non-performable score if the action can't be
     * performed,
     * otherwise calculates the score with the provided scorer.
     *
     * @param botPlayer the bot player
     * @param scorer    a supplier that provides the score if the action can be
     *                  performed
     * @return the score of the action
     */
    protected Score evaluateOrNonPerformable(final BotPlayer botPlayer, final Supplier<Score> scorer) {
        return canPerform(botPlayer) ? scorer.get() : new Score(NON_PERFORMABLE_SCORE);
    }
}
