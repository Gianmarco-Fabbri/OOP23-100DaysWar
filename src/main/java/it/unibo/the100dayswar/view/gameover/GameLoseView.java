package it.unibo.the100dayswar.view.gameover;

import it.unibo.the100dayswar.controller.maincontroller.api.MainController;

/**
 * The view that shows when the player loses the game.
 */
public class GameLoseView extends AbstractGameOverView {
    private static final long serialVersionUID = 1L;
    /** 
     * The constructor of the game lose view.
     * @param mainController the main controller
     */
    public GameLoseView(final MainController mainController) {
        super("Sorry, you lost!", "/gameover/lose.png", mainController);
    }
}
