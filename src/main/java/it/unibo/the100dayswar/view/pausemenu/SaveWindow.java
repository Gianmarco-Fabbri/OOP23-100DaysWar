package it.unibo.the100dayswar.view.pausemenu;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import javax.swing.SwingUtilities;
import java.util.function.Consumer;

import it.unibo.the100dayswar.controller.maincontroller.api.MainController;

/**
 * Utility class that implements the dialog to save or not save
 * the current game.
 */
public final class SaveWindow {

    /** 
     * A private constructor to hide the implicit public one.
     */
    private SaveWindow() {
    }

    /**
     * Dialog displayed before quitting the game.
     * 
     * @param parent the JDialog that launch this window
     * @param savingPath the location of the saving file
     * @param mainController the main controller
     * @param callback the callback to be called with true if saved, false otherwise
     */
    public static void saveDialog(final JDialog parent,
                                  final String savingPath,
                                  final MainController mainController,
                                  final Consumer<Boolean> callback) {
        final int save = JOptionPane.showConfirmDialog(
            parent,
            "Do you want to save the game?",
            "Exit Confirmation",
            JOptionPane.YES_NO_OPTION
        );

        if (save == JOptionPane.YES_OPTION) {
            mainController.saveGame(savingPath).thenAccept(success -> {
                SwingUtilities.invokeLater(() -> {
                    if (success) {
                        JOptionPane.showMessageDialog(
                            parent,
                            "Game saved successfully!",
                            "Save Status",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        callback.accept(true);
                    } else {
                        JOptionPane.showMessageDialog(
                            parent,
                            "Failed to save the game. Please try again.",
                            "Save Status",
                            JOptionPane.ERROR_MESSAGE
                        );
                        callback.accept(false);
                    }
                });
            });
        } else {
            callback.accept(false);
        }
    }
}
