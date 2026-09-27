package it.unibo.the100dayswar.application;

import it.unibo.the100dayswar.controller.maincontroller.api.MainController;
import it.unibo.the100dayswar.controller.maincontroller.impl.MainControllerImpl;

/**
 * The main class of The100DaysWar game.
 */
public final class The100DaysWar {

    /** 
     * A private constructor to hide the implicit public one.
     */
    private The100DaysWar() {
    }

    /**
     * The main method to start the game.
     *
     * @param args the arguments of the application
     */
    public static void main(final String[] args) {
        final MainController controller = new MainControllerImpl();
        controller.startGame();
    }
}
