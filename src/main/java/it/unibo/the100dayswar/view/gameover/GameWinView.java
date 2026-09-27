package it.unibo.the100dayswar.view.gameover;

import it.unibo.the100dayswar.controller.maincontroller.api.MainController;

/**
 * The view that shows when the player wins the game.
 */
public class GameWinView extends AbstractGameOverView {
    private static final long serialVersionUID = 1L;
    /** 
     * The constructor of the game win view.
     * @param mainController the main controller
     */
    public GameWinView(final MainController mainController) {
        super("Congrats, you won!", "/gameover/win.png", mainController);
    }
}
