package it.unibo.the100dayswar.controller.maincontroller.impl;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import javax.swing.SwingUtilities;

import it.unibo.the100dayswar.controller.events.EventBus;
import it.unibo.the100dayswar.controller.events.GameUpdateEvent;
import it.unibo.the100dayswar.controller.events.BattleResultEvent;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.the100dayswar.controller.gamecontroller.api.GameController;
import it.unibo.the100dayswar.controller.gamecontroller.impl.GameControllerImpl;
import it.unibo.the100dayswar.controller.maincontroller.api.MainController;
import it.unibo.the100dayswar.controller.mapcontroller.api.MapController;
import it.unibo.the100dayswar.controller.mapcontroller.impl.MapControllerImpl;
import it.unibo.the100dayswar.controller.movementcontroller.api.MovementController;
import it.unibo.the100dayswar.controller.movementcontroller.impl.MovementControllerImpl;
import it.unibo.the100dayswar.controller.shopcontroller.api.ShopController;
import it.unibo.the100dayswar.controller.shopcontroller.impl.ShopControllerImpl;
import it.unibo.the100dayswar.controller.statisticscontoller.api.StatisticController;
import it.unibo.the100dayswar.controller.statisticscontoller.impl.StatisticControllerImpl;
import it.unibo.the100dayswar.model.Model;
import it.unibo.the100dayswar.model.ModelImpl;
import it.unibo.the100dayswar.view.startmenu.StartMenuView;


/**
 * The implementation of the main controller of the game.
 */
public class MainControllerImpl implements MainController {
    private final StatisticController statisticController;
    private final ShopController shopController;
    private final MovementController movementController;
    private final MapController mapController;
    private final GameController gameController;
    private Model model;

    private final EventBus eventBus;

    /**
     * Constructor of the main controller.
     */
    public MainControllerImpl() {
        this.eventBus = new EventBus();
        this.statisticController = new StatisticControllerImpl(this);
        this.shopController = new ShopControllerImpl(this);
        this.mapController = new MapControllerImpl(this);
        this.gameController = new GameControllerImpl(this);
        this.movementController = new MovementControllerImpl(this);
        this.model = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EventBus getEventBus() {
        return this.eventBus;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void startGame() {
        new StartMenuView(this).initialize();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", 
        justification = "Exposing 'model' is intentional for design reasons.")
    @Override
    public Model getGameInstance() {
        return this.model;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public StatisticController getStatisticController() {
        return this.statisticController;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP",
        justification = "Exposing 'mapController' is intentional for design reasons.")
    @Override
    public MapController getMapController() {
        return this.mapController;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ShopController getShopController() {
        return this.shopController;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MovementController getMovementController() {
        return this.movementController;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameController getGameController() {
        return this.gameController;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startNewGame(final String username) {
        this.model = new ModelImpl(username);
        this.model.setUpdateCallback(() -> {
            SwingUtilities.invokeLater(() -> {
                this.model.getGameStatistics().updateAllStatistics();
                this.eventBus.publish(new GameUpdateEvent());
            });
        });
        this.model.setTowerShotCallback(data -> {
            SwingUtilities.invokeLater(() -> {
                this.eventBus.publish(new BattleResultEvent(
                    BattleResultEvent.BattleType.TOWER_VS_SOLDIER,
                    0, 0, true, data.getFirst(), data.getSecond()
                ));
            });
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<Boolean> saveGame(final String path) {
        return CompletableFuture.supplyAsync(() -> this.getGameInstance().saveGame(path));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<Boolean> loadOldGame(final String path) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                this.model = new ModelImpl(Optional.ofNullable(path));
                this.model.setUpdateCallback(() -> {
                    SwingUtilities.invokeLater(() -> {
                        this.model.getGameStatistics().updateAllStatistics();
                        this.eventBus.publish(new GameUpdateEvent());
                    });
                });
                this.model.setTowerShotCallback(data -> {
                    SwingUtilities.invokeLater(() -> {
                        this.eventBus.publish(new BattleResultEvent(
                            BattleResultEvent.BattleType.TOWER_VS_SOLDIER,
                            0, 0, true, data.getFirst(), data.getSecond()
                        ));
                    });
                });
                return true;
            } catch (IllegalStateException e) {
                return false;
            }
        });
    }
}
