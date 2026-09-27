package it.unibo.the100dayswar.controller.maincontroller.api;

import it.unibo.the100dayswar.controller.gamecontroller.api.GameController;
import it.unibo.the100dayswar.controller.mapcontroller.api.MapController;
import it.unibo.the100dayswar.controller.movementcontroller.api.MovementController;
import it.unibo.the100dayswar.controller.shopcontroller.api.ShopController;
import it.unibo.the100dayswar.controller.statisticscontoller.api.StatisticController;
import it.unibo.the100dayswar.model.Model;

/** 
 * MainController is a provider that allow to get access to the model
 * and to the other controllers of the game.
 */
public interface MainController {
    /** 
     * Starts the game.
     */
    void startGame();

    /** 
     * Returns the statistic controller.
     * @return the statistic controller
     */
    StatisticController getStatisticController();

    /** 
     * Returns the shop controller.
     * 
     * @return the shop controller
     */
    ShopController getShopController();

    /**
     * Returns the movement controller.
     * 
     * @return the movement controller
    */
    MovementController getMovementController();

    /** 
     * Returns the map controller.
     * @return the map controller
     */
    MapController getMapController();

    /**
     * returns the game controller.
     * @return the game controller
     */
    GameController getGameController();

    /** 
     * Returns the game instance.
     * @return the game instance
     */
    Model getGameInstance();

    /**
     * Start a new game initializing the model.
     * 
     * @param username the username of the player
     */
    void startNewGame(String username);

    /**
     * Save the current game asynchronously.
     * 
     * @param path the path of the saving_file
     * @return a CompletableFuture representing the asynchronous operation
     * 
     * @implNote if the path is null the game will be 
     * saved in the default path.
     */
    java.util.concurrent.CompletableFuture<Boolean> saveGame(String path);

    /**
     * Load a previous game asynchronously initializing the model as the
     * one saved in the saving_file.
     * 
     * @param path the path of the saving file
     * 
     * @return a CompletableFuture representing the asynchronous operation
     * 
     * @implNote if the path is null the old game will be 
     * laoded following the default path.
     */
    java.util.concurrent.CompletableFuture<Boolean> loadOldGame(String path);

    /**
     * Returns the event bus for messaging.
     * @return the event bus
     */
    it.unibo.the100dayswar.controller.events.EventBus getEventBus();
}
