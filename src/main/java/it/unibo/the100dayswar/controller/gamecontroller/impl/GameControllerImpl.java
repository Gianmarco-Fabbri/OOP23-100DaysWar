package it.unibo.the100dayswar.controller.gamecontroller.impl;

import it.unibo.the100dayswar.controller.events.BattleResultEvent;
import it.unibo.the100dayswar.controller.gamecontroller.api.GameController;
import it.unibo.the100dayswar.model.fight.impl.BattleSoldierSoldierImpl;
import it.unibo.the100dayswar.model.player.api.Player;
import it.unibo.the100dayswar.model.soldier.api.Soldier;
import it.unibo.the100dayswar.model.unit.api.Combatant;
import it.unibo.the100dayswar.view.gameover.GameLoseView;
import it.unibo.the100dayswar.view.gameover.GameWinView;

import it.unibo.the100dayswar.controller.maincontroller.api.MainController;

/**
 * The implementation of the game controller of the game.
 */
public class GameControllerImpl implements GameController {
    private final MainController mainController;

    /**
     * Constructor for GameControllerImpl.
     * @param mainController the main controller
     */
    public GameControllerImpl(final MainController mainController) {
        this.mainController = mainController;
    }
    /** 
     * {@inheritDoc}
     */
    @Override
    public void attack() {
        final var selectedCell = mainController.getMapController().getSelectedCell();
        if (selectedCell.getSecond() != null && selectedCell.getSecond().getUnit().isPresent()) {
            final var unit = selectedCell.getSecond().getUnit().get();
            if (unit instanceof Soldier) {
                final var soldier = (Soldier) unit;
                final var adjacentCells = mainController.getMapController().getAdjacentCells(selectedCell.getSecond());
                for (final var cell : adjacentCells) {
                    if (cell.getUnit().isPresent() && cell.getUnit().get() instanceof Combatant) {
                        final var defender = cell.getUnit().get();
                        if (defender instanceof Soldier) {
                            final BattleSoldierSoldierImpl battle = new BattleSoldierSoldierImpl();
                            battle.startFight(soldier, (Soldier) defender);
                            final boolean won = battle.getLastAttackerRoll() > battle.getLastDefenderRoll();
                            mainController.getEventBus().publish(
                                new BattleResultEvent(
                                    BattleResultEvent.BattleType.SOLDIER_VS_SOLDIER,
                                    battle.getLastAttackerRoll(),
                                    battle.getLastDefenderRoll(),
                                    won,
                                    0, 0));
                        } else {
                            soldier.performAttack(defender);
                        }
                    }
                }
            }
        }
        mainController.getMapController().clearSelectedCell();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void skip() {
        mainController.getGameInstance().skipTurn();
        this.checkGameOver();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkGameOver() {
        if (mainController.getGameInstance().isOver()) {
            displayGameOver(mainController.getGameInstance().getWinner());
        }
    }

    /**
     * Display the game over message.
     * 
     * @param winner the winner of the game
     */
    private void displayGameOver(final Player winner) {
        if (winner == null) {
            throw new IllegalStateException("Game ended in a draw");
        } else if (winner.equals(mainController.getGameInstance().getHumanPlayer())) {
            new GameWinView(mainController).initialize();
        } else {
            new GameLoseView(mainController).initialize();
        }
    }
}
