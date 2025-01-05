package it.unibo.the100dayswar.controller.maincontroller.api;

import it.unibo.the100dayswar.controller.gamecontroller.api.GameController;
import it.unibo.the100dayswar.controller.mapcontroller.api.MapController;
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
     * Save the current game.
     * 
     * @param path the path of the saving_file
     * @return true if the game was saved correctly
     *         false otherwise
     * 
     * @implNote if the path is null the game will be 
     * saved in the default path.
     */
    boolean saveGame(String path);

    /**
     * Start a new game initializing the model.
     * 
     * @param username the username of the player
     */
    void startNewGame(String username);

    /**
     * Load a previous game initializing the model as the 
     * one saved in the saving_file.
     * 
     * @param path the path of the saving file
     * 
     * @return true if the old game is loaded correctly
     *         false otherwise
     * 
     * @implNote if the path is null the old game will be 
     * laoded following the default path.
     */
    boolean loadOldGame(String path);
}
